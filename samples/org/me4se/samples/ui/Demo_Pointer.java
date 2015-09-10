package org.me4se.samples.ui;

import javax.microedition.lcdui.*;

public class Demo_Pointer extends Canvas {

	String eventType = "Press Pointer!";
	int x;
	int y;

	public void pointerPressed(int x, int y) {
		eventType = "Pointer Pressed";
		this.x = x;
		this.y = y;
		repaint();
	}

	public void pointerReleased(int x, int y) {
		eventType = "Pointer Released";
		this.x = x;
		this.y = y;
		repaint();
	}

	public void pointerDragged(int x, int y) {
		eventType = "Pointer Repeated";
		this.x = x;
		this.y = y;
		repaint();
	}

	public void paint(Graphics g) {
		g.setGrayScale(255);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setGrayScale(0);
		g.drawString(eventType + " " + x + "/" + y, 0, 0, Graphics.TOP | Graphics.LEFT);
		g.drawLine(x - 4, y, x + 4, y);
		g.drawLine(x, y - 4, x, y + 4);
	}
}
