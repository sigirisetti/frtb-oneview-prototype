package com.quark.lang.reflect;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.*;

public class ReflectUtil {

	public static Map<String, String> getSubClassesAsMap(Class c) throws ClassNotFoundException {

		Map<String, String> m = new TreeMap<String, String>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		provider.addIncludeFilter(new AssignableTypeFilter(c));

		// scan in org.example.package
		Set<BeanDefinition> components = provider.findCandidateComponents(c.getPackage().getName());
		for (BeanDefinition component : components) {
			Class cls = Class.forName(component.getBeanClassName());
			m.put(cls.getSimpleName(), cls.getName());
		}
		return m;
	}

	public static List<String> getSubClassesSimpleNames(Class c) throws ClassNotFoundException {

		List<String> l = new ArrayList<String>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
		provider.addIncludeFilter(new AssignableTypeFilter(c));

		// scan in org.example.package
		Set<BeanDefinition> components = provider.findCandidateComponents(c.getPackage().getName());
		for (BeanDefinition component : components) {
			Class cls = Class.forName(component.getBeanClassName());
			l.add(cls.getSimpleName());
		}
		return l;
	}

}
