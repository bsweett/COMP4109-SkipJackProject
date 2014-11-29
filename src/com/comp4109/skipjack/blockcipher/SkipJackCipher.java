package com.comp4109.skipjack.blockcipher;

import com.comp4109.skipjack.utils.Utilities;

/**
 * An implementation of the SkipJack Cipher. Encrypts and decrypts single 64-bit block over 32 rounds
 * using rules, F-Table, permutation G, and an 80-bit key.
 * 
 * @author bensweett
 * @version 1.0.0
 * 
 */
public class SkipJackCipher {

	private int[] Key;
	private final int FTable[] = { 
			0xa3, 0xd7, 0x09, 0x83, 0xf8, 0x48, 0xf6, 0xf4, 0xb3, 0x21, 0x15, 0x78, 0x99, 0xb1, 0xaf, 0xf9,
			0xe7, 0x2d, 0x4d, 0x8a, 0xce, 0x4c, 0xca, 0x2e, 0x52, 0x95, 0xd9, 0x1e, 0x4e, 0x38, 0x44, 0x28,
			0x0a, 0xdf, 0x02, 0xa0, 0x17, 0xf1, 0x60, 0x68, 0x12, 0xb7, 0x7a, 0xc3, 0xe9, 0xfa, 0x3d, 0x53,
			0x96, 0x84, 0x6b, 0xba, 0xf2, 0x63, 0x9a, 0x19, 0x7c, 0xae, 0xe5, 0xf5, 0xf7, 0x16, 0x6a, 0xa2,
			0x39, 0xb6, 0x7b, 0x0f, 0xc1, 0x93, 0x81, 0x1b, 0xee, 0xb4, 0x1a, 0xea, 0xd0, 0x91, 0x2f, 0xb8,
			0x55, 0xb9, 0xda, 0x85, 0x3f, 0x41, 0xbf, 0xe0, 0x5a, 0x58, 0x80, 0x5f, 0x66, 0x0b, 0xd8, 0x90,
			0x35, 0xd5, 0xc0, 0xa7, 0x33, 0x06, 0x65, 0x69, 0x45, 0x00, 0x94, 0x56, 0x6d, 0x98, 0x9b, 0x76,
			0x97, 0xfc, 0xb2, 0xc2, 0xb0, 0xfe, 0xdb, 0x20, 0xe1, 0xeb, 0xd6, 0xe4, 0xdd, 0x47, 0x4a, 0x1d,
			0x42, 0xed, 0x9e, 0x6e, 0x49, 0x3c, 0xcd, 0x43, 0x27, 0xd2, 0x07, 0xd4, 0xde, 0xc7, 0x67, 0x18,
			0x89, 0xcb, 0x30, 0x1f, 0x8d, 0xc6, 0x8f, 0xaa, 0xc8, 0x74, 0xdc, 0xc9, 0x5d, 0x5c, 0x31, 0xa4,
			0x70, 0x88, 0x61, 0x2c, 0x9f, 0x0d, 0x2b, 0x87, 0x50, 0x82, 0x54, 0x64, 0x26, 0x7d, 0x03, 0x40,
			0x34, 0x4b, 0x1c, 0x73, 0xd1, 0xc4, 0xfd, 0x3b, 0xcc, 0xfb, 0x7f, 0xab, 0xe6, 0x3e, 0x5b, 0xa5,
			0xad, 0x04, 0x23, 0x9c, 0x14, 0x51, 0x22, 0xf0, 0x29, 0x79, 0x71, 0x7e, 0xff, 0x8c, 0x0e, 0xe2,
			0x0c, 0xef, 0xbc, 0x72, 0x75, 0x6f, 0x37, 0xa1, 0xec, 0xd3, 0x8e, 0x62, 0x8b, 0x86, 0x10, 0xe8,
			0x08, 0x77, 0x11, 0xbe, 0x92, 0x4f, 0x24, 0xc5, 0x32, 0x36, 0x9d, 0xcf, 0xf3, 0xa6, 0xbb, 0xac,
			0x5e, 0x6c, 0xa9, 0x13, 0x57, 0x25, 0xb5, 0xe3, 0xbd, 0xa8, 0x3a, 0x01, 0x05, 0x59, 0x2a, 0x46 };
	

	public SkipJackCipher() {}
	
	
	/**
	 * Encrypts one 64-bit block using a 80-bit key. Keeps a total round counter and runs over 32 rounds. First 8
	 * uses RuleA, Second 8 uses RuleB, Third 8 uses RuleA again, and last 8 uses RuleB again. Outputs an 
	 * encrypted 64-bit block.
	 *
	 * @param block
	 * @return 64-bit cipher block
	 */
	public long encryptBlock(long block) {
		
		long internal = block;
		int[] key = getKey();
		int counter = 0;
		
		while (counter < 8) {
			internal = ruleA(counter, key, internal);
			counter++;
		}
		
		while (counter < 16) {
			internal = ruleB(counter, key, internal);
			counter++;
		}
		
		while (counter < 24) {
			internal = (ruleA(counter, key, internal));
			counter++;
		}
		
		while (counter < 32) {
			internal = (ruleB(counter, key, internal));
			counter++; 
		}
		
		//NOTE: Masking with "& 0x7fffffffffffffffL;" doesn't work
		return internal;
		
	}
	
	
	/**
	 * Decrypts one 64-bit block using a 80-bit sub key. Keeps a total round counter and runs over 32 rounds. First 
	 * 8 uses RuleAPrime (inverse of Rule A), Second 8 uses RuleBPrime (inverse of RuleB, Third 8 uses RuleAPrime 
	 * again, last 8 uses RuleBPrime. Outputs a decrypted 64-bit block. 
	 * 
	 * @param block
	 * @return 64-bit plain block
	 */
	public long decryptBlock(long block) {
		
		long internal = block;
		
		int[] key = getKey();
		int counter = 31;
		
		while( counter > 23) {
			internal = (ruleBPrime(counter, key, internal));
			counter--;
		}
		
		while( counter > 15) {
			internal = (ruleAPrime(counter, key, internal));
			counter--;
		}
		
		while( counter > 7) {
			internal = (ruleBPrime(counter, key, internal));
			counter--;
		}
		
		while(counter > -1) {
			internal = (ruleAPrime(counter, key, internal));
			counter--;
		}
		
		return internal;
		
	}
	
	
	/**
	 * Gets the first 16-bit word from a 64-bit block
	 * 
	 * @param internal
	 * @return w1
	 */
	private long getW1(long internal) {
		
		return (internal >>> 48);
		
	}
	
	
	/**
	 * Gets the second 16-bit word from a 64-bit block
	 * 
	 * @param internal
	 * @return w2
	 */
	private long getW2(long internal) {
		
		return ((internal >> 32) & 0xFFFFL);
		
	}
	
	
	/**
	 * Gets the third 16-bit word from a 64-bit block
	 * 
	 * @param internal
	 * @return w3
	 */
	private long getW3(long internal) {
		
		return ((internal >> 16) & 0xFFFFL);
		
	}
	
	
	/**
	 * Gets the fourth 16-bit word from a 64-bit block
	 * 
	 * @param internal
	 * @return w4
	 */
	private long getW4(long internal) {
		
		return (internal & 0xFFFFL);
		
	}
	
	
	/**
	 * Takes 4 16-bit words and concatenates them together into one 64 bit block and returns the result
	 * 
	 * @param w1
	 * @param w2
	 * @param w3
	 * @param w4
	 * @return block
	 */
	private long concatenateWords(long w1, long w2, long w3, long w4) {
		
		return w1 << 48 | w2 << 32 | w3 << 16 | w4;
		
	}
	
	
	/**
	 * RuleA takes a 64-bit block and splits it into 16 bit words. It outputs a 64-bit block after each word has 
	 * been modified in the following format: 
	 * w1 is the permutation of w1 XORed with w4 and the counter
	 * w2 is the permutation of w1
	 * w3 is w2
	 * w4 is w3
	 * The result are concatenated together and returned
	 * 
	 * @param counter
	 * @param key
	 * @param internal
	 * @return 64-bit internal state block
	 */
	private long ruleA(int counter, int[] key, long internal) {	
		
		long w1_in = getW1(internal);
		long w2_in = getW2(internal);
		long w3_in = getW3(internal);
		long w4_in = getW4(internal);
		
		long w1_out = permutationG(counter, key, w1_in) ^ w4_in ^ (counter + 1);
		long w2_out = permutationG(counter, key, w1_in);
		long w3_out = w2_in;
		long w4_out = w3_in;
		
		return concatenateWords(w1_out, w2_out, w3_out, w4_out);
		
	}
	
	
	/**
	 * RuleB takes a 64-bit block and splits it into 16 bit words. It outputs a 64-bit block after each word has 
	 * been modified in the following format: 
	 * w1 is w4
	 * w2 is the permutation of w1
	 * w3 is w1 XORed with w2 and the counter
	 * w4 is w3
	 * The result are concatenated together and returned
	 * 
	 * @param counter
	 * @param key
	 * @param internal
	 * @return 64-bit internal state block
	 */
	private long ruleB(int counter, int[] key, long internal) {
		
		long w1_in = getW1(internal);
		long w2_in = getW2(internal);
		long w3_in = getW3(internal);
		long w4_in = getW4(internal);
		
		long w1_out = w4_in;
		long w2_out = permutationG(counter, key, w1_in);
		long w3_out = w1_in ^ w2_in ^ (counter + 1); 
		long w4_out = w3_in;
				
		return concatenateWords(w1_out, w2_out, w3_out, w4_out);
		
	}
	
	
	/**
	 * RuleAPrime is the inverse of RuleA and works similarly:
	 * w1 is the inverse permutation of w2
	 * w2 is w3
	 * w3 is w4
	 * w4 is w1 XORed with w2 and the counter
	 * 
	 * @param counter
	 * @param key
	 * @param internal
	 * @return 64-bit internal state block
	 */
	private long ruleAPrime(int counter, int[] key, long internal) {
		
		long w1_in = getW1(internal);
		long w2_in = getW2(internal);
		long w3_in = getW3(internal);
		long w4_in = getW4(internal);
		
		long w1_out = permutationGPrime(counter, key, w2_in);
		long w2_out = w3_in;
		long w3_out = w4_in;
		long w4_out = w1_in ^ w2_in ^ (counter + 1);
		
		return concatenateWords(w1_out, w2_out, w3_out, w4_out);
		
	} 
	
	
	/**
	 * RuleBPrime is the inverse of RuleB and works similarly:
	 * w1 is the inverse permutation of w2
	 * w2 is the inverse permutation of w2 XORed with w3 and the counter
	 * w3 is w4
	 * w4 is w1
	 * 
	 * @param counter
	 * @param key
	 * @param internal
	 * @return 64-bit internal state block
	 */
	private long ruleBPrime(int counter, int[] key, long internal) {
		
		long w1_in = getW1(internal);
		long w2_in = getW2(internal);
		long w3_in = getW3(internal);
		long w4_in = getW4(internal);
		
		long w1_out = permutationGPrime(counter, key, w2_in);
		long w2_out = permutationGPrime(counter, key, w2_in) ^ w3_in ^ (counter + 1);
		long w3_out = w4_in;
		long w4_out = w1_in;
		
		return concatenateWords(w1_out, w2_out, w3_out, w4_out);
		
	}
	
	
	/**
	 * PermutationG is a four round Feistel permutation function. It takes a 16-bit word a 80-bit sub key and the 
	 * step counter. cv0 -> cv3 are computed based on the counter multiplied by the number of rounds modulo key 
	 * length (10). They are XORed in increasing order with g2 -> g5 and the result is used as the index for 
	 * looking up a hex value in the FTable. The hex value is then XORed with g1 -> g4 and set to g3 -> g6. g5 is 
	 * concatenated with g6 and returned.
	 *  
	 * @param counter
	 * @param key
	 * @param internal
	 * @return 16-bit word
	 */
	private long permutationG(int counter, int[] key, long internal) {	
		
		int cv0 = key[(counter * 4) % key.length]; 
		int cv1 = key[(counter * 4 + 1) % key.length];
		int cv2 = key[(counter * 4 + 2) % key.length];
		int cv3 = key[(counter * 4 + 3) % key.length];
		
		// NOTE: For treating as unsigned types use >>> and mask.
		int g1 = Utilities.safeLongToInt((internal >>> 8));  
		int g2 = Utilities.safeLongToInt((internal & 0xFF));	
		
		int g3 = (FTable[g2 ^ cv0] ^ g1);
		int g4 = (FTable[g3 ^ cv1] ^ g2);
		int g5 = (FTable[g4 ^ cv2] ^ g3);
		int g6 = (FTable[g5 ^ cv3] ^ g4);
		
		return ((long)g5 << 8) | g6;
		
	}
	
	
	/**
	 * PermutationGPrime is the inverse of PermutationG. The cv values are reversed along with g1 and g2. The result 
	 * is g6 concatenated with g5. 
	 * 
	 * @param counter
	 * @param key
	 * @param internal
	 * @return 16-bit word
	 */
	private long permutationGPrime(int counter, int[] key, long internal) {
		
		int cv0 = key[(counter * 4 + 3) % key.length];
		int cv1 = key[(counter * 4 + 2) % key.length];
		int cv2 = key[(counter * 4 + 1) % key.length];
		int cv3 = key[(counter * 4) % key.length];
		
		// NOTE: For treating as unsigned types use >>> and mask.
		int g1 = Utilities.safeLongToInt((internal & 0xFF));
		int g2 = Utilities.safeLongToInt((internal >>> 8));	
		
		int g3 = (FTable[g2 ^ cv0] ^ g1);
		int g4 = (FTable[g3 ^ cv1] ^ g2);
		int g5 = (FTable[g4 ^ cv2] ^ g3);
		int g6 = (FTable[g5 ^ cv3] ^ g4);
		
		return ((long)g6 << 8) | g5;
		
	}

	
	/**
	 * Returns the key for the cipher
	 * 
	 * @return key
	 */
	public int[] getKey() {
		
		return Key;
		
	}

	
	/**
	 * Sets the key for the cipher
	 * 
	 * @param key
	 */
	public void setKey(int[] key) {
		
		Key = key;
		
	}
	
	
}
