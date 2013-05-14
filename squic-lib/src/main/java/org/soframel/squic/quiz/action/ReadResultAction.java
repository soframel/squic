package org.soframel.squic.quiz.action;

import java.util.List;

public class ReadResultAction extends TextToSpeechResultAction{

	
	private boolean showResponseDialog;
	
	private List<ReadActionItem> items;

    public ReadResultAction() {
    }

    public void setItems(List<ReadActionItem> items){
		this.items=items;
	}
	public List getItems(){
		return items;
	}
	
	public String buildText(String question, String response, String goodResponse){
		StringBuilder s=new StringBuilder();
		for(ReadActionItem item: items){
			s.append(" ");
			if(item.getActionKind()== ReadActionItem.ActionKind.QUESTION)
				s.append(question);
			else if(item.getActionKind()==ReadActionItem.ActionKind.RESPONSE)
				s.append(response);
			else if(item.getActionKind()==ReadActionItem.ActionKind.GOODRESPONSE)
				s.append(goodResponse);
			else if(item.getActionKind()==ReadActionItem.ActionKind.GOODRESPONSE)
				s.append(item.getText());
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
