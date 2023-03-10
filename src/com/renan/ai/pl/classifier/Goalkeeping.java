package com.renan.ai.pl.classifier;

public record Goalkeeping(double gkDiving, double gkHandling, double gkKicking, double gkPositioning,
		double gkReflexes) {
	public String toString() {
		double goalkeeping = (gkDiving() + gkHandling() + gkKicking() + gkPositioning() + gkReflexes()) / 5;
		return String.valueOf(goalkeeping);

	}
}
