package ir.ac.kntu.Javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {


    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField memberIdField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private void handleSignUp(ActionEvent event) {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String memberId = memberIdField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (firstName.isEmpty()
                || lastName.isEmpty()
                || memberId.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
                || password.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sign Up",
                    "Please fill in all fields."
            );

            return;
        }

        showAlert(
                Alert.AlertType.INFORMATION,
                "Sign Up",
                "Registration information received."
        );
    }


    @FXML
    private void handleBack(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/ir/ac/kntu/Javafx/MainMenu.fxml"
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
