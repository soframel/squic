package org.soframel.android.squic.quiz;

public class TextResponse extends TouchResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1738001428212904219L;
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	 @Override
	    public String toString(){
	    	String s=super.toString();
	    	return s+", text="+text;
	    }
}
