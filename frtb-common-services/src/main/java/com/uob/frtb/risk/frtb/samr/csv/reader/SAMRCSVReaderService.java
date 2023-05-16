package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.common.csv.reader.BaseCsvReaderService;
import com.uob.frtb.risk.common.csv.reader.CsvReader;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.frtb.samr.csv.model.SAMRData;
import com.uob.frtb.risk.frtb.samr.results.SAMRValidationMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SAMRCSVReaderService extends BaseCsvReaderService {

	@Value("${samr.ir.delta.filename:IRDelta}")
	private String irDeltaFilename;

	@Value("${samr.ir.vega.filename:IRVega}")
	private String irVegaFilename;

	@Value("${samr.ir.curvature.filename:IRCurvature}")
	private String irCurvatureFilename;

	@Value("${samr.fx.delta.filename:FXDelta}")
	private String fxDeltaFilename;

	@Value("${samr.fx.vega.filename:FXVega}")
	private String fxVegaFilename;

	@Value("${samr.fx.curvature.filename:FXCurvature}")
	private String fxCurvatureFilename;

	@Value("${samr.eq.delta.filename:EQDelta}")
	private String eqDeltaFilename;

	@Value("${samr.eq.vega.filename:EQVega}")
	private String eqVegaFilename;

	@Value("${samr.eq.curvature.filename:EQCurvature}")
	private String eqCurvatureFilename;

	@Value("${samr.cr.non.sec.delta.filename:CRNonSecDelta}")
	private String crNonSecDeltaFilename;

	@Value("${samr.cr.non.sec.vega.filename:CRNonSecVega}")
	private String crNonSecVegaFilename;

	@Value("${samr.cr.non.sec.curvature.filename:CRNonSecCurvature}")
	private String crNonSecCurvatureFilename;

	@Value("${samr.comm.delta.filename:COMMDelta}")
	private String commDeltaFilename;

	@Value("${samr.comm.vega.filename:COMMVega}")
	private String commVegaFilename;

	@Value("${samr.comm.curvature.filename:COMMCurvature}")
	private String commCurvatureFilename;

	@Value("${samr.drc.non.sec.filename:DRCNonSec}")
	private String drcNonSecFilename;

	@Value("${samr.drc.sec.non.ctp.filename:DRCSecNonCTP}")
	private String drcSecNonCtpFilename;

	@Value("${samr.drc.sec.ctp.filename:DRCSecCTP}")
	private String drcSecCtpFilename;

	@Value("${samr.residuals.filename:Residuals}")
	private String residualsFilename;

	@Value("${samr.cr.issuer.info.filename:CreditIssuerInfo}")
	private String crIssuerInfoFilename;

	@Value("${samr.market.data.filename:MarketData}")
	private String marketDataFilename;

	@Value("${samr.hierarchy.filename:Hierarchy}")
	private String hierarchyFilename;

	@Value("${samr.equity.info.filename:EquityInfo}")
	private String eqInfoFilename;

	@Value("${samr.comm.info.filename:CommodityInfo}")
	private String commodityInfoFilename;

	@Autowired
	private CoreDao coreDao;

	protected Map<String, CsvReader> init() {
		Map<String, CsvReader> readers = new LinkedHashMap<>();
		readers.put(irDeltaFilename, new IRDeltaCsvReader(irDeltaFilename, coreDao));
		readers.put(irVegaFilename, new IRVegaCsvReader(irVegaFilename, coreDao));
		readers.put(irCurvatureFilename, new IRCurvatureCsvReader(irCurvatureFilename, coreDao));

		readers.put(fxDeltaFilename, new FXDeltaCsvReader(fxDeltaFilename, coreDao));
		readers.put(fxVegaFilename, new FXVegaCsvReader(fxVegaFilename, coreDao));
		readers.put(fxCurvatureFilename, new FXCurvatureCsvReader(fxCurvatureFilename, coreDao));

		readers.put(eqDeltaFilename, new EquityDeltaCsvReader(eqDeltaFilename, coreDao));
		readers.put(eqVegaFilename, new EquityVegaCsvReader(eqVegaFilename, coreDao));
		readers.put(eqCurvatureFilename, new EquityCurvatureCsvReader(eqCurvatureFilename, coreDao));

		readers.put(crNonSecDeltaFilename, new CRNonSecDeltaCsvReader(crNonSecDeltaFilename, coreDao));
		readers.put(crNonSecVegaFilename, new CRNonSecVegaCsvReader(crNonSecVegaFilename, coreDao));
		readers.put(crNonSecCurvatureFilename, new CRNonSecCurvatureCsvReader(crNonSecCurvatureFilename, coreDao));

		readers.put(commDeltaFilename, new CommodityDeltaCsvReader(commDeltaFilename, coreDao));
		// readers.put(commVegaFilename, new
		// CRNonSecVegaCsvReader(crNonSecVegaFilename, coreDao));
		// readers.put(commCurvatureFilename, new
		// CRNonSecCurvatureCsvReader(crNonSecCurvatureFilename, coreDao));

		readers.put(drcNonSecFilename, new DRCNonSecCsvReader(drcNonSecFilename, coreDao));
		readers.put(drcSecNonCtpFilename, new DRCSecNonCTPCsvReader(drcSecNonCtpFilename, coreDao));
		readers.put(drcSecCtpFilename, new DRCSecCTPCsvReader(drcSecCtpFilename, coreDao));

		readers.put(residualsFilename, new ResidualsCsvReader(residualsFilename, coreDao));

		// Info files
		readers.put(marketDataFilename, new MarketDataCsvReader(marketDataFilename, coreDao));
		readers.put(hierarchyFilename, new HierarchyCsvReader(hierarchyFilename, coreDao));
		readers.put(eqInfoFilename, new EquityInfoCsvReader(eqInfoFilename, coreDao));
		readers.put(crIssuerInfoFilename, new CreditIssuerInfoCsvReader(crIssuerInfoFilename, coreDao));
		readers.put(commodityInfoFilename, new CommodityInfoCsvReader(commodityInfoFilename, coreDao));

		return readers;
	}

	@Transactional
	public Map<String, List<SAMRData>> validate(WorkflowInstance wfInst) throws ApplicationException, IOException {
		String dir = getUploadDir(wfInst);
		Map<String, CsvReader> readers = init();
		Map<String, List<SAMRData>> failedValidation = new HashMap<>();
		for (Map.Entry<String, CsvReader> me : readers.entrySet()) {
			log.info("Reading SAMR tab {} using reader {}", me.getKey(), me.getValue().getClass().getName());
			if (!dir.endsWith("/")) {
				dir = dir.concat("/");
			}
			String path = dir.concat(me.getKey().concat(".csv"));
			File f = new File(path);
			if (!f.exists()) {
				log.info("{} sensitivity file is not uploaded. Checked path {}", me.getKey(), path);
				continue;
			}
			me.getValue().setupCsvReader();
			me.getValue().setWorkflowInstance(wfInst);
			me.getValue().read(path);
			List<SAMRData> data = ((BaseSAMRCsvReader) me.getValue()).getSamrData();
			List<SAMRData> failed = new ArrayList<>();
			for (SAMRData d : data) {
				if (d.getMessages() != null) {
					failed.add(d);
				}
			}
			failedValidation.put(me.getKey(), failed);
		}
		saveValidationMessages(wfInst, failedValidation);
		return failedValidation;
	}

	private void saveValidationMessages(WorkflowInstance wfInst, Map<String, List<SAMRData>> failedValidation) {
		SAMRValidationMessages msgs = new SAMRValidationMessages();
		msgs.setValidationMessages(failedValidation);
		msgs.setWorkflowInstance(wfInst);
		coreDao.save(msgs);
	}
}
