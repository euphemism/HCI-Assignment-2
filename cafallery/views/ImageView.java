package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Nicholas
 *
 * Class for the JPanel that houses the displayed image.
 */
public class ImageView extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2963675488872291438L;
	
	private BufferedImage currentImage;
	private int currentImageWidth;
	private int currentImageHeight;
	private int lastMouseX;
	private int lastMouseY;
	private int lastXOffset;
	private int lastYOffset;
	private int xOffset;
	private int yOffset;
	private int xStart;
	private int yStart;
	private int xEnd;
	private int yEnd;
	private boolean autoResize;
	private boolean isDraggable;
	private double zoomRatio;	
	
	/**
	 * Constructor for the ImageView class.
	 */
	public ImageView()
	{
		
		isDraggable = false;
		resetOffset();
		setBackground(Color.BLACK);
		setOpaque(true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g)
	{
	
		super.paintComponent(g);	
		
		g.drawImage(currentImage, xStart, yStart, xEnd, yEnd, 0, 0,
				currentImageWidth, currentImageHeight, null);
	}

	public int getCurrentImageWidth() {return currentImageWidth;}

	public void setCurrentImageWidth(int currentImageWidth) {this.currentImageWidth = currentImageWidth;}

	public int getCurrentImageHeight() {return currentImageHeight;}

	public void setCurrentImageHeight(int currentImageHeight) {this.currentImageHeight = currentImageHeight;}

	public int getXOffset() {return xOffset;}

	public void setXOffset(int xOffset) {this.xOffset = xOffset;}

	public int getYOffset() {return yOffset;}

	public void setYOffset(int yOffset) {this.yOffset = yOffset;}

	public boolean getIsDraggable() {return isDraggable;};

	public int getLastMouseX() {return lastMouseX;}

	public void setLastMouseX(int lastMouseX) {this.lastMouseX = lastMouseX;}

	public int getLastMouseY() {return lastMouseY;}

	public void setLastMouseY(int lastMouseY) {this.lastMouseY = lastMouseY;}

	public int getLastXOffset() {return lastXOffset;}

	public void setLastXOffset(int lastXOffset) {this.lastXOffset = lastXOffset;}

	public int getLastYOffset() {return lastYOffset;}

	public void setLastYOffset(int lastYOffset) {this.lastYOffset = lastYOffset;}

	public void resetOffset()
	{
	
		lastXOffset = lastYOffset = xOffset = yOffset = 0;
	}

	public boolean getAutoResize() {return autoResize;}

	public void setAutoResize(boolean turnOnAutoResize)
	{
	
		autoResize = turnOnAutoResize;
		isDraggable = ! turnOnAutoResize;
	}

	public void setCurrentImage(BufferedImage image)
	{

		resetOffset();
		currentImage = image;
		currentImageWidth = image.getWidth();
		currentImageHeight = image.getHeight();
	}

	public int getXStart() {return xStart;}

	public void setXStart(int xStart) {this.xStart = xStart;}

	public int getYStart() {return yStart;}

	public void setYStart(int yStart) {this.yStart = yStart;}

	public int getXEnd() {return xEnd;}

	public void setXEnd(int xEnd) {this.xEnd = xEnd;}

	public int getYEnd() {return yEnd;}

	public void setYEnd(int yEnd) {this.yEnd = yEnd;}

	public double getZoomRatio() {return zoomRatio;}

	public void setZoomRatio(double zoomRatio) {this.zoomRatio = zoomRatio;}
}