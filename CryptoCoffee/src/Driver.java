import javafx.application.Application;

public class Driver {
	public static void main(String args[]) {
		new Thread() {
			@Override
			public void run() {
				Application.launch(GUI.class, null);
			}
		}.start();
	}

	public static void encrypt() {

	}
}
