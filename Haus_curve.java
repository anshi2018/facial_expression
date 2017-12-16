/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haus_curve;
import java.awt.Color;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
public class Haus_curve {

    static String input1="F:\\emotion final yr\\OutputImages\\jaffe2\\neutral_bezier.png";
    static String input2="F:\\emotion final yr\\OutputImages\\jaffe2\\surprise_bezier.png";
    
    /**
     * @param args the command line arguments
     */
    public static ArrayList<Integer> getCoordinates(String path)
    {
        ArrayList<Integer> data = new ArrayList<>();
        //ArrayList<Integer> data_j = new ArrayList<>();
        Mat img = Imgcodecs.imread(path);
        //int count=0;
        int k=0;
        for (int i = 0; i < img.height(); i++) {
                for (int j = 0; j < img.width(); j++) {
                        double[] data_img = img.get(i, j);
                        if (data_img[0] ==0 && data_img[1] == 255 && data_img[2] == 0) {
                                System.out.println("i= " + i + " j= " + j);
                                //count++;
                                //data[k++]=j;
                                //data[k++]=i;
                                data_img[0]=255;
                                data_img[1]=0;
                                data_img[2]=0;
                                //img.put(i, j, data_img);
                                data.add(j);
                                data.add(i);
                        }
                        
                }
               
        }
        Imgcodecs.imwrite(path, img);
        return data;
        
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //List<int> data1=new ArrayList<int>();
        ArrayList<Integer> data1_i = new ArrayList<>();
        ArrayList<Integer> data1_j = new ArrayList<>();
        ArrayList<Integer> data2_i = new ArrayList<>();
        ArrayList<Integer> data2_j = new ArrayList<>();
        ArrayList<Integer> data1=new ArrayList<>();
        ArrayList<Integer> data2=new ArrayList<>();
        ArrayList<Double> min1=new ArrayList<>();
        ArrayList<Double> min2=new ArrayList<>();
        data1=getCoordinates(input1);
        data2=getCoordinates(input2);
        int i,j,k;
        for(i=0;i<data1.size()-1;i+=2)
        {
            data1_j.add(data1.get(i));
            data1_i.add(data1.get(i+1));
        }
        for(i=0;i<data2.size()-1;i+=2)
        {
            data2_j.add(data2.get(i));
            data2_i.add(data2.get(i+1));
        }
        double dist=0.0;
        double min=0.0,max_1=0.0,max_2=0.0;
        for(i=0;i<data1_j.size();i++)
        {
            min=Math.sqrt(Math.pow(data2_i.get(0)-data1_i.get(i), 2)+Math.pow(data2_j.get(0)-data1_j.get(i), 2));
            //max_1=Math.sqrt(Math.pow(data2_i.get(0)-data1_i.get(i), 2)+Math.pow(data2_j.get(0)-data1_j.get(i), 2));
            for(j=0;j<data2_j.size();j++)
            {
                dist=Math.sqrt(Math.pow(data2_i.get(j)-data1_i.get(i), 2)+Math.pow(data2_j.get(j)-data1_j.get(i), 2));
                if(dist<min)
                {
                    min=dist;
                }
            }
            min1.add(min);
        }
        max_1=min1.get(0);
        for(i=0;i<min1.size();i++)
        {
            if(min1.get(i)>max_1)
            {
                max_1=min1.get(i);
            }
        }
        for(i=0;i<data2_j.size();i++)
        {
            min=Math.sqrt(Math.pow(data2_i.get(i)-data1_i.get(0), 2)+Math.pow(data2_j.get(i)-data1_j.get(0), 2));
            //max_2=Math.sqrt(Math.pow(data2_i.get(i)-data1_i.get(0), 2)+Math.pow(data2_j.get(i)-data1_j.get(0), 2));
            for(j=0;j<data1_j.size();j++)
            {
                dist=Math.sqrt(Math.pow(data2_i.get(i)-data1_i.get(j), 2)+Math.pow(data2_j.get(i)-data1_j.get(j), 2));
                if(dist<min)
                {
                    min=dist;
                }
            }
            min2.add(min);
        }
        max_2=min2.get(0);
        for(i=0;i<min2.size();i++)
        {
            if(min2.get(i)>max_2)
            {
                max_2=min2.get(i);
            }
        }
        double max=0.0;
        if(max_1>max_2)
            max=max_1;
        else
            max=max_2;
        System.out.println("Maximum :D "+ max);
           
        //BufferedWriter out = new BufferedWriter(new FileWriter("F:\\emotion final yr\\testout.txt"));
        try(FileWriter fw = new FileWriter("F:\\\\emotion final yr\\\\testout.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
        {
            out.println("img1:: "+input1);
            //more code
            out.println("img2:: "+input2);
            //more code
            out.println("max:: "+max);
            out.println();
            out.println();
            out.println();
        } 
        catch (IOException e) 
        {
    //exception handling left as an exercise for the reader
        }
    } 
    
}
