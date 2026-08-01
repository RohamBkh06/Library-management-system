package ir.ac.kntu.Javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {


    @FXML
    private void handleSignUp(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ir/ac/kntu/Javafx/SignUp.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {

        showAlert(
                Alert.AlertType.INFORMATION,
                "Log In",
                "Log In page will be added soon."
        );
    }


    @FXML
    private void handleExit(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }


    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


}
