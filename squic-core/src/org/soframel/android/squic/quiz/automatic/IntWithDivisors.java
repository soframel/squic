package org.soframel.android.squic.quiz.automatic;

import java.util.List;

public class IntWithDivisors {
	private int n;
	private List<Integer> divisors;
	
	public IntWithDivisors(int n, List<Integer> divisors){
		this.n=n;
		this.divisors=divisors;
	}
	
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public List<Integer> getDivisors() {
		return divisors;
	}
	public void setDivisors(List<Integer> divisors) {
		this.divisors = divisors;
	}
}
