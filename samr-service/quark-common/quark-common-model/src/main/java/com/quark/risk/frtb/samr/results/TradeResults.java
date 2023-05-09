package com.quark.risk.frtb.samr.results;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class TradeResults extends IntermediateResult {

    private static final long serialVersionUID = 2329008173496147865L;
    private String tradeIdentifier;
}
