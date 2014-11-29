package com.comp4109.skipjack.demo;

import java.util.Scanner;

import com.comp4109.skipjack.utils.Utilities;

import com.comp4109.skipjack.blockcipher.OutputFeedback;
import com.comp4109.skipjack.blockcipher.SkipJackCipher;


/**
 * Skipjack demo that uses console output and a menu system to give the user
 * control over encrypting with the Skipjack Cipher with OFB.
 * 
 * @author bensweett
 * @version 1.1.0
 *
 */
public class SkipJackDemo {

	// State booleans
	private boolean appContinue = true;
	private boolean encryptMenuContinue = false;
	private boolean encryptSourceContiune = false;
	private boolean settingsMenuContiune = false;
	private boolean settingsOptionContiune = false;

	// OFB instance containing the cipher
	private OutputFeedback OFB;

	// Input Scanner
	private Scanner sc;

	
	/**
	 *  Setup some default values so there always is an IV and key
	 */
	public SkipJackDemo() {
		
		SkipJackCipher cipher = new SkipJackCipher();
		this.OFB = new OutputFeedback(cipher);

		this.OFB.setKey("0123456789");
		this.OFB.generateRandomIV();
		
	}


	/**
	 * Starts the demo by creating the main menu and displaying it. Prompts the user to select an option.
	 * Init boolean is used to display spacing if this menu has been called for the first time.
	 * 
	 * @param init
	 */
	public void start(boolean init) {

		Menu menu = buildMainMenuWithHandlers();

		while(appContinue) {
			if(!init) newInput();
			menu.show("Please choose an option: ");
			init = false;
		}

		stop();

	}


	/**
	 * Stops the demo by safely exiting the application.
	 * 
	 */
	public void stop() {

		print("\nSkipJack Demo Stopped.");
		print("--------------------------------");
		print("Goodbye!");
		System.exit(0);

	}


	/**
	 * Event handler for encrypt main menu option. Displays a new encrypt menu and waits for user
	 * to select an option from the menu before invoking another handler.
	 * 
	 */
	private void encryptMenuHandler() {

		encryptMenuContinue = true;

		Menu menu = buildEncryptMenuWithHandlers();

		while(encryptMenuContinue) {
			newInput();
			menu.show("Encrypt: Choose a plaintext source..");
		}

	}


	/**
	 * Event handler for encrypt from file encrypt menu option. Creates a cipher demo with this
	 * skipjack cipher and displays a prompt from the cipher demo. Once the cipher demo is
	 * complete it returns to the previous menu.
	 * 
	 */
	private void encryptFromFileHandler() {

		encryptSourceContiune = true;
		CipherDemo eDemo;

		eDemo = new CipherDemo(this.OFB);
		while(encryptSourceContiune) {
			newInput();
			if(eDemo.showFile())
				encryptSourceContiune = false;
		}

	}

	/**
	 * Event handler for encrypt from console encrypt menu option. Creates a cipher demo with this
	 * skipjack cipher and displays a prompt from the cipher demo. Once the cipher demo is
	 * complete it returns to the previous menu.
	 * 
	 */
	private void encryptFromConsoleHandler() {
		
		encryptSourceContiune = true;
		CipherDemo eDemo;

		eDemo = new CipherDemo(this.OFB);
		while(encryptSourceContiune) {
			newInput();
			if(eDemo.showConsole())
				encryptSourceContiune = false;
		}
		
	}


	/**
	 * Event handler for settings main menu option. Displays a new settings menu and waits for user
	 * to select an option from the menu before invoking another handler.
	 * 
	 */
	private void settingsMenuHandler() {

		settingsMenuContiune = true;

		Menu menu = buildSettingsMenuWithHandlers();

		while(settingsMenuContiune) {
			newInput();
			menu.show("Settings: Please select an option..");
		}

	}


	/**
	 * Event handler for back option from settings menu. Sends the user back to the settings 
	 * menu.
	 * 
	 */
	private void backSettingsHandler() {

		settingsOptionContiune = false;

	}


	/**
	 * Event handler for set IV menu. Sets up a menu for setting an IV from two sources and 
	 * enters a run loop until the user selects a valid option.
	 * 
	 */
	private void setIVHandler() {

		settingsOptionContiune = true;

		Menu menu = new Menu();

		menu.add("Generate Random IV", new MenuCallback() { public void Invoke() { generateIVHandler(); } });
		menu.add("Input IV", new MenuCallback() { public void Invoke() { inputIVHandler(); } });
		menu.add("Back", new MenuCallback() { public void Invoke() { backSettingsHandler(); } });

		while(settingsOptionContiune) {
			newInput();
			menu.show("Set IV: Select a method..");
		}

	}


	/**
	 * Event handler for generating a random IV for OFB mode. Calls the random IV generator for
	 * OFB and sends the user back to the settings menu.
	 * 
	 */
	private void generateIVHandler() {

		this.OFB.generateRandomIV();
		print("Random IV Generated for Output Feedback Mode");
		backSettingsHandler();

	}


	/**
	 * Event handler for setting the IV of OFB mode. Tells the user the requirements for the IV 
	 * and allows them to input one. Checks to see if the IV is valid and sets it. Returns the 
	 * user to the settings menu if an error or if successfully set.
	 * 
	 */
	private void inputIVHandler() {

		sc = new Scanner(System.in);
		String input = "";

		print("IVs must be 8 characters long and only contain valid ASCII characeters.");
		print("Please input a valid IV...");
		print("-> ");

		try {
			input = sc.nextLine();
		} catch (Exception e) {}


		long value = 0L;

		try {
			String hexcode = Utilities.toHex(input);
			value = Long.valueOf(hexcode, 16);
		} catch(Exception e) {
			print("User error: invalid IV, please try again..");
			backSettingsHandler();
			return;
		}

		this.OFB.setIV(value);
		print("IV set for: Output Feedback Mode");
		backSettingsHandler();

	}


	/**
	 * Event handler for setting the key of the Skipjack cipher through the OFB mode. Tells
	 * the user the requirements for the key and allows them to input one. Checks to see if
	 * the key length is valid and sets the key. Tells the user if the key was set correctly
	 * or not and returns them to the settings menu.
	 * 
	 */
	private void setKeyHandler() {

		sc = new Scanner(System.in);
		String input = "";

		print("Keys must be 10 characters long and only contain valid ASCII characters.");
		print("Please input a valid key...");
		print("-> ");

		try {
			input = sc.nextLine();
		} catch (Exception e) {}

		boolean result = false;
		result = this.OFB.setKey(input);

		if(result) 
			print("Key set for: Output Feedback Mode");
		else
			print("User error: invalid key, please try again..");

		backSettingsHandler();

	}


	/**
	 * Event handler for back encrypt and settings menu option. Returns the user to the main menu
	 * loop.
	 * 
	 */
	private void backHandler() {

		settingsMenuContiune = false;
		encryptMenuContinue = false;

	}


	/**
	 * Event handler for exit main menu option. Breaks the runtime loop.
	 * 
	 */
	private void exitHandler() {

		appContinue = false;	

	}


	/**
	 * Sets up the main menu options for the user with corresponding event handlers
	 * 
	 * @return menu
	 */
	private Menu buildMainMenuWithHandlers() {

		Menu menu = new Menu();

		menu.add("Encrypt", new MenuCallback() { public void Invoke() { encryptMenuHandler(); } });
		menu.add("Settings", new MenuCallback() { public void Invoke() { settingsMenuHandler(); } });
		menu.add("Exit", new MenuCallback() { public void Invoke() { exitHandler(); } });

		return menu;

	}


	/**
	 * Sets up the encrypt menu options for the user and the corresponding event handlers
	 * 
	 * @return menu
	 */
	private Menu buildEncryptMenuWithHandlers() {

		Menu menu = new Menu();

		menu.add("Encrypt from file", new MenuCallback() { public void Invoke() { encryptFromFileHandler(); } });
		menu.add("Encrypt from console", new MenuCallback() { public void Invoke() { encryptFromConsoleHandler(); } });
		menu.add("Back", new MenuCallback() { public void Invoke() { backHandler(); } });

		return menu;

	}


	/**
	 * Sets up the settings menu options for the user and the corresponding event handlers
	 * 
	 * @return menu
	 */
	private Menu buildSettingsMenuWithHandlers() {

		Menu menu = new Menu();

		menu.add("Set IV", new MenuCallback() { public void Invoke() { setIVHandler(); } });
		menu.add("Set Key", new MenuCallback() { public void Invoke() { setKeyHandler(); } });
		menu.add("Back", new MenuCallback() { public void Invoke() { backHandler(); } });

		return menu;

	}


	/**
	 * Prints two newlines some dashes and two more new lines to separate menus
	 * 
	 */
	private void newInput() {

		System.out.print("\n\n---------------------------------\n\n");

	}


	/**
	 * Prints a string to standard output on a new line.
	 * 
	 * @param s
	 */
	private void print(String s) {

		System.out.println(s);

	}


}
