package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

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
	
	public void changeCurrentIndex(boolean increaseIndex)
	{

		if (files.size() > 0)
		{
			if (increaseIndex == false)
			{
	
				if (0 < currentImageIndex)	
					currentImageIndex--;
				else
					currentImageIndex = (files.size() - 1);
			}
			else
			{
			
				if (currentImageIndex < (files.size() - 1))
					currentImageIndex++;
				else
					currentImageIndex = 0;
			}	
		
			updateLoadedImage();
		}
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
		this.imageView.setAutoResize(true);
		this.imageView.setZoomRatio(1);
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
	
	/**
	 * @param fileName
	 * @return
	 */
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
	
	public void updateLoadedImage()
	{
		
		if (files.size() == 0)
			return;
		
		imageView.setCurrentImage(loadImageFromFile(files.get(currentImageIndex)));
		
		applicationView.getFilePathLabel().setText(files
				.get(currentImageIndex).getPath());
		
		applicationView.getFileIndexLabel().setText(
				String.valueOf(currentImageIndex + 1) + "/" +
						String.valueOf(files.size()));

		applicationView.revalidate();
		
		applicationView.setMinimumSize(
				new Dimension(applicationView.getBottomMenuBar()
				.getPreferredSize().width, applicationView
				.getMinimumSize().height));
	}

	/**
	 * 
	 */
	public void start()
	{
	
		files = new ArrayList<File>();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addKeyEventDispatcher(new KeyEventDispatcher()
		{
		    
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) 
			{
				  				
				if (e.getID() == KeyEvent.KEY_RELEASED)
					switch (e.getKeyCode())
					{
					
						case KeyEvent.VK_LEFT:
						{
							
							changeCurrentIndex(false);
							return true;
						}
								
						case KeyEvent.VK_RIGHT:
						{
										
							changeCurrentIndex(true);
							return true;
						}
								
						default:
							return true;
					}	
				else
					return true;
			}
		});
		
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
						files.add(file);
					
					currentImageIndex = 1;
					changeCurrentIndex(false);
				}
			}			
		});
		
		/*Implementing listener for the exit file menu item.*/
		applicationView.getFileMenuExit().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				
				applicationView.dispose();
				System.exit(0);
			}
		});

		/*Implementing listener for the auto-resize check box view menu item.*/
		applicationView.getViewMenuPreviousImage().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {changeCurrentIndex(false);}	
		});
		
		/*Implementing listener for the auto-resize check box view menu item.*/
		applicationView.getViewMenuNextImage().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {changeCurrentIndex(true);}
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
		
		/*Implementing listener for clicking on the image view panel.*/
		imageView.addMouseListener(new MouseAdapter()
		{
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			
				switch(arg0.getButton())
				{
				
					case MouseEvent.BUTTON1:
					{
						
						changeCurrentIndex(true);
						break;
					}
					
					case MouseEvent.BUTTON3:
					{
						
						changeCurrentIndex(false);
						break;
					}
				}
			}
		});
		
		/*Implementing listener for the previous image button on the bottom tool bar.*/
		applicationView.getPreviousButton().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {changeCurrentIndex(false);}
		});

		/*Implementing listener for the next image button on the bottom tool bar.*/
		applicationView.getNextButton().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {changeCurrentIndex(true);}
		});
		
		
		/*Implementing listeners for zoom slider on the bottom tool bar.*/
		applicationView.getZoomSlider().addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				JSlider source = (JSlider) arg0.getSource();
				
				applicationView.getZoomTextField().setText(source.getValue() + "%");
				imageView.setZoomRatio((double) source.getValue() / 100);
			}	
		});
		
		applicationView.getZoomSlider().addMouseMotionListener(new MouseMotionListener()
		{

			@Override
			public void mouseDragged(MouseEvent arg0) {


			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
		}});
		
		applicationView.setVisible(true);
	}
}