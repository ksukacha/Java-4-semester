package format;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("format.fxml"));
        primaryStage.setTitle("Format validation");
        primaryStage.setScene(new Scene(root, 480, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
