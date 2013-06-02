package org.soframel.squic.quiz.mode;

import org.soframel.squic.quiz.reward.Reward;


public class GameModeCountPoints extends GameMode{


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
