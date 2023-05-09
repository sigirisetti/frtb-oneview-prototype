package com.quark.risk.saccr.csv.reader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.saccr.csv.model.TradeData;
import com.quark.risk.saccr.model.RequiredTradeAttributes;
import com.quark.risk.saccr.model.Trade;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class TradeCsvReader extends BaseSACCRCsvReader<Trade> {

	private Map<String, RequiredTradeAttributes> config;

	public TradeCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("productName")
				.addColumn("contractType").addColumn("legalEntity").addColumn("laName").addColumn("csaName")
				.addColumn("po").addColumn("tradeId").addColumn("sourceSystem").addColumn("npvCCY").addColumn("npv")
				.addColumn("startDate").addColumn("endDate").addColumn("nextExerciseDate").addColumn("buySell")
				.addColumn("putCall").addColumn("payRateIndex").addColumn("recRateIndex").addColumn("payoffCcy")
				.addColumn("payOffAmount").addColumn("notionalCcy").addColumn("notional").addColumn("quantity")
				.addColumn("tickSize").addColumn("underlyingPrice").addColumn("strike").addColumn("quantoFXRate")
				.addColumn("payCCY").addColumn("recCCY").addColumn("payNotional").addColumn("recNotional")
				.addColumn("subCls").addColumn("underlyingProduct").addColumn("settlementType")
				.addColumn("earlyTerminationDate").addColumn("optionType").addColumn("strike2")
				.addColumn("exerciseType").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(TradeData.class).with(schema);
	}

    public void read(String path) throws IOException {
		MappingIterator<TradeData> it = getReader().readValues(new File(path));
		int seq = 0;
		while (it.hasNext()) {
			TradeData d = it.next();
			if (d != null) {
				d.setConfig(config);
				saccrData.add(d);
				if (d.getMessages() != null && !d.getMessages().isEmpty()) {
					log.info(d.getMessages().toString());
				}
				Trade e = d.build();
				log.info(ToStringBuilder.reflectionToString(e));
				if (e != null) {
					e.setSeq(seq++);
					e.setWorkflowInstance(workflowInstance);
					saccrEntities.add(e);
				} else {
					log.info("Trade " + d.getTradeId() + " failed to build");
				}
			}
		}
	}

}
