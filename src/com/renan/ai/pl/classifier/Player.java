package com.renan.ai.pl.classifier;

public record Player(String name, Integer height, Attacking attacking, Skill skill, Movement movement, Power power,
		Mentality mentality, Defending defending, Goalkeeping goalkeeping, String position) {

}