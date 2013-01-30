package org.soframel.android.squic.quiz;


public class GameModeCountPoints implements GameMode{
	private int correctAnswerPoints;
	private int incorrectAnswerPoints;
	private Reward reward;
	
	public int getCorrectAnswerPoints() {
		return correctAnswerPoints;
	}
	public void setCorrectAnswerPoints(int correctAnswerPoints) {
		this.correctAnswerPoints = correctAnswerPoints;
	}
	public int getIncorrectAnswerPoints() {
		return incorrectAnswerPoints;
	}
	public void setIncorrectAnswerPoints(int incorrectAnswerPoints) {
		this.incorrectAnswerPoints = incorrectAnswerPoints;
	}
	public Reward getReward() {
		return reward;
	}
	public void setReward(Reward reward) {
		this.reward = reward;
	}
}
