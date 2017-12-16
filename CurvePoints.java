
public class CurvePoints {

	public static class point{
		
		private final int x;
		private final int y;
		
		public point(int x,int y){
			this.x=x;
			this.y=y;
		}
	}
	public static double dist(point p1,point p2){
		
		return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
		
	}
	
	public static double  lengthOfCurve (point[] points){
		double length=0;
		for(int i=0;i<points.length-1;i++){
			length+= dist(points[i],points[i+1]);
		}
		return length;
	}
	
	public static point pointAtOneFourth(point[] points){
		double length= lengthOfCurve(points);
		length/=4;
		int x = 0 ,y = 0;
		int i=0;
		double seg=dist(points[i],points[i+1]);
		while(seg<length){
			i++;
			seg=dist(points[i],points[i+1]);
			length-=seg;
		}
		
		x= (int) (points[i].x + ( length*(points[i+1].x-points[i].x))/ seg);
		y= (int) (points[i].y + ( length*(points[i+1].y-points[i].y))/ seg);
		
		CurvePoints.point p= new CurvePoints.point(x,y);
		return p;
	}
	
	public static point pointAtThreeFourth(point[] points){
		double length= lengthOfCurve(points);
		length/=4;
		int x = 0 ,y = 0;
		int i=points.length-1;
		double seg=dist(points[i],points[i-1]);
		while(seg<length){
			i--;
			seg=dist(points[i],points[i-1]);
			length-=seg;
		}
		
		x= (int) (points[i].x + ( length*(points[i-1].x-points[i].x))/seg);
		y= (int) (points[i].y + ( length*(points[i-1].y-points[i].y))/seg);
		
		CurvePoints.point p= new CurvePoints.point(x,y);
		return p;
	}
	

	public static void main(String[] args){
		
		
	}
}
