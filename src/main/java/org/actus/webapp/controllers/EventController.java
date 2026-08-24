package org.actus.webapp.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.events.EventFactory;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.csh.STF_AD_CSH;
import org.actus.functions.pam.POF_AD_PAM;
import org.actus.states.StateSpace;
import org.actus.types.ContractTypeEnum;
import org.actus.types.EventType;
import org.actus.webapp.models.BatchInputData;
import org.actus.webapp.models.Event;
import org.actus.webapp.models.EventStream;
import org.actus.webapp.models.InputData;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.ObservedEvent;
import org.actus.webapp.utils.TimeSeries;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EventController {

    private final ObjectMapper objectMapper;

    public EventController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    class MarketModel implements RiskFactorModelProvider {
        HashMap<String,TimeSeries<LocalDateTime,Double>> multiSeries = new HashMap<String,TimeSeries<LocalDateTime,Double>>();
        Set<ContractEvent> eventsObserved = new LinkedHashSet<>();
        
        public Set<String> keys() {
            return multiSeries.keySet();
        }

        public void add(String symbol, TimeSeries<LocalDateTime,Double> series) {
            multiSeries.put(symbol,series);
        }

        public double stateAt(String id, LocalDateTime time, StateSpace states,
                ContractModelProvider terms, boolean isMarket) {
            return multiSeries.get(id).getValueFor(time,1);
        }

        public void addEventsObserved(List<ContractEvent> events) {
            eventsObserved.addAll(events);
        }

        // CEC and CEG consume externally observed contract events through this core extension point.
        public Set<ContractEvent> events(ContractModelProvider model) {
            return eventsObserved;
        }
    }

    // String -> ArrayList<ContractEvent>
    @RequestMapping(method = RequestMethod.POST, value = "/events")
    @CrossOrigin(origins = "*")
    public List<Event> solveContract(@RequestBody InputData json) {

        // extract contract terms from body
        ContractModel terms = ContractModel.parse(json.getContract());
        List<ObservedData> riskFactorData = json.getRiskFactors();
        List<ObservedEvent> eventsObserved = json.getEventsObserved();

        // create risk factor observer
        RiskFactorModelProvider observer = createObserver(riskFactorData, eventsObserved, terms);

        // compute and return events
        return computeEvents(terms, observer);

    }

    // param:   Json Array of Json Objects
    // return:  ArrayList of ArrayList of ContractEvents
    @RequestMapping(method = RequestMethod.POST, value = "/eventsBatch")
    @CrossOrigin(origins = "*")
    public List<EventStream> solveContractBatch(@RequestBody BatchInputData json) {
        
        // extract body parameters
        List<Map<String, Object>> contractData = json.getContracts();
        List<ObservedData> riskFactorData = json.getRiskFactors();
        List<ObservedEvent> eventsObserved = json.getEventsObserved();

        ArrayList<EventStream> output = new ArrayList<>();
        contractData.forEach(entry -> {
            // extract contract terms
            ContractModel terms;
            String contractID = (entry.get("contractID") == null)? "NA":entry.get("contractID").toString();
            try {
                terms = ContractModel.parse(entry); 
            } catch(Exception e){
                output.add(new EventStream(contractID, "Failure", e.toString(), new ArrayList<Event>()));
                return; // skipt this iteration and continue with next
            }
            // compute contract events
            try {
                RiskFactorModelProvider observer = createObserver(riskFactorData, eventsObserved, terms);
                output.add(new EventStream(contractID, "Success", "", computeEvents(terms, observer)));
            }catch(Exception e){
                output.add(new EventStream(contractID, "Failure", e.toString(), new ArrayList<Event>()));
            }
        });
        return output;
    }

    private RiskFactorModelProvider createObserver(List<ObservedData> riskFactorData, List<ObservedEvent> eventsObserved, ContractModelProvider terms) {
        MarketModel observer = new MarketModel();

        riskFactorData.forEach(entry -> {
            String symbol = entry.getMarketObjectCode();
            Double base = entry.getBase();
            LocalDateTime[] times = entry.getData().stream().map(obs -> LocalDateTime.parse(obs.getTime())).toArray(LocalDateTime[]::new);
            Double[] values = entry.getData().stream().map(obs -> 1/base*obs.getValue()).toArray(Double[]::new);
            
            TimeSeries<LocalDateTime,Double> series = new TimeSeries<LocalDateTime,Double>();
            series.of(times,values);
            observer.add(symbol,series);
        });

        // eventsObserved is part of the CEC/CEG trigger path only; other contract types remain unaffected.
        if(isCreditEnhancement(terms) && eventsObserved != null && !eventsObserved.isEmpty()) {
            observer.addEventsObserved(readObservedEvents(eventsObserved, terms));
        }

        return observer;
    }

    private boolean isCreditEnhancement(ContractModelProvider terms) {
        ContractTypeEnum contractType = terms.getAs("contractType");
        return contractType == ContractTypeEnum.CEC || contractType == ContractTypeEnum.CEG;
    }

    private List<ContractEvent> readObservedEvents(List<ObservedEvent> eventsObserved, ContractModelProvider terms) {
        return eventsObserved.stream().map(e -> {
        	// Match actus-core fixture handling; these functions are placeholders required to construct ContractEvent.
            ContractEvent event = EventFactory.createEvent(
                    LocalDateTime.parse(e.getTime()),
                    EventType.valueOf(e.getType()),
                    terms.getAs("currency"),
                    new POF_AD_PAM(),
                    new STF_AD_CSH(),
                    e.getContractId()
            );
            event.setStates(readStateSpace(e.getStates()));
            return event;
        }).collect(Collectors.toList());
    }

    private StateSpace readStateSpace(Map<String,Object> input) {
        return input == null ? new StateSpace() : objectMapper.convertValue(input, StateSpace.class);
    }

    private List<Event> computeEvents(ContractModel model, RiskFactorModelProvider observer) {
        // define projection end-time
        LocalDateTime to = model.getAs("terminationDate");
        if(to == null) to = model.getAs("maturityDate");
        if(to == null) to = model.getAs("amortizationDate");
        if(to == null) to = model.getAs("settlementDate");
        if(to == null) to = LocalDateTime.now().plusYears(5);

        // compute actus schedule
        ArrayList<ContractEvent> schedule = ContractType.schedule(to, model);

        // apply schedule to contract
        schedule = ContractType.apply(schedule, model, observer);
        
        // transform schedule to event list and return
        return schedule.stream().map(e -> new Event(e)).collect(Collectors.toList());
    }

}
