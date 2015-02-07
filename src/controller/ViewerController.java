package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import views.ImageView;
import views.ViewerView;

/**
 * @author Nicholas
 *
 */
public class ViewerController {

	private ViewerView applicationView;
	private ImageView imageView;
	private List<File> files;
	private int currentImageIndex;
	
	/**
	 * 
	 */
	public ViewerController()
	{
		
	
	}
	
	/**
	 * @param applicationView
	 */
	public void setApplicationView(ViewerView applicationView)
	{
		
		this.applicationView = applicationView;
	}
	
	/**
	 * @param imageView
	 */
	public void setImageView(ImageView imageView)
	{
		
		this.imageView = imageView;
	}
	
	/**
	 * @param acceptFilesAndDirectories
	 * @param f
	 * @return
	 */
	public boolean fileAccept(int acceptFilesAndDirectories, File f) {
		
		if (f.isDirectory())
			if (acceptFilesAndDirectories == JFileChooser.FILES_AND_DIRECTORIES)
				return true;
			
		Matcher matcher = Pattern.compile(".*\\.(?<extension>.*)").matcher(f.getName());
		
		if (! matcher.matches())
			return false;
		
		switch(matcher.group("extension").toLowerCase())
		{
		
			case "bmp": case "gif": case "jpeg": case "jpg": case "png":
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * @return
	 */
	public JFileChooser spawnFileChooser()
	{
	
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter()
		{

			@Override
			public boolean accept(File f)
			{
				
				return fileAccept(JFileChooser.FILES_AND_DIRECTORIES, f);
			}
			
			@Override
			public String getDescription() {return null;}		
		});
		
		return fileChooser;
	}
	
	public BufferedImage loadImageFromFile(File fileName)
	{
		
		try
		{
			
			return ImageIO.read(fileName);
			
		}
		catch(IOException e)
		{
			
			return null;
		}
	}
	
	public int calculateMinimumWidth()
	{
		
		
		return (applicationView.getFilePathLabel()
				.getSize().width + applicationView
				.getZoomSlider().getWidth() + 200); //Invisible 100px horizontal strut * 2 added into new width.
	}
	
	/**
	 * 
	 */
	public void start()
	{
	
		files = new ArrayList<File>();
		
		/*Implementing listener for the open menu item.*/
		applicationView.getFileMenuOpen().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = spawnFileChooser();

				int returnValue = fileChooser.showOpenDialog(applicationView.getApplicationFrame());
				
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					
					files.clear();
					File file = fileChooser.getSelectedFile();
					
					if (file.isDirectory())
					{
						
						Collections.addAll(files, file.listFiles(new FileFilter(){

							@Override
							public boolean accept(File f)
							{
								
								return fileAccept(JFileChooser.FILES_ONLY, f);
							}
						}));						
					}
					else
					{
						
						files.add(file);
					}
					
					currentImageIndex = 0;
					imageView.setCurrentImage(loadImageFromFile(files.get(currentImageIndex)));
				
					applicationView.getFilePathLabel().setText(files
							.get(currentImageIndex).getPath());

					applicationView.getApplicationFrame().revalidate();
					
					applicationView.getApplicationFrame().setMinimumSize(
							new Dimension(calculateMinimumWidth(),
									applicationView.getApplicationFrame()
									.getMinimumSize().height)); 					
				}
			}			
		});
		
		/*Implementing listener for the exit file menu item.*/
		applicationView.getFileMenuExit().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				
				applicationView.getApplicationFrame().dispose();
				System.exit(0);
			}
		});
		
		/*Implementing listener for the auto-resize check box view menu item.*/
		applicationView.getViewMenuAutoResizeCheckBox().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				imageView.setAutoResize(applicationView.getViewMenuAutoResizeCheckBox()
						.isSelected() ? true : false);
				imageView.paintComponent(imageView.getGraphics());
			}	
		});
		
		/*Implementing listener for the invert viewer background color view menu item.*/
		applicationView.getViewMenuInvertBackground().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				if (imageView.getBackground() == Color.BLACK)
					imageView.setBackground(Color.WHITE);
				else
					imageView.setBackground(Color.BLACK);
			}	
		});
		
		applicationView.getApplicationFrame().setVisible(true);
	}
}