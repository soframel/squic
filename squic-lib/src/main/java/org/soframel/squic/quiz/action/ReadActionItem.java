package org.soframel.squic.quiz.action;

/**
 * User: sophie.ramel
 * Date: 12/5/13
 */
public class ReadActionItem {
    public enum ActionKind {QUESTION, RESPONSE, GOODRESPONSE, TEXT};

    private String id;

    public ReadActionItem(){
    }

    public ReadActionItem(ActionKind action){
        this.actionKind=action;
    }

    public ReadActionItem(ActionKind action, String text){
        this.text=text;
        this.actionKind=action;
    }

    private ActionKind actionKind;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ActionKind getActionKind() {
        return actionKind;
    }

    public void setActionKind(ActionKind actionKind) {
        this.actionKind = actionKind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
