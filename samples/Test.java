/*
 * Created on Jun 26, 2003
 *
 */
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * @author haustein
 *
 * Simple test MIDlet, consisting of only one class 
 */
public class Test extends MIDlet implements CommandListener {

    Form form = new Form("ME4SE");

    public Test(){
		form.addCommand(new Command("Quit", Command.EXIT, 0));
        form.addCommand(new Command("Foo", Command.SCREEN, 0));
        form.addCommand(new Command("Bar", Command.SCREEN, 0));
        form.addCommand(new Command("Del(0)", Command.SCREEN, 0));
        
        form.setTicker(new Ticker("This is only a ME4SE test MIDlet. Nothing to see here, please move along..."));
        form.setCommandListener(this);
    }

    protected void destroyApp(boolean unconditional)
        throws MIDletStateChangeException {

    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        Display display = Display.getDisplay(this);

        display.setCurrent(form);
     }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command cmd, Displayable d) {
		if(cmd.getLabel().equals("Quit"))
			notifyDestroyed();
        else if(cmd.getLabel().equals("Foo"))
            form.append("Foo");
        else if(cmd.getLabel().equals("Bar"))
            form.append("Bar");
        else if(cmd.getLabel().equals("Del(0)"))
            form.delete(0);
    }

}
