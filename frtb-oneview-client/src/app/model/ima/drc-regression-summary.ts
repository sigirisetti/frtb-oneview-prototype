import { DrcRegressionData } from "./drc-regression-data";

export class DrcRegressionSummary {
    regressionData: DrcRegressionData;
    multipleR: number;
    rSquare: number;
    adjRSquare: number;
    observations: number;
    beta1: number;
    beta1_tstat: number;
    beta1_pvalue: number;
    beta2: number;
    beta2_tstat: number;
    beta2_pvalue: number;
}