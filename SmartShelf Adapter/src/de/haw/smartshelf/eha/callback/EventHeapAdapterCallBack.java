package de.haw.smartshelf.eha.callback;

import java.util.Map;

public interface EventHeapAdapterCallBack {
	
	/**
	 * Wird aufgerufen, wenn ein <tt>Event</tt> empfangen wird. 
	 * Die Felder des Events werden dabei in einer Map &uuml;bergeben. 
	 * 
	 * @param event Die Felder des Events als Map.
	 * 
	 * @return <code>true</code>, wenn weitere Events empfangen werden sollen; 
	 * 			<code>false</code>, wenn keine weiteren Informationen gesendet 
	 * 			werden sollen.
	 */
	public boolean eventReceived(Map<String, String> eventName);
}
