package com.comp4109.skipjack.demo;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Menu object is used for modeling a given menu. Contains an array of items and
 * a scanner for input. 
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public class Menu {

	public Menu() {}
	
	
	/**
	 * Menu Item for a menu instance. Contains a callback and some display text.
	 * 
	 * @author bensweett
	 * @version 1.0.0
	 *
	 */
	private class MenuItem {
		
		private MenuCallback callback;
		private String text;
		
		
		public MenuItem(String text, MenuCallback callback) {
			
			this.callback = callback;
			this.text = text;
			
		}
		
		
		/**
		 * Gets the menu items callback
		 * 
		 * @return callback
		 */
		public MenuCallback getMC() {
			
			return callback;
			
		}
		
		
		/**
		 * Gets the menu items text
		 * 
		 * @return text
		 */
		public String getText() {
			return text;
		}
		
		
	}
	
	
	private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
	private Scanner sc;
	
	
	/**
	 * Creates a new menu item using the given text and callback method and
	 * adds it to the items list. returns true if successful or false if the
	 * add function fails.
	 * 
	 * @param text
	 * @param callback
	 * @return true or false
	 */
	public boolean add(String text, MenuCallback callback) {
		
		return items.add(new MenuItem(text, callback));
		
	}
	
	
	/**
	 * Shows the menu instance to the user through the console. Displays the 
	 * items by looping through the items array and sending their text to the
	 * standard output. Prompts the user for a menu option (ignoring any 
	 * exceptions). If the input is valid select the menu item from the list
	 * and invoke it's handler.
	 * 
	 * @param message
	 */
	public void show(String message) {
		
		sc = new Scanner(System.in);
		Integer selection = 0;
		
		for(int i = 0; i < items.size(); i++) {
			MenuItem item = items.get(i);
			System.out.printf(" [%d] %s \n", i + 1, item.getText());
		}
		
		System.out.println();
		System.out.println(message);
		
		try {
			selection = sc.nextInt();
		} catch (Exception e) { }

		if (selection > items.size() || selection < 1) {
			System.out.println("Invalid option.\nPress enter to continue...");
			sc.nextLine();
			sc.nextLine();
		} else {
			MenuItem mi = items.get(selection - 1);
			MenuCallback callback = mi.getMC();
			callback.Invoke();
		}
		
	}
	
	
}
