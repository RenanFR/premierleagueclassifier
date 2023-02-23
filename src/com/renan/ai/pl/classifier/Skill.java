package com.renan.ai.pl.classifier;

public record Skill(double dribbling, double curve, double fkAccuracy, double longPassing, double ballControl) {
	public String toString() {
		double skill = (dribbling() + curve() + fkAccuracy() + longPassing() + ballControl()) / 5;
		return String.valueOf(skill);
	}
}
