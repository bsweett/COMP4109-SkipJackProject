package com.comp4109.skipjack.blockcipher;

import com.google.common.primitives.UnsignedLong;

import com.comp4109.skipjack.utils.Utilities;

/**
 * A Hex encoder and decoder utility class used for breaking up strings into hex and then
 * converting hex string into blocks of longs. Also reverses the process.
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public class HexEncoder {

	
	/**
	 * Takes a string converts it to hex and breaks it in blocks of unsigned longs.
	 * 
	 * @param message
	 * @return
	 */
	public UnsignedLong[] encodeMessage(String message) {
		
		message += "."; // FIXME: This is a temporary fix for the missing bits.
		
		String hexcode = Utilities.toHex(message);
		String[] blocks = Utilities.splitEqually(hexcode, 16);	
		UnsignedLong[] output = new UnsignedLong[blocks.length];
		
		String[] padded = new String[blocks.length];
		
		for(int i = 0; i < padded.length; i++) {
			output[i] = UnsignedLong.valueOf(blocks[i], 16) ;
		}
		
		return output;
		
	}
		
	
	/**
	 * Takes an array of Unsigned Blocks and breaks it up into hex strings, concatenates 
	 * the array of hex strings and then converts the array to an ASCII string.
	 * 
	 * @param blocks
	 * @return hexcode as ASCII
	 */
	public String decodeMessage(UnsignedLong[] blocks) {
		
		String[] strings = new String[blocks.length];
		for(int i = 0; i < blocks.length; i++) {
			strings[i] = blocks[i].toString(16);
		}

		String hexcode = Utilities.appendStringsFromArray(strings); 
		return Utilities.hexToASCIIString(hexcode);
		
	}
			
}
