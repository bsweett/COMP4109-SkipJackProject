package com.comp4109.skipjack.demo;

import java.util.EventListener;

/**
 * Menu callback interface that is an Event Listener for when users select a menu option.
 * Requires an invoke method to be created for handling the event.
 * 
 * @author bensweett
 * @version 1.0.0
 *
 */
public interface MenuCallback extends EventListener {
	
	public void Invoke();
	
}
