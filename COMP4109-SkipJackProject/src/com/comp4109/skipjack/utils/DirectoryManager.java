package com.comp4109.skipjack.utils;

import java.io.File;

public class DirectoryManager {

	private final String OS = System.getProperty("os.name").toLowerCase();
	private String PWD = "";
	
	public DirectoryManager() {
		setPwdByOS();
	}
	
	/**
	 * Sets the PWD for the application to a folder in the users home directory. Creates 
	 * the folder if it doesn't exist.
	 * 
	 */
	private void setPwdByOS() {
		String home = System.getProperty("user.home");
		String pwd = "";

		if (isWindows()) {
			pwd = home + "\\SkipJack_Demo\\";
		} else if (isMac() || isUnix()) {
			pwd = home + "/SkipJack_Demo/";
		}

		boolean exists = createDirectory(pwd);
		if (!exists) {
			System.err.println("Failed to create PWD: " + pwd);
		}
		
		this.setPWD(pwd);
	}

	/**
	 * Creates a directory at a given path if it doesn't exist.
	 * 
	 * @param path
	 * @return
	 */
	private boolean createDirectory(String path) {
		boolean success = false;

		File dir = new File(path);
		if (!dir.exists()) {
			try {
				success = dir.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			success = true;
		}

		return success;
	}
	
	/**
	 * Checks if the current OS is windows
	 * 
	 * @return boolean
	 */
	private boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	/**
	 * Checks if the current OS is Mac
	 * 
	 * @return boolean
	 */
	private boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}
	
	/**
	 * Checks if the current OS is unix
	 * 
	 * @return boolean
	 */
	private boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public String getPWD() {
		return PWD;
	}

	public void setPWD(String pWD) {
		PWD = pWD;
	}
}
