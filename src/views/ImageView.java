package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Nicholas
 *
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
	 * 
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
	
	
	/**
	 * @return
	 */
	public int getCurrentImageWidth() {return currentImageWidth;}

	/**
	 * @param currentImageWidth
	 */
	public void setCurrentImageWidth(int currentImageWidth) {this.currentImageWidth = currentImageWidth;}

	/**
	 * @return
	 */
	public int getCurrentImageHeight() {return currentImageHeight;}

	/**
	 * @param currentImageHeight
	 */
	public void setCurrentImageHeight(int currentImageHeight) {this.currentImageHeight = currentImageHeight;}

	/**
	 * @return
	 */
	public int getXOffset() {return xOffset;}

	/**
	 * @param xOffset
	 */
	public void setXOffset(int xOffset) {this.xOffset = xOffset;}

	/**
	 * @return
	 */
	public int getYOffset() {return yOffset;}

	/**
	 * @param yOffset
	 */
	public void setYOffset(int yOffset) {this.yOffset = yOffset;}

	/**
	 * @return
	 */
	public boolean getIsDraggable() {return isDraggable;};
	
	/**
	 * @return
	 */
	public int getLastMouseX() {return lastMouseX;}

	/**
	 * @param lastMouseX
	 */
	public void setLastMouseX(int lastMouseX) {this.lastMouseX = lastMouseX;}

	/**
	 * @return
	 */
	public int getLastMouseY() {return lastMouseY;}

	/**
	 * @param lastMouseY
	 */
	public void setLastMouseY(int lastMouseY) {this.lastMouseY = lastMouseY;}

	/**
	 * @return
	 */
	public int getLastXOffset() {return lastXOffset;}

	/**
	 * @param lastXOffset
	 */
	public void setLastXOffset(int lastXOffset) {this.lastXOffset = lastXOffset;}

	/**
	 * @return
	 */
	public int getLastYOffset() {return lastYOffset;}

	/**
	 * @param lastYOffset
	 */
	public void setLastYOffset(int lastYOffset) {this.lastYOffset = lastYOffset;}

	/**
	 * 
	 */
	public void resetOffset()
	{
	
		lastXOffset = lastYOffset = xOffset = yOffset = 0;
	}
	
	/**
	 * @return
	 */
	public boolean getAutoResize() {return autoResize;}
	
	/**
	 * @param turnOnAutoResize
	 */
	public void setAutoResize(boolean turnOnAutoResize)
	{
	
		autoResize = turnOnAutoResize;
		isDraggable = ! turnOnAutoResize;
	}
	
	/**
	 * @param image
	 */
	public void setCurrentImage(BufferedImage image)
	{

		resetOffset();
		currentImage = image;
		currentImageWidth = image.getWidth();
		currentImageHeight = image.getHeight();
	}
	
	/**
	 * @return
	 */
	public int getXStart() {return xStart;}

	/**
	 * @param xStart
	 */
	public void setXStart(int xStart) {this.xStart = xStart;}

	/**
	 * @return
	 */
	public int getYStart() {return yStart;}

	/**
	 * @param yStart
	 */
	public void setYStart(int yStart) {this.yStart = yStart;}

	/**
	 * @return
	 */
	public int getXEnd() {return xEnd;}

	/**
	 * @param xEnd
	 */
	public void setXEnd(int xEnd) {this.xEnd = xEnd;}

	/**
	 * @return
	 */
	public int getYEnd() {return yEnd;}

	/**
	 * @param yEnd
	 */
	public void setYEnd(int yEnd) {this.yEnd = yEnd;}

	/**
	 * @return
	 */
	public double getZoomRatio() {return zoomRatio;}
	
	/**
	 * @param zoomRatio
	 */
	public void setZoomRatio(double zoomRatio) {this.zoomRatio = zoomRatio;}
}