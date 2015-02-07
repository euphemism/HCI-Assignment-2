import views.ViewerView;

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
		controller.setImageView(applicationView.getImageView());
		controller.start();
	}
}