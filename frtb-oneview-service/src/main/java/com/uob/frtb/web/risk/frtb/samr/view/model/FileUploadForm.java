package com.uob.frtb.web.risk.frtb.samr.view.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadForm {
	private Date valueDate;
	private Long workflowId;
	private MultipartFile[] files;
}
