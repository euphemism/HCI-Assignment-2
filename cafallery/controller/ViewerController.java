package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import views.ImageView;
import views.ViewerView;

import com.leapmotion.leap.*;

/**
 * @author Nicholas
 *
 * Program controller, handles all logic and manipulation of UI elements.
 */
public class ViewerController
{

	private ViewerView applicationView;
	private ImageView imageView;
	private Controller leapController;
	private List<File> files;
	private int currentImageIndex;
	
	final private int MAXIMUM_WAIT_FOR_CHANGE = 800; //In milliseconds.
	final private int MINIMUM_WAIT_FOR_CHANGE = 100; //In milliseconds.
	private int timeToWaitForChange;
	private long lastImageChange;
	private long imageChangeStart;
	
	private long slideshowDelay;
	private boolean slideshowDelayIsCounting;
	private JFrame slideshowFrame;
	private Timer slideshowTimer;
	private TimerTask slideshowTimerTask;
	
	LeapListener listener;
	
	/**
	 * @author Nicholas
	 *
	 * Event-driven listener for the Leap Motion controller.  Handles
	 * connecting/disconnecting and gestures.
	 */
	private class LeapListener extends Listener
	{	
		
		/**
		 * Constructor for the LeapListener class.
		 */
		public LeapListener() {}
		
		/* (non-Javadoc)
		 * @see com.leapmotion.leap.Listener#onConnect(com.leapmotion.leap.Controller)
		 */
		@Override
		public void onConnect(Controller controller)
		{

			controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
			applicationView.getHelpMenuLeapConnection().setText("Leap Motion connected");

			try 
			{
				
				applicationView.getHelpMenuLeapConnection().setIcon(
						new ImageIcon(ImageIO.read(new File("cafallery/resources/connected.png"))));
			} 
			catch (IOException e) {e.printStackTrace();}
			
			applicationView.getHelpMenuLeapConnection().setSize(applicationView.getHelpMenuLeapConnection().getPreferredSize());
		}
		
		/* (non-Javadoc)
		 * @see com.leapmotion.leap.Listener#onDisconnect(com.leapmotion.leap.Controller)
		 */
		@Override
		public void onDisconnect(Controller controller)
		{
		
			applicationView.getHelpMenuLeapConnection().setText("Leap Motion disconnected");

			try 
			{
				
				applicationView.getHelpMenuLeapConnection().setIcon(
						new ImageIcon(ImageIO.read(new File("cafallery/resources/disconnected.png"))));
			} 
			catch (IOException e) {e.printStackTrace();}			
			
			applicationView.getHelpMenuLeapConnection().setSize(applicationView.getHelpMenuLeapConnection().getPreferredSize());
		}
		
		/**
		 * @param circle The circle to check the clockwise-ness of.
		 * @return true if the circle is clockwise, false otherwise.
		 */
		public boolean circleIsClockwise(CircleGesture circle)
		{
		
    		if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2)
    			return true;
    		else
    			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.leapmotion.leap.Listener#onFrame(com.leapmotion.leap.Controller)
		 */
		@Override
		public void onFrame(Controller controller)
		{

			Frame frame = controller.frame();
			
			
			if (frame.gestures().count() > 0)
			{
			
				for (Iterator<Gesture> it = frame.gestures().iterator(); it.hasNext();)
				{
				
					Gesture gesture = it.next();
					
					if (gesture.isValid())
			    	if (gesture.type() == Gesture.Type.TYPE_CIRCLE)
						switch (gesture.state())
						{
						
							case STATE_UPDATE:
							{
								
								if (checkIfReadyToAdvance())
								{

									CircleGesture circleGesture = new CircleGesture(gesture);
						    		
						    		if (circleIsClockwise(circleGesture))
						    			changeCurrentIndex(true);
						    		else
						    			changeCurrentIndex(false);						    		
								}

								break;
							}
							
							default:
								break;
						}
				}
			}
		}
	}
	
	/**
	 * Constructor for the ViewerController class.
	 */
	public ViewerController()
	{
		
		listener = new LeapListener();
	}
	
	/**
	 * Formula for generating a few steps in time duration to ramp up the
	 * speed at which images change when holding down the arrow keys or
	 * using the Leap Motion.  Zooming with the up and down arrow keys also
	 * uses this.
	 * 
	 * @param currentTime The current system time, in milliseconds.
	 * @return The new maximum time to wait before changing images.
	 */
	public double changeCurveFormula(long currentTime)
	{
	
		double result = MAXIMUM_WAIT_FOR_CHANGE -
				(3.25 * Math.pow(Math.E, (((currentTime -
						imageChangeStart) / 1000) + 2)));
		
		if (result < MINIMUM_WAIT_FOR_CHANGE)
			return MINIMUM_WAIT_FOR_CHANGE;
		else
			return result;
	}
	
	/**
	 * Checks if the time since the last image change is greater or equal to
	 * the current maximum time to wait between changes.
	 * 
	 * @return true if the application should change the current image,
	 * false otherwise.
	 */
	public boolean checkIfReadyToAdvance()
	{

		long currentTimeMillis = System.currentTimeMillis();
		long timeDifference = currentTimeMillis - lastImageChange;
		
		if (timeDifference > MAXIMUM_WAIT_FOR_CHANGE)
		{
			
			imageChangeStart = currentTimeMillis;
			timeToWaitForChange =  (int) changeCurveFormula(currentTimeMillis);
			lastImageChange = currentTimeMillis;
			return true;
		}
		else if (timeDifference > timeToWaitForChange)
		{
			
			timeToWaitForChange = (int) changeCurveFormula(currentTimeMillis);
			lastImageChange = currentTimeMillis;
			return true;
		}

		return false;
	}
	
	/**
	 * Handles image advancement, wraps to last file if at the first and going
	 * backwards, or wraps to the first file if at the last and going forward.
	 * 
	 * @param increaseIndex True if the viewer should advance to the next
	 * image, false if the viewer should go to the previous image.
	 */
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
	 * @param applicationView The ViewerView to set as belonging to this
	 * controller.
	 */
	public void setApplicationView(ViewerView applicationView)
	{
		
		this.applicationView = applicationView;
	}
	
	/**
	 * @param imageView The ImageView to set as belonging to this controller.
	 */
	public void setImageView(ImageView imageView)
	{
		
		this.imageView = imageView;
		this.imageView.setAutoResize(true);
		this.imageView.setZoomRatio(1);
	}
	
	/**
	 * @param leapController The Leap Motion Controller object to set as
	 * belonging to this controller.
	 */
	public void setLeapController(Controller leapController)
	{
		
		this.leapController = leapController;
	}
	
	/**
	 * File and directory acceptance filter.
	 * 
	 * @param acceptFilesAndDirectories true if this should accept files and
	 * directories, false if this should accept only files.
	 * @param f The file or directory to match or reject.
	 * @return true if this is a directory and acceptFilesAndDirectories is true,
	 * or true if this is a file and matches any of the accepted extensions,
	 * returns false otherwise.
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
	 * Spawns a file chooser and implements the image file filter.
	 * 
	 * @return The spawned JFileChooser object.
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
	 * Loads and returns a BufferedImage object from the specified File,
	 * returns null if there is an IOException.
	 * 
	 * @param fileName The file object representing the image on-disk.
	 * @return
	 */
	public BufferedImage loadImageFromFile(File fileName)
	{
		
		try
		{
			
			return ImageIO.read(fileName);}
		catch(IOException e) {return null;}
	}
	
	/**
	 * Loads and displays the image specified by the currentImageIndex field,
	 * and updates all necessary UI elements to reflect the change.
	 */
	public void updateLoadedImage()
	{
		
		if (files.size() == 0)
			return;
		
		imageView.setCurrentImage(loadImageFromFile(files.get(currentImageIndex)));
		applicationView.getZoomSlider().setValue(100);
		imageView.setZoomRatio(1);
		
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
		
		recalculateImagePositioning();
	}
	
	/**
	 * Calculates the positioning and size of the currently displayed image
	 * within the image view.
	 */
	public void recalculateImagePositioning()
	{

		int resizedWidth;
		int resizedHeight;
		double widthRatio;
		double heightRatio;
		double widthScaledByZoomRatio;
		double heightScaledByZoomRatio;
		double zoomRatio;

		Dimension size = imageView.getSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		
		zoomRatio = imageView.getZoomRatio();
		widthScaledByZoomRatio = (imageView.getCurrentImageWidth() * zoomRatio);
		heightScaledByZoomRatio = (imageView.getCurrentImageHeight() * zoomRatio);
		
		if (imageView.getAutoResize())
		{
			
			if ((widthScaledByZoomRatio <= width) && (heightScaledByZoomRatio <= height))
			{
	
				imageView.setXStart((int) (Math.floor((width / 2) - 
						(widthScaledByZoomRatio / 2))));
				imageView.setYStart((int) (Math.floor((height / 2) - 
						(heightScaledByZoomRatio / 2))));
	
				imageView.setXEnd(imageView.getXStart() + (int) widthScaledByZoomRatio);
				imageView.setYEnd(imageView.getYStart() + (int) heightScaledByZoomRatio);
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
				
				imageView.setXStart((int) (Math.floor((width / 2) - (resizedWidth / 2))));
				imageView.setYStart((int) (Math.floor((height / 2) - (resizedHeight / 2))));
				
				imageView.setXEnd(imageView.getXStart() + resizedWidth);
				imageView.setYEnd(imageView.getYStart() + resizedHeight);
			}
		}
		else
		{
	
			imageView.setXStart(((int) (Math.floor((width / 2) - 
					(widthScaledByZoomRatio / 2)) + imageView.getXOffset())));
			imageView.setYStart(((int) (Math.floor((height / 2) - 
					(heightScaledByZoomRatio / 2)) + imageView.getYOffset())));
			
			imageView.setXEnd(imageView.getXStart() + (int) widthScaledByZoomRatio);
			imageView.setYEnd(imageView.getYStart() + (int) heightScaledByZoomRatio);			
		}		
		
		imageView.repaint();
	}
	
	/**
	 * Enables or disables the slideshow functionality.
	 * 
	 * @param enableSlideshow True if this should enable the slideshow view,
	 * false if this should close the current slideshow view.
	 */
	public void toggleSlideshow(boolean enableSlideshow)
	{
		
		if (enableSlideshow)
		{

			if (! imageView.getAutoResize())
			{
			
				imageView.setAutoResize(true);
				applicationView.getViewMenuAutoResizeCheckBox().setSelected(true);
			}
			
			slideshowFrame = new JFrame();
			
			slideshowFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			slideshowFrame.setUndecorated(true);
			slideshowFrame.setAlwaysOnTop(true);
			
			slideshowFrame.getContentPane().add(imageView);
			
			imageView.requestFocus();
			slideshowFrame.setVisible(true);
			
			slideshowTimerTask = new TimerTask()
			{

				@Override
				public void run() {changeCurrentIndex(true);}
			};
			
			slideshowTimer = new Timer("Slideshow Timer");
			slideshowTimer.schedule(slideshowTimerTask, slideshowDelay, slideshowDelay);
			imageView.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null));
		}
		else
		{
		
			slideshowTimer.cancel();
			slideshowDelay = 0;
			slideshowFrame.setAlwaysOnTop(false);
			slideshowFrame.setExtendedState(JFrame.NORMAL);
			applicationView.setImageView(imageView);
			slideshowFrame.dispose();
			
			imageView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			applicationView.revalidate();
		}
	}
	
	/**
	 * Starts the application, initializing necessary fields and adding
	 * listeners to the UI components.
	 */
	public void start()
	{
	
		imageChangeStart = 0;
		lastImageChange = 0;
		timeToWaitForChange = MAXIMUM_WAIT_FOR_CHANGE;
		
		slideshowDelay = 0;
		slideshowDelayIsCounting = false;
		slideshowFrame = null;
			
		files = new ArrayList<File>();
		
		/*Implementing listeners for the keyboard.*/
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addKeyEventDispatcher(new KeyEventDispatcher()
		{
		    
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) 
			{
				  	
				if (! applicationView.getZoomTextField().hasFocus())
				{
					
					if (e.getID() == KeyEvent.KEY_PRESSED)
					{
					
						switch (e.getKeyCode())
						{
						
							case (KeyEvent.VK_SPACE):
							{
								
								if (applicationView.isFocusOwner())
								if (! slideshowDelayIsCounting)
								{
																	
									if (slideshowDelay == 0)
									{
										
										slideshowDelay = System.currentTimeMillis();
										slideshowDelayIsCounting = true;
									}
								}
								
								return false;
							}
							
							case (KeyEvent.VK_LEFT):
							{
								
								if (checkIfReadyToAdvance())
									changeCurrentIndex(false);
								return false;
							}
									
							case (KeyEvent.VK_RIGHT):
							{
											
								if (checkIfReadyToAdvance())
									changeCurrentIndex(true);
								return false;
							}
							
							case (KeyEvent.VK_UP):
							{
								
								if (checkIfReadyToAdvance())
									applicationView.getZoomSlider()
									.setValue(applicationView.getZoomSlider()
											.getValue() + 10);
								
								return false;
							}
							
							case (KeyEvent.VK_DOWN):
							{
								
								if (checkIfReadyToAdvance())
									applicationView.getZoomSlider()
									.setValue(applicationView.getZoomSlider()
											.getValue() - 10);
								
								return false;
							}
							
						
							default:
								return false;
						}
					}
					
					if (e.getID() == KeyEvent.KEY_RELEASED)
						switch (e.getKeyCode())
						{
							
							case (KeyEvent.VK_LEFT): case (KeyEvent.VK_RIGHT):
							{

								imageChangeStart = 0;
								lastImageChange = 0;
								return false;
							}

							case (KeyEvent.VK_UP): case (KeyEvent.VK_DOWN):
							{

								imageChangeStart = 0;
								lastImageChange = 0;
								return false;
							}
							
							case (KeyEvent.VK_SPACE):
							{
								
								if (slideshowDelayIsCounting)
								{
									
									slideshowDelay = System.currentTimeMillis() - slideshowDelay;
									slideshowDelayIsCounting = false;
									toggleSlideshow(true);
								}
								else
								{
								
									if (slideshowDelay > 0)
									{
										
										slideshowDelay = 0;
										toggleSlideshow(false);
									}
								}
								
								return false;
							}
							
							default:
								return false;
						}	
					else
						return false;
				}
				else
					return false;
			}
		});

		/*Implement listener for the Leap Motion controller.*/
		leapController.addListener(listener);
		
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
						
						Collections.addAll(files, file.listFiles(new FileFilter()
						{

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
					
					applicationView.requestFocus();
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
				recalculateImagePositioning();
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
		
		/*Implementing listener for the how to enable slideshow menu item in the help menu.*/
		applicationView.getHelpMenuHowToEnableSlideshow().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showMessageDialog(applicationView,
						"Enabling slideshow functionality is easy and intuitive.\n\n" +
				" Simply hold down the space bar for the length of time that" +
								" you would like for each image to be\n" +
				"displayed and then release.  To exit slidshow mode either press the" +
								" space bar again, or double click.\n"
						, "How to enable slideshow", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		/*Implementing listener for resizing the image view panel.*/
		imageView.addComponentListener(new ComponentAdapter()
		{
			
			@Override
			public void componentResized(ComponentEvent e) {recalculateImagePositioning();}
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
						
						if (arg0.getClickCount() >= 2) //Close slideshow mode.
						{
						
							if ((slideshowDelay > 0) && (slideshowDelayIsCounting == false))
								toggleSlideshow(false);
							else
								changeCurrentIndex(true);
							
							break;
						}
						
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
		
		/*Implementing listener for dragging the image on the image view panel.*/
		imageView.addMouseMotionListener(new MouseAdapter()
		{
			
			@Override
            public void mouseMoved(MouseEvent e)
			{
				
                imageView.setLastMouseX(e.getX());
                imageView.setLastMouseY(e.getY());
			}

            @Override
            public void mouseDragged(MouseEvent e)
            {
  
            	if (! imageView.getIsDraggable())
            		return;
            	
            	imageView.setXOffset(imageView.getLastXOffset() +
            			(e.getX() - imageView.getLastMouseX()));
            	imageView.setYOffset(imageView.getLastYOffset() + 
            			(e.getY() - imageView.getLastMouseY()));

            	recalculateImagePositioning();
            	
				if (! (imageView.getCursor().getType() == Cursor.MOVE_CURSOR))
					imageView.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
		});
		
		/*Implementing listener to reset mouse cursor after dragging on the image view panel.*/
		imageView.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseReleased(MouseEvent arg0) {

				if (imageView.getCursor().getType() == Cursor.MOVE_CURSOR)
					imageView.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				
                imageView.setLastXOffset(imageView.getXOffset());
                imageView.setLastYOffset(imageView.getYOffset());
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
		
		/*Implementing listener for the zoom text field on the bottom tool bar*/
		applicationView.getZoomTextField().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent event) 
			{
			
				try 
				{

					Document document = applicationView.getZoomTextField().getDocument();
					String text = document.getText(0, document.getLength());
					Matcher matcher = Pattern.compile("(?<value>\\d+)%*").matcher(text);
					
					if (matcher.matches())
					{
						
						int value = Integer.valueOf(matcher.group("value"));
						
						if ((0 <= value) && (value <= 800))
						{
							
							applicationView.getZoomSlider().setValue(value);
							applicationView.requestFocus();
						}
					}
				} 
				catch (BadLocationException e) {e.printStackTrace();}
			}
		});
		
		/*Implementing listener for zoom slider on the bottom tool bar.*/
		applicationView.getZoomSlider().addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				JSlider source = (JSlider) arg0.getSource();

				applicationView.getZoomTextField().setText(source.getValue() + "%");
				imageView.setZoomRatio((double) source.getValue() / 100);
				recalculateImagePositioning();
			}	
		});
		
		applicationView.setVisible(true);
	}
}