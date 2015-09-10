package org.me4se.samples.ui;

import javax.microedition.lcdui.*;

public class Demo_Drawing extends Canvas {

	public int hueToRgb(int hue) {
		int r = 0, g = 0, b = 0;

		int h = hue / 43; // 0..5
		int f = (hue % 43) * 6; // 0..255 

		int p = 0;
		int q = 255 - f;
		int t = f;

		switch (h) {
			case 0 :
				r = 255;
				g = f;
				b = 0;
				break;
			case 1 :
				r = 255 - f;
				g = 255;
				b = 0;
				break;
			case 2 :
				r = 0;
				g = 255;
				b = f;
				break;
			case 3 :
				r = 0;
				g = 255 - f;
				b = 255;
				break;
			case 4 :
				r = f;
				g = 0;
				b = 255;
				break;
			case 5 :
				r = 255;
				g = 0;
				b = 255 - f;
				break;
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
	}

	public void paint(Graphics g) {

		int width = getWidth();
		int height = getHeight();

		int m = width > height ? height : width;
		int step = m / 10;
		m = step * 10;

		g.setColor(127, 127, 127);
		g.fillRect(0, 0, width, height);

		g.setColor(255, 255, 255);

		for (int i = step / 2; i < width; i += step)
			g.drawLine(i, 0, i, height);

		for (int i = step / 2; i < height; i += step)
			g.drawLine(0, i, width, i);

		g.translate((width - m) / 2, (height - m) / 2);
		g.fillRect(step, step, 8 * step, 8 * step);

		int w = 8 * step;
		for (int i = 0; i < w; i++) {

			int j = (i * 255) / w;
			g.setColor(hueToRgb(j));
			g.drawLine(i + step, step, i + step, 3 * step + step / 2);

			g.setColor(j, j, j);
			g.drawLine(i + step, 3 * step + step / 2, i + step, 5 * step);
		}

		g.setColor(255, 255, 255);
		
		g.drawArc(0, 0, m, m, 0, 360);

		int ty = 6 * step;

		g.setColor(0);

		boolean small = m < 100;

		g.translate(-g.getTranslateX(), 0);
		g.drawString("ME4SE.org", getWidth()/2, ty, Graphics.TOP | Graphics.HCENTER);
		g.drawString("Testscreen", getWidth()/2, ty + step, Graphics.TOP | Graphics.HCENTER);
	}
}
