package com.quark.risk.frtb.samr.calculators;

import com.quark.core.data.CoreDataService;
import com.quark.core.exception.ApplicationException;
import com.quark.risk.frtb.samr.model.CalcRequest;
import com.quark.risk.frtb.samr.results.PoResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * SAMR calculator main class
 * 
 * @author Surya
 */
@Getter
@Setter
@Component
@Scope("prototype")
public class SAMRCalculator {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CoreDataService coreDataService;

	private CalcRequest calcRequest;

	private List<BaseRiskClassLevelRiskChargeCalculator> marginCalculators;

	/**
	 * API to trigger SAMR calculations
	 * 
	 * @throws ApplicationException
	 */
	public void calculate(PoResults poResults) throws ApplicationException {
		initCalculators();
		setupCalculators();
		computeMargins();
		saveResults(poResults, calcRequest.isPersistResults());
	}

	void setupCalculators() {
		for (BaseRiskClassLevelRiskChargeCalculator calc : marginCalculators) {
			calc.initCalculators();
			calc.setupCalculators(calcRequest);
		}
	}

	private void initCalculators() {
		marginCalculators = new ArrayList<>();
		marginCalculators.add(applicationContext.getBean(IRRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(FXRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(EQRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(CRNonSecRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(CRSecNonCTPRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(CRSecCTPRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(COMMRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(DefaultRiskChargeCalc.class));
		marginCalculators.add(applicationContext.getBean(ResidualsRiskChargeCalc.class));
	}

	private void computeMargins() throws ApplicationException {
		for (BaseRiskClassLevelRiskChargeCalculator calc : marginCalculators) {
			calc.calculateRiskCharge();
		}
	}

	private void saveResults(PoResults poResults, boolean persist) {
		// SAMRResults results = new
		// SAMRResults(calcRequest.getWorkflowInstance(),
		// calcRequest.getBaseCurrency());
		for (BaseRiskClassLevelRiskChargeCalculator calc : marginCalculators) {
			calc.saveResults(poResults);
		}
		/*
		 * if (persist) { coreDataService.save(results); }
		 */
	}
}
