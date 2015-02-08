package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImageView extends JPanel
{

	private BufferedImage currentImage;
	private int currentImageWidth;
	private int currentImageHeight;
	private boolean autoResize;
	private double zoomRatio;	
	
	public ImageView()
	{
	
		this.setBackground(Color.BLACK);
	}
	
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
		
		Dimension size = this.getSize();
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
					((currentImageWidth / 2) * zoomRatio)));
			yStart = (int) (Math.floor((height / 2) - 
					((currentImageHeight / 2) * zoomRatio)));
			
			g.drawImage(currentImage, xStart, yStart, xStart + (int)
					widthScaledByZoomRatio, yStart + (int)
					heightScaledByZoomRatio, 0, 0, currentImageWidth,
					currentImageHeight, null);
		}
	}
	
	public void setAutoResize(boolean turnOnAutoResize) {autoResize = turnOnAutoResize;}
	
	public void setCurrentImage(BufferedImage image)
	{
		
		currentImage = image;
		currentImageWidth = image.getWidth();
		currentImageHeight = image.getHeight();
		paintComponent(this.getGraphics());
	}
	
	public void setZoomRatio(double zoomRatio)
	{
		
		this.zoomRatio = zoomRatio;
		this.repaint();
	}
}