export class SamrResultLineItem {
    identifier: string;
    parentName: string;
    nodeName: string;
    showTradeResults: boolean;
    amountLowCorr: number;
    amountBaseLowCorr: number;
    amount: number;
    amountBase: number;
    amountHighCorr: number;
    amountBaseHighCorr: number;
    children: SamrResultLineItem[];
}

