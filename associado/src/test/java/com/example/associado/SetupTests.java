package com.example.associado;

public abstract class SetupTests {

	public static void setupProperties() {
		System.setProperty("START_TIME", "" + System.currentTimeMillis());
	}
}
