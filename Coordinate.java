/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate;


import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
/**
 *
 * @author seema
 */
public class Coordinate extends JFrame {
        static String outLoc = "F:\\emotion final yr\\final _output\\";
        static String inLoc = "F:\\InputImages\\dlib_final_outputs\\";
	static String filename = "";
	static String extension = ".tiff";
        static String[] emotions={"neutral","happy","sad","surprise","disgust"};
        static String excel_file="";
    /**
     * @param args the command line arguments
     */
        static int height=0;
        static int width=0;
        public static int[] getControlPoints(int[] coord)
        {
            int[] ctrl=new int[12];
            ctrl[0]=(int)(Math.round((double)(8/9.0)*(double)(3*coord[0]-coord[2])-(double)(1/9.0)*(double)(10*coord[4]-3*coord[6])));
            ctrl[1]=(int)(Math.round((double)(8/9.0)*(double)(3*coord[1]-coord[3])-(double)(1/9.0)*(double)(10*coord[5]-3*coord[7])));
            ctrl[2]=(int)Math.round((double)(8/9.0)*(double)(3*coord[2]-coord[0])+(double)(1/9.0)*(double)(3*coord[4]-10*coord[6]));
            ctrl[3]=(int)Math.round((double)(8/9.0)*(double)(3*coord[3]-coord[1])+(double)(1/9.0)*(double)(3*coord[5]-10*coord[7]));
            ctrl[4]=(int)Math.round((double)(8/9.0)*(double)(3*coord[8]-coord[10])-(double)(1/9.0)*(double)(10*coord[12]-3*coord[14]));
            ctrl[5]=(int)Math.round((double)(8/9.0)*(double)(3*coord[9]-coord[11])-(double)(1/9.0)*(double)(10*coord[13]-3*coord[15]));
            ctrl[6]=(int)Math.round((double)(8/9.0)*(double)(3*coord[10]-coord[8])+(double)(1/9.0)*(double)(3*coord[12]-10*coord[14]));
            ctrl[7]=(int)Math.round((double)(8/9.0)*(double)(3*coord[11]-coord[9])+(double)(1/9.0)*(double)(3*coord[13]-10*coord[15]));
            ctrl[8]=(int)Math.round((double)(8/9.0)*(double)(3*coord[18]-coord[20])-(double)(1/9.0)*(double)(10*coord[16]-3*coord[22]));
            ctrl[9]=(int)Math.round((double)(8/9.0)*(double)(3*coord[19]-coord[21])-(double)(1/9.0)*(double)(10*coord[17]-3*coord[23]));       
            ctrl[10]=(int)Math.round((double)(8/9.0)*(double)(3*coord[20]-coord[18])+(double)(1/9.0)*(double)(3*coord[16]-10*coord[22]));
            ctrl[11]=(int)Math.round((double)(8/9.0)*(double)(3*coord[21]-coord[19])+(double)(1/9.0)*(double)(3*coord[17]-10*coord[23]));
            return ctrl;
        }
        public static int[] getCoordinates(String path) throws IOException
        {
            int k=0;
        
            int[] data1=new int[8];
            int[] data2=new int[8];
            int[] data3=new int[8];
            int[] result=new int[data1.length+data2.length+data3.length];
            Mat img = Imgcodecs.imread(path);
            //System.out.println(img.dump());
            int i,j;
            int count=0;
            //System.out.println("height"+img.height());
            //System.out.println("height"+img.width());
            for (i = 0; i <= img.height()/2; i++) 
            {
                for (j = 0; j <= img.width()/2; j++) 
                {
                        double[] data = img.get(i, j);
                        //System.out.println(data[0]+" "+data[1]+" "+data[2]);
                        if (data[0] ==255&& data[1] == 0 && data[2] == 0) 
                        {
                                //System.out.println("i= " + i + " j= " + j);
                                data1[k++]=j;
                                data1[k++]=i;

                                //System.out.println("hey");
                                count++;
                                

                        }

                        if(count==4)
                            break;

                }
                if(count==4)
                    break;   
            }
            count =0;
            k=0;
            for(i=0;i<=img.height()/2;i++)
            {
                for(j=img.width()/2;j<img.width();j++)
                {
                    double[] data = img.get(i, j);
                    //System.out.println(data[0]+" "+data[1]+" "+data[2]);
                    if (data[0] ==255 && data[1] == 0 && data[2] == 0) 
                    {
                            //System.out.println("i= " + i + " j= " + j);
                            data2[k++]=j;
                            data2[k++]=i;

                            //System.out.println("hey");
                            count++;

                    }
                    if(count==4)
                        break;
                }
                if(count==4)
                    break;
            }
            //mouth
            count =0;
            k=0;
            for(j=0;j<img.width();j++)
            {
                for(i=img.height()/2;i<img.height();i++)
                {
                    double[] data=img.get(i, j);
                    if (data[0] ==255 && data[1] == 0 && data[2] == 0) 
                    {
                            //System.out.println("i= " + i + " j= " + j);
                            data3[k++]=j;
                            data3[k++]=i;
                            data[0]=0;
                            data[1]=255;
                            data[2]=0;
                            //img.put(j, count, data)
                            System.out.println("hey"+j+" "+i);
                            count++;
                            break;
                            

                    }
                }
                if(count==4)
                    break;
            }
            
            System.arraycopy(data1, 0, result, 0,data1.length);
            System.arraycopy(data2, 0, result, data1.length, data2.length);
            System.arraycopy(data3,0,result,data1.length+data2.length,data3.length);
            return result;
        
        }
        
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");
 
        CascadeClassifier faceDetector = new CascadeClassifier
        		(Coordinate.class.getResource("face_detection.xml").getPath().substring(1).replace("%20", " "));
        for(int i=0;i<emotions.length;i++)
        {
            File folder = new File(inLoc +emotions[i]);
	    File[] listOfFiles = folder.listFiles();
            for(int j=0;j<listOfFiles.length;j++)
            {
                filename=listOfFiles[j].getName();
                int pos=filename.lastIndexOf(".");
                if(pos>0)
                {
                    filename=filename.substring(0,pos);
                }
                System.out.println(filename);
                Mat src = null, image = Imgcodecs.imread(inLoc+emotions[i]+"\\"+filename+extension);
                MatOfRect faceDetections = new MatOfRect();

                faceDetector.detectMultiScale(image, faceDetections);


                System.out.println(String.format("Detected %s face", faceDetections.toArray().length));

                for (Rect rect : faceDetections.toArray()) {
                    Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(255, 255, 255));
                    src=new Mat(image,rect);
                    Imgcodecs.imwrite(outLoc+emotions[i]+"\\"+filename+"_cropped"+".png", src);
                }
                height=src.height();
                width=src.width();
                System.out.println(String.format("Writing %s", filename));
                Imgcodecs.imwrite(outLoc+emotions[i]+"\\"+filename+"_DetectedFace"+".tiff", image);
               int[] coordinates=getCoordinates(outLoc+emotions[i]+"\\"+filename+"_cropped"+".png");

                int k;
                for(k=0;k<24;k++)
                    System.out.print("coordi"+coordinates[k]+" ");
                System.out.println();

                int[] ctrlpts=getControlPoints(coordinates);
                BufferedImage img=ImageIO.read(new File(outLoc+emotions[i]+"\\"+filename+"_cropped"+".png"));

                Color color = Color.GREEN;

                Graphics2D g=img.createGraphics();
                CubicCurve2D.Double cubicCurve,cubicCurve2;
                cubicCurve = new CubicCurve2D.Double( coordinates[4],coordinates[5], ctrlpts[0], ctrlpts[1], ctrlpts[2], ctrlpts[3], coordinates[6], coordinates[7]);
                cubicCurve2=new CubicCurve2D.Double(coordinates[16],coordinates[17],ctrlpts[8],ctrlpts[9],ctrlpts[10],ctrlpts[11],coordinates[22],coordinates[23]);
                g.setColor(color);
                g.draw(cubicCurve2);
                g.draw(cubicCurve);

                System.out.println("control points"+ctrlpts[0]+" "+ctrlpts[1]+" "+ctrlpts[2]+" "+ctrlpts[3]);
                
                ImageIcon ii = new ImageIcon(img);
                 if (ImageIO.write(img, "png", new File(outLoc+emotions[i]+"\\"+filename+"_bezier"+".png")))
                    {
                        System.out.println("-- saved");
                    }
                   int[] final_cord=new int[24];
                   final_cord[0]=coordinates[4];
                   final_cord[1]=coordinates[5];
                   final_cord[2]=ctrlpts[0];
                   final_cord[3]=ctrlpts[1];
                   final_cord[4]=ctrlpts[2];
                   final_cord[5]=ctrlpts[3];
                   final_cord[6]=coordinates[6];
                   final_cord[7]=coordinates[7];
                   
                   final_cord[8]=coordinates[12];
                   final_cord[9]=coordinates[13];
                   final_cord[10]=ctrlpts[4];
                   final_cord[11]=ctrlpts[5];
                   final_cord[12]=ctrlpts[6];
                   final_cord[13]=ctrlpts[7];
                   final_cord[14]=coordinates[14];
                   final_cord[15]=coordinates[15];
                   
                   final_cord[16]=coordinates[16];
                   final_cord[17]=coordinates[17];
                   final_cord[18]=ctrlpts[8];
                   final_cord[19]=ctrlpts[9];
                   final_cord[20]=ctrlpts[10];
                   final_cord[21]=ctrlpts[11];
                   final_cord[22]=coordinates[22];
                   final_cord[23]=coordinates[23];
                   
                  try(FileWriter fw = new FileWriter("F:\\\\emotion final yr\\\\test.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw))
                    {
                        for(int h=0;h<24;h++)
                        {
                            out.print(final_cord[h]+",");
                        }
                        out.println(emotions[i]);
                    } 
                    catch (IOException e) 
                    {
                //exception handling left as an exercise for the reader
                    } 
            }
            
            
                
        }
        /*
        */
        
       
    }
    
}
