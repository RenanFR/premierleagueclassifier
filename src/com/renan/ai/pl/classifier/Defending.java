package com.renan.ai.pl.classifier;

public record Defending(double defensiveAwareness, double standingTackle, double slidingTackle) {
	public String toString() {
		double defending = (defensiveAwareness() + standingTackle() + slidingTackle()) / 3;
		return String.valueOf(defending);

	}
}
