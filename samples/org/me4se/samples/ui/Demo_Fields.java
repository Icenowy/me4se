package org.me4se.samples.ui;

import javax.microedition.lcdui.*;


public class Demo_Fields extends Form {

		
    public Demo_Fields() {
    	super("Text&Date Fields");
    	for(int i=0; i < Demo_TextBox.constr.length; i++){
    		append(new TextField(Demo_TextBox.labels[i], Demo_TextBox.samples[i], 255, Demo_TextBox.constr[i]));
    	}

		append(new DateField("Date", DateField.DATE));
		append(new DateField("Time", DateField.TIME));
		append(new DateField("DateTime", DateField.DATE_TIME));
    }

}
