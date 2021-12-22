package com.olexiy.tourguideModule.helper;

public class InternalTestHelper {

	// Set this default up to 100,000 for testing
	private static int internalUserNumber = 5000;

	private static boolean testProfile = false;
	
	public static void setInternalUserNumber(int internalUserNumber) {
		InternalTestHelper.internalUserNumber = internalUserNumber;
	}
	
	public static int getInternalUserNumber() {
		return internalUserNumber;
	}

	public static boolean setTestProfile(boolean mode) {
		testProfile = mode;
		return testProfile;
	}

	public static boolean isTestProfile() {
		return testProfile;
	}
}
