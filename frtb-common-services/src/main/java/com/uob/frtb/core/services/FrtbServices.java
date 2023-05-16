package com.uob.frtb.core.services;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FrtbServices {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("frtb-common-dao.xml", "frtb-common-dao.xml",
				"frtb-common-services.xml");
		System.out.println("FRTB Services started");
		context.registerShutdownHook();
	}
}
