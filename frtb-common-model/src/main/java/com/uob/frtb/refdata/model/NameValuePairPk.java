package com.uob.frtb.refdata.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NameValuePairPk implements Serializable {
	private static final long serialVersionUID = -2445202693074377301L;
	private String refType;
	private Integer seq;
	private String name;
}
