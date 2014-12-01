# [COMP 4109 - SkipJack Project](https://github.com/venom889/COMP4109-SkipJackProject)
Benjamin Sweett

An implementation of the Skipjack block cipher with Output Feedback opertaion mode for COMP 4109. For full documentation see
the files found in the documents folder. 

* [Source](https://github.com/venom889/COMP4109-SkipJackProject/tree/master/src)
* [Documentation](https://github.com/venom889/COMP4109-SkipJackProject/tree/master/doc)
* [Library Release](https://github.com/venom889/COMP4109-SkipJackProject/releases/tag/v1.0)


## Quick start

To run the demo application included:

1. Add as a new project to Ecplise or IDE of choice.

2. Run SkipJackApplication.java.

3. Follow prompts in console.


## Use as a library

Add the following dependencies to your project:

* [guava-18.0.jar](https://code.google.com/p/guava-libraries/)
* [COMP4109-SkipjackLib-ver1.0.jar](https://github.com/venom889/COMP4109-SkipJackProject/releases/tag/v1.0)

Add the example code below to your project. See the JUnit tests found in the tests folder if you need more examples.

```
  SkipJackCipher cipher = new SkipJackCipher();
  OutputFeedback OFB = new OutputFeedback(cipher);
  HexEncoder encoder = new HexEncoder();
		
  OFB.generateRandomIV();
  OFB.setKey("0123456789");
		
  String pt = "I would also like to encrypt this message.";
  String ct = "1BTÂÙ?L]34rÀ¹ÁõòÉ^ÃÚî%¬wD¼«ìÐ* ;:Ø+T\"NR~ê"
		
  // Encrypting
  UnsignedLong[] encoded = encoder.encodeMessage(pt);
  UnsignedLong[] cipherblocks = OFB.encrypt(encoded);
  String ciphertext = encoder.decodeMessage(cipherblocks);
	
  // Decrypting
  UnsignedLong[] encoded2 = encoder.encodeMessage(ct);
  UnsignedLong[] plaintextblocks = OFB.decrypt(encoded2);
  String plaintext = encoder.decodeMessage(plaintextblocks);
```

## Known Issues

See documentation for known issues.
