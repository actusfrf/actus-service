package org.actus.webapp.core.functions;

import java.time.LocalDateTime;

import org.actus.attributes.ContractModelProvider;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.StateTransitionFunction;
import org.actus.states.StateSpace;

public final class STF_AFD_rf2 implements StateTransitionFunction {
	private String riskFactorID;   // the riskFactorId of the depositTrx model for this callout event
	public STF_AFD_rf2(String riskFactorID) {
		this.riskFactorID = riskFactorID;
	} 
	// accepts stateAt( ) as an absolute delta to notionalPrincipal; correct sign, checked for sufficient funds 
	@Override
	public StateSpace eval(LocalDateTime time, StateSpace states, ContractModelProvider model,
			RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter, BusinessDayAdjuster timeAdjuster) {
        // update state space
        double timeFromLastEvent = dayCounter.dayCountFraction(timeAdjuster.shiftCalcTime(states.statusDate), timeAdjuster.shiftCalcTime(time));
        states.accruedInterest += states.nominalInterestRate * states.notionalPrincipal * timeFromLastEvent;
        states.feeAccrued += model.<Double>getAs("feeRate") * states.notionalPrincipal * timeFromLastEvent;
        states.notionalPrincipal += riskFactorModel.stateAt(this.riskFactorID,time,states,model,false);
        states.statusDate = time;

        // return post-event-states
        return StateSpace.copyStateSpace(states);
	}

}
