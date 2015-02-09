package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Nicholas
 *
 */
/**
 * @author Nicholas
 *
 */
/**
 * @author Nicholas
 *
 */
/**
 * @author Nicholas
 *
 */
public class ImageView extends JPanel
{

	private BufferedImage currentImage;
	private int currentImageWidth;
	private int currentImageHeight;
	private int lastMouseX;
	private int lastMouseY;
	private int lastXOffset;
	private int lastYOffset;
	private int xOffset;
	private int yOffset;
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
		
		int xStart;
		int yStart;
		int resizedWidth;
		int resizedHeight;
		double widthRatio;
		double heightRatio;
		double widthScaledByZoomRatio;
		double heightScaledByZoomRatio;
		
		Dimension size = getSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		
		widthScaledByZoomRatio = (currentImageWidth * zoomRatio);
		heightScaledByZoomRatio = (currentImageHeight * zoomRatio);
		
		if (autoResize)
		{
			
			if ((widthScaledByZoomRatio <= width) && (heightScaledByZoomRatio <= height))
			{
	
				xStart = (int) (Math.floor((width / 2) - 
						((currentImageWidth / 2) * zoomRatio)));
				yStart = (int) (Math.floor((height / 2) - 
						((currentImageHeight / 2) * zoomRatio)));
				
				g.drawImage(currentImage, xStart, yStart,
						xStart + (int) widthScaledByZoomRatio,
						yStart + (int) heightScaledByZoomRatio, 0, 0,
						currentImageWidth, currentImageHeight, null);
			}
			else
			{
				
				widthRatio = (width / widthScaledByZoomRatio);
				heightRatio = (height / heightScaledByZoomRatio);
	
				if (widthRatio <= heightRatio)
				{
					
					resizedWidth = (int) (widthScaledByZoomRatio * widthRatio);
					resizedHeight = (int) (heightScaledByZoomRatio * widthRatio);
				}
				else
				{
	
					resizedWidth = (int) (widthScaledByZoomRatio * heightRatio);
					resizedHeight = (int) (heightScaledByZoomRatio * heightRatio);
				}
				
				xStart = (int) (Math.floor((width / 2) - (resizedWidth / 2)));
				yStart = (int) (Math.floor((height / 2) - (resizedHeight / 2)));
				
				g.drawImage(currentImage, xStart, yStart, xStart + resizedWidth,
						yStart + resizedHeight, 0, 0, currentImageWidth,
						currentImageHeight, null);
			}
		}
		else
		{
	
			xStart = (int) (Math.floor((width / 2) - 
					((currentImageWidth / 2) * zoomRatio)) + xOffset);
			yStart = (int) (Math.floor((height / 2) - 
					((currentImageHeight / 2) * zoomRatio)) + yOffset);
			
			g.drawImage(currentImage, xStart, yStart, xStart + (int)
					widthScaledByZoomRatio, yStart + (int)
					heightScaledByZoomRatio, 0, 0, currentImageWidth,
					currentImageHeight, null);
		}
	}
	
	
	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int xOffset) {
		
		this.xOffset = xOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	/**
	 * @return
	 */
	public boolean getIsDraggable() {return isDraggable;};
	
	public int getLastMouseX() {
		return lastMouseX;
	}

	public void setLastMouseX(int lastMouseX) {
		this.lastMouseX = lastMouseX;
	}

	public int getLastMouseY() {
		return lastMouseY;
	}

	public void setLastMouseY(int lastMouseY) {
		this.lastMouseY = lastMouseY;
	}

	public int getLastXOffset() {
		return lastXOffset;
	}

	public void setLastXOffset(int lastXOffset) {
		this.lastXOffset = lastXOffset;
	}

	public int getLastYOffset() {
		return lastYOffset;
	}

	public void setLastYOffset(int lastYOffset) {
		this.lastYOffset = lastYOffset;
	}

	/**
	 * 
	 */
	public void resetOffset()
	{
	
		lastXOffset = lastYOffset = xOffset = yOffset = 0;
	}
	
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
		paintComponent(this.getGraphics());
	}
	
	/**
	 * @param zoomRatio
	 */
	public void setZoomRatio(double zoomRatio)
	{
		
		this.zoomRatio = zoomRatio;
		this.repaint();
	}
}