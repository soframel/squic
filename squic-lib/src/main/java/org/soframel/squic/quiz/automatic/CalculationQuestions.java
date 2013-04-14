package org.soframel.squic.quiz.automatic;


/**
 * Simple arithmetic operations
 * @author sophie
 *
 */
public class CalculationQuestions extends AutomaticQuestions {

	private int nbRandom;
	private int minValue;
	private int maxValue;
	private int nbOperands;
	private Operator operator;		
	
	public CalculationQuestions(int nbQuestions, int nbRandom, int minValue, int maxValue, int nbOperands, Operator operator){
		super(nbQuestions);
		this.nbRandom=nbRandom;
		this.minValue=minValue;
		this.maxValue=maxValue;
		this.nbOperands=nbOperands;
		this.operator=operator;
		
	}

	

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public int getNbRandom() {
		return nbRandom;
	}

	public void setNbRandom(int nbRandom) {
		this.nbRandom = nbRandom;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getNbOperands() {
		return nbOperands;
	}

	public void setNbOperands(int nbOperands) {
		this.nbOperands = nbOperands;
	}


}
