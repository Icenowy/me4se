package org.me4se.samples.ui;


import javax.microedition.lcdui.*;

public class Demo_Clip extends Canvas {

    public void paint (Graphics g) {
	g.setGrayScale (255);
	g.fillRect (0, 0, getWidth (), getHeight ());

	int m = Math.min (getWidth (), getHeight ());	
	g.setGrayScale (0);

	g.setStrokeStyle (Graphics.DOTTED);
	g.drawLine (0, 0, m, m);

	g.setClip (m / 4, m / 4, m / 2, m / 2);

	g.setStrokeStyle (Graphics.SOLID);
	g.drawLine (0, 0, m, m);
    }
}



