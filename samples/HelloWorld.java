/*
 * Created on Jun 26, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * @author haustein
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HelloWorld extends MIDlet  {


    protected void destroyApp(boolean unconditional)
        throws MIDletStateChangeException {

    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        Display display = Display.getDisplay(this);
        
        display.setCurrent(new Form("Hello World!"));
    }


}
