package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.IRDeltaConfig;
import com.uob.frtb.risk.frtb.samr.model.RiskWeight;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/frtb-common-services.xml"})
public class TestCorrelationUtils {

	@Autowired
	private IRDeltaConfig irCorrConfig;

	private static double[][] ratesCorr = {
			{ 1, 0.970445534, 0.913931185, 0.810584246, 0.718923733, 0.565525439, 0.4, 0.4, 0.4, 0.4 },
			{ 0.970445534, 1, 0.970445534, 0.913931185, 0.860707976, 0.763379494, 0.565525439, 0.418951549, 0.4, 0.4 },
			{ 0.913931185, 0.970445534, 1, 0.970445534, 0.941764534, 0.886920437, 0.763379494, 0.65704682, 0.565525439,
					0.418951549 },
			{ 0.810584246, 0.913931185, 0.970445534, 1, 0.98511194, 0.955997482, 0.886920437, 0.822834658, 0.763379494,
					0.65704682 },
			{ 0.718923733, 0.860707976, 0.941764534, 0.98511194, 1, 0.980198673, 0.93239382, 0.886920437, 0.843664817,
					0.763379494 },
			{ 0.565525439, 0.763379494, 0.886920437, 0.955997482, 0.980198673, 1, 0.970445534, 0.941764534, 0.913931185,
					0.860707976 },
			{ 0.4, 0.565525439, 0.763379494, 0.886920437, 0.93239382, 0.970445534, 1, 0.98511194, 0.970445534,
					0.941764534 },
			{ 0.4, 0.418951549, 0.65704682, 0.822834658, 0.886920437, 0.941764534, 0.98511194, 1, 0.990049834,
					0.970445534 },
			{ 0.4, 0.4, 0.565525439, 0.763379494, 0.843664817, 0.913931185, 0.970445534, 0.990049834, 1, 0.98511194 },
			{ 0.4, 0.4, 0.418951549, 0.65704682, 0.763379494, 0.860707976, 0.941764534, 0.970445534, 0.98511194, 1 }, };

	private static double[][] interRatesCorr = {
			{ 0.999, 0.9694750880149596, 0.9130172540859569, 0.8097736617242169, 0.7182048096984942, 0.5649599132608375,
					0.3996, 0.3996, 0.3996, 0.3996, },
			{ 0.9694750880149596, 0.999, 0.9694750880149596, 0.9130172540859569, 0.8598472684486328, 0.7626161148425163,
					0.5649599132608375, 0.41853259769839135, 0.3996, 0.3996, },
			{ 0.9130172540859569, 0.9694750880149596, 0.999, 0.9694750880149596, 0.9408227690506644, 0.8860335162804404,
					0.7626161148425163, 0.6563897729952417, 0.5649599132608375, 0.41853259769839135, },
			{ 0.8097736617242169, 0.9130172540859569, 0.9694750880149596, 0.999, 0.9841268276634596, 0.9550414843512669,
					0.8860335162804404, 0.8220118233979624, 0.7626161148425163, 0.6563897729952417, },
			{ 0.7182048096984942, 0.8598472684486328, 0.9408227690506644, 0.9841268276634596, 0.999, 0.9792184746334485,
					0.9314614260860423, 0.8860335162804404, 0.8428211517797873, 0.7626161148425163, },
			{ 0.5649599132608375, 0.7626161148425163, 0.8860335162804404, 0.9550414843512669, 0.9792184746334485, 0.999,
					0.9694750880149596, 0.9408227690506644, 0.9130172540859569, 0.8598472684486328, },
			{ 0.3996, 0.5649599132608375, 0.7626161148425163, 0.8860335162804404, 0.9314614260860423,
					0.9694750880149596, 0.999, 0.9841268276634596, 0.9694750880149596, 0.9408227690506644, },
			{ 0.3996, 0.41853259769839135, 0.6563897729952417, 0.8220118233979624, 0.8860335162804404,
					0.9408227690506644, 0.9841268276634596, 0.999, 0.9890597839154189, 0.9694750880149596, },
			{ 0.3996, 0.3996, 0.5649599132608375, 0.7626161148425163, 0.8428211517797873, 0.9130172540859569,
					0.9694750880149596, 0.9890597839154189, 0.999, 0.9841268276634596, },
			{ 0.3996, 0.3996, 0.41853259769839135, 0.6563897729952417, 0.7626161148425163, 0.8598472684486328,
					0.9408227690506644, 0.9694750880149596, 0.9841268276634596, 0.999, }, };

	private static List<RiskWeight> weights;

	@BeforeClass
	public static void setup() {
		weights = new ArrayList<RiskWeight>();
		weights.add(new RiskWeight(1, 0.25, 0.024));
		weights.add(new RiskWeight(1, 0.5, 0.024));
		weights.add(new RiskWeight(1, 1, 0.0225));
		weights.add(new RiskWeight(1, 2, 0.0188));
		weights.add(new RiskWeight(1, 3, 0.0173));
		weights.add(new RiskWeight(1, 5, 0.015));
		weights.add(new RiskWeight(1, 10, 0.015));
		weights.add(new RiskWeight(1, 15, 0.015));
		weights.add(new RiskWeight(1, 20, 0.015));
		weights.add(new RiskWeight(1, 30, 0.015));
	}

	@Test
	public void testRatesCorrelation() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method m = IRDeltaConfig.class.getDeclaredMethod("ratesCorrelation", List.class);
		m.setAccessible(true);
		RealMatrix rm = (RealMatrix) m.invoke(irCorrConfig, weights);

		for (int i = 0; i < weights.size(); i++) {
			for (int j = 0; j < weights.size(); j++) {
				Assert.assertEquals(rm.getEntry(i, j), ratesCorr[i][j], 1E-6);
			}
		}
	}

	@Test
	public void testInterRatesCorrelation() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method m = IRDeltaConfig.class.getDeclaredMethod("interRatesCorrelation", List.class);
		m.setAccessible(true);
		RealMatrix rm = (RealMatrix) m.invoke(irCorrConfig, weights);

		for (int i = 0; i < weights.size(); i++) {
			for (int j = 0; j < weights.size(); j++) {
				Assert.assertEquals(rm.getEntry(i, j), interRatesCorr[i][j], 1E-6);
			}
		}
	}
}
