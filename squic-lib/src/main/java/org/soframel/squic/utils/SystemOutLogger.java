package org.soframel.squic.utils;

public class SystemOutLogger implements SquicLogger {

	public void debug(String text){
		System.out.println("DEBUG: "+text);
	}

	public void info(String text) {
		System.out.println("INFO: "+text);
	}

	public void warn(String text) {
		System.out.println("WARN: "+text);
	}

	public void warn(String text, Throwable t) {
		System.out.println("WARN: "+text+"\n	cause by: ");
		t.printStackTrace();
	}

	public void error(String text) {
		System.out.println("ERROR: "+text);
	}

	public void error(String text, Throwable t) {
		System.out.println("ERROR: "+text+"\n	cause by: ");
		t.printStackTrace();
	}
}
