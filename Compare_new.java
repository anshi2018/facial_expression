/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compare_new;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_8UC1;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
/**
 *
 * @author seema
 */
public class Compare_new {

    
    static String dlib="F:\\InputImages\\dlib_compare_inputs";
    static String canny="F:\\emotion final yr\\comparison_ch_canny\\canny\\";
    static String ch="F:\\emotion final yr\\comparison_ch_canny\\ch\\";
    static String filename="";
    static String extension=".tiff";
    static String python_outputs="F:\\emotion final yr\\python_outputs\\";
    /**
     * @param args the command line arguments
     */
    public static void detectPython(String path)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("\nRunning FaceDetector");

		CascadeClassifier faceDetector = new CascadeClassifier(
				Compare_new.class.getResource("face_detection.xml").getPath().substring(1).replace("%20", " "));

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			filename = listOfFiles[i].getName();
			int pos = filename.lastIndexOf(".");
			if (pos > 0) {
				filename = filename.substring(0, pos);
			}
			System.out.println(filename);

			Mat src = null, image = Imgcodecs.imread(path +"\\"+ filename + extension);
                        
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(image, faceDetections);

			System.out.println(String.format("Detected %s face", faceDetections.toArray().length));

			for (Rect rect : faceDetections.toArray()) {
				Imgproc.rectangle(image, new Point(rect.x, rect.y),
						new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
				src = new Mat(image, rect);
				Imgcodecs.imwrite(python_outputs+filename + "_cropped" + extension, src);
			}
			int height = src.height();
			int width = src.width();
			System.out.println(String.format("Writing %s", filename));
			Imgcodecs.imwrite(python_outputs+ filename + "_DetectedFace" + extension, image);

			Mat leye, reye, mouth;
			leye = new Mat(src, new Rect((int) (0.125f * width), (int) (0.32 * height), (int) (0.375f * width),
					(int) (0.25f * height)));
			Imgcodecs.imwrite(python_outputs+ filename + "_leye" + extension, leye);

			reye = new Mat(src, new Rect((int) (0.50f * width), (int) (0.32 * height), (int) (0.375f * width),
					(int) (0.25f * height)));
			Imgcodecs.imwrite(python_outputs+ filename + "_reye" + extension, reye);

			
			mouth = new Mat(src, new Rect((int) (0.25f * width), (int) (0.72f * height), (int) (0.50f * width),
					(int) (0.25f * height)));
			Imgcodecs.imwrite(python_outputs + filename + "_mouth" + extension, mouth);
    }
    }
    public static int[] getCoordinates(String path)
    {
        int[] data=new int[8];
        Mat img = Imgcodecs.imread(path);
        int count=0;
        int k=0;
        for (int i = 0; i < img.height(); i++) {
                for (int j = 0; j < img.width(); j++) {
                        double[] data_img = img.get(i, j);
                        if (data_img[0] ==255 && data_img[1] == 0 && data_img[2] == 0) {
                                System.out.println("i= " + i + " j= " + j);
                                count++;
                                data[k++]=j;
                                data[k++]=i;
                        }
                        if(count==4)
                            break;
                }
                if(count==4)
                    break;
        }
        return data;
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        
        detectPython(dlib);
        int[] coord_leye=new int[8];
        int[] coord_reye=new int[8];
        //int[] coord_mouth=new int[8];
        int[] coord_leye_ch=new int[8];
        int[] coord_reye_ch=new int[8];
        //int[] coord_mouth_ch=new int[8];
        int[] coord_leye_canny=new int[8];
        int[] coord_reye_canny=new int[8];
        //int[] coord_mouth_canny=new int[8];
        
        File folder = new File(dlib);
        File[] listOfFiles = folder.listFiles();
        int count_ch=0,count_canny=0,tot=0;
        for (int i = 0; i < listOfFiles.length; i++) 
        {
                filename = listOfFiles[i].getName();
                int pos = filename.lastIndexOf(".");
                if (pos > 0) 
                {
                        filename = filename.substring(0, pos);
                }
                System.out.println(filename);

                coord_leye=getCoordinates(python_outputs+filename + "_leye" + extension);
                coord_reye=getCoordinates(python_outputs+ filename + "_reye" + extension);
                //coord_mouth=getCoordinates(python_outputs+filename + "_mouth" + extension);
                coord_leye_ch=getCoordinates(ch+filename+"_leye_lin"+extension);
                coord_reye_ch=getCoordinates(ch+filename+"_reye_lin"+extension);
                //coord_mouth_ch=getCoordinates(ch+filename+"_mouth_lin"+extension);
                coord_leye_canny=getCoordinates(canny+filename+"_leye_lin"+extension);
                coord_reye_canny=getCoordinates(canny+filename+"_reye_lin"+extension);
                //coord_mouth_canny=getCoordinates(canny+filename+"_mouth_lin"+extension);
                /*int inst;
                for(inst=0;inst<8;inst++)
                {
                    System.out.println("hello"+coord_leye[inst]+" "+coord_reye[inst]+" "+coord_leye_ch[inst]+" "+coord_reye_ch[inst]+" "+coord_leye_canny[inst]+" "+coord_reye_canny[inst]);
                }*/
                tot++;
                if(Math.abs(coord_leye[4]-coord_leye_ch[0])<=35&&Math.abs(coord_leye[5]-coord_leye_ch[1])<=35)
                    count_ch++;
                if(Math.abs(coord_leye[6]-coord_leye_ch[2])<=35&&Math.abs(coord_leye[7]-coord_leye_ch[3])<=35)
                    count_ch++;
                if(Math.abs(coord_reye[4]-coord_reye_ch[0])<=35&&Math.abs(coord_reye[5]-coord_reye_ch[1])<=35)
                    count_ch++;
                if(Math.abs(coord_reye[6]-coord_reye_ch[2])<=35&&Math.abs(coord_reye[7]-coord_reye_ch[3])<=35)
                    count_ch++;
                if(Math.abs(coord_leye[4]-coord_leye_canny[0])<=25&&Math.abs(coord_leye[5]-coord_leye_canny[1])<=25)
                    count_canny++;
                if(Math.abs(coord_leye[6]-coord_leye_canny[2])<=25&&Math.abs(coord_leye[7]-coord_leye_canny[3])<=25)
                    count_canny++;
                if(Math.abs(coord_reye[4]-coord_reye_canny[0])<=25&&Math.abs(coord_reye[5]-coord_reye_canny[1])<=25)
                    count_canny++;
                if(Math.abs(coord_reye[6]-coord_reye_canny[2])<=25&&Math.abs(coord_reye[7]-coord_reye_canny[3])<=25)
                    count_canny++;
        }
        int div=tot*4;
        System.out.println(count_ch+"im count_ch");
        System.out.println(count_canny+"im count_canny");
        double per_ch=(double)(count_ch/(double)div)*100;
        double per_canny=(double)(count_canny/(double)div)*100;
        System.out.println(per_ch+" "+per_canny);
        
        
    }
    
}
