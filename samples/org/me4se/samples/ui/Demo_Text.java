package org.me4se.samples.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class Demo_Text extends Canvas {

	public void paint (Graphics g) {
	g.setGrayScale (255);
	g.fillRect (0, 0, getWidth (), getHeight ());

	g.setGrayScale (0);
	g.drawString ("Top/Left", 0, 0, Graphics.TOP |  Graphics.LEFT);
	g.drawString ("Baseline/Center", getWidth () / 2, getHeight () / 2, 
			  Graphics.HCENTER | Graphics.BASELINE);
	g.drawString ("Bottom/Right", getWidth (), getHeight (), 
			  Graphics.BOTTOM | Graphics.RIGHT);
	}
}



