package com.quark.web.file.upload.utils;

import com.quark.web.risk.frtb.samr.view.model.FileStatus;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadResponse {
	List<FileStatus> fileStatus = new ArrayList<>();

	public void addFileStatus(String fn, String status) {
		fileStatus.add(new FileStatus(fn, status));
	}
}
