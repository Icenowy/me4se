/*
 * Created on 08.11.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.me4se.samples.ui;

import java.io.IOException;

import javax.microedition.lcdui.*;

/**
 * @author haustein
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Demo_Image extends Canvas{

	Image image;
	int transform;
	
	public Demo_Image() {
		/*
		image = Image.createImage (10,10);
		image.getGraphics ().fillArc (0,0,10,10,0, 360);
		*/

		try {
			image = Image.createImage("/me4se-logo-small.png");
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to load Image: " + e);
		}
	}

	public void keyPressed(int code){
		if(code >= '0' && code <= '7'){
			transform = (code - 48);
		}
		else {
			int ga = getGameAction(code);
			switch(ga){
			case LEFT: transform--; break;
			case RIGHT: transform++; break;
			}
		}
		transform = transform & 7;
		repaint();
	}
	
	public void paint(Graphics g) {
		g.setGrayScale(255);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
		g.drawImage(image, getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.VCENTER);
//		g.drawRegion(image, 0, 0, image.getWidth(), image.getHeight(), transform, getWidth() / 2, getHeight() / 2, Graphics.VCENTER|Graphics.HCENTER); 
		g.drawImage(image, getWidth(), getHeight(), Graphics.BOTTOM | Graphics.RIGHT);
	}
}
