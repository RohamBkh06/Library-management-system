package ir.ac.kntu.Javafx;

import ir.ac.kntu.util.HtmlReportGenerator;
import ir.ac.kntu.util.SaveLoadManager;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ir/ac/kntu/Javafx/SignUp.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Sign Up page.");
        }
    }


    @FXML
    private void handleLogin(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ir/ac/kntu/Javafx/Login.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Login page.");
        }
    }


    @FXML
    private void handleExit(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SaveLoadManager.save();
        try {
            HtmlReportGenerator.generateReport("report.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.close();
    }


    private void showAlert(Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

}
