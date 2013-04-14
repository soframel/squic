package org.soframel.squic.utils;

public interface SquicLogger {
	public void debug(String text);
	public void info(String text);
	public void warn(String text);
	public void warn(String text, Throwable t);
	public void error(String text);
	public void error(String text, Throwable t);
}
