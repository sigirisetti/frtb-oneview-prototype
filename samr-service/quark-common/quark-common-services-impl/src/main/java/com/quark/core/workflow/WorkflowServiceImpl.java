package com.quark.core.workflow;

import com.quark.core.data.CoreDataService;
import com.quark.core.security.Organization;
import com.quark.risk.common.model.WorkflowInstance;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	private CoreDataService coreDataService;

	@Autowired
	private WorkflowDao workflowDao;

	@Override
	public List<String> getWorflows() {
		Map<String, Object> tasks = appCtx.getBeansWithAnnotation(Workflow.class);
		List<String> processes = new ArrayList<>();
		for (Map.Entry<String, Object> me : tasks.entrySet()) {
			log.debug("Key : {} , value : {}", me.getKey(), me.getValue());
			Class<? extends Object> fooClass = me.getValue().getClass();
			Workflow annotation = fooClass.getAnnotation(Workflow.class);
			processes.add(annotation.name());
		}
		return processes;
	}

	@Override
	public List<WorkflowConfig> getWorkflowConfigs(Organization org) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkflowConfig.class)
				.add(Property.forName("organization.id").eq(org.getId()));
		return coreDataService.get(criteria);
	}

	@Override
	public List<WorkflowConfig> getWorkflowConfigs(String process, Organization org) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkflowConfig.class)
				.add(Property.forName("organization.id").eq(org.getId())).add(Restrictions.eq("process", process));
		return coreDataService.get(criteria);
	}

	@Override
	public List<WorkflowInstance> getAllWorkflowInstances(Organization organization) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkflowInstance.class)
				.add(Restrictions.eq("organization.id", organization.getId()));
		return coreDataService.get(criteria);
	}

	@Override
	public List<WorkflowInstance> getWorkflowInstances(String workflow, Organization organization) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkflowInstance.class).createAlias("workflow", "b")
				.add(Restrictions.eq("organization.id", organization.getId()))
				.add(Restrictions.eq("b.process", workflow));
		log.info(criteria.toString());
		return coreDataService.get(criteria);
	}

	@Override
	public WorkflowConfig saveOrUpdate(WorkflowConfig workflow) {
		return coreDataService.save(workflow);
	}

	@Override
	@Transactional
	public WorkflowInstance getWorkflowInstance(int excelDate, Long workflowId) {
		return workflowDao.getWorkflowInstance(excelDate, workflowId);
	}
}
