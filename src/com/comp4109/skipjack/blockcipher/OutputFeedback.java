package com.comp4109.skipjack.blockcipher;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.google.common.primitives.UnsignedLong;

import com.comp4109.skipjack.utils.Utilities;

/**
 * Extends the functionality of the SkipJack Cipher using the OFB mode of Operation.
 * Has functions for setting the IV and key as well as randomly generating. 
 * 
 * @author bensweett
 * @version 1.1.0
 *
 */
public class OutputFeedback {
	
	// Cipher and IV
	private SkipJackCipher cipher;
	private long IV;
	
	
	public OutputFeedback(SkipJackCipher cipher) {
		
		this.cipher = cipher;
		this.IV = 0L;
		
	}
	
	
	/**
	 * Returns the IV
	 * 
	 * @return
	 */
	public long getIV() {	
		
		return IV;
		
	}
	
	
	/**
	 * Sets the IV
	 * 
	 * @param iv
	 */
	public void setIV(long iv) {
		
		this.IV = iv;
		
	}
	
	
	/**
	 * Randomly generates an IV using a preset alphabet and a secure random number 
	 * generated. Does some conversion on the type to keep it unsigned and then sets
	 * it using the setter.
	 * 
	 */
	public void generateRandomIV() {
		
		SecureRandom random = new SecureRandom();
	    char[] result = new char[8];
	    
	    for (int i = 0; i < result.length; i++) {
	        int randomCharIndex = random.nextInt(Utilities.ALPHABET.length);
	        result[i] = Utilities.ALPHABET[randomCharIndex];
	    }
	    
	    String value = new String(result);
		String hex = Utilities.toHex(value);
		long asLong = Long.valueOf(hex, 16);
		String unsign = Long.toUnsignedString(asLong, 16);
		
		setIV(Long.valueOf(unsign, 16));
		
	}
	
	
	/**
	 * Sets the key but breaking it into an array of integers 1 byte each. Uses the
	 * index of each byte as the key schedule order for the SkipJack cipher.
	 * 
	 * @param input
	 * @return
	 */
	public boolean setKey(String input) {
		
		if(input.length() == 10) {
			
			try {
				
				String hexcode = Utilities.toHex(input);
				
				int[] key = new int[10];
				BigInteger mask = new BigInteger("FF", 16);
				BigInteger k = new BigInteger(hexcode, 16);
				for(int i = 0, v = 9; i < 10; ++i, --v) {
					key[v] = k.shiftRight(i*8).and(mask).shortValue();
				}
				
				cipher.setKey(key);
				
				return true;
				
			} catch (Exception e) {
				
				System.err.println("Exception setting key: " + e.getLocalizedMessage());
				return false;
				
			}
		}
		
		return false;
		
	}
	
	
	/**
	 * Encrypts blocks of unsigned long plain text using OFB. 
	 * O[0] is encrypted IV
	 * O[i] is encrypted o - 1
	 * ct[i] is pt[i] xor o[i]
	 * 
	 * @param pt
	 * @return ct
	 */
	public UnsignedLong[] encrypt(UnsignedLong[] pt) {
		
		UnsignedLong[] ct = new UnsignedLong[pt.length];
		UnsignedLong[] o = new UnsignedLong[pt.length];

		o[0] = UnsignedLong.fromLongBits(cipher.encryptBlock(getIV()));

		for(int k = 1; k < o.length; k++) {		
			long oMinusIndex = o[k - 1].longValue();
			o[k] = UnsignedLong.fromLongBits(cipher.encryptBlock(oMinusIndex));			
		}
		
		for(int i = 0; i < pt.length; i++) {
			long ptIndex = pt[i].longValue();
			long oIndex = o[i].longValue();
			ct[i] = UnsignedLong.fromLongBits((ptIndex ^ oIndex));
		}
		
		return ct;
		
	}
	
	
	/**
	 * Decrypts blocks of unsigned long cipher text using OFB
	 * O[0] is encrypted IV
	 * O[i] is encrypted o - 1
	 * pt[i] is ct[i] xor o[i]
	 * 
	 * @param ct
	 * @return pt
	 */
	public UnsignedLong[] decrypt(UnsignedLong[] ct) {
		
		UnsignedLong[] pt = new UnsignedLong[ct.length];
		UnsignedLong[] o = new UnsignedLong[ct.length];
		
		o[0] = UnsignedLong.fromLongBits(cipher.encryptBlock(getIV()));
		
		for(int k = 1; k < o.length; k++) {
			long oMinusIndex = o[k - 1].longValue();
			o[k] = UnsignedLong.fromLongBits(cipher.encryptBlock(oMinusIndex));
		}
		
		for(int i = 0; i < ct.length; i++) {
			long ctIndex = ct[i].longValue();
			long oIndex = o[i].longValue();
			pt[i] = UnsignedLong.fromLongBits((ctIndex ^ oIndex));
		}
		
		return pt;
		
	}	
	
	
}
