package com.quark.bean.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

public class BeanIntrospector {

	public static List<Class> getHibernateClasses() throws ClassNotFoundException {

		List<Class> classes = new ArrayList<>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));
		for (BeanDefinition bd : scanner.findCandidateComponents("com.quark")) {
			System.out.println(bd.getBeanClassName());
			classes.add(Class.forName(bd.getBeanClassName()));
		}
		return classes;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		for (Class c : getHibernateClasses()) {
			System.out.println(c);
		}
	}
}
