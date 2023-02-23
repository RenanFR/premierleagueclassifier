package com.renan.ai.pl.classifier;

public class ColourRange {

	private int minR;

	private int maxR;

	private int minG;

	private int maxG;

	private int minB;

	private int maxB;

	public ColourRange(int minR, int maxR, int minG, int maxG, int minB, int maxB) {
		this.minR = minR;
		this.maxR = maxR;
		this.minG = minG;
		this.maxG = maxG;
		this.minB = minB;
		this.maxB = maxB;
	}

	public int getMinR() {
		return minR;
	}

	public int getMaxR() {
		return maxR;
	}

	public int getMinG() {
		return minG;
	}

	public int getMaxG() {
		return maxG;
	}

	public int getMinB() {
		return minB;
	}

	public int getMaxB() {
		return maxB;
	}

}
