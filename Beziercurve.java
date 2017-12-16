/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beziercurve;

/**
 *
 * @author seema
 */

import java.awt.*;
import java.util.*;
import java.awt.geom.Point2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;

public class Beziercurve extends Frame {
	// Set control points
	private int x1 = 200;
	private int y1 = 200;
	
	private int ctrlx = 300;
	private int ctrly = 300;
	
	private int x2 = 300;
	private int y2 = 500;
	
	// Construct frame
 	public Beziercurve() {
		  super("First Bezier Curve");
   	setSize(600, 600);
		//center();
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		// Set points
		Point2D.Double P1 = new Point2D.Double(50, 75); // Start Point
   	Point2D.Double P2 = new Point2D.Double(150, 75); // End Point
		Point2D.Double ctrl1 = new Point2D.Double(80, 25); // Control Point 1
		Point2D.Double ctrl2 = new Point2D.Double(160, 100); // Control Point 2
		
		CubicCurve2D.Double cubicCurve; // Cubic curve
                QuadCurve2D.Double quadCurve;
                
                //quadCurve =new QuadCurve2D.Double(P1.x, P1.y, ctrl1.x, ctrl1.y,P2.x, P2.y);

		cubicCurve = new CubicCurve2D.Double( 0, 0, ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, P2.x, P2.y);
                
		
		Graphics2D g2 = (Graphics2D)g;
		g2.draw(cubicCurve);
                g2.drawLine((int)0, (int)0, (int)ctrl1.x, (int)ctrl1.y);
                g2.drawLine((int)ctrl1.x, (int)ctrl1.y, (int)ctrl2.x, (int)ctrl2.y);
                g2.drawLine((int)ctrl2.x, (int)ctrl2.y, (int)P2.x, (int)P2.y);
                
                //g2.draw(quadCurve);
                
	}

	public static void main(String[] args) {
   	new Beziercurve();
	  } // End of main method
}
