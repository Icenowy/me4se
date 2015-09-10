package org.me4se.samples.ui;

import javax.microedition.lcdui.*;

public class Demo_Fonts extends Canvas {

	static final int[] styles =
		{
			Font.STYLE_PLAIN,
			Font.STYLE_BOLD,
			Font.STYLE_ITALIC,
			Font.STYLE_BOLD | Font.STYLE_ITALIC };
			
	static final int[] sizes = { Font.SIZE_SMALL, Font.SIZE_MEDIUM, Font.SIZE_LARGE };
	static final int[] faces = { Font.FACE_SYSTEM, Font.FACE_MONOSPACE, Font.FACE_PROPORTIONAL };

	public void paint(Graphics g) {
		Font font = null;
		int y = 0;

		g.setGrayScale(255);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setGrayScale(0);

		for (int size = 0; size < sizes.length; size++) {
			for (int face = 0; face < faces.length; face++) {
				int x = 0;
				for (int style = 0; style < styles.length; style++) {
					font = Font.getFont(faces[face], styles[style], sizes[size]);
					g.setFont(font);
					g.drawString("Test", x + 1, y + 1, Graphics.TOP | Graphics.LEFT);

					g.drawRect(x, y, font.stringWidth("Test") + 1, font.getHeight() + 1);
					x += font.stringWidth("Test") + 1;
				}

				y += font.getHeight() + 1;
			}
		}
	}
}
