package views;

import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ImageView extends JPanel
{

	private BufferedImage currentImage;
	private int currentImageWidth;
	private int currentImageHeight;
	private int resizedWidth;
	private int resizedHeight;
	private int xStart;
	private int yStart;
	
	public ImageView()
	{
		
	}
	
	@Override
    public void paintComponent(Graphics g)
    {
    
		super.paintComponent(g);
		
		Dimension size = this.getSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		xStart = (int) Math.floor((width / 2) - (currentImageWidth / 2));
		yStart = (int) Math.floor((height / 2) - (currentImageHeight / 2));
		
		if ((currentImageWidth < width) && (currentImageHeight < height))
		{
					
			g.drawImage(currentImage, xStart, yStart, null);
		}
		else
		{
			
			g.drawImage(currentImage, 0, 0, width, height, 0, 0, currentImageWidth, currentImageHeight, null);
/*
		
			if (currentImageWidth > width)
			{
				
				if (currentImageHeight > height)
				{
					
					g.drawImage(currentImage, 0, 0, width, height, 0, 0, currentImageWidth, currentImageHeight, null);
				}
				else
				{
					
					g.drawImage(currentImage, 0, 0, width, height, 0, 0, currentImageWidth, currentImageHeight, null);

				}
			}
			else
			{
			
				if (currentImageHeight > height)
				{
				
				}
				else
				{
					
				}
			}	*/
		}
    }
	
	public void setCurrentImage(BufferedImage image)
	{
		
		currentImage = image;
		currentImageWidth = image.getWidth();
		currentImageHeight = image.getHeight();
		paintComponent(this.getGraphics());
	}
}