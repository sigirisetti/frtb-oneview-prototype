package com.quark.refdata.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "name_value_ref_data")
@IdClass(NameValuePairPk.class)
public class NameValuePair implements Serializable {

	private static final long serialVersionUID = -8936867638237525384L;

	@Id
	@Column(name = "ref_type", length = 64)
	private String refType;
	@Id
	private Integer seq;

	@Id
	@Column(length = 64)
	private String name;

	@Column(length = 64)
	private String value;

	@Column
	private Boolean isDefault;

	public boolean isComplete() {
		return StringUtils.isNotBlank(name) && StringUtils.isNotBlank(value);
	}

	public boolean equalsRefTypeAndName(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NameValuePair other = (NameValuePair) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (refType == null) {
            return other.refType == null;
        } else return refType.equals(other.refType);
    }

}
