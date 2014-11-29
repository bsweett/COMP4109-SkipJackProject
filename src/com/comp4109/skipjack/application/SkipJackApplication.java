package com.comp4109.skipjack.application;

import com.comp4109.skipjack.demo.SkipJackDemo;

/**
 * Skipjack cipher with OFB demo application's main execution point. Starts
 * the application, initializes a demo, and starts it.
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public class SkipJackApplication {
	
	/**
	 * Lets the user know the application has started and initialize 
	 * a demo. Starts and passes control to the demo.
	 * 
	 */
	public static void init() {
		
		System.out.println("\nSkipJack Demo Launched.");
		System.out.println("--------------------------------");
		
		SkipJackDemo demo = new SkipJackDemo();
		demo.start(true);
		
	}
		
	/**
	 * Main execution point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
				
		init();
		
	}
}
