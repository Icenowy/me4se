package org.me4se.samples.ui;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.*;

/**
 * @author haustein
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UIDemo extends MIDlet implements CommandListener {

	String[] DEMOS = { "TextBox", "Gauge", "Fields", "Fonts", "Image", "Key", "Pointer", "Drawing", "Clip" };

	List menu = new List("ME4SE UI Demo", List.IMPLICIT, DEMOS, null);

	static Command BACK = new Command("Back", Command.BACK, 0);
	static Command QUIT = new Command("Quit", Command.EXIT, 0);

	static Display display;

	public UIDemo(){
		menu.addCommand(QUIT);
		menu.setCommandListener(this);
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);
		display.setCurrent(menu);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command cmd, Displayable d) {
		Displayable next;
		if (cmd == BACK)
			next = menu;
		else if(cmd == QUIT){
			notifyDestroyed();
			return;
		}
		else {
			if (d == menu) {

				try {
					next =
						(Displayable) Class
							.forName(
								"org.me4se.samples.ui.Demo_"
									+ menu.getString(menu.getSelectedIndex()))
							.newInstance();
				}
				catch (Exception e) {
					e.printStackTrace();
					next = new TextBox("Error", e.toString(), 1024, TextField.ANY);
				}
			}
			else if (d instanceof CommandListener) {
				((CommandListener) d).commandAction(cmd, d);
				return;
			}
			else {
				next = new TextBox("Unknown CMD", cmd.toString(), 1024, TextField.ANY);
			}

			next.addCommand(BACK);
			next.setCommandListener(this);
		}

		display.setCurrent(next);
	}
}
