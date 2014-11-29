package com.comp4109.skipjack.demo;

import java.io.IOException;
import java.util.Scanner;

import com.google.common.primitives.UnsignedLong;

import com.comp4109.skipjack.utils.DirectoryManager;
import com.comp4109.skipjack.utils.FileComposer;
import com.comp4109.skipjack.utils.FileReader;
import com.comp4109.skipjack.blockcipher.OutputFeedback;
import com.comp4109.skipjack.blockcipher.HexEncoder;


/**
 * Cipher Demo controls the encrypting/decrypting from the console and files as well as text
 * encoding and decoding.
 * 
 * @author bensweett
 * @version 1.1.0
 *
 */
public class CipherDemo {

	// Mode of operation with cipher and the hex encoder
	private OutputFeedback OFB;
	private HexEncoder encoder;

	// File writer, reader, directory manager
	private FileReader reader;
	private FileComposer writer;
	private final DirectoryManager dirManager;
	
	// Input Scanner
	private Scanner sc;

	
	public CipherDemo(OutputFeedback OFB) { 
		
		this.OFB = OFB;
		this.encoder = new HexEncoder();
		this.dirManager = new DirectoryManager();
		
	}

	
	/**
	 * Prompts user to enter text for encrypting in the console and handles any input errors.
	 * Sends the text to be encrypted to get the output and send the text to be output to
	 * the console. Returns a boolean if its finished or not.
	 * 
	 * @return true or false
	 */
	public boolean showConsole() {
		
		sc = new Scanner(System.in);
		String input = "";

		print("Encrypt: Please enter plaintext to encrypt.. \n-> ");

		try {
			input = sc.nextLine();
		} catch (Exception e) {}

		if(input.length() == 0) {
			print("No plaintext entered.\nPress enter to continue...");
			sc.nextLine();
			sc.nextLine();
			return false;
		} else {
			String output = runCipher(input);
			displayConsoleOutput(output);
			return true;
		}
		
	}

	
	/**
	 * Prompts user to enter a file name for encrypting from the file and handles any input
	 * errors. Gets the text from a file reader and checks to make sure there isn't a problem
	 * with in before getting it encrypted and sending the results to a file writer. If no
	 * issues calls for a success message to be played.
	 * 
	 * @return
	 */
	public boolean showFile() {
		
		sc = new Scanner(System.in);
		String input = "";
		boolean result = false;

		String pwd = getDirManager().getPWD();
		print("Encrypt: Place file to encrypt in: " + pwd);
		print("Encrypt: Please enter filename to encrypt.. \n-> ");

		try {
			input = sc.nextLine();
		} catch (Exception e) {}

		if(input.length() == 0) {
			print("No filename entered.\nPress enter to continue...");
			sc.nextLine();
			sc.nextLine();
			return result;
		} else {
			String plaintext = handleInFile(input);

			if(!plaintext.contains("Error")) {
				String output = runCipher(plaintext);
				result = handleOutFile("SkipJack-Output.txt", output);
			}
		}

		displayFileOutput(result, "SkipJack-Output.txt");		
		return result;
		
	}

	
	/**
	 * Displays a message based on the result from encrypting from file.
	 * 
	 * @param result
	 * @param outfile
	 */
	private void displayFileOutput(boolean result, String outfile) {
		
		String pwd = getDirManager().getPWD();
		
		if(!result)  {
			print("File encryption failed. Please try again.");
		} else {
			print("Encryption successful. Ciphertext is in: " + pwd + outfile + ".");
		}
		
	}

	
	/**
	 * Returns string from the bytes taken from a file reader reading from the filename
	 * given.
	 * 
	 * @param filename
	 * @return contents or error
	 */
	private String handleInFile(String filename) {
		
		this.reader = new FileReader(filename, getDirManager().getPWD());

		try {
			byte[] contents = this.reader.readFile();
			return new String(contents);
		} catch (IOException e) {
			return "Error: " + e.getLocalizedMessage();
		}
		
	}

	/**
	 * Returns a boolean if writing the given contents to a given filename is successful
	 * using a file writer.
	 * 
	 * @param filename
	 * @param content
	 * @return
	 */
	private boolean handleOutFile(String filename, String content) {
		
		this.writer = new FileComposer(filename, getDirManager().getPWD());
		return this.writer.writeFile(content);
		
	}

	
	/**
	 * Encodes the given text using the hex encoder passes results to be encrypted. Decrypts
	 * the ciphertext blocks generated and returns the result by decoding both sets of blocks
	 * from encrypting and decrypting.
	 * 
	 * @param text
	 * @return
	 */
	private String runCipher(String text) {

		UnsignedLong[] blocks = this.encoder.encodeMessage(text);
		UnsignedLong[] ciphertext = this.OFB.encrypt(blocks);	
		UnsignedLong[] plaintext = this.OFB.decrypt(ciphertext);
		return "Plaintext: " + this.encoder.decodeMessage(plaintext) + "\n\n" + "Ciphertext: " + this.encoder.decodeMessage(ciphertext);

	}

	
	/**
	 * Displays encryption result given to the console.
	 * 
	 * @param out
	 */
	private void displayConsoleOutput(String out) {
		print("Encryption returned: \n\n" + out);
		print("");
	}

	
	/**
	 * Prints a given string to the standard out
	 * 
	 * @param s
	 */
	private void print(String s) {
		
		System.out.println(s);
		
	}
	
	/**
	 * Gets the directory manager
	 * 
	 * @return dirManager
	 */
	public DirectoryManager getDirManager() {
		return this.dirManager;
	}
	
}
