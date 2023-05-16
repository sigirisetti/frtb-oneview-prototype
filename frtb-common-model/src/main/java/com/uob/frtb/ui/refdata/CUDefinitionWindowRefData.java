package com.uob.frtb.ui.refdata;

import com.uob.frtb.marketdata.curve.CurveUnderlying;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CUDefinitionWindowRefData<T extends CurveUnderlying> extends WindowRefData implements Serializable {

	private static final long serialVersionUID = 9137717997404138148L;

	private List<T> underlyings;
}
