package com.comp4109.skipjack.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles file reading from a input stream and holds the file object for reading
 * 
 * @author bensweett
 * @version 1.2.0
 *
 */
public class FileReader {

	private File file;

	public FileReader(String filename, String pwd) {
		
		file = new File(pwd + filename);
		
	}

	
	/**
	 * Attempts to read the classes file from a fileinputstream and return it as a byte array.
	 * Exits the application if the file cannot be found or cannot be read.
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] readFile() throws IOException {

		if(!file.exists()) {
			System.err.println("No such input file '" + file.getAbsoluteFile() + "'.");
			return null;
		} else if(file.isDirectory()) {
			System.err.println("File '" + file.getAbsoluteFile() + "' is a directory.  Cannot read.");
			return null;
		}

		InputStream stream = new FileInputStream(file);
		
		try {
			return readStream(stream);
		} finally {
			stream.close();
		} 
		
	}
	
	
	/**
	 * Takes an input stream and returns it's content to a byte array.
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	private byte[] readStream(InputStream stream) throws IOException {

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		byte[] byteBuf = new byte[1024];
		int readCount = 0;

		while ((readCount = stream.read(byteBuf)) != -1) {
			bytesOut.write(byteBuf, 0, readCount);
		}

		return bytesOut.toByteArray();
	}

	
}
