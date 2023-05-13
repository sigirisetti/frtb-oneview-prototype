package com.quark.web.file.upload.utils;

import com.quark.core.security.Organization;
import com.quark.core.workflow.WorkflowConfig;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.common.model.WorkflowInstanceProperties;
import com.quark.web.risk.frtb.samr.view.model.FileUploadForm;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
@Getter
public class FileUploadHelper {

    @Value("${samr.file.upload.dir:tmp}")
	private String fileUploadDir;

	private WorkflowInstance workflowInst;
	private FileUploadResponse response;

    public void saveUploads(String module, Organization org, FileUploadForm uploadForm) {
		int excelDate = (int) DateUtil.getExcelDate(uploadForm.getValueDate());
		MultipartFile[] files = uploadForm.getFiles();
		response = new FileUploadResponse();
		workflowInst = new WorkflowInstance();
		workflowInst.setExcelDate(excelDate);
		WorkflowConfig workflowConfig = new WorkflowConfig();
		workflowConfig.setId(uploadForm.getWorkflowId());
		workflowInst.setWorkflow(workflowConfig);
		workflowInst.setOrganization(org);
		Set<WorkflowInstanceProperties> wfInstProps = new HashSet<>();
		File uploadDir = getFileUploadDir(module, excelDate, uploadForm.getWorkflowId(), String.valueOf(org.getId()));
		if (null != files && files.length != 0) {
			for (MultipartFile multipartFile : files) {
				String fileName = multipartFile.getOriginalFilename();
				File fullPath = new File(uploadDir, fileName);
				try (FileOutputStream fos = new FileOutputStream(fullPath)) {
					log.info(String.format("Writing upload '%s' file to '%s'", fileName, uploadDir.getAbsolutePath()));
					fos.write(multipartFile.getBytes());
					response.addFileStatus(fileName, "Success");
					WorkflowInstanceProperties uf = new WorkflowInstanceProperties(workflowInst, fileName,
							fullPath.getAbsolutePath());
					uf.setWorkflowInstance(workflowInst);
					wfInstProps.add(new WorkflowInstanceProperties(workflowInst, fileName, fullPath.getAbsolutePath()));
				} catch (Exception e) {
					String msg = String.format("Failed to write file %s to %s", fileName, uploadDir);
					log.error(msg, e);
					response.addFileStatus(fileName, "Failed : " + e.getMessage());
				}
			}
		}
		WorkflowInstanceProperties uf = new WorkflowInstanceProperties(workflowInst, "uploadDir",
				uploadDir.getAbsolutePath());
		wfInstProps.add(uf);
		workflowInst.setProperties(wfInstProps);
	}

    private File getFileUploadDir(String module, int excelDate, Long workflowId, String orgId) {
		String target = fileUploadDir;
		if (StringUtils.isBlank(target)) {
			target = System.getProperty("java.io.tmpdir");
		}
		if (!target.endsWith("/")) {
			target = target.concat("/");
		}
		target = target.concat("asterix/uploads/").concat(module);
		if (!target.endsWith("/")) {
			target = target + "/";
		}
		target = target.concat(orgId).concat("/");
		target = target.concat(String.valueOf(excelDate)).concat("/").concat(String.valueOf(workflowId));
		File f = new File(target);
		if (!f.exists()) {
			boolean created = f.mkdirs();
			if (!created) {
				log.error("Unable to create file upload dir : " + target);
				return null;
			}
		}
		return f;
	}
}
