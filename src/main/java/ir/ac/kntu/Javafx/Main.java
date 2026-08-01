package ir.ac.kntu.Javafx;

import ir.ac.kntu.util.InitializerCheck;
import ir.ac.kntu.util.SaveLoadManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void init() {
        SaveLoadManager.load();
        InitializerCheck.run();
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ir/ac/kntu/Javafx/MainMenu.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setHeight(600);


        stage.setOnCloseRequest(event -> {
            SaveLoadManager.save();
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
