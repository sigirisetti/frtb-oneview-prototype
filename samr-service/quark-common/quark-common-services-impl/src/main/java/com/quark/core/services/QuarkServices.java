package com.quark.core.services;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuarkServices {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("quark-dao.xml", "quark-dao.xml",
				"quark-services.xml");
		System.out.println("Quark Services started");
		context.registerShutdownHook();
	}
}
