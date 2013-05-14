package org.soframel.squic.quiz.reward;

public class IntentReward extends Reward {

    private String action;
	private String uri;

    public IntentReward() {
    }

    public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

}
