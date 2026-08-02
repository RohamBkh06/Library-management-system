package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.main.UserMenu;
import ir.ac.kntu.modules.Faculty;
import ir.ac.kntu.modules.Guest;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.modules.Student;
import ir.ac.kntu.util.Validator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

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

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String memberId = memberIdField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText();



        if (firstName.isEmpty()
                || lastName.isEmpty()
                || memberId.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
                || password.isEmpty()) {

            showAlert(Alert.AlertType.WARNING, "Sign Up", "Please fill in all fields.");
            return;
        }



        if (!Validator.isValidMemberId(memberId)) {

            showAlert(Alert.AlertType.ERROR, "Invalid Member ID", "The Member ID format is invalid.");
            return;
        }



        if (!Validator.isValidEmail(email)) {

            showAlert(Alert.AlertType.ERROR, "Invalid Email", "The email format is invalid.");
            return;
        }

        if (!Validator.isValidPhoneNum(phone)) {

            showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "The phone number format is invalid.");
            return;
        }



        if (!Validator.isValidPassword(password)) {

            showAlert(Alert.AlertType.ERROR, "Weak Password", "The password does not meet the required requirements.");
            return;
        }

        if (LibraryManger.getInstance().getUserById().containsKey(memberId)) {

            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "A user with this Member ID already exists.");
            return;
        }

        String verificationCode = generateVerificationCode();

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Two-Factor Authentication");
        dialog.setHeaderText("Email Verification");
        dialog.setContentText("Enter the verification code sent to your email:");

        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String enteredCode = result.get().trim();

        if (!enteredCode.equals(verificationCode)) {

            showAlert(Alert.AlertType.ERROR, "Verification Failed", "The verification code is incorrect.");
            return;
        }


        try {

            NormalUser user;
            if (memberId.contains("GST")){
                user = new Guest(firstName, lastName, memberId, email, phone, password);
            } else if (memberId.contains("STU")) {
                user = new Student(firstName, lastName, memberId, email, phone, password);
            } else {
                user = new Faculty(firstName, lastName, memberId, email, phone, password);
            }


            LibraryManger.getInstance().addUser(user);

            showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Your account has been created successfully.");

            handleBack(event);

        } catch (IllegalArgumentException | IllegalStateException e) {

            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", e.getMessage());
        }
    }


    private String generateVerificationCode() {

        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return "123456";
    }


    @FXML
    private void handleBack(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ir/ac/kntu/Javafx/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to the main menu.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
