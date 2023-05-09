package com.quark.core.workflow;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkflowTriggerHelper {

	@Autowired
	private RuntimeService runtimeService;

	public void triggerWorkflow(String name, Map<String, Object> variables) {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(name, variables);
		log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
	}
}
