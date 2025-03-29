package org.actus.webapp.core.functions;

import java.time.LocalDateTime;

import org.actus.attributes.ContractModelProvider;
import org.actus.conventions.businessday.BusinessDayAdjuster;
import org.actus.conventions.daycount.DayCountCalculator;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.functions.PayOffFunction;
import org.actus.states.StateSpace;
import org.actus.util.CommonUtils;

public final class POF_AFD_rf2 implements PayOffFunction {
	private String riskFactorID;   // the id of the behavior model for this Absolute FundsChecked Delta callout event 
	public POF_AFD_rf2(String riskFactorID) {
		this.riskFactorID = riskFactorID;
	}
	@Override
	public double eval(LocalDateTime time, StateSpace states, ContractModelProvider model,
			RiskFactorModelProvider riskFactorModel, DayCountCalculator dayCounter,
			BusinessDayAdjuster timeAdjuster) {
		System.out.println("****fnp111 PayoffFunction POF_AFD_rf2 entered");  // fnp diagnostic mar 2025  
		
		return CommonUtils.settlementCurrencyFxRate(riskFactorModel, model, time, states)
				* riskFactorModel.stateAt(this.riskFactorID,time,states,model,false)*(-1.0);
	}
	// the returned amount is the absolute change in principal; available funds have been checked and 
	// the amount limited -so amount of notionalPrincipal can go to zero but no sign change. There 
	// could be conversion to a different settlement currency ( from the blockchain account ) . The 
	// actual payoff is (-1.0) * (actual change in notionalPrincipal) in all cases ?  
}
