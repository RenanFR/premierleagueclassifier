package com.renan.ai.pl.classifier;

import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerPositionByNaiveBayes {
	public static void main(String[] args) {

		Formatter formatter = new Formatter();

		List<Player> samplePlayers = List.of(
				new Player("W. Saliba", 193, new Attacking(58, 37, 69, 81, 34), new Skill(72, 55, 35, 74, 74),
						new Movement(77, 84, 50, 82, 59), new Power(60, 72, 69, 86, 23),
						new Mentality(82, 84, 43, 62, 47, 79), new Defending(84, 86, 82),
						new Goalkeeping(7, 10, 6, 8, 11), "CB"),

				new Player("M. Hummels", 191, new Attacking(65, 57, 86, 81, 64), new Skill(69, 66, 53, 84, 77),
						new Movement(45, 55, 60, 86, 62), new Power(71, 70, 66, 87, 49),
						new Mentality(71, 87, 57, 80, 68, 89), new Defending(88, 86, 85),
						new Goalkeeping(15, 6, 10, 5, 6), "CB"),

				new Player("Gabriel", 190, new Attacking(38, 31, 85, 78, 28), new Skill(52, 47, 27, 74, 67),
						new Movement(61, 73, 51, 81, 37), new Power(51, 70, 66, 87, 30),
						new Mentality(84, 80, 44, 59, 40, 74), new Defending(83, 84, 81),
						new Goalkeeping(12, 8, 13, 10, 7), "CB"),

				new Player("M. Maignan", 191, new Attacking(18, 15, 17, 57, 16), new Skill(31, 38, 16, 53, 40),
						new Movement(50, 53, 41, 85, 56), new Power(64, 74, 39, 78, 20),
						new Mentality(35, 25, 15, 61, 15, 66), new Defending(23, 16, 19),
						new Goalkeeping(85, 82, 85, 85, 89), "GK"),

				new Player("H. Lloris", 188, new Attacking(13, 10, 9, 52, 15), new Skill(12, 12, 10, 55, 29),
						new Movement(61, 59, 54, 83, 56), new Power(53, 72, 41, 43, 14),
						new Mentality(31, 25, 9, 57, 40, 55), new Defending(24, 12, 18),
						new Goalkeeping(86, 82, 70, 82, 86), "GK"),

				new Player("Kepa", 189, new Attacking(25, 11, 18, 60, 18), new Skill(15, 25, 19, 60, 34),
						new Movement(34, 33, 41, 71, 39), new Power(64, 71, 37, 51, 14),
						new Mentality(18, 21, 15, 61, 24, 63), new Defending(19, 18, 16),
						new Goalkeeping(80, 78, 85, 78, 84), "GK"),

				new Player("M. Ødegaard", 178, new Attacking(84, 76, 52, 89, 76), new Skill(86, 83, 85, 84, 87),
						new Movement(78, 71, 85, 77, 83), new Power(80, 46, 81, 56, 78),
						new Mentality(63, 65, 81, 89, 63, 78), new Defending(65, 49, 51),
						new Goalkeeping(14, 15, 15, 8, 10), "CAM"),

				new Player("Bernardo Silva", 173, new Attacking(84, 78, 51, 88, 78), new Skill(92, 84, 67, 78, 91),
						new Movement(81, 65, 94, 87, 92), new Power(79, 51, 93, 54, 78),
						new Mentality(77, 65, 83, 84, 68, 89), new Defending(66, 73, 58),
						new Goalkeeping(9, 10, 14, 12, 9), "CAM"),

				new Player("Bruno Fernandes", 179, new Attacking(86, 83, 64, 89, 87), new Skill(79, 86, 85, 87, 84),
						new Movement(75, 69, 78, 91, 79), new Power(88, 71, 93, 66, 88),
						new Mentality(82, 67, 85, 90, 90, 79), new Defending(66, 73, 65),
						new Goalkeeping(12, 14, 15, 8, 14), "CAM"),

				new Player("D. Núñez", 187, new Attacking(67, 83, 76, 69, 79), new Skill(76, 75, 61, 64, 76),
						new Movement(88, 91, 78, 85, 67), new Power(86, 78, 89, 87, 75),
						new Mentality(80, 41, 86, 73, 78, 82), new Defending(36, 39, 33),
						new Goalkeeping(14, 10, 12, 10, 13), "ST"),

				new Player("V. Osimhen", 185, new Attacking(62, 90, 81, 72, 76), new Skill(80, 63, 48, 53, 80),
						new Movement(87, 93, 78, 85, 68), new Power(80, 90, 81, 86, 73),
						new Mentality(70, 34, 86, 70, 75, 79), new Defending(36, 45, 22),
						new Goalkeeping(14, 14, 10, 9, 6), "ST"),

				new Player("Morata", 190, new Attacking(72, 84, 85, 76, 79), new Skill(78, 78, 50, 63, 82),
						new Movement(81, 84, 74, 80, 62), new Power(80, 86, 78, 81, 74),
						new Mentality(69, 26, 84, 77, 75, 78), new Defending(37, 14, 20),
						new Goalkeeping(4, 5, 4, 4, 5), "ST"));

		List<PlayerFeatures> playerFeatures = samplePlayers.stream().map(player -> {
			double attacking = (player.attacking().crossing() + player.attacking().finishing()
					+ player.attacking().headingAccuracy() + player.attacking().shortPassing()
					+ player.attacking().volleys()) / 5;
			double skill = (player.skill().dribbling() + player.skill().curve() + player.skill().fkAccuracy()
					+ player.skill().longPassing() + player.skill().ballControl()) / 5;
			double movement = (player.movement().acceleration() + player.movement().sprintSpeed()
					+ player.movement().agility() + player.movement().reactions() + player.movement().balance()) / 5;
			double power = (player.power().shotPower() + player.power().jumping() + player.power().stamina()
					+ player.power().strength() + player.power().longShots()) / 5;
			double mentality = (player.mentality().aggression() + player.mentality().interceptions()
					+ player.mentality().positioning() + player.mentality().vision() + player.mentality().penalties()
					+ player.mentality().composure()) / 6;
			double defending = (player.defending().defensiveAwareness() + player.defending().standingTackle()
					+ player.defending().slidingTackle()) / 3;
			double goalkeeping = (player.goalkeeping().gkDiving() + player.goalkeeping().gkHandling()
					+ player.goalkeeping().gkKicking() + player.goalkeeping().gkPositioning()
					+ player.goalkeeping().gkReflexes()) / 5;
			return new PlayerFeatures(getRange(player.height()), getRange(attacking), getRange(skill), getRange(movement),
					getRange(power), getRange(mentality), getRange(defending), getRange(goalkeeping),
					player.position());
		}).collect(Collectors.toList());

		formatter.format("%15s %15s %15s %15s %15s %15s %15s %15s %15s\n", "HEIGHT", "ATTACKING", "SKILL", "MOVEMENT",
				"POWER", "MENTALITY", "DEFENDING", "GOALKEEPING", "POSITION");

		playerFeatures.forEach(feature -> {
			formatter.format("%14.7s %14.6s %14.6s %14.6s %14.6s %14.6s %14.6s %14.6s %17s\n", feature.height(),
					feature.attacking(), feature.skill(), feature.movement(), feature.power(), feature.mentality(),
					feature.defending(), feature.goalkeeping(), feature.position());
		});

		System.out.println(formatter);

	}

	private static String getRange(double featureValue) {
		int largerMultiple = (int) (5 * (Math.ceil(Math.abs(featureValue / 5))));
		int smallerMultiple = largerMultiple - 5;
		String featureOverallRange = smallerMultiple + "-" + largerMultiple;
		return featureOverallRange;
	}
}
