/*
 * Created on Nov 12, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.me4se.samples.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;

/**
 * @author haustein
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Demo_Gauge extends Form implements CommandListener{

	
	Gauge interactive = new Gauge("Interactive", true, 100, 50);
	Gauge passive = new Gauge("Non Interactive", false, 100, 0);
	

    public Demo_Gauge() {
        super("Gauge");
		append(interactive);
		append(passive);
		
		addCommand(new Command("Commit", Command.SCREEN, 0));
    }


     
    public void commandAction(Command cmd, Displayable d) {
    	passive.setValue(interactive.getValue());
    }
    

}
