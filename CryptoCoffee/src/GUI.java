import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * GUI to implement the RefrigeratorDisplay interface.
 */
public class GUI extends Application {
	private Button encrypt;

	/**
	 * Sets up the interface
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		encrypt = new Button("Encrypt!");
		encrypt.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				new Thread() {
					@Override
					public void run() {
						Driver.encrypt();
					}
				};
			}
		});

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		VBox paneCenter = new VBox();
		paneCenter.setSpacing(10);
		pane.setCenter(paneCenter);
		paneCenter.getChildren().add(encrypt);
		Scene scene = new Scene(pane, 400, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Encrypt");

		primaryStage.show();
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent window) {
				System.exit(0);
			}
		});
	}

}
