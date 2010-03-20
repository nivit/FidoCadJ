package primitives;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.geom.*;


/**
    Arrow class: draws an arrow of the given size, style and direction.
    This is a static class.
    
    @author Davide Bucci
    @version 1.0, October 2009
 
   <pre>
    This file is part of FidoCadJ.

    FidoCadJ is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FidoCadJ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FidoCadJ.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009-2010 by Davide Bucci
   </pre>
   
*/

public class Arrow {

	/** A few constants in order to define the arrow style.
	
	*/
	public static final int flagLimiter = 0x01;
	public static final int flagEmpty = 0x02;
	

	/** Draw an arrow at the given position.
	
	@param g the graphic context to be used
	@param x the x coordinate of the arrow point
	@param y the y coordinate of the arrow point
	@param xc the x coordinate of the direction point
	@param yc the y coordinate of the direction point
	@param l the length of the arrow
	@param h the half width of the arrow
	@param style the arrow style
	
	*/
	public static void drawArrow(Graphics2D g, int x, int y, int xc, 
		int yc, int l, int h, int style)
	{
		double s;
		double alpha;
		double x0;
		double y0;
		double x1;
		double y1;
		double x2;
		double y2;
		
		// At first we need the angle giving the direction of the arrow
		// a little bit of trigonometry :-)
		
		if (x!=xc)
			alpha = Math.atan((double)(y-yc)/(double)(x-xc));
		else
			alpha = Math.PI/2.0+((y-yc<0)?0:Math.PI);
		
		alpha += (x-xc>0)?0:Math.PI;
		
		
	
		// Then, we calculate the points for the polygon
		x0 = x - l*Math.cos(alpha);
		y0 = y - l*Math.sin(alpha);
		
		x1 = x0 - h*Math.sin(alpha);
		y1 = y0 + h*Math.cos(alpha);
		
		x2 = x0 + h*Math.sin(alpha);
		y2 = y0 - h*Math.cos(alpha);
		
		Polygon p = new Polygon();
      		
     	p.addPoint((int)(x+0.5),(int)(y+0.5));
      	p.addPoint((int)(x1+0.5),(int)(y1+0.5));
      	p.addPoint((int)(x2+0.5),(int)(y2+0.5));
   
      
        if ((style & flagEmpty) == 0)
 			g.fillPolygon(p);	
 		else
 			g.drawPolygon(p);
 			
 		if ((style & flagLimiter) != 0) {
 			double x3;
			double y3;
			double x4;
			double y4;
			x3 = x - h*Math.sin(alpha);
			y3 = y + h*Math.cos(alpha);
		
			x4 = x + h*Math.sin(alpha);
			y4 = y - h*Math.cos(alpha);
			g.drawLine((int)x3,(int)y3,(int)x4,(int)y4);
 		}
 		
	}
	
}