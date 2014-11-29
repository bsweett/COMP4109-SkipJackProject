package com.comp4109.skipjack.tests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.comp4109.skipjack.blockcipher.SkipJackCipher;

/**
 * JUnit tests for SkipJackCipher class. Tests encryption and decryption with a preset key and test
 * message.
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public class BasicSkipJackTests {
	
	
	/**
	 * Test encryption by generating sub keys for given test key, encrypting the plain text and
	 * comparing the results with the NSA documentation.
	 * 
	 */
	@Test
	public void hexBlockVectorEncryptionTest() {
		
		String pt = "33221100ddccbbaa";
		String testKey = "00998877665544332211";
		SkipJackCipher cipher = new SkipJackCipher();
		
		int[] key = new int[10];
		BigInteger mask = new BigInteger("FF", 16);
		BigInteger k = new BigInteger(testKey, 16);
		
		for(int i = 0, v = 9; i < 10; ++i, --v) {
			key[v] = k.shiftRight(i*8).and(mask).shortValue();
		}
		
		cipher.setKey(key);
		
		long result = cipher.encryptBlock(Long.valueOf(pt, 16));
		
		assertEquals("Input: 33221100ddccbbaa with Key 00998877665544332211 must be 2587cae27a12d300", 
				"2587cae27a12d300", Long.toHexString(result));
		
	}
	
	
	/**
	 * Test decryption by generating sub keys for given test key, decrypting the cipher text and
	 * comparing the results with the NSA documentation.
	 * 
	 */
	@Test
	public void hexBlockVectorDecryptionTest() {
		
		String ct = "2587cae27a12d300";
		String testKey = "00998877665544332211";
		SkipJackCipher cipher = new SkipJackCipher();
		
		int[] key = new int[10];
		BigInteger mask = new BigInteger("FF", 16);
		BigInteger k = new BigInteger(testKey, 16);
		
		for(int i = 0, v = 9; i < 10; ++i, --v) {
			key[v] = k.shiftRight(i*8).and(mask).shortValue();
		}
		
		cipher.setKey(key);
		
		long result = cipher.decryptBlock(Long.valueOf(ct, 16));
		
		assertEquals("Input: 2587cae27a12d300 with Key 00998877665544332211 must be 33221100ddccbbaa", 
				"33221100ddccbbaa", Long.toHexString(result));
		
	}

	
}
