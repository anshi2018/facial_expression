
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.*;
import org.opencv.objdetect.CascadeClassifier;

public class Emotionbibhu {

	static String outLoc = "f:\\InputImages\\comparison_ch_canny\\";
	static String inLoc = "f:\\InputImages\\";
	static String filename;
	static String extension = ".tiff";

	public static void linsearch(String file, String method) throws IOException {
		Mat gray = Imgcodecs.imread(outLoc + filename + "_" + file + "_" + method + extension, 0);
		Imgproc.cvtColor(gray, gray, COLOR_GRAY2BGR);
		// gray.convertTo(gray, CV_8UC1);
		int rows = gray.height();
		int cols = gray.width();
		System.out.println(rows + " " + cols);

		int flag = 0;
		int left = 0, right = 0;

		int absent = 0;
		for (int j = cols / 2; j < .90 * cols; j++) {
			int khali = 0;
			for (int i = (int) (rows * 0.25); i < rows * 0.75; i++) {
				double[] data = gray.get(i, j);

				if (data[0] == 255 && data[1] == 255 && data[2] == 255) {
					khali = 1;
					break;
				}
			}
			if (khali == 0)
				absent++;
			else
				absent = 0;

			if (absent == 4) {
				// System.out.println("andar hun\n\n");
				for (int k = j; k >= cols / 2; k--) {
					for (int l = (int) (rows * 0.25); l < rows * 0.75; l++) {
						double[] data = gray.get(l, k);
						// System.out.println(" aur andar hun\n\n");
						if (data[0] == 255 && data[1] == 255 && data[2] == 255) {
							data[0] = 255;
							data[1] = 0;
							data[2] = 0;
							gray.put(l, k, data);
							left = 1;
							flag = 1;
							break;
						}
					}
					if (flag == 1)
						break;
				}
			}
			if (flag == 1)
				break;

		}
		flag = 0;
		absent = 0;
		for (int j = cols / 2; j > .1 * cols; j--) {
			int khali = 0;
			for (int i = (int) (rows * 0.25); i < rows * 0.75; i++) {
				double[] data = gray.get(i, j);

				if (data[0] == 255 && data[1] == 255 && data[2] == 255) {
					khali = 1;
					break;
				}
			}
			if (khali == 0)
				absent++;
			else
				absent = 0;

			if (absent == 4) {
				for (int k = j; k < cols / 2; k++) {
					for (int l = (int) (rows * 0.25); l < rows * 0.75; l++) {
						double[] data = gray.get(l, k);

						// System.out.println(" aur andar hun\n\n");
						if (data[0] == 255 && data[1] == 255 && data[2] == 255) {
							data[0] = 255;
							data[1] = 0;
							data[2] = 0;
							gray.put(l, k, data);
							right = 1;
							flag = 1;
							break;
						}
					}
					if (flag == 1)
						break;
				}
			}
			if (flag == 1)
				break;
		}
		int i, j;
		if (right == 0) {
			flag = 0;
			for (j = 0; j < cols; j++) {
				for (i = rows - 1; i >= 0; i--) {
					double[] data = gray.get(i, j);
					if (data[0] == 255 && data[1] == 255 && data[2] == 255) {
						data[0] = 255;
						data[1] = 0;
						data[2] = 0;
						gray.put(i, j, data);
						flag = 1;
						break;
					}
				}
				if (flag == 1)
					break;
			}

		}
		if (left == 0) {
			flag = 0;
			for (j = cols - 1; j >= 0; j--) {
				for (i = rows - 1; i >= 0; i--) {
					double[] data = gray.get(i, j);
					if (data[0] == 255 && data[1] == 255 && data[2] == 255) {
						data[0] = 255;
						data[1] = 0;
						data[2] = 0;
						gray.put(i, j, data);
						flag = 1;
						break;
					}
				}
				if (flag == 1)
					break;
			}
		}

		Imgcodecs.imwrite(outLoc + "\\" + method + "\\" + filename + "_" + file + "_lin" + extension, gray);

	}

	public static void chMethod(String file) {

		Mat gray = Imgcodecs.imread(outLoc + filename + "_" + file + extension);
		Imgproc.cvtColor(gray, gray, Imgproc.COLOR_RGB2GRAY);

		double threshold = 0;
		switch (file) {
		case "lbrow":
			threshold = 0.240;
			break;
		case "rbrow":
			threshold = 0.220;
			break;
		case "leye":
			threshold = 0.070;
			break;
		case "reye":
			threshold = 0.060;
			break;
		case "nose":
			threshold = 0.010;
			break;
		case "mouth":
			threshold = 0.060;
			break;

		}

		double totalPixels = gray.width() * gray.height();
		double[] pixels = new double[256];
		double[] px = new double[256];
		for (int i = 0; i < 256; i++) {
			pixels[i] = 0;
			px[i] = 0;
		}

		for (int i = 0; i < gray.height(); i++) {
			for (int j = 0; j < gray.width(); j++) {
				double[] data = gray.get(i, j);
				pixels[(int) data[0]]++;
			}
		}

		for (int i = 0; i < 256; i++) {
			px[i] = pixels[i] / totalPixels;
		}
		for (int i = 1; i < 256; i++) {
			px[i] += px[i - 1];
		}

		for (int i = 0; i < gray.height(); i++) {
			for (int j = 0; j < gray.width(); j++) {
				double[] data = gray.get(i, j);
				if (px[(int) data[0]] <= threshold) {
					data[0] = 255;
				} else
					data[0] = 0;
				gray.put(i, j, data);
			}
		}
		Imgcodecs.imwrite(outLoc + filename + "_" + file + "_ch" + extension, gray);
	}

	public static class OpenCVCannyEdgeDetection {

		public void detectEdges(String path) {
			// read the RGB image
			Mat rgbImage = Imgcodecs.imread(outLoc + filename + "_" + path + extension);
			// mat gray image holder
			Mat imageGray = new Mat();
			// mat canny image
			Mat imageCny = new Mat();
			// Show the RGB Image

			Imgproc.cvtColor(rgbImage, imageGray, Imgproc.COLOR_BGR2GRAY);
			Imgproc.Canny(imageGray, imageCny, 140, 200, 3, true);
			Imgcodecs.imwrite(outLoc + filename + "_" + path + "_canny" + extension, imageCny);
		}
	}

	public static void main(String[] args) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("\nRunning FaceDetector");

		CascadeClassifier faceDetector = new CascadeClassifier(
				Emotionbibhu.class.getResource("face_detection.xml").getPath().substring(1).replace("%20", " "));
		// CascadeClassifier eyeDetector = new CascadeClassifier
		// (Emotionbibhu.class.getResource("haarcascade_eye.xml").getPath().substring(1).replace("%20",
		// " "));
		File folder = new File(inLoc + "\\jaffe");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			filename = listOfFiles[i].getName();
			int pos = filename.lastIndexOf(".");
			if (pos > 0) {
				filename = filename.substring(0, pos);
			}
			System.out.println(filename);

			Mat src = null, image = Imgcodecs.imread("f:\\InputImages\\jaffe\\" + filename + extension);

			MatOfRect faceDetections = new MatOfRect();
			// MatOfRect eyeDetections = new MatOfRect();
			faceDetector.detectMultiScale(image, faceDetections);
			// eyeDetector.detectMultiScale(image2, eyeDetections);

			System.out.println(String.format("Detected %s face", faceDetections.toArray().length));

			for (Rect rect : faceDetections.toArray()) {
				Imgproc.rectangle(image, new Point(rect.x, rect.y),
						new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
				src = new Mat(image, rect);
				Imgcodecs.imwrite(outLoc + filename + "_cropped" + extension, src);
			}
			int height = src.height();
			int width = src.width();
			System.out.println(String.format("Writing %s", filename));
			Imgcodecs.imwrite(outLoc + filename + "_DetectedFace" + extension, image);

			Mat lbrow, rbrow, leye, reye, nose, mouth;
			/*
			 * lbrow=new Mat(src,new
			 * Rect((int)(0.125f*width),(int)(0.25*height),(int)(0.375f*width),(
			 * int)(0.125f*height)));
			 * Imgcodecs.imwrite(outLoc+filename+"_lbrow"+extension, lbrow);
			 * 
			 * rbrow=new Mat(src,new
			 * Rect((int)(0.50f*width),(int)(0.25*height),(int)(0.375f*width),(
			 * int)(0.125f*height)));
			 * Imgcodecs.imwrite(outLoc+filename+"_rbrow"+extension, rbrow);
			 */

			leye = new Mat(src, new Rect((int) (0.125f * width), (int) (0.32 * height), (int) (0.375f * width),
					(int) (0.25f * height)));
			Imgcodecs.imwrite(outLoc + filename + "_leye" + extension, leye);

			reye = new Mat(src, new Rect((int) (0.50f * width), (int) (0.32 * height), (int) (0.375f * width),
					(int) (0.25f * height)));
			Imgcodecs.imwrite(outLoc + filename + "_reye" + extension, reye);

			/*
			 * nose=new Mat(src,new
			 * Rect((int)(0.25f*width),(int)(0.50f*height),(int)(0.50f*width),(
			 * int)(0.23f*height)));
			 * Imgcodecs.imwrite(outLoc+filename+"_nose"+extension, nose);
			 */

			mouth = new Mat(src, new Rect((int) (0.25f * width), (int) (0.72f * height), (int) (0.50f * width),
					(int) (0.25f * height)));
			Imgcodecs.imwrite(outLoc + filename + "_mouth" + extension, mouth);

			// chMethod("lbrow");
			// chMethod("rbrow");
			chMethod("leye");
			chMethod("reye");
			chMethod("mouth");
			// chMethod("nose");
			OpenCVCannyEdgeDetection edgeDetection_leye = new OpenCVCannyEdgeDetection();
			edgeDetection_leye.detectEdges("leye");
			OpenCVCannyEdgeDetection edgeDetection_reye = new OpenCVCannyEdgeDetection();
			edgeDetection_reye.detectEdges("reye");
			OpenCVCannyEdgeDetection edgeDetection_mouth = new OpenCVCannyEdgeDetection();
			edgeDetection_mouth.detectEdges("mouth");

			linsearch("leye", "ch");
			linsearch("reye", "ch");
			linsearch("mouth", "ch");

			linsearch("leye", "canny");
			linsearch("reye", "canny");
			linsearch("mouth", "canny");

			System.out.println("Done");
		}
	}

}
