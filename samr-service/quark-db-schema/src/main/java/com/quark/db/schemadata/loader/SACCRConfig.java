package com.quark.db.schemadata.loader;

import com.quark.common.dao.CoreDao;
import com.quark.risk.saccr.model.AssetClass;
import com.quark.risk.saccr.model.RequiredTradeAttributes;
import com.quark.risk.saccr.model.SingleNameOrIndex;
import com.quark.risk.saccr.model.SupervisoryParameters;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SACCRConfig {

	@Autowired
	private CoreDao coreDao;

	@Value("${saccr.data.csv.req.trade.attrs.fn:data/csv/saccr/config/RequiredTradeAttributes.csv}")
	private String reqTradeAttributes;

	@Value("${saccr.data.csv.sup.params.attrs.fn:data/csv/saccr/config/SupervisoryParameters.csv}")
	private String supervisoryParameters;

	public void loadSACCRConfig() {
		loadSupervisoryParameters();
		loadSACCRRequiredTradeAttributes();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	private void loadSupervisoryParameters() {
		try {
			List<String> lines = FileUtils.readLines(new File(supervisoryParameters), Charset.defaultCharset());
			List<SupervisoryParameters> configList = new ArrayList<>();
			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				if (StringUtils.isNoneBlank(line)) {
					String[] tokens = line.split(",", -1);
					SupervisoryParameters r = new SupervisoryParameters();
					int k = 0;
					r.setAssetClass(AssetClass.valueOf(tokens[k++]));
					r.setSingleNameOrIndex(SingleNameOrIndex.valueOf(tokens[k++]));
					r.setSubClass(tokens[k++]);
					String supFac = tokens[k++];
					if (NumberUtils.isNumber(supFac)) {
						r.setSupervisoryFactor(new Double(supFac));
					}

					String corr = tokens[k++];
					if (NumberUtils.isNumber(corr)) {
						r.setCorrelation(new Double(corr));
					}

					String vol = tokens[k++];
					if (NumberUtils.isNumber(vol)) {
						r.setSupervisoryOptionVol(new Double(vol));
					}
					configList.add(r);
				}
			}
			coreDao.save(configList);
		} catch (Exception e) {
			System.out.println("Current directory : " + new File(".").getAbsolutePath());
			e.printStackTrace();
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	private void loadSACCRRequiredTradeAttributes() {
		try {
			List<String> lines = FileUtils.readLines(new File(reqTradeAttributes), Charset.defaultCharset());
			List<RequiredTradeAttributes> configList = new ArrayList<>();
			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				if (StringUtils.isNoneBlank(line)) {
					String[] tokens = line.split(",", -1);
					RequiredTradeAttributes r = new RequiredTradeAttributes();
					int k = 0;
					r.setProductName(tokens[k++]);
					r.setAssetClass(tokens[k++]);
					r.setDescription(tokens[k++]);
					r.setProduct("Y".equalsIgnoreCase(tokens[k++]));
					r.setLegalEntity("Y".equalsIgnoreCase(tokens[k++]));
					r.setLaName("Y".equalsIgnoreCase(tokens[k++]));
					r.setCsaName("Y".equalsIgnoreCase(tokens[k++]));
					r.setPo("Y".equalsIgnoreCase(tokens[k++]));
					r.setTradeId("Y".equalsIgnoreCase(tokens[k++]));
					r.setSourceSystem("Y".equalsIgnoreCase(tokens[k++]));
					r.setOtcType("Y".equalsIgnoreCase(tokens[k++]));
					r.setNpvCcy("Y".equalsIgnoreCase(tokens[k++]));
					r.setNpv("Y".equalsIgnoreCase(tokens[k++]));
					r.setStartDate("Y".equalsIgnoreCase(tokens[k++]));
					r.setEndDate("Y".equalsIgnoreCase(tokens[k++]));
					r.setNextExerciseDate("Y".equalsIgnoreCase(tokens[k++]));
					r.setBuySell("Y".equalsIgnoreCase(tokens[k++]));
					r.setPutCall("Y".equalsIgnoreCase(tokens[k++]));
					r.setPayRefIndex("Y".equalsIgnoreCase(tokens[k++]));
					r.setRecRefIndex("Y".equalsIgnoreCase(tokens[k++]));
					r.setPayoffCcy("Y".equalsIgnoreCase(tokens[k++]));
					r.setPayOffAmount("Y".equalsIgnoreCase(tokens[k++]));
					r.setNotionalCcy("Y".equalsIgnoreCase(tokens[k++]));
					r.setNotional("Y".equalsIgnoreCase(tokens[k++]));
					r.setQuantity("Y".equalsIgnoreCase(tokens[k++]));
					r.setTickSize("Y".equalsIgnoreCase(tokens[k++]));
					r.setUnderlyingPrice("Y".equalsIgnoreCase(tokens[k++]));
					r.setStrike("Y".equalsIgnoreCase(tokens[k++]));
					r.setQuantoFXRate("Y".equalsIgnoreCase(tokens[k++]));
					r.setPayCcy("Y".equalsIgnoreCase(tokens[k++]));
					r.setRecCcy("Y".equalsIgnoreCase(tokens[k++]));
					r.setPayNotional("Y".equalsIgnoreCase(tokens[k++]));
					r.setRecNotional("Y".equalsIgnoreCase(tokens[k++]));
					r.setSubCls("Y".equalsIgnoreCase(tokens[k++]));
					r.setUnderlyingProduct("Y".equalsIgnoreCase(tokens[k++]));
					r.setSettlementType("Y".equalsIgnoreCase(tokens[k++]));
					r.setEarlyTerminationDate("Y".equalsIgnoreCase(tokens[k++]));
					r.setOptionType("Y".equalsIgnoreCase(tokens[k++]));
					r.setStrike2("Y".equalsIgnoreCase(tokens[k++]));
					configList.add(r);
				}
			}
			coreDao.save(configList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
