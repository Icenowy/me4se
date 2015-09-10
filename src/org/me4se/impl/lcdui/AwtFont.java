package org.me4se.impl.lcdui;

import java.awt.*;

/**
 * @author Stefan Haustein
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */

public class AwtFont extends PhysicalFont {

    public Font font;
	FontMetrics metrics;	
    String name = "WenQuanYi Zen Hei Sharp";
    
    boolean bold;
    boolean italic;
    boolean overprint;
 
    AwtFont (int height, boolean bold, boolean italic) {
        this.height = height*3/2;
        this.bold = bold;
        this.italic = italic;
        
        init ();
    }

	AwtFont (String desc) {

		if (desc != null) {

	        int cut0 = desc.indexOf('-');
    	    int cut1 = desc.indexOf('-', cut0 + 1);

			name = desc.substring (0, cut0);
	        String style = desc.substring(cut0 + 1, cut1).trim().toLowerCase();

    	    height = Integer.parseInt(desc.substring(cut1 + 1).trim());


        	bold = (style.indexOf("bold") != -1);
            italic = (style.indexOf("italic") != -1);
		}

        init ();
   }

    public void init () {
        
       int flags =
                (bold ? java.awt.Font.BOLD : 0)
                    + (italic ? java.awt.Font.ITALIC : 0);

       int points = height * 3;
 
//       do {
            font = new java.awt.Font(name, flags, points);
            metrics =
                Toolkit.getDefaultToolkit().getFontMetrics(font);
            
            
            points = (points * height) /  (metrics.getAscent() + metrics.getDescent());
            
            font = new java.awt.Font(name, flags, points);
            metrics =
                Toolkit.getDefaultToolkit().getFontMetrics(font);
/*       }
       while ((metrics.getAscent() + metrics.getDescent() > height)
                && (points > 0));*/

       ascent = metrics.getAscent();
       descent = metrics.getDescent();
       leading = height -ascent-descent;

     //  if ((flags & Font.BOLD) != 0)
     //      overprint = height < 12;   
    }

    public void drawString(Graphics g, String s, int x, int y) {
        g.setFont(font);
        g.drawString(s, x, y);
       /* g.drawLine(x, y, x+stringWidth(s), y);
        
        if (overprint)
            g.drawString(s, x + 1, y);*/
    }


	public int stringWidth (String s) {
		return metrics.stringWidth (s);	
	}

	public int charWidth (char c) {
		return metrics.charWidth (c);	
	}

	public int charsWidth (char []c, int start, int len) {
		return metrics.charsWidth (c, start, len);	
	}
}
