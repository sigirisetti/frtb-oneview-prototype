package com.quark.db.schemadata.setup;

import com.quark.core.security.Group;
import com.quark.core.security.Organization;
import com.quark.db.schemadata.loader.CurveDefSampleSetup;
import com.quark.db.schemadata.loader.CurveUnderlyingsSampleDataSetup;
import com.quark.db.schemadata.loader.PricingContextConfigSampleSetup;
import com.quark.db.schemadata.loader.PricingEngineConfigSampleSetup;
import com.quark.db.schemadata.loader.QuoteDataSetup;
import com.quark.db.schemadata.loader.RefTypeCSVLoader;
import com.quark.db.schemadata.loader.SACCRConfig;
import com.quark.db.schemadata.loader.UsersDataLoader;

import groovy.util.logging.Slf4j;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

@Slf4j
public class QuarkDataSetupMain {

	private ClassPathXmlApplicationContext context;

	public QuarkDataSetupMain() {
	}

    public void setup() {
		try {
			context = new ClassPathXmlApplicationContext("quark-db-setup.xml");
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
		new QuarkDataSetupMain().setup();
	}
}
