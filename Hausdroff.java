/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hausdroff;
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

public class Hausdroff {

    /**
     * @param args the command line arguments
     */
    static String input1="F:\\InputImages\\KL.DI1.170_cropped.tiff";
    static String input2="F:\\InputImages\\KL.SU3.166_cropped.tiff";
    public static int[] getControlPoints(int[] coord)
        {
            int[] ctrl=new int[8];
            ctrl[0]=(int)(Math.round((double)(8/9.0)*(double)(3*coord[0]-coord[2])-(double)(1/9.0)*(double)(10*coord[4]-3*coord[6])));
            ctrl[1]=(int)(Math.round((double)(8/9.0)*(double)(3*coord[1]-coord[3])-(double)(1/9.0)*(double)(10*coord[5]-3*coord[7])));
            ctrl[2]=(int)Math.round((double)(8/9.0)*(double)(3*coord[2]-coord[0])+(double)(1/9.0)*(double)(3*coord[4]-10*coord[6]));
            ctrl[3]=(int)Math.round((double)(8/9.0)*(double)(3*coord[3]-coord[1])+(double)(1/9.0)*(double)(3*coord[5]-10*coord[7]));
            ctrl[4]=(int)Math.round((double)(8/9.0)*(double)(3*coord[8]-coord[10])-(double)(1/9.0)*(double)(10*coord[12]-3*coord[14]));
            ctrl[5]=(int)Math.round((double)(8/9.0)*(double)(3*coord[9]-coord[11])-(double)(1/9.0)*(double)(10*coord[13]-3*coord[15]));
            ctrl[6]=(int)Math.round((double)(8/9.0)*(double)(3*coord[10]-coord[8])+(double)(1/9.0)*(double)(3*coord[12]-10*coord[14]));
            ctrl[7]=(int)Math.round((double)(8/9.0)*(double)(3*coord[11]-coord[9])+(double)(1/9.0)*(double)(3*coord[13]-10*coord[15]));
            return ctrl;
        }
        public static int[] getCoordinates(String path) throws IOException
        {
            int k=0;
        
            int[] data1=new int[8];
            int[] data2=new int[8];
            int[] result=new int[data1.length+data2.length];
            Mat img = Imgcodecs.imread(path);
           
            int i,j;
            int count=0;
            
            for (i = 0; i < img.height(); i++) 
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
            for(i=0;i<img.height();i++)
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
            System.arraycopy(data1, 0, result, 0,data1.length);
            System.arraycopy(data2, 0, result, data1.length, data2.length);
            return result;
        
        }
        public static double min(double a,double b)
        {
            
            if(a<b)
                return a;
            return b;
            
            
        }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int[] coordinates_1=new int[16];
        coordinates_1=getCoordinates(input1);
        int[] ctrlpts_1=new int[8];
        ctrlpts_1=getControlPoints(coordinates_1);
        int[] coordinates_2=new int[16];
        coordinates_2=getCoordinates(input2);
        int[] ctrlpts_2=new int[8];
        ctrlpts_2=getControlPoints(coordinates_2);
        double[] dper=new double[3];
        double[] dper1=new double[3];
        double[] dper2=new double[3];
        double[] dpar=new double[3];
        double[] dpar1=new double[3];
        double[] dpar2=new double[3];
        double[] dls=new double[3];
        int[] xm1=new int[3];
        int[] ym1=new int[3];
        int[] xt1=new int[3];
        int[] yt1=new int[3];
        int[] xm2=new int[3];
        int[] ym2=new int[3];
        int[] xt2=new int[3];
        int[] yt2=new int[3];
        double[] xmt1=new double[3];
        double[] ymt1=new double[3];
        double[] xmt2=new double[3];
        double[] ymt2=new double[3];
        double[] mm=new double[3];
        double[] mt=new double[3];
        double[] cm=new double[3];
        double[] ct=new double[3];
        double[] theta=new double[3];
        xm1[0]=coordinates_1[4];
        ym1[0]=coordinates_1[5];
        xm2[0]=ctrlpts_1[0];
        ym2[0]=ctrlpts_1[1];
        xm1[1]=ctrlpts_1[0];
        ym1[1]=ctrlpts_1[1];
        xm2[1]=ctrlpts_1[2];
        ym2[1]=ctrlpts_1[3];
        xm1[2]=ctrlpts_1[2];
        ym1[2]=ctrlpts_1[3];
        xm2[2]=coordinates_1[6];
        ym2[2]=coordinates_1[7];
        
        xt1[0]=coordinates_2[4];
        yt1[0]=coordinates_2[5];
        xt2[0]=ctrlpts_2[0];
        yt2[0]=ctrlpts_2[1];
        xt1[1]=ctrlpts_2[0];
        yt1[1]=ctrlpts_2[1];
        xt2[1]=ctrlpts_2[2];
        yt2[1]=ctrlpts_2[3];
        xt1[2]=ctrlpts_2[2];
        yt1[2]=ctrlpts_2[3];
        xt2[2]=coordinates_2[6];
        yt2[2]=coordinates_2[7];
        
        int i;
        for(i=0;i<3;i++)
        {
            mm[i]=(double)((ym2[i]-ym1[i])/(double)(xm2[i]-xm1[i]));
        }
        for(i=0;i<3;i++)
        {
            mt[i]=(double)((yt2[i]-yt1[i])/(double)(xt2[i]-xt1[i]));
        }
        for(i=0;i<3;i++)
        {
            cm[i]=(double)((xm2[i]*ym1[i]-xm1[i]*ym2[i])/(double)(xm2[i]-xm1[i]));
        }
        for(i=0;i<3;i++)
        {
            ct[i]=(double)((xt2[i]*yt1[i]-xt1[i]*yt2[i])/(double)(xt2[i]-xt1[i]));
        }
        for(i=0;i<3;i++)
        {
            theta[i]=(double)(Math.atan((mt[i]-mm[i])/(double)(1+mt[i]*mm[i])));
            //System.out.println(theta[i]);
        }
        for(i=0;i<3;i++)
        {
            if(theta[i]==0.0||theta[i]==3.14)
            {
                dper[i]=(double)(Math.abs(cm[i]-ct[i])/(double)(Math.sqrt(1+Math.pow(mm[i], 2))));
                //System.out.println("hey");
            }
            else
            {
                dper1[i]=(double)(Math.sqrt(Math.pow(yt1[i]-ym1[i], 2)+Math.pow(xt1[i]-xm1[i],2)));
                dper2[i]=(double)(Math.sqrt(Math.pow(yt2[i]-ym2[i], 2)+Math.pow(xt2[i]-xm2[i],2)));
                dper[i]=min(dper1[i],dper[i]);
            }
            //System.out.println(dper[i]);
        }
        for(i=0;i<3;i++)
        {
            xmt1[i]=(double)(mm[i]*yt1[i]+xt1[i]-mm[i]*cm[i])/(double)(1+Math.pow(mm[i], 2));
            ymt1[i]=(double)(Math.pow(mm[i], 2)*yt1[i]+mm[i]*xt1[i]+cm[i])/(double)(1+Math.pow(mm[i], 2));
            xmt2[i]=(double)(mm[i]*yt2[i]+xt2[i]-mm[i]*cm[i])/(double)(1+Math.pow(mm[i], 2));
            ymt2[i]=(double)(Math.pow(mm[i], 2)*yt2[i]+mm[i]*xt2[i]+cm[i])/(double)(1+Math.pow(mm[i], 2));
            dpar1[i]=(double)(Math.sqrt(Math.pow(ymt1[i]-ym1[i], 2)+Math.pow(xmt1[i]-xm1[i],2)));
            dpar2[i]=(double)(Math.sqrt(Math.pow(ymt2[i]-ym2[i], 2)+Math.pow(xmt2[i]-xm2[i],2)));
            dpar[i]=min(dpar1[i],dpar2[i]);
            System.out.println("par"+dpar[i]);
            dls[i]=(double)(Math.sqrt(Math.pow(theta[i],2)+Math.pow(dper[i],2)+Math.pow(dpar[i], 2)));
            System.out.println(dls[i]);
        }
        
    }
    
}
