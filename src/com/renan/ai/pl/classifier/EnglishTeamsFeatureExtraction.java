package com.renan.ai.pl.classifier;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCloneImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetMat;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.javatuples.Triplet;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class EnglishTeamsFeatureExtraction {

	public static void main(String[] args) throws IOException {
		int nextImage;
		int teamCategory;
		String teamCategoryStr;
		double red, green, blue;

		String featureVectorFile = """
				@relation teams\n\n
				@attribute arsenal_main_colour real\n
				@attribute arsenal_shield_detail_colour real\n
				@attribute arsenal_goalkeeper_jersey_colour real\n
				@attribute city_main_colour real\n
				@attribute city_shield_detail_colour real\n
				@attribute city_goalkeeper_jersey_colour real\n
				@attribute united_main_colour real\n
				@attribute united_shield_detail_colour real\n
				@attribute united_goalkeeper_jersey_colour real\n
				@attribute chelsea_main_colour real\n
				@attribute chelsea_shield_detail_colour real\n
				@attribute chelsea_goalkeeper_jersey_colour real\n
				@data\n""";

		File imageFolder = new File("src\\images");
		File[] imageFiles = imageFolder.listFiles();

		float arsenalMainColour = 0, arsenalShieldDetailColour = 0, arsenalGoalkeeperJerseyColour = 0;
		float cityMainColour = 0, cityShieldDetailColour = 0, cityGoalkeeperJerseyColour = 0;
		float unitedMainColour = 0, unitedShieldDetailColour = 0, unitedGoalkeeperJerseyColour = 0;
		float chelseaMainColour = 0, chelseaShieldDetailColour = 0, chelseaGoalkeeperJerseyColour = 0;

		Pixel arsenalMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.ARSENAL,
				List.of(Triplet.with(207, 22, 43), Triplet.with(220, 32, 47), Triplet.with(149, 146, 160),
						Triplet.with(237, 233, 240), Triplet.with(144, 7, 26), Triplet.with(104, 1, 8)));
		Pixel arsenalShieldDetailColourPixel = new Pixel(PixelType.SHIELD_DETAIL_COLOUR, Team.ARSENAL,
				List.of(Triplet.with(33, 53, 121), Triplet.with(23, 55, 133), Triplet.with(166, 140, 65),
						Triplet.with(5, 0, 35)));
		Pixel arsenalGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.ARSENAL,
				List.of(Triplet.with(255, 224, 54), Triplet.with(251, 230, 93)));

		Pixel cityMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.CITY,
				List.of(Triplet.with(177, 228, 255), Triplet.with(179, 223, 255), Triplet.with(131, 184, 224),
						Triplet.with(91, 146, 187), Triplet.with(178, 229, 255), Triplet.with(192, 237, 255)));

		Pixel cityShieldDetailColourPixel = new Pixel(PixelType.SHIELD_DETAIL_COLOUR, Team.CITY,
				List.of(Triplet.with(153, 63, 64), Triplet.with(180, 71, 77)));

		Pixel cityGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.CITY,
				List.of(Triplet.with(255, 190, 116), Triplet.with(254, 149, 84)));

		Pixel unitedMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.MANCHESTER_UNITED,
				List.of(Triplet.with(231, 56, 66), Triplet.with(230, 50, 61), Triplet.with(241, 44, 60),
						Triplet.with(216, 19, 47), Triplet.with(239, 31, 49), Triplet.with(234, 33, 49)));

		Pixel unitedShieldDetailColourPixel = new Pixel(PixelType.SHIELD_DETAIL_COLOUR, Team.MANCHESTER_UNITED,
				List.of(Triplet.with(255, 241, 93), Triplet.with(232, 205, 36), Triplet.with(247, 254, 70)));

		Pixel unitedGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.MANCHESTER_UNITED,
				List.of(Triplet.with(0, 152, 222), Triplet.with(135, 217, 244)));

		Pixel chelseaMainColourPixel = new Pixel(PixelType.MAIN_COLOUR, Team.CHELSEA,
				List.of(Triplet.with(82, 104, 203), Triplet.with(38, 59, 150), Triplet.with(74, 82, 163),
						Triplet.with(65, 77, 160), Triplet.with(12, 47, 118), Triplet.with(43, 91, 191)));

		Pixel chelseaShieldDetailColourPixel = new Pixel(PixelType.SHIELD_DETAIL_COLOUR, Team.CHELSEA,
				List.of(Triplet.with(232, 220, 88), Triplet.with(255, 245, 120)));

		Pixel chelseaGoalkeeperJerseyColourPixel = new Pixel(PixelType.GOALKEEPER_JERSEY_COLOUR, Team.CHELSEA,
				List.of(Triplet.with(254, 202, 116), Triplet.with(249, 155, 76)));

		List<Pixel> allMappedPixels = List.of(arsenalMainColourPixel, arsenalShieldDetailColourPixel,
				arsenalGoalkeeperJerseyColourPixel, cityMainColourPixel, cityShieldDetailColourPixel,
				arsenalGoalkeeperJerseyColourPixel, cityGoalkeeperJerseyColourPixel, unitedMainColourPixel,
				unitedShieldDetailColourPixel, unitedGoalkeeperJerseyColourPixel, chelseaMainColourPixel,
				chelseaShieldDetailColourPixel, chelseaMainColourPixel, chelseaGoalkeeperJerseyColourPixel);

		float[][] features = new float[41][13];

		for (int i = 0; i < imageFiles.length; i++) {

			arsenalMainColour = 0;
			arsenalShieldDetailColour = 0;
			arsenalGoalkeeperJerseyColour = 0;
			
			cityMainColour = 0;
			cityShieldDetailColour = 0;
			cityGoalkeeperJerseyColour = 0;
			
			unitedMainColour = 0;
			unitedShieldDetailColour = 0;
			unitedGoalkeeperJerseyColour = 0;
			
			chelseaMainColour = 0;
			chelseaShieldDetailColour = 0;
			chelseaGoalkeeperJerseyColour = 0;

			IplImage rawImage = cvLoadImage(imageFolder.getAbsolutePath() + "\\" + imageFiles[i].getName());
			CvSize imageOriginalSize = cvGetSize(rawImage);

			IplImage processedImage = cvCreateImage(imageOriginalSize, IPL_DEPTH_8U, 3);
			processedImage = cvCloneImage(rawImage);

			Team team = Team.getByTeamPrefix(imageFiles[i].getName().split("_")[0]);
			teamCategory = team.getTeamCategory();
			teamCategoryStr = team.getPrefix();

			CvMat colourChannelMatrix = CvMat.createHeader(processedImage.height(), processedImage.width());
			CvScalar processedRGB = new CvScalar();
			cvGetMat(processedImage, colourChannelMatrix, null, 0);

			for (int height = 0; height < processedImage.height(); height++) {
				for (int width = 0; width < processedImage.width(); width++) {
					CvScalar rgbPixel = cvGet2D(processedImage, height, width);
					blue = rgbPixel.val(0);
					green = rgbPixel.val(1);
					red = rgbPixel.val(2);
					for (Pixel pixel : allMappedPixels) {
						if (pixel.match(red, green, blue)) {
							switch (pixel.getTeam()) {
							case ARSENAL: {
								switch (pixel.getType()) {
								case MAIN_COLOUR: {
									arsenalMainColour++;
								}
								case SHIELD_DETAIL_COLOUR: {
									arsenalShieldDetailColour++;
								}
								case GOALKEEPER_JERSEY_COLOUR: {
									arsenalGoalkeeperJerseyColour++;
								}
								}
							}
							case CITY: {
								switch (pixel.getType()) {
								case MAIN_COLOUR: {
									cityMainColour++;
								}
								case SHIELD_DETAIL_COLOUR: {
									cityShieldDetailColour++;
								}
								case GOALKEEPER_JERSEY_COLOUR: {
									cityGoalkeeperJerseyColour++;
								}
								}
							}
							case MANCHESTER_UNITED: {
								switch (pixel.getType()) {
								case MAIN_COLOUR: {
									unitedMainColour++;
								}
								case SHIELD_DETAIL_COLOUR: {
									unitedShieldDetailColour++;
								}
								case GOALKEEPER_JERSEY_COLOUR: {
									unitedGoalkeeperJerseyColour++;
								}
								}
							}
							case CHELSEA: {
								switch (pixel.getType()) {
								case MAIN_COLOUR: {
									chelseaMainColour++;
								}
								case SHIELD_DETAIL_COLOUR: {
									chelseaShieldDetailColour++;
								}
								case GOALKEEPER_JERSEY_COLOUR: {
									chelseaGoalkeeperJerseyColour++;
								}
								}
							}
							}

							processedRGB.setVal(0, 8);
							processedRGB.setVal(1, 100);
							processedRGB.setVal(2, 22);
							cvSet2D(colourChannelMatrix, height, width, processedRGB);
						}
					}
				}
			}
			processedImage = new IplImage(colourChannelMatrix);

			features[i][0] = arsenalMainColour;
			features[i][1] = arsenalShieldDetailColour;
			features[i][2] = arsenalGoalkeeperJerseyColour;

			features[i][3] = cityMainColour;
			features[i][4] = cityShieldDetailColour;
			features[i][5] = cityGoalkeeperJerseyColour;

			features[i][6] = unitedMainColour;
			features[i][7] = unitedShieldDetailColour;
			features[i][8] = unitedGoalkeeperJerseyColour;

			features[i][9] = chelseaMainColour;
			features[i][10] = chelseaShieldDetailColour;
			features[i][11] = chelseaGoalkeeperJerseyColour;

			features[i][12] = teamCategory;

			System.out.println("Arsenal main colour: " + features[i][0] + " - Arsenal shield detail colour: "
					+ features[i][1] + " - Arsenal goalkeeper jersey colour: " + features[i][2]
					+ " - City main colour: " + features[i][3] + " - City shield detail colour: " + features[i][4]
					+ " - City goalkeeper jersey colour: " + features[i][5] + " - United main colour: " + features[i][6]
					+ " - United shield detail colour: " + features[i][7] + " - United goalkeeper jersey colour: "
					+ features[i][8] + " - Chelsea main colour: " + features[i][9] + " - Chelsea shield detail colour: "
					+ features[i][10] + " - Chelsea goalkeeper jersey colour: " + features[i][11] + " - Team category: "
					+ features[i][12]);

			featureVectorFile += features[i][0] + "," + features[i][1] + "," + features[i][2] + "," + features[i][3]
					+ "," + features[i][4] + "," + features[i][5] + "," + features[i][6] + "," + features[i][7] + ","
					+ features[i][8] + "," + features[i][9] + "," + features[i][10] + "," + features[i][11] + ","
					+ teamCategoryStr + "\n";

//			cvShowImage("Imagem original", rawImage);
//			cvShowImage("Imagem processada", processedImage);
//			nextImage = cvWaitKey();

		}
		File arffFile = new File("teams.arff");
		FileOutputStream outputStream = new FileOutputStream(arffFile);
		outputStream.write(featureVectorFile.getBytes());
		outputStream.close();
	}

}
