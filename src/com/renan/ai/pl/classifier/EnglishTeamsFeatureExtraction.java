package com.renan.ai.pl.classifier;

import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Triplet;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class EnglishTeamsFeatureExtraction {

	public static synchronized void main(String[] args) throws IOException {
		int teamCategory;
		String teamCategoryStr;
		double red, green, blue;

		String featureVectorFile = """
				@relation teams\n
				@attribute arsenal_main_colour real
				@attribute arsenal_goalkeeper_jersey_colour real
				@attribute city_main_colour real
				@attribute city_goalkeeper_jersey_colour real
				@attribute chelsea_main_colour real
				@attribute chelsea_goalkeeper_jersey_colour real
				@attribute classe {arsenal, chelseafc, mancity}\n
				@data\n""";

		File imageFolder = new File("src\\images");
		File[] imageFiles = imageFolder.listFiles();

		float[][] features = new float[32][7];

		for (int i = 0; i < imageFiles.length; i++) {

			Team team = Team.getByTeamPrefix(imageFiles[i].getName().split("_")[0]);
			teamCategory = team.getTeamCategory();
			teamCategoryStr = team.getPrefix();

			float[] featureArrayForImage = loadImageAndGetFeatureVector(
					imageFolder.getAbsolutePath() + "\\" + imageFiles[i].getName());

			features[i][0] = featureArrayForImage[0];
			features[i][1] = featureArrayForImage[1];

			features[i][2] = featureArrayForImage[2];
			features[i][3] = featureArrayForImage[3];

			features[i][4] = featureArrayForImage[4];
			features[i][5] = featureArrayForImage[5];

			features[i][6] = teamCategory;

			System.out.println("Arsenal main colour: " + features[i][0] + " - Arsenal goalkeeper jersey colour: "
					+ features[i][1] + " - City main colour: " + features[i][2] + " - City goalkeeper jersey colour: "
					+ features[i][3] + " - Chelsea main colour: " + features[i][4]
					+ " - Chelsea goalkeeper jersey colour: " + features[i][5] + " - Team category: " + features[i][6]);

			featureVectorFile += features[i][0] + "," + features[i][1] + "," + features[i][2] + "," + features[i][3]
					+ "," + features[i][4] + "," + features[i][5] + "," + teamCategoryStr + "\n";

		}
		File arffFile = new File("teams.arff");
		FileOutputStream outputStream = new FileOutputStream(arffFile);
		outputStream.write(featureVectorFile.getBytes());
		outputStream.close();
	}

	public static float[] loadImageAndGetFeatureVector(String imagePath) {

		SelectedTeamFeatures selectedTeamFeatures = new SelectedTeamFeatures();

		IplImage rawImage = loadPlayerImage(imagePath);

		iterateEveryPixelAndSetFeatureValues(selectedTeamFeatures, getFeatureSpecificationForEachTeam(), rawImage);

		setFeaturesPercentage(selectedTeamFeatures, rawImage);

		float[] featureArrayForImage = getFeatureArrayForImage(selectedTeamFeatures);
		return featureArrayForImage;
	}

	private static IplImage loadPlayerImage(String imagePath) {
		IplImage rawImage = cvLoadImage(imagePath);
		Logger.getLogger(Pixel.class.getName()).log(Level.INFO, imagePath);
		return rawImage;
	}

	private static void setFeaturesPercentage(SelectedTeamFeatures selectedTeamFeatures, IplImage rawImage) {
		selectedTeamFeatures.setArsenalMainColour(
				(selectedTeamFeatures.getArsenalMainColour() / (rawImage.height() * rawImage.width())) * 100);
		selectedTeamFeatures.setArsenalGoalkeeperJerseyColour(
				(selectedTeamFeatures.getArsenalGoalkeeperJerseyColour() / (rawImage.height() * rawImage.width()))
						* 100);

		selectedTeamFeatures.setCityMainColour(
				(selectedTeamFeatures.getCityMainColour() / (rawImage.height() * rawImage.width())) * 100);
		selectedTeamFeatures.setCityGoalkeeperJerseyColour(
				(selectedTeamFeatures.getCityGoalkeeperJerseyColour() / (rawImage.height() * rawImage.width())) * 100);

		selectedTeamFeatures.setChelseaMainColour(
				(selectedTeamFeatures.getChelseaMainColour() / (rawImage.height() * rawImage.width())) * 100);
		selectedTeamFeatures.setChelseaGoalkeeperJerseyColour(
				(selectedTeamFeatures.getChelseaGoalkeeperJerseyColour() / (rawImage.height() * rawImage.width()))
						* 100);
	}

	private static void iterateEveryPixelAndSetFeatureValues(SelectedTeamFeatures selectedTeamFeatures,
			List<Pixel> allMappedPixels, IplImage rawImage) {
		double red;
		double green;
		double blue;
		for (int height = 0; height < rawImage.height(); height++) {
			for (int width = 0; width < rawImage.width(); width++) {
				CvScalar rgbPixel = cvGet2D(rawImage, height, width);
				blue = rgbPixel.val(0);
				green = rgbPixel.val(1);
				red = rgbPixel.val(2);
				for (Pixel pixel : allMappedPixels) {
					if (pixel.match(red, green, blue)) {
						switch (pixel.getTeam()) {
						case ARSENAL: {
							switch (pixel.getType()) {
							case MAIN_COLOUR: {
								selectedTeamFeatures
										.setArsenalMainColour(selectedTeamFeatures.getArsenalMainColour() + 1);
								break;
							}
							case GOALKEEPER_JERSEY_COLOUR: {
								selectedTeamFeatures.setArsenalGoalkeeperJerseyColour(
										selectedTeamFeatures.getArsenalGoalkeeperJerseyColour() + 1);
								break;
							}
							}
							break;
						}
						case CITY: {
							switch (pixel.getType()) {
							case MAIN_COLOUR: {
								selectedTeamFeatures.setCityMainColour(selectedTeamFeatures.getCityMainColour() + 1);
								break;
							}
							case GOALKEEPER_JERSEY_COLOUR: {
								selectedTeamFeatures.setCityGoalkeeperJerseyColour(
										selectedTeamFeatures.getCityGoalkeeperJerseyColour() + 1);
								break;
							}
							}
							break;
						}
						case CHELSEA: {
							switch (pixel.getType()) {
							case MAIN_COLOUR: {
								selectedTeamFeatures
										.setChelseaMainColour(selectedTeamFeatures.getChelseaMainColour() + 1);
								break;
							}
							case GOALKEEPER_JERSEY_COLOUR: {
								selectedTeamFeatures.setChelseaGoalkeeperJerseyColour(
										selectedTeamFeatures.getChelseaGoalkeeperJerseyColour() + 1);
								break;
							}
							}
							break;
						}
						}
					}
				}
			}
		}
	}

	private static List<Pixel> getFeatureSpecificationForEachTeam() {
		Pixel arsenalMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.ARSENAL,
				List.of(new ColourRange(104, 218, 1, 61, 1, 64), new ColourRange(118, 217, 0, 28, 0, 39),
						new ColourRange(207, 227, 47, 83, 44, 80), new ColourRange(109, 254, 0, 53, 8, 60),
						new ColourRange(103, 225, 0, 58, 8, 50)));

		Pixel arsenalGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.ARSENAL,
				List.of(new ColourRange(222, 255, 193, 238, 0, 137)));

		Pixel cityMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.CITY,
				List.of(new ColourRange(54, 204, 153, 247, 206, 255), new ColourRange(161, 187, 219, 237, 250, 255),
						new ColourRange(106, 129, 158, 178, 220, 237), new ColourRange(124, 228, 181, 239, 245, 255),
						new ColourRange(123, 161, 187, 214, 241, 255), new ColourRange(96, 133, 149, 186, 188, 225)));
		Pixel cityGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.CITY,
				List.of(new ColourRange(231, 255, 88, 193, 47, 120)));

		Pixel chelseaMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.CHELSEA,
				List.of(new ColourRange(7, 87, 16, 110, 72, 211), new ColourRange(19, 64, 60, 108, 147, 202),
						new ColourRange(49, 85, 55, 94, 128, 172), new ColourRange(2, 27, 42, 61, 111, 139),
						new ColourRange(6, 26, 53, 86, 151, 201)));
		Pixel chelseaGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.CHELSEA,
				List.of(new ColourRange(246, 254, 152, 223, 75, 153)));

		return List.of(arsenalMainColourPixel, arsenalGoalkeeperJerseyColourPixel, cityMainColourPixel,
				arsenalGoalkeeperJerseyColourPixel, cityGoalkeeperJerseyColourPixel, chelseaMainColourPixel,
				chelseaMainColourPixel, chelseaGoalkeeperJerseyColourPixel);

	}

	private static float[] getFeatureArrayForImage(SelectedTeamFeatures selectedTeamFeatures) {
		return new float[] { selectedTeamFeatures.getArsenalMainColour(),
				selectedTeamFeatures.getArsenalGoalkeeperJerseyColour(),

				selectedTeamFeatures.getCityMainColour(), selectedTeamFeatures.getCityGoalkeeperJerseyColour(),

				selectedTeamFeatures.getChelseaMainColour(), selectedTeamFeatures.getChelseaGoalkeeperJerseyColour() };

	}

}
