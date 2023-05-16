package com.uob.frtb.risk.samr.results.model;

import com.uob.frtb.risk.samr.model.HierarchyFilters;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hierarchy implements HierarchyFilters {
	
	private SortedSet<String> pos = new TreeSet<>();
	private SortedSet<String> filterNames = new TreeSet<>(Arrays.asList(BOOK, DESK, LOCATION));
	private Map<String, SortedSet<String>> filterValuesMap = new TreeMap<>();

	public void addHierarchyRow(com.uob.frtb.risk.samr.model.Hierarchy hierarchy) {
		pos.add(hierarchy.getPo());

		SortedSet<String> books = filterValuesMap.get(BOOK);
		if (books == null) {
			books = new TreeSet<>();
			filterValuesMap.put(BOOK, books);
		}
		books.add(hierarchy.getBook());

		SortedSet<String> desks = filterValuesMap.get(DESK);
		if (desks == null) {
			desks = new TreeSet<>();
			filterValuesMap.put(DESK, desks);
		}
		desks.add(hierarchy.getDesk());

		SortedSet<String> locations = filterValuesMap.get(LOCATION);
		if (locations == null) {
			locations = new TreeSet<>();
			filterValuesMap.put(LOCATION, locations);
		}
		locations.add(hierarchy.getLocation());
	}
}
