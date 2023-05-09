package com.quark.security.random;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionIdentifierGenerator {
	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	public static void main(String[] args) {
		SessionIdentifierGenerator idGen = new SessionIdentifierGenerator();
		System.out.println(idGen.nextSessionId());
	}
}
