package com.uob.frtb.db.schemadata.setup;

import com.uob.frtb.core.security.Group;
import com.uob.frtb.core.security.Organization;
import com.uob.frtb.db.schemadata.loader.CurveDefSampleSetup;
import com.uob.frtb.db.schemadata.loader.CurveUnderlyingsSampleDataSetup;
import com.uob.frtb.db.schemadata.loader.PricingContextConfigSampleSetup;
import com.uob.frtb.db.schemadata.loader.PricingEngineConfigSampleSetup;
import com.uob.frtb.db.schemadata.loader.QuoteDataSetup;
import com.uob.frtb.db.schemadata.loader.RefTypeCSVLoader;
import com.uob.frtb.db.schemadata.loader.SACCRConfig;
import com.uob.frtb.db.schemadata.loader.UsersDataLoader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

@Slf4j
public class FrtbDataSetupMain {

	private ClassPathXmlApplicationContext context;

	public FrtbDataSetupMain() {
	}

    public void setup() {
		try {
			context = new ClassPathXmlApplicationContext("frtb-db-setup.xml");
			loadAllRefTypeFromCSV();
			setupSampleCurveUnderlyings();
			setupSampleQuotes();
			setupSampleCurves();
			setupSamplePricingEngineConfig();
			setupSamplePricingContextConfig();
			setupUserssGroupsPermissions();
			setupSACCRConfig();
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}

	private void setupSACCRConfig() {
		SACCRConfig saccr=context.getBean(SACCRConfig.class);
		saccr.loadSACCRConfig();
	}

	private void setupUserssGroupsPermissions() {
		UsersDataLoader udl = context.getBean(UsersDataLoader.class);
		Organization ast = udl.setupOrg();
		udl.setupPermissions();
		Set<Group> groups = udl.setupGroups();
		udl.setupUsers(ast, groups);
	}

	private void setupSamplePricingContextConfig() {
		PricingContextConfigSampleSetup setup = context.getBean(PricingContextConfigSampleSetup.class);
		setup.setupSamplePricingContext();
	}

	private void setupSamplePricingEngineConfig() {
		PricingEngineConfigSampleSetup setup = context.getBean(PricingEngineConfigSampleSetup.class);
		setup.setupSamplePricingEngines();
	}

	private void setupSampleCurves() {
		CurveDefSampleSetup setup = context.getBean(CurveDefSampleSetup.class);
		setup.setupSampleCurveDefs();
	}

	private void setupSampleCurveUnderlyings() {
		CurveUnderlyingsSampleDataSetup loader = context.getBean(CurveUnderlyingsSampleDataSetup.class);
		loader.setupSampleCurveUnderlyings();
	}

	private void loadAllRefTypeFromCSV() {
		RefTypeCSVLoader loader = context.getBean(RefTypeCSVLoader.class);
		loader.load();
	}

	private void setupSampleQuotes() {
		QuoteDataSetup loader = context.getBean(QuoteDataSetup.class);
		loader.setupQuotes();
	}

	public static void main(String[] args) {
		new FrtbDataSetupMain().setup();
	}
}
