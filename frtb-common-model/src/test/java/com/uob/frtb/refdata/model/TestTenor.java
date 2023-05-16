package com.uob.frtb.refdata.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTenor {

	@Test
	public void testParseTenor() {
		Tenor quaterYear = Tenor.parseTenor("3M");
		assertEquals(90, quaterYear.getNumberOfDays());
	}

}
