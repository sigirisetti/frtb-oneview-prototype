package com.uob.frtb.risk.samr.config;

import java.util.List;
import java.util.Map;

class ConfigUtil {

	static void populateWithStringKeyAndDoubleValues(String keys, String values, Map<String, Double> toBePopulated) {
		String[] tokens = keys.split(",");
		String[] rws = values.split(",");
		for (int i = 0; i < tokens.length; i++) {
			toBePopulated.put(tokens[i], new Double(rws[i]));
		}
	}

	static void populateWithStringKeyAndDoubleValues(List<String> keys, String values,
			Map<String, Double> toBePopulated) {
		String[] rws = values.split(",");
		for (int i = 0; i < keys.size() && i < rws.length; i++) {
			toBePopulated.put(keys.get(i), new Double(rws[i]));
		}
	}

	static void populateWithIntegers(String values, List<Integer> toBePopulated) {
		String[] vals = values.split(",");
		for (int i = 0; i < vals.length && i < vals.length; i++) {
			toBePopulated.add(new Integer(vals[i]));
		}
	}

	static void populateWithDouble(String values, List<Double> toBePopulated) {
		String[] vals = values.split(",");
		for (int i = 0; i < vals.length && i < vals.length; i++) {
			toBePopulated.add(new Double(vals[i]));
		}
	}

	static void populateWithIntegerKeyAndDoubleValues(List<Integer> keys, String values,
			Map<Integer, Double> toBePopulated) {
		String[] vals = values.split(",");
		for (int i = 0; i < keys.size() && i < vals.length; i++) {
			toBePopulated.put(keys.get(i), new Double(vals[i]));
		}
	}

}
