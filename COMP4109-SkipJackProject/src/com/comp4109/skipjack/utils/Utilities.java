package com.comp4109.skipjack.utils;

import java.util.Arrays;

import com.google.common.primitives.UnsignedLong;

/**
 * A cryptographic utility class for general use. Unused functions are removed based on
 * project.
 * 
 * @author bensweett
 * @version 2.1.0
 *
 */
public class Utilities {
	
	// Supported Alphabet for IV generation
	public static final char[] ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
											'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
											'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 
											'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
											'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
											'Y', 'Z', ' '};
	
	/**
	 * Splits a given string into blocks of a given size. Returns all the blocks in an array 
	 * of strings.
	 * 
	 * @param text
	 * @param size
	 * @return result
	 */
	public static String[] splitEqually(String text, int size) {
		
		String[] result = new String[ (int)Math.ceil((double)text.length() / (double)size) ];
	    for (int i = 0; i < result.length; i++)
	        result[i] = text.substring(i * size, Math.min(text.length(), (i + 1) * size));
		
	    return result;
	    
	}
	
	
	/**
	 * Appends each string in an array to a string builder and returns the result as a string
	 * 
	 * @param arr
	 * @return result
	 */
	public static String appendStringsFromArray(String[] arr) {
		
		StringBuilder builder = new StringBuilder();
		for(String s : arr)
		    builder.append(s);
		
		return builder.toString();
		
	}

	
	/**
	 * Takes a string argument and returns it to a hexadecimal result in a string
	 * 
	 * @param arg
	 * @return result
	 */
	public static String toHex(String arg) {
		
		char[] chars = arg.toCharArray();
	    StringBuffer hex = new StringBuffer();
	    
	    for (int i = 0; i < chars.length; i++)
	        hex.append(Integer.toHexString((int) chars[i]));
	    
	    return hex.toString();
	    
	}
	
	
	/**
	 * Takes a hexadecimal string and converts it to ASCII. If the character is not in valid
	 * hexadecimal form it appends it to the string anyway
	 * 
	 * @param hex
	 * @return result
	 */
	public static String hexToASCIIString(String hex) {
		
		StringBuilder output = new StringBuilder();
	    
		for (int i = 0; i < hex.length(); i+=2) {
	    	String str = "";
	    	if(i+2 < hex.length())
	    		str = hex.substring(i, i+2);
	    	else 
	    		 str = hex.substring(i, i+1);
	    	
	        output.append((char)Integer.parseInt(str, 16));
	    }
		
		return output.toString();
		
	}
	

	/**
	 * Checks to make sure no bits are lost when casting to from long to integer
	 * 
	 * @param l
	 * @return
	 */
	public static int safeLongToInt(long l) {
		
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    
	    return (int) l;
	    
	}
			
	
	/**
	 * Prints a given integer array to the standard output
	 * 
	 * @param array
	 */
	public static void printArray(int array[]) {
		
		System.out.println(Arrays.toString(array));   
		
	}
	
	
	/**
	 * Prints a given long array to the standard output
	 * 
	 * @param array
	 */
	public static void printLArray(long array[]) {
		
		System.out.println(Arrays.toString(array));   
		
	}
	
	/**
	 * Prints a given UnsignedLong array to standard output
	 * 
	 * @param array
	 */
	public static void printULArray(UnsignedLong array[]) {
		
		for(int i = 0; i < array.length; i ++) 
			System.out.print(array[i].longValue() + " ");  
		
	}
	
	
}
