import views.ViewerView;
import controller.ViewerController;
import com.leapmotion.leap.*;

/**
 * @author Nicholas
 *
 * Picture Viewer for second assignment in Human Computer Interaction
 */
public class Cafallery {

	/**
	 * @param args command line arguments
	 * 
	 * Entry point for program.
	 */
	public static void main(String[] args) {

		ViewerView applicationView = new ViewerView();
		Controller leapController = new Controller();
		ViewerController controller = new ViewerController();

		controller.setApplicationView(applicationView);
		controller.setImageView(applicationView.getImageView());
		controller.setLeapController(leapController);
		
		controller.start();
	}
}