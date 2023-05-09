package com.quark.risk.frtb.samr.config;

import com.quark.refdata.model.Tenor;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IRVegaConfig {

	@Value("${samr.ir.option.maturity.tenors:0.5,1,3,5,10}")
	private String strOptionMaturityTenors;

	private double[] optionMaturityTenors;
	private int[] optionMaturityTenorCodes;

	@Value("${samr.ir.vega.opt.mat.alpha:0.01}")
	private double irVegaOptMatAlpha;

	@Value("${samr.ir.vega.opt.und.alpha:0.01}")
	private double irVegaUndMatAlpha;

	@Value("${samr.ir.vega.cross.bucket.corr:0.5}")
	private double irCrossBucketCorr;

	@Value("${samr.ir.vega.rw:0.55}")
	private double irVegaRw;

	@Value("${samr.ir.vega.lh:60}")
	private double irVegaLh;

	private RealMatrix irVegaCorr;

	private double rw;

	@PostConstruct
	private void init() {
		String tokens[] = strOptionMaturityTenors.split(",");
		optionMaturityTenors = new double[tokens.length];
		optionMaturityTenorCodes = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			optionMaturityTenors[i] = new Double(tokens[i]);
			optionMaturityTenorCodes[i] = (int) (optionMaturityTenors[i] * Tenor.DEFAULT_NUM_DAYS_IN_A_YEAR);
		}
		irVegaCorr = buildVegaCorrelation();
		rw = Math.min(irVegaRw * Math.sqrt(irVegaLh / 10), 1) * 100;
	}

	public RealMatrix getIRVegaCorr() {
		return new Array2DRowRealMatrix(irVegaCorr.getData());
	}

	public int[] getOptionMaturityTenorCodes() {
		return optionMaturityTenorCodes;
	}

	public double[] getOptionMaturityTenors() {
		return optionMaturityTenors;
	}

	public double getIrCrossBucketCorr() {
		return irCrossBucketCorr;
	}

	private RealMatrix buildVegaCorrelation() {
		int dim = optionMaturityTenors.length;
		irVegaCorr = new Array2DRowRealMatrix(dim, dim);
		RealMatrix optMatCorr = buildOptionMaturityCorrelation();
		RealMatrix undMatCorr = buildUnderlyingMaturityCorrelation();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				irVegaCorr.setEntry(i, j, optMatCorr.getEntry(i, j) * undMatCorr.getEntry(i, j));
			}
		}
		return irVegaCorr;
	}

	private RealMatrix buildOptionMaturityCorrelation() {
		int dim = optionMaturityTenors.length;
		RealMatrix optMatCorr = new Array2DRowRealMatrix(dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				optMatCorr.setEntry(i, j,
						Math.exp(-irVegaOptMatAlpha * Math.abs(optionMaturityTenors[i] - optionMaturityTenors[j])
								/ Math.min(optionMaturityTenors[i], optionMaturityTenors[j])));
			}
		}
		return optMatCorr;
	}

	private RealMatrix buildUnderlyingMaturityCorrelation() {
		int dim = optionMaturityTenors.length;
		RealMatrix undMatCorr = new Array2DRowRealMatrix(dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				undMatCorr.setEntry(i, j,
						Math.exp(-irVegaUndMatAlpha * Math.abs(optionMaturityTenors[i] - optionMaturityTenors[j])
								/ Math.min(optionMaturityTenors[i], optionMaturityTenors[j])));
			}
		}
		return undMatCorr;
	}

	public double getRw() {
		return rw;
	}
}
