import javafx.application.Application;

/**
 * The main driver class for CryptoCoffee. Run this program in a VM only,
 * otherwise you risk accidental encryption of local files.
 * 
 * 
 * @author Gabriel
 *
 */
public class Driver {

	public static void main(String args[]) {
		new Thread() {
			@Override
			public void run() {
				Application.launch(GUI.class, null);
			}
		}.start();
	}

}
