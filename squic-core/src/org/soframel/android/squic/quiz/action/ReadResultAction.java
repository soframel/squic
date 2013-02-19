package org.soframel.android.squic.quiz.action;

import java.util.List;

public class ReadResultAction extends TextToSpeechResultAction{
	public enum specialAction {QUESTION, RESPONSE, GOODRESPONSE};
	
	private boolean showResponseDialog;
	
	private List items;
	
	public void setItems(List items){
		this.items=items;
	}
	public List getItems(){
		return items;
	}
	
	public String buildText(String question, String response, String goodResponse){
		StringBuilder s=new StringBuilder();
		for(Object item: items){
			s.append(" ");
			if(item==specialAction.QUESTION)
				s.append(question);
			else if(item==specialAction.RESPONSE)
				s.append(response);
			else if(item==specialAction.GOODRESPONSE)
				s.append(goodResponse);
			else if(item instanceof String)
				s.append((String)item);
		}
		return s.toString();
	}
	public boolean isShowResponseDialog() {
		return showResponseDialog;
	}
	public void setShowResponseDialog(boolean showResponseDialog) {
		this.showResponseDialog = showResponseDialog;
	}
}
