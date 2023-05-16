package com.uob.frtb.risk.samr.results;

import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.samr.csv.model.SAMRData;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_validation_messages")
public class SAMRValidationMessages {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_id")
	private WorkflowInstance workflowInstance;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "validation_messages")
	private byte[] msgs;

	@Transient
	private Map<String, List<SAMRData>> validationMessages;

	public void setMsgs(byte[] msgs) {
		this.msgs = msgs;
		this.validationMessages = SerializationUtils.deserialize(msgs);
	}

	public void setValidationMessages(Map<String, List<SAMRData>> validationMessages) {
		this.validationMessages = validationMessages;
		this.msgs = SerializationUtils.serialize((HashMap<String, List<SAMRData>>) validationMessages);
	}
}
