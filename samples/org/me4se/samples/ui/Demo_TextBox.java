package org.me4se.samples.ui;

import javax.microedition.lcdui.*;


public class Demo_TextBox extends List implements CommandListener {

	static int[] constr = {TextField.ANY, TextField.EMAILADDR, TextField.PASSWORD, TextField.URL, TextField.PHONENUMBER};
	static String[] labels = {"Any", "Email", "Password", "Url", "Phone Number"};
	static String[] samples = {"Hello World", "test@hotmail.com", "secret", "http://me4se.org", "12345"};
		
    public Demo_TextBox() {
    	super("TextBox", List.IMPLICIT, labels, null);
    }

    public void commandAction(Command cmd, Displayable d) {
		if(d == this){
			int i = getSelectedIndex();
			TextBox tb = new TextBox("TextBox - "+labels[i], samples[i], 1024, constr[i]);
			tb.addCommand(UIDemo.BACK);
			tb.setCommandListener(this);
			UIDemo.display.setCurrent(tb);			
		}
		else
			UIDemo.display.setCurrent(this);
    }
}
