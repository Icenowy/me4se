/*
 * Created on 19/12/2005
 *
 * Copyright (c) 2005, Funda��o Des. Paulo Feitoza. All rights reserved.
 * 
 */
package javax.microedition.m3g;
 
 
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import javax.microedition.midlet.ApplicationManager;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
//import javax.media.opengl.Version;

 

/**
 * [Description for M3gRender]
 *
 * @author Rodrigo Cal (rcal@fpf.br) 
 */
class M3gRender implements GLEventListener 
{
	
	static final int SURFACE_OPAQUE = 1;
	static final int SURFACE_TRANSPARENT = 2;
    
    private Vector listRender   = new Vector();
    
    private int viewPortX       = 0;
    private int viewPortY       = 0;
    private int viewPortWidth   = 255;
    private int viewPortHeight  = 255;
    
    private double near = 0;
    private double far  = 1;
    
    private Camera camera;
    private Transform transform;
    
    private RenderOffScreen renderOffScreen = new RenderOffScreen();
    private BufferedImage   image ;
    
    private Object          renderingTarget ;
         
    private int dataImage[];
  
    class RenderWorldModel
    {
        World           world;
    }
    
    class RenderNodeModel
    {
        Node            node;
        Transform       transform;
    }
    
    class RenderVertexModel
    {
        VertexBuffer    vertices;
        IndexBuffer     triangles;
        Appearance      appearance;
        Transform       transform;
        int             scope;        
    }
    
    class RenderClearModel
    {
        Background      background;
    }
    
    
    
    /**
     * 
     */
    public M3gRender() {  
    	ApplicationManager manager = ApplicationManager.getInstance();
    	int h = manager.screenHeight;
    	int w = manager.screenWidth; 	
    	//System.out.println("M3gRender(): using jogl Version: " + Version.getVersion());
    	//System.out.println("M3gRender(): setting viewport size to w="+ w + ", h=" + h);	
    	//setViewPort(0,0, 240, 310);
    	setViewPort(0,0, w, h);
        this.renderOffScreen.addGLEventListener(this);
      

    }
    /**
     * @param near
     * @param far
     */
    public void setDepthRange(float near, float far) {
        this.near = near;
        this.far  = far;
    }
    
    /**
     * @param renderingTarget
     */
    public void bindTarget(Object renderingTarget) {
        this.renderingTarget = renderingTarget;
    };
    
    /**
     * 
     */
    public void releaseTarget()
    { 
       
        this.renderOffScreen.callRender();
//        this.renderOffScreen.callRender();
        Thread.yield();
        
    
    }
    
    /**
     * @param camera
     * @param transform
     */
    public void setCamera(Camera camera, Transform transform) {
        this.camera = camera; 
        
        this.transform = new Transform (camera.projectionMatrix);
 
//        this.transform = RenderUtils.getTransform(new Transform(), camera);
  
        if (transform != null)
        {
            transform = new Transform(transform );
            transform.invert(); 
            this.transform.postMultiply(transform);
        }
    }
    
    public Camera getCamera()
    {
        return this.camera;
    }

    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void setViewPort(int x, int y, int width, int height) {
        this.viewPortX       = x;
        this.viewPortY       = y;
        this.viewPortWidth   = width;
        this.viewPortHeight  = height;
        this.renderOffScreen.setSize(width, height);
        this.dataImage = new int[width * height];
    }
    
    /**
     * 
     */
    public void comitImageInTarget()
    {
        if (this.renderingTarget instanceof Graphics)
        {
            Graphics g = (Graphics) this.renderingTarget;
            
            Raster raster = this.image.getRaster();
            int color[] = new int[4];
            for (int y = 0; y < this.image.getHeight(); y++)
                for (int x = 0; x < this.image.getWidth(); x++)
                {
                    int loc = ( (this.image.getWidth()) * y + x );
                    raster.getPixel(x,this.image.getHeight()-y-1, color);
                    int red     = color[0];  
                    int green   = color[1]; 
                    int blue    = color[2];      
                    int alpha = color[3];
                    
                    dataImage[loc] = 
                    	(alpha << 24) +
                        (red   << 16) +   
                        (green << 8) +
                        (blue);
                }
 
//            raster.getDataBuffer()
            Image img = null;
            img = Image.createRGBImage(
			    this.dataImage, 
			    this.image.getWidth(),
			    this.image.getHeight(),
			    true);
            
            g.drawImage(img,this.viewPortX,this.viewPortY,0);
            
//            BufferedImage imageTemp = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_INT_RGB);
//            Raster raster = this.image.getRaster();
//            DataBuffer dataBuffer = image.getRaster().getDataBuffer();
//            
//            int color[] = new int[4];
//            for (int y = 1; y < this.image.getHeight(); y++)
//                for (int x = 0; x < this.image.getWidth(); x++)
//                {
//                    image.getRaster().getPixel(x, this.image.getHeight()-y-1, color);
//                    imageTemp.getRaster().setPixel(x, y, color);
//                }
// 
//          
//            g._getAwtGraphics().drawImage(imageTemp,this.viewPortX,this.viewPortY,null);
        
        }
        else
            if (this.renderingTarget instanceof Image2D)
            {
                
                Image2D image2D = (Image2D) this.renderingTarget;
           
                BufferedImage imageRendered = new BufferedImage(image2D.width, image2D.height, BufferedImage.TYPE_3BYTE_BGR);
                imageRendered.getGraphics().drawImage(this.image,0,0,image2D.width, image2D.height,null);
                
                int heightTarget = image2D.height;
                int widthTarget  = image2D.width;
                
                int heightRendered = imageRendered.getHeight();
                int widthRendered  = imageRendered.getWidth();
                 
                Raster raster = imageRendered.getRaster();
                 
                int color[] = new int[4];
                for (int y = 0; y < heightTarget; y++)
                    for (int x = 0; x < widthTarget; x++)
                    {
                        int loc = ( widthRendered * y + x );
                        raster.getPixel(x,heightRendered-y-1, color);
                        int red     = color[0];  
                        int green   = color[1]; 
                        int blue    = color[2];      
                        int alfa    = color[3];
                        
                        int locImage2D = (widthTarget * y + x );
                        image2D.rgba[locImage2D] = 
                        	(alfa << 24) +
                            (red   << 16) +   
                            (green << 8) +
                            (blue);
                    }    
                image2D.buildDataJOGL();
 
            }
   
       // System.out.println("Comit"); //$NON-NLS-1$
        
        
    };

    /**
     * @param background
     */
    public void clear(Background background) {
        RenderClearModel renderClearModel = new RenderClearModel();
        renderClearModel.background = background;
        listRender.add(renderClearModel );
    }

    
    /**
     * @param vertices
     * @param triangles
     * @param appearance
     * @param transform
     * @param scope 
     */
    public void render(
        VertexBuffer vertices,
        IndexBuffer triangles,
        Appearance appearance,
        Transform transform,
        int scope) 
    {       
    	if (transform == null)
    		transform = new Transform ( );
    	
        RenderVertexModel renderVertexModel = new RenderVertexModel();
        renderVertexModel.vertices      = (VertexBuffer)    vertices.duplicate();   // TODO .duplicate()
        renderVertexModel.triangles     = (IndexBuffer)     triangles.duplicate();  // TODO .duplicate()
        renderVertexModel.appearance    = (Appearance)      appearance.duplicate(); // TODO .duplicate() 
        renderVertexModel.transform     = new Transform ( transform );
        renderVertexModel.scope         = scope;
        listRender.add(renderVertexModel );
    }
  
    /**
     * @param node
     * @param transform
     */
    public void render(Node node, Transform transform) {
        RenderNodeModel renderNodeModel = new RenderNodeModel();
        renderNodeModel.node            = (Node)node;//.duplicate();
        renderNodeModel.transform       = new Transform(transform);
        listRender.add(renderNodeModel);        
    }
    

    /**
     * @param world
     */
    public void render(World world) {
        
        RenderWorldModel renderWorldModel   = new RenderWorldModel();
        renderWorldModel.world              = (World)world;//.duplicate();
        listRender.add(renderWorldModel);        
    }
 
    
    public void renderLight(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glEnable(GL.GL_LIGHTING);
        
        Graphics3D g3d = Graphics3D.getInstance();
        int count = g3d.getLightCount();
        Light light = g3d.getLight(0);
        if ( light == null  )
        {
            gl.glDisable(GL.GL_LIGHT0);
            gl.glDisable(GL.GL_LIGHT1);
            gl.glDisable(GL.GL_LIGHT2);
            gl.glDisable(GL.GL_LIGHT3);
            gl.glDisable(GL.GL_LIGHT4);
            gl.glDisable(GL.GL_LIGHT5);
            gl.glDisable(GL.GL_LIGHT6);
            gl.glDisable(GL.GL_LIGHT7);             
            gl.glDisable(GL.GL_LIGHTING);
            return;
        }
        
        light.getIntensity();
        float color[] = RenderVertices.getColorArray(light.getColor() );
//        color[0] = light.getIntensity();
//        color[1] = light.getIntensity();
//        color[2] = light.getIntensity();
//        
//        color[0] = 0;
//        color[1] = 0;
//        color[2] = 0;

 
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, color, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, color, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, color, 0);


		
        Transform lightTransformArray = g3d.getLightTransformArray(0);
        Transform transform = new Transform ();
        if (lightTransformArray != null)
        {
            transform = new Transform (lightTransformArray);
        }
        if(this.transform != null){
        	transform.postMultiply(this.transform);
        }
        
        float position[] = 
        {
            transform.matrix[3] , 
            transform.matrix[7] , 
            transform.matrix[11],
            0
        };      
		
//        light.setMode( light.AMBIENT );
        switch(light.getMode())
        {
        	case Light.AMBIENT:
        	{
        		float settingColor[] = new float[]{1,1,1,1};
         		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, settingColor, 0);
         		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, settingColor, 0);
         	
        	}
        
        	case Light.DIRECTIONAL:
        	{
        		position[3] = 0; // directional
        		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
        		break;
        	}
        
        	case Light.OMNI:
        	{
        		position[3] = 1; // positional
        		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
  
           		gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, light.attenuationConstant);
        		gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, light.getLinearAttenuation());//linearAttenuation
        		gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, light.getQuadraticAttenuation());//quadraticAttenuation

        		break;
        	}
        	  
        	
        	case Light.SPOT:
        	{
        		
        		gl.glLightf(GL.GL_LIGHT0, GL.GL_CONSTANT_ATTENUATION, light.attenuationConstant);
        		gl.glLightf(GL.GL_LIGHT0, GL.GL_LINEAR_ATTENUATION, light.getLinearAttenuation());//linearAttenuation
        		gl.glLightf(GL.GL_LIGHT0, GL.GL_QUADRATIC_ATTENUATION, light.getQuadraticAttenuation());//quadraticAttenuation

        		
//              //      spot direction
//              float xSpotDir = 0 , ySpotDir = 0, zOffset = 0;
//              float direction[] = {xSpotDir, ySpotDir, zOffset};
//              gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, direction);
        		
                gl.glLightf(GL.GL_LIGHT0, GL.GL_SPOT_EXPONENT, light.getSpotExponent());
                gl.glLightf(GL.GL_LIGHT0, GL.GL_SPOT_CUTOFF, light.getSpotAngle());  // value between 0 to 180
        		break;
        	}        	 
        }
 
        
//      gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, new float[]{1,1,1,1});   
 
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_LIGHTING);
    }

       

    
    //// Render GL ////
    
    private synchronized void render(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        
   //     gl.glEnable(GL.GL_BLEND);
        
        gl.glViewport(0, 0, this.viewPortWidth, this.viewPortHeight);
        gl.glMatrixMode(GL.GL_PROJECTION); 
  
        gl.glOrtho(
            -this.viewPortWidth,  // left
             this.viewPortWidth,  // right
            -this.viewPortHeight, // bottom
             this.viewPortHeight, // top
             this.near,           // near_val
             this.far);           // far_val

        
//        gl.glDepthRange(this.near,this.far);
        
        
        if (this.listRender.size() > 0)
        {
            RenderClear.renderClear (drawable, new Background());
            renderLight(drawable);
        }
        
        for (int i = 0; i < this.listRender.size(); i++ )
        {
            Object objectRender = this.listRender.get(i);
          
            if (objectRender instanceof RenderWorldModel)           
            {
                // getting transform from world 
                World world = ((RenderWorldModel)objectRender).world;
                Transform transformWorld = new Transform();
                world.getTransform(transformWorld);
                
                // setting camera
                Camera camera = world.getActiveCamera();
                Transform transformCamera = RenderWorld.getTransformCamera(world); 
                Graphics3D.getInstance().setCamera(camera, transformCamera);
                
                // setting lights
                Graphics3D.getInstance().resetLights();                
                RenderWorld.addLights(drawable.getGL(), world, transformWorld ); // TODO add Graphics3D.getInstance().addLight() 
                renderLight(drawable);
                
                RenderClear.renderClear (drawable, world.getBackground());
                
                RenderNode.render(drawable, world, transformWorld , this.transform, SURFACE_OPAQUE);
                RenderNode.render(drawable, world, transformWorld , this.transform, SURFACE_TRANSPARENT);
            
                  
            }
            else if (objectRender instanceof RenderNodeModel)
            {
                valiteCamera();
                RenderNodeModel renderNodeModel = (RenderNodeModel)objectRender;
                RenderNode.render(
                    drawable,
                    renderNodeModel.node, 
                    renderNodeModel.transform, this.transform, SURFACE_OPAQUE|SURFACE_TRANSPARENT);
            }
            else if (objectRender instanceof RenderVertexModel)
            {
                valiteCamera();
                RenderVertexModel renderVertexModel = (RenderVertexModel)objectRender;
                RenderVertices.render(
                    drawable,
                    renderVertexModel.vertices, 
                    renderVertexModel.triangles,
                    renderVertexModel.appearance, 
                    renderVertexModel.transform,
                    renderVertexModel.scope,
                    this.transform
                    
                    );
            }
            else if (objectRender instanceof RenderClearModel)
            {
                RenderClearModel renderClearModel = (RenderClearModel)objectRender;
                RenderClear.renderClear(drawable, renderClearModel.background);
            }
         }  
         listRender.clear();
         if (!this.renderOffScreen.RENDER_IN_FRAME)
         {
	         this.image = this.renderOffScreen.getImage();
	         comitImageInTarget();
         }
         gl.glFlush();
         
        
     }

    private void valiteCamera()
    {
        if (this.camera == null)
        {
          throw new RuntimeException("no camera"); //$NON-NLS-1$
        }
    }
    
    
    /**
     * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLDrawable)
     */
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, this.viewPortWidth, this.viewPortHeight);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-this.viewPortWidth, this.viewPortWidth, -this.viewPortHeight, this.viewPortHeight, -1, 1);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //erasing color
        gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }


    /**
     * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLDrawable)
     */
    public void display(GLAutoDrawable drawable) {
        try
        {
            render(drawable);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @see javax.media.opengl.GLEventListener#reshape(javax.media.opengl.GLDrawable, int, int, int, int)
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int sizeWidth, int sizeHeight) {
        render(drawable);
    }

    /**
     * @see javax.media.opengl.GLEventListener#displayChanged(javax.media.opengl.GLDrawable, boolean, boolean)
     */
    public void displayChanged(GLAutoDrawable drawable, boolean sizeWidth, boolean sizeHeight) {
        render(drawable);
    }
    
    

    
}
