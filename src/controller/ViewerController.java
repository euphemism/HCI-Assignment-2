package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public void start()
	{
		
		/*Implementing listener for the open menu item.*/
		applicationView.getFileMenuOpen().addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {

				applicationView.spawnFileChooser();
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