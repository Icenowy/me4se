/*
 * Created on 19/12/2005
 *
 * Copyright (c) 2005, Funda��o Des. Paulo Feitoza. All rights reserved.
 * 
 */
package javax.microedition.m3g;

import java.awt.Frame;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.util.Vector;

import javax.swing.JFrame;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.BufferUtil;

/**
 * [Description for RenderOffScreen]
 */
class RenderOffScreen 
{
	
	public static final boolean RENDER_IN_FRAME = false;
	
    private Vector listeners = new Vector();
    
    private GL gl;

    private final GLU glut;
    
    private GLPbuffer glPBuffer;
    private GLAutoDrawable glDrawable;
    
    private BufferedImage bufferedImage = null;
    
    private DataBufferByte dbByte = null;
    
    private int maxPbufferHeight = 512;

    private int maxPbufferWidth = 512;

  //  private GLCanvas canvas = null;
    
    private Window tempWindow = null;
    /**
     * @param w 
     * @param h 
     */
    public RenderOffScreen(int w, int h)
    {
        this.maxPbufferHeight = w;
        this.maxPbufferHeight = h;
        initGLDrawable();
        this.gl   = this.glDrawable.getGL();
        this.glut = new GLU(); // JSRCHG this.glDrawable.getGLU();
    }
    
    public GLDrawable getDrawable()
    {
        return this.glDrawable;
    }
    
    
    /**
     * 
     */
    public RenderOffScreen()
    {
        initGLDrawable();
        this.gl   = this.glDrawable.getGL();
        this.glut = new GLU(); // JSRCHG this.glDrawable.getGLU();
    }
    
    /**
     * @param listener
     */
    public void addGLEventListener(GLEventListener listener)
    {
        this.listeners.add(listener);
        this.glDrawable.addGLEventListener( listener );
    }
    
    /**
     * @return GL
     */
    public GL getGl()
    {
        return this.gl;
    }

    /**
     * @return GLU
     */
    public GLU getGlut()
    {
        return this.glut;
    }

    private void initGLDrawable()
    {
        // Create a temporary window to create a GLCanvas
        // that is never displayed on screen.
    /*		if(RENDER_IN_FRAME){
    			tempWindow = new Frame();
    			tempWindow.setSize(320, 240);
    		}
    		else{
    			this.tempWindow = new Window( new JFrame() );
    			this.tempWindow.setLayout(null);
    			this.tempWindow.setSize( 0, 0 );
    		}
        this.canvas = new GLCanvas(); // JSRCHG GLDrawableFactory.getFactory().createGLCanvas( new GLCapabilities() );
        this.tempWindow.add( this.canvas );
        this.tempWindow.setVisible( true );
        */
        
        setSize(this.maxPbufferWidth, this.maxPbufferHeight);
        
        
        
    }
    
    /**
     * @param w
     * @param h
     */
    public void setSize(int w, int h)
    {
        if (this.glPBuffer != null)
        {
            this.glPBuffer.destroy();
        }
            
        w = w & Integer.parseInt("11111111100",2);
        h = h & Integer.parseInt("11111111100",2);
        
        this.maxPbufferWidth  = w;
        this.maxPbufferHeight = h;
        
        GLCapabilities pbCaps = new GLCapabilities();
        pbCaps.setAlphaBits( 8 );
        pbCaps.setDoubleBuffered( false );
        
        this.glPBuffer = GLDrawableFactory.getFactory().createGLPbuffer(pbCaps, null, this.maxPbufferWidth, 
                this.maxPbufferHeight, null );
        
        this.glDrawable = this.glPBuffer; 
        	
        /*	
        	this.glPBuffer = this.canvas.createOffscreenDrawable( 
                pbCaps,
                this.maxPbufferWidth, 
                this.maxPbufferHeight );
             
        }
        // this.glDrawable.display(); 
         
         */
        
        this.bufferedImage = new BufferedImage(
                this.maxPbufferWidth, 
                this.maxPbufferHeight,  
                BufferedImage.TYPE_4BYTE_ABGR);
        
        this.dbByte = (DataBufferByte) this.bufferedImage.getRaster().getDataBuffer();
        
        for (int i = 0 ; i < this.listeners.size(); i++)
        {
            GLEventListener listener = (GLEventListener) this.listeners.get(i);
            this.glDrawable.addGLEventListener( listener );
        }
        
    }
    
    /**
     * @return bufferedImage
     */
    public BufferedImage getImage()
    {
        int[] view = new int[4];
        this.gl.glGetIntegerv(GL.GL_VIEWPORT, view, 0);
        int originalW = view[2], originalH = view[3];

        this.gl.glReadBuffer( GL.GL_FRONT);
                
//        // set viewport to full buffer
//        this.gl.glViewport(
//                0,
//                0,
//                this.maxPbufferWidth,
//                this.maxPbufferHeight);
        
        // read full buffer 
        
        ByteBuffer buf = BufferUtil.newByteBuffer(this.dbByte.getData().length);
        
        try{
        	this.gl.glReadPixels( 
                0, 
                0,
                this.maxPbufferWidth,
                this.maxPbufferHeight, 
                GL.GL_ABGR_EXT,
                GL.GL_UNSIGNED_BYTE, 
                buf);
        
        	buf.get(this.dbByte.getData());
        }
        catch(GLException e){
        	e.printStackTrace();
        }
//        // set original viewport
//        this.gl.glViewport(
//                0,
//                0,
//                originalW, 
//                originalH );
        
        // return viewport
        return bufferedImage;
//        return this.bufferedImage.getSubimage(
//                0,
//                0,
//                originalW, 
//                originalH);
    }

    /**
     * 
     */
    void callRender()
    {
        this.glDrawable.display();
    }

}