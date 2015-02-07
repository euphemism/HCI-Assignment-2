import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import views.ViewerView;

import com.sun.glass.events.KeyEvent;

import controller.ViewerController;


/**
 * @author Nicholas
 *
 * Picture Viewer for second assignment in Human Computer Interaction
 */
public class PictureViewer {

	/**
	 * @param args command line arguments
	 * 
	 * Entry point for program.
	 */
	public static void main(String[] args) {

		ViewerController controller = new ViewerController();
		ViewerView applicationView = new ViewerView();
		
		controller.setApplicationView(applicationView);
		controller.setImageView(applicationView.getApplicationPanel());
		controller.start();
	}
}