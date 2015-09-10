package javax.microedition.lcdui;


/**
 * @author Stefan Haustein
 *
 * @ME4SE INTERNAL
 */

import java.awt.image.BufferedImage;

import org.me4se.impl.Log;
import org.me4se.scm.*;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.ApplicationManager;


class ScmCanvas extends ScmComponent {

	Canvas canvas;
	boolean first = true;
	int keyCode;
	Object repaintLock = new Object();
	boolean repaintPending;
    
    
	ScmCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public void paint(java.awt.Graphics g) {

		//  g.drawLine (0, 50, 50, 0);
		//System.out.println ("paint called");
        
		try {
			synchronized (repaintLock) {

				ApplicationManager manager = ApplicationManager.getInstance();

				//java.awt.Dimension d = getSize();

				if (manager.offscreen == null) {
					manager.offscreen = new BufferedImage(
							manager.screenWidth,
							manager.screenHeight, BufferedImage.TYPE_INT_RGB);
				}

				if (repaintPending) {
					Graphics mg =
						new Graphics(
							canvas,
							manager.offscreen,
							manager.offscreen.getGraphics());
                    
					if (mg != null) {
						repaintPending = false;  // moved up here to allow the request of a repaint in paint
					
					   Log.log(Log.DRAW_EVENTS, "ScmCanvas.paint() entering");
                        
                        if(canvas.oldW != canvas.getWidth() || canvas.oldH != canvas.getHeight()){
                            try{
                                canvas.sizeChanged(canvas.getWidth(), canvas.getHeight());
                                canvas.oldW = canvas.getWidth();
                                canvas.oldH = canvas.getHeight();
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                      
                        canvas.paint(mg);  // thanks for the fix to Steven Lagerweij
                      
                        Log.log(Log.DRAW_EVENTS, "ScmCanvas.paint() left");

                        
//						mg.stale = true;
						if (canvas.videoFrameImage != null)
							mg.drawImage(canvas.videoFrameImage, 
									canvas.videoFrameX, canvas.videoFrameY,
									Graphics.TOP | Graphics.LEFT);
						
					}
					else repaint ();
				}

				//TODO: Clarify under which circumstances g may be null ... 
				
				if(g != null){
				g.drawImage(
					manager.offscreen,
					0,
					0,
					manager.awtContainer);
				}
				repaintLock.notify();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
        
		//System.out.println ("paint left");
		//g.drawLine (0, 0, 999, 999);
	}




	public boolean mouseDragged(int x, int y, int modifiers) {
		if(canvas.hasPointerEvents)
			canvas.pointerDragged(x, y);
		return true;
	}

	public boolean mousePressed(int button, int x, int y, int modifiers) {
		if (button != 1) return false;
	//	if(canvas.hasPointerEvents)
			canvas.pointerPressed(x, y);
		return true;
	}

	public boolean mouseReleased(int button, int x, int y, int modifiers) {
		if (button != 1) return false;
	//	if(canvas.hasPointerEvents)
			canvas.pointerReleased(x, y);
		return true;
	}

	public boolean keyPressed(String code) {
		//System.out.println ("key: "+code);
        
        Log.log(Log.INPUT_EVENTS, "ScmCanvas.keyPressed() entering; key: "+code);
        
        int kc = ApplicationManager.getInstance().getDeviceKeyCode(code);
        ApplicationManager.getInstance().keyStates |= getKeyFlag(kc);
        
		canvas.keyPressed(kc);        	

        Log.log(Log.INPUT_EVENTS, "ScmCanvas.keyPressed() leaving");

		return true;
	}

	public boolean keyRepeated(String code) {
		//System.out.println ("key: "+ev+" decoded: "+decode(ev));
        
        Log.log(Log.INPUT_EVENTS, "ScmCanvas.keyRepeated() entering; key: "+code);
        
		canvas.keyRepeated(ApplicationManager.getInstance().getDeviceKeyCode(code));        	

        Log.log(Log.INPUT_EVENTS, "ScmCanvas.keyRepeated() leaving");

		return true;
	}

	
    int getKeyFlag(int code){
        switch(canvas.getGameAction(code)){
        case Canvas.UP: return GameCanvas.UP_PRESSED;
        case Canvas.DOWN: return GameCanvas.DOWN_PRESSED;
        case Canvas.LEFT: return GameCanvas.LEFT_PRESSED;
        case Canvas.RIGHT: return GameCanvas.RIGHT_PRESSED;
        case Canvas.FIRE: return GameCanvas.FIRE_PRESSED;
        case Canvas.GAME_A: return GameCanvas.GAME_A_PRESSED;
        case Canvas.GAME_B: return GameCanvas.GAME_B_PRESSED;
        case Canvas.GAME_C: return GameCanvas.GAME_C_PRESSED;
        case Canvas.GAME_D: return GameCanvas.GAME_D_PRESSED;
        }
        return 0;
    }

	public boolean keyReleased(String code) {

		Log.log(Log.INPUT_EVENTS, "ScmCanvas.keyReleased() entering; key: "+code);

		
        int kc = ApplicationManager.getInstance().getDeviceKeyCode(code);
        ApplicationManager.getInstance().keyStates &= ~getKeyFlag(kc);

		canvas.keyReleased(kc); 

        Log.log(Log.INPUT_EVENTS, "ScmCanvas.keyReleased() leaving");

		return true;
	}

}


