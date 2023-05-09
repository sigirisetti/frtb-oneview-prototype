package com.quark.csv.converter;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvToJsonUtils {

	public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
		CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);

		return mappingIterator.readAll();
	}

}
