package com.olexiy.tourguideModule.helper;

public class InternalTestHelper {

	// Set this default up to 100,000 for testing
	private static int internalUserNumber = 5000;
	
	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}
	
	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}