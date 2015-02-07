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
	private double widthRatio;
	private double heightRatio;
	private boolean autoResize;
	
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
		
		Dimension size = this.getSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		
		if (autoResize)
		{
			
			if ((currentImageWidth <= width) && (currentImageHeight <= height))
			{

				xStart = (int) (Math.floor((width / 2) - (currentImageWidth / 2)));
				yStart = (int) (Math.floor((height / 2) - (currentImageHeight / 2)));
				
				g.drawImage(currentImage, xStart, yStart, null);
			}
			else
			{
				
				widthRatio = ((double) width / currentImageWidth);
				heightRatio = ((double) height / currentImageHeight);

				if (widthRatio <= heightRatio)
				{
					
					resizedWidth = (int) (currentImageWidth * widthRatio);
					resizedHeight = (int) (currentImageHeight * widthRatio);
				}
				else
				{

					resizedWidth = (int) (currentImageWidth * heightRatio);
					resizedHeight = (int) (currentImageHeight * heightRatio);
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
			
			xStart = (int) (Math.floor((width / 2) - (currentImageWidth / 2)));
			yStart = (int) (Math.floor((height / 2) - (currentImageHeight / 2)));
			
			g.drawImage(currentImage, xStart, yStart, null);
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
}