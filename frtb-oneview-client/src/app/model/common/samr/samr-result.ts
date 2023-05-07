import { SamrResultLineItem } from "./samr-result-line-item";

export class SamrResult {
    currency: string;
    totalRiskCharge: number;
    results: SamrResultLineItem[];
}

