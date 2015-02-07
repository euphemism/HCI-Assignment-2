package views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.glass.events.KeyEvent;

/**
 * @author Nicholas
 *
 */
public class ViewerView
{

	/*Application Frame identifiers.*/
	private JFrame applicationFrame;
	private GridBagConstraints constraints;
	
	/*File Chooser identifiers.*/
	JFileChooser fileChooser;
	
	/*Menu Bar identifiers.*/
	private JMenuBar menuBar;
	private JMenuBar bottomMenuBar;
	
	/*File Menu identifiers.*/
	private JMenu fileMenu;
	private JMenuItem fileMenuOpen;
	private JMenuItem fileMenuExit;
	
	/*View Menu Identifiers*/
	private JMenu viewMenu;
	private JCheckBoxMenuItem viewMenuAutoResizeCheckBox;
	private JMenuItem viewMenuInvertBackground;
	
	/*Help Menu identifiers.*/
	private JMenu helpMenu;
	private ImageView imageView;
	
	/*Bottom Tool Bar identifier.*/
	private JLabel filePathLabel; 
	
	/**
	 * 
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
		applicationFrame = new JFrame();
		applicationFrame.setTitle("Picture Viewer");
		applicationFrame.setSize(800, 600);
		applicationFrame.setMinimumSize(new Dimension(350, 200));
		applicationFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		applicationFrame.setLayout(new GridBagLayout());
		
		/*Menu bar for application.*/
		menuBar = new JMenuBar();
		
		/*File menu for menu bar.*/
		fileMenu = new JMenu("File");
		fileMenu.getAccessibleContext().setAccessibleDescription("File menu.");
		fileMenu.setMnemonic(KeyEvent.VK_F); //Mnemonic 'F'ile
		
		fileMenuOpen = new JMenuItem("Open", KeyEvent.VK_O); //Mnemonic: 'O'pen
		fileMenuExit = new JMenuItem("Exit", KeyEvent.VK_E); //Mnemonic: 'E'xit
		
		fileMenu.addSeparator();
		fileMenu.add(fileMenuOpen);
		fileMenu.addSeparator();
		fileMenu.add(fileMenuExit);
		menuBar.add(fileMenu);
		
		/*View menu for menu bar.*/
		viewMenu = new JMenu("View");
		viewMenu.getAccessibleContext().setAccessibleDescription("View menu");
		viewMenu.setMnemonic(KeyEvent.VK_V); //Mnemonic: 'V'iew
		
		viewMenuAutoResizeCheckBox = new JCheckBoxMenuItem("Auto-resize image");
		viewMenuAutoResizeCheckBox.setMnemonic(KeyEvent.VK_A); //Mnemonic: 'A'uto-resize
		viewMenuAutoResizeCheckBox.setSelected(true);
		
		viewMenuInvertBackground = new JMenuItem("Invert viewer background", KeyEvent.VK_I); //Mnemonic: 'I'nvert
		
		viewMenu.add(viewMenuAutoResizeCheckBox);
		viewMenu.add(viewMenuInvertBackground);
		menuBar.add(viewMenu);
		
		menuBar.add(Box.createHorizontalGlue());

		/*Help menu for menu bar.*/
		helpMenu = new JMenu("Help");
		helpMenu.getAccessibleContext().setAccessibleDescription("Help menu.");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);
		
		applicationFrame.setJMenuBar(menuBar);
		
		imageView = new ImageView();
		imageView.setAutoResize(true);
		
		bottomMenuBar = new JMenuBar();
		filePathLabel = new JLabel(" ");
		bottomMenuBar.add(filePathLabel);
		bottomMenuBar.add(Box.createHorizontalGlue());

		/*Layout constraints for the main application panel.*/
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		applicationFrame.add(imageView, constraints);
		
		/*Layout constraints for the bottom menu bar.*/
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridheight = 1;
		constraints.weightx = 1.0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		applicationFrame.add(bottomMenuBar, constraints);
	}
	
	public GridBagConstraints getConstraints() {return constraints;}
	
	public JFileChooser getFileChooser() {return fileChooser;}

	public JMenuBar getMenuBar() {return menuBar;}
	
	public JMenuBar getBottomMenuBar() {return bottomMenuBar;}

	public JMenu getFileMenu() {return fileMenu;}

	public JMenuItem getFileMenuOpen() {return fileMenuOpen;}

	public JMenuItem getFileMenuExit() {return fileMenuExit;}

	public JMenu getHelpMenu() {return helpMenu;}

	public JMenu getViewMenu() {return viewMenu;}

	public JCheckBoxMenuItem getViewMenuAutoResizeCheckBox() {return viewMenuAutoResizeCheckBox;}

	public JMenuItem getViewMenuInvertBackground() {return viewMenuInvertBackground;}

	public JLabel getFilePathLabel() {return filePathLabel;}

	public ImageView getImageView() {return imageView;}
	
	public JFrame getApplicationFrame(){return applicationFrame;}
}