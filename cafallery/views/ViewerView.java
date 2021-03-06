package views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.sun.glass.events.KeyEvent;

/**
 * @author Nicholas
 *
 * Class for the view that houses the JFrame that is the application and its
 * UI elements. 
 */
public class ViewerView extends javax.swing.JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1008356999338809798L;
	
	/*Application Frame identifiers.*/
	private JFrame applicationFrame;
	private GridBagConstraints constraints;
	
	/*File Chooser identifiers.*/
	JFileChooser fileChooser;
	
	/*Menu Bar identifiers.*/
	private JMenuBar mainMenuBar;
	private JMenuBar bottomMenuBar;
	
	/*File Menu identifiers.*/
	private JMenu fileMenu;
	private JMenuItem fileMenuOpen;
	private JMenuItem fileMenuExit;
	
	/*View Menu Identifiers*/
	private JMenu viewMenu;
	private JMenuItem viewMenuPreviousImage;
	private JMenuItem viewMenuNextImage;
	private JCheckBoxMenuItem viewMenuAutoResizeCheckBox;
	private JMenuItem viewMenuInvertBackground;
	
	/*Help Menu identifiers.*/
	private JMenu helpMenu;
	private JMenuItem helpMenuLeapConnection;
	private JMenuItem helpMenuHowToEnableSlideshow;
	
	private ImageView imageView;
	
	/*Bottom Tool Bar identifier.*/
	private JLabel filePathLabel; 
	private JLabel fileIndexLabel;
	private JButton previousButton;
	private JButton nextButton;
	private JLabel zoomLabel;
	private JTextField zoomTextField;
	private JLabel zoomMinusLabel;
	private JSlider zoomSlider;
	private JLabel zoomPlusLabel;
	private static final int ZOOM_MIN = 0;
	private static final int ZOOM_INIT = 100;
	private static final int ZOOM_MAX = 800;
	
	/**
	 * Constructor for the ViewerView class.
	 */
	public ViewerView()
	{
	
		/*Attempt to set the Look and Feel as the system's native.*/
		try 
		{
			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e)
		{

			e.printStackTrace();
		}
				
		/*Main application window.*/
		setTitle("Cafallery");//setTitle("Picture Viewer");
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		setMinimumSize(new Dimension(350, 200));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		setLayout(new GridBagLayout());
		
		/*Menu bar for application.*/
		mainMenuBar = new JMenuBar();
		
		/*File menu for menu bar.*/
		fileMenu = new JMenu("File");
		fileMenu.getAccessibleContext().setAccessibleDescription("File menu.");
		fileMenu.setMnemonic(KeyEvent.VK_F); //Mnemonic 'F'ile
		
		fileMenuOpen = new JMenuItem("Open", KeyEvent.VK_O); //Mnemonic: 'O'pen
		fileMenuExit = new JMenuItem("Exit", KeyEvent.VK_E); //Mnemonic: 'E'xit
		fileMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileMenuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		
		fileMenu.addSeparator();
		fileMenu.add(fileMenuOpen);
		fileMenu.addSeparator();
		fileMenu.add(fileMenuExit);
		mainMenuBar.add(fileMenu);
		
		/*View menu for menu bar.*/
		viewMenu = new JMenu("View");
		viewMenu.getAccessibleContext().setAccessibleDescription("View menu");
		viewMenu.setMnemonic(KeyEvent.VK_V); //Mnemonic: 'V'iew
		
		viewMenuPreviousImage = new JMenuItem("Previous image", KeyEvent.VK_P); //Mnemonic: 'P'revious
		viewMenuNextImage = new JMenuItem("Next image", KeyEvent.VK_N); //Mnemonic: 'N'ext
		viewMenuPreviousImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		viewMenuNextImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		
		viewMenuAutoResizeCheckBox = new JCheckBoxMenuItem("Auto-resize image");
		viewMenuAutoResizeCheckBox.setMnemonic(KeyEvent.VK_A); //Mnemonic: 'A'uto-resize
		viewMenuAutoResizeCheckBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		viewMenuAutoResizeCheckBox.setSelected(true);
		
		viewMenuInvertBackground = new JMenuItem("Invert viewer background", KeyEvent.VK_I); //Mnemonic: 'I'nvert
		viewMenuInvertBackground.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));

		viewMenu.add(viewMenuPreviousImage);
		viewMenu.add(viewMenuNextImage);
		viewMenu.addSeparator();
		viewMenu.add(viewMenuAutoResizeCheckBox);
		viewMenu.add(viewMenuInvertBackground);
		mainMenuBar.add(viewMenu);
		
		mainMenuBar.add(Box.createHorizontalGlue());

		/*Help menu for menu bar.*/
		helpMenu = new JMenu("Help");
		helpMenu.getAccessibleContext().setAccessibleDescription("Help menu.");
		helpMenu.setMnemonic(KeyEvent.VK_H);
	
		helpMenuLeapConnection = new JMenuItem("Leap Motion disconnected");
		
		try {
			
			helpMenuLeapConnection.setIcon(new ImageIcon(
					ImageIO.read(new File("cafallery/resources/disconnected.png"))));
		} 
		catch (IOException e) {e.printStackTrace();}
		
		helpMenuHowToEnableSlideshow = new JMenuItem("How to enable slideshow", KeyEvent.VK_H); //Mnemonic: 'H'elp
		helpMenuHowToEnableSlideshow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
		
		helpMenu.add(helpMenuLeapConnection);
		helpMenu.add(helpMenuHowToEnableSlideshow);
		mainMenuBar.add(helpMenu);
		
		setJMenuBar(mainMenuBar);
		
		/*Bottom tool bar for application.*/
		bottomMenuBar = new JMenuBar();
		filePathLabel = new JLabel("No image or directory opened");
		bottomMenuBar.add(filePathLabel);

		bottomMenuBar.add(Box.createHorizontalStrut(100));
		bottomMenuBar.add(Box.createHorizontalGlue());
		
		fileIndexLabel = new JLabel();
		bottomMenuBar.add(fileIndexLabel);
		bottomMenuBar.add(Box.createHorizontalStrut(10));
		
		/*Navigation components for bottom tool bar.*/
		previousButton = new JButton();
		nextButton = new JButton();
		try {
			
			previousButton.setIcon(new ImageIcon(ImageIO.read(
					new File("cafallery/resources/arrow_left.png"))));
			nextButton.setIcon(new ImageIcon(ImageIO.read(
					new File("cafallery/resources/arrow_right.png"))));
		} 
		catch (IOException e) 
		{
		
			e.printStackTrace();
			previousButton.setText("<");
			nextButton.setText(">");
		}
		previousButton.setFocusable(false);
		nextButton.setFocusable(false);
		bottomMenuBar.add(previousButton);
		bottomMenuBar.add(nextButton);
		bottomMenuBar.add(Box.createHorizontalStrut(10));
		
		/*Zoom slider components for bottom tool bar.*/
		zoomLabel = new JLabel("Zoom: ");
		zoomTextField = new JTextField(4);
		zoomTextField.setText("100%");
		zoomTextField.setEditable(true);
		zoomTextField.setMinimumSize(new Dimension(zoomTextField
				.getPreferredSize().width, zoomTextField.getPreferredSize().height));
		zoomTextField.setMaximumSize(new Dimension(zoomTextField
				.getPreferredSize().width, zoomTextField.getPreferredSize().height));

		zoomMinusLabel = new JLabel("-");
		
		zoomSlider = new JSlider(ZOOM_MIN, ZOOM_MAX, ZOOM_INIT);
		
		zoomSlider.setMaximumSize(new Dimension(150, zoomSlider
				.getPreferredSize().height));
		zoomSlider.setMinimumSize(new Dimension(150, zoomSlider
				.getPreferredSize().height));
		
		zoomSlider.setMajorTickSpacing(100);
		zoomSlider.setMinorTickSpacing(10);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setFocusable(false);
		
		zoomPlusLabel = new JLabel("+");
		
		bottomMenuBar.add(zoomLabel);
		bottomMenuBar.add(zoomTextField);
		bottomMenuBar.add(Box.createHorizontalStrut(10));
		bottomMenuBar.add(zoomMinusLabel);
		bottomMenuBar.add(zoomSlider);
		bottomMenuBar.add(zoomPlusLabel);
		
		/*Layout constraints for the main image view panel.*/
		imageView = new ImageView();
		setImageView(imageView);

		/*Layout constraints for the bottom menu bar.*/
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		add(bottomMenuBar, constraints);		
	}
	
	public GridBagConstraints getConstraints() {return constraints;}
	
	public JFileChooser getFileChooser() {return fileChooser;}

	public JMenuBar getMainMenuBar() {return mainMenuBar;}
	
	public JMenuBar getBottomMenuBar() {return bottomMenuBar;}

	public JMenu getFileMenu() {return fileMenu;}

	public JMenuItem getFileMenuOpen() {return fileMenuOpen;}

	public JMenuItem getFileMenuExit() {return fileMenuExit;}

	public JMenu getHelpMenu() {return helpMenu;}

	public JMenu getViewMenu() {return viewMenu;}

	public JMenuItem getViewMenuPreviousImage() {return viewMenuPreviousImage;}

	public JMenuItem getViewMenuNextImage() {return viewMenuNextImage;}

	public JCheckBoxMenuItem getViewMenuAutoResizeCheckBox() {return viewMenuAutoResizeCheckBox;}

	public JMenuItem getViewMenuInvertBackground() {return viewMenuInvertBackground;}

	public JMenuItem getHelpMenuLeapConnection() {return helpMenuLeapConnection;}

	public JMenuItem getHelpMenuHowToEnableSlideshow() {return helpMenuHowToEnableSlideshow;}

	public JLabel getFilePathLabel() {return filePathLabel;}

	public JLabel getFileIndexLabel() {return fileIndexLabel;}

	public JTextField getZoomTextField() {return zoomTextField;}

	public JButton getPreviousButton() {return previousButton;}

	public JButton getNextButton() {return nextButton;}

	public JSlider getZoomSlider() {return zoomSlider;}

	public ImageView getImageView() {return imageView;}
	
	public void setImageView(ImageView view)
	{
	
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;	
		add(view, constraints);
	}
	
	public JFrame getApplicationFrame(){return applicationFrame;}
}