package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import views.ImageView;
import views.ViewerView;

public class ViewerController {

	ViewerView applicationView;
	ImageView imageView;
	
	public ViewerController()
	{
		
	
	}
	
	public void setApplicationView(ViewerView applicationView)
	{
		
		this.applicationView = applicationView;
	}
	
	public void setImageView(ImageView imageView)
	{
		
		this.imageView = imageView;
	}
	
	public JFileChooser spawnFileChooser(FileFilter fileFilter)
	{
	
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (! (fileFilter == null))
			fileChooser.setFileFilter(fileFilter);
		
		return fileChooser;
	}
	
	public void start()
	{
		
		FileFilter fileFilter = new FileFilter(){

			@Override
			public boolean accept(File f) {
				
				if (f.isDirectory())
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
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}		
		};
		
		/*Implementing listener for the open menu item.*/
		applicationView.getFileMenuOpen().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = spawnFileChooser(fileFilter);

				int returnValue = fileChooser.showOpenDialog(applicationView.getApplicationFrame());
				System.out.println(returnValue);
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
		
		applicationView.getApplicationFrame().setVisible(true);
	}
}