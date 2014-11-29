package com.comp4109.skipjack.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles writing to a file using an output stream. Holds the file for writing.
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public class FileComposer {

	private File file;
	
	public FileComposer(String filename, String pwd) {
		file = new File(pwd + filename);
	}
	
	/**
	 * Writes the given content string to an file using an output stream. If the file doesn't exist it 
	 * creates it. Returns true if process was completed successfully returns false if there was an io
	 * exception or if file was a directory.
	 * 
	 * @param content
	 * @return
	 */
	public boolean writeFile(String content) {
		boolean result = false;
		
		try (OutputStream stream = new FileOutputStream(file)) {
			
			if(!file.exists()) {
				file.createNewFile();
			} else if(file.isDirectory()) {
				System.err.println("File '" + file.getAbsoluteFile() + "' is a directory.  Cannot write.");
				return result;
			}
			
			stream.write(content.getBytes());
			stream.flush();
			stream.close();
			return true;
			
		} catch (IOException e) {
			System.err.println("IOException caught while writting to file: " + e.getLocalizedMessage());
			return result;
		}
	}
}
