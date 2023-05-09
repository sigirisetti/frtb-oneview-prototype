package com.quark.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Auditable implements Serializable {

	private static final long serialVersionUID = -4631563647629745234L;

	@Version
	private Long version;

	@Column(name = "created_by", length = 32)
	private String createBy;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "last_updated_by", length = 32)
	private String lastUpdatedBy;

	@Column(name = "updated_on")
	private Date updatedOn;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
}
