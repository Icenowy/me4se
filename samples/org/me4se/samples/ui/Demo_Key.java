
package org.me4se.samples.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;


public class Demo_Key extends Canvas {

	String eventType = "- Press any!";
	int keyCode;

	public void keyPressed(int keyCode) {
		eventType = "pressed";
		this.keyCode = keyCode;
		repaint();
	}

	public void keyReleased(int keyCode) {
		eventType = "released";
		this.keyCode = keyCode;
		repaint();
	}

	public void keyRepeated(int keyCode) {
		eventType = "repeated";
		this.keyCode = keyCode;
		repaint();
	}

	public int write(Graphics g, int y, String s) {
		g.drawString(s, 0, y, Graphics.LEFT | Graphics.TOP);
		return y + g.getFont().getHeight();
	}

	public void paint(Graphics g) {
		g.setGrayScale(255);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setGrayScale(0);

		int y = 0;
		y = write(g, y, "Key " + eventType);
		if (keyCode == 0)
			return;

		y =
			write(
				g,
				y,
				"Char/Code: " + ((keyCode < 0) ? "N/A" : "" + (char) keyCode) + "/" + keyCode);
		y = write(g, y, "Name: " + getKeyName(keyCode));
		String gameAction;
		switch (getGameAction(keyCode)) {
			case LEFT :
				gameAction = "LEFT";
				break;
			case RIGHT :
				gameAction = "RIGHT";
				break;
			case UP :
				gameAction = "UP";
				break;
			case DOWN :
				gameAction = "DOWN";
				break;
			case FIRE :
				gameAction = "FIRE";
				break;
			case GAME_A :
				gameAction = "GAME_A";
				break;
			case GAME_B :
				gameAction = "GAME_B";
				break;
			case GAME_C :
				gameAction = "GAME_C";
				break;
			case GAME_D :
				gameAction = "GAME_D";
				break;
			default :
				gameAction = "N/A";
		}
		write(g, y, "Action: " + gameAction);
	}


}
