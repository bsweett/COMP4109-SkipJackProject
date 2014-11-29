package com.comp4109.skipjack.tests;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.primitives.UnsignedLong;

import com.comp4109.skipjack.blockcipher.*;


/**
 * JUnit tests for OutputFeedback class. Tests encryption and decryption with a preset key, IV and test
 * message.
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public class OFBSkipJackTests {

	
	/**
	 * Test encryption by setting a fixed key and IV for OFB and giving it the Skipjack Cipher and a test
	 * message to encrypt. 
	 * 
	 */
	@Test
	public void plainASCIIEncryptOFBTest() {
		
		SkipJackCipher cipher = new SkipJackCipher();
		OutputFeedback OFB = new OutputFeedback(cipher);
		HexEncoder encoder = new HexEncoder();
		String IV = "3132333435363738";
		
		OFB.setIV(Long.valueOf(IV, 16));
		OFB.setKey("0123456789");
		
		String pt = "I would also like to encrypt this message.";
		
		UnsignedLong[] encoded = encoder.encodeMessage(pt);
		UnsignedLong[] cipherblocks = OFB.encrypt(encoded);
		String decoded = encoder.decodeMessage(cipherblocks);
		
		
		assertEquals("Input: 33221100ddccbbaa with Key 00998877665544332211 must be 2587cae27a12d300", "1BTÂÙ?L]34rÀ¹ÁõòÉ^ÃÚî%¬wD¼«ìÐ* ;:Ø+T\"NR~ê", decoded);
		
	}
	
	
	/**
	 * Test decrypting by setting a fixed key and IV for OFB and giving it the Skipjack Cipher and a test
	 * message to encrypt. After its encrypted decrypt the resulting cipher blocks and decode the result.
	 * 
	 */
	@Test
	public void plainASCIIDecryptOFBTest() {
		
		SkipJackCipher cipher = new SkipJackCipher();
		OutputFeedback OFB = new OutputFeedback(cipher);
		HexEncoder encoder = new HexEncoder();
		String IV = "3132333435363738";
		
		OFB.setIV(Long.valueOf(IV, 16));
		OFB.setKey("0123456789");
		
		String pt = "I would also like to encrypt this message.";
		
		UnsignedLong[] encoded = encoder.encodeMessage(pt);
		UnsignedLong[] cipherblocks = OFB.encrypt(encoded);
		UnsignedLong[] plaintextblocks = OFB.decrypt(cipherblocks);
		String decoded = encoder.decodeMessage(plaintextblocks);
		
		assertEquals("Input: 33221100ddccbbaa with Key 00998877665544332211 must be 2587cae27a12d300", "I would also like to encrypt this message.", decoded);
		
	}

	
}
