package org.actus.webapp.core.functions;

import java.time.LocalDateTime;

import org.actus.attributes.ContractModelProvider;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.StateTransitionFunction;
import org.actus.states.StateSpace;

public final class STF_CL implements StateTransitionFunction {
    Double weight;

    public STF_CL() {
        this.weight = 1.0;
    }

    public STF_CL(Double weight) {
        this.weight = weight;
    }

    @Override
    public StateSpace eval(LocalDateTime time, StateSpace states,
    ContractModelProvider model, RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {// update state space
        double timeFromLastEvent = dayCounter.dayCountFraction(timeAdjuster.shiftCalcTime(states.statusDate), timeAdjuster.shiftCalcTime(time));
        states.accruedInterest += states.nominalInterestRate * states.notionalPrincipal * timeFromLastEvent;
        states.feeAccrued += model.<Double>getAs("FeeRate") * states.notionalPrincipal * timeFromLastEvent;

        double lossFactor = weight * riskFactorModel.stateAt(model.<String>getAs("ObjectCodeOfCreditLossModel"),time,states,model,false);
        states.notionalPrincipal -= lossFactor * states.notionalPrincipal;
        states.nextPrincipalRedemptionPayment -= lossFactor * states.nextPrincipalRedemptionPayment;

        states.statusDate = time;
        // return post-event-states
        return StateSpace.copyStateSpace(states);
    }
    
}
