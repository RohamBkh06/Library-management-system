package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.modules.Supporter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {


    @FXML
    private void handleUserLogin(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("User Login");
        dialog.setHeaderText("User Login");
        dialog.setContentText("Enter your Member ID:");

        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String id = result.get().trim();

        if (id.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login", "Member ID cannot be empty.");
            return;
        }

        try {

            LibraryManger manager = LibraryManger.getInstance();

            NormalUser user = manager.loginUser(id);

            openUserDashboard(event, user);

        } catch (IllegalArgumentException | IllegalStateException e) {

            showAlert(Alert.AlertType.ERROR, "Login Failed", e.getMessage());
        }
    }


    @FXML
    private void handleSupporterLogin(
            ActionEvent event
    ) {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle("Supporter Login");
        dialog.setHeaderText(
                "Supporter Login"
        );

        dialog.setContentText(
                "Enter your password:"
        );

        Optional<String> result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String password =
                result.get();

        if (password.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Login",
                    "Password cannot be empty."
            );

            return;
        }

        try {

            LibraryManger manager =
                    LibraryManger.getInstance();

            Supporter supporter =
                    manager.loginSupporter(
                            password
                    );

            openSupporterDashboard(event, supporter);

        } catch (
                IllegalArgumentException |
                IllegalStateException e
        ) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Failed",
                    e.getMessage()
            );
        }
    }


    @FXML
    private void handleAdminLogin(
            ActionEvent event
    ) {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle("Admin Login");
        dialog.setHeaderText(
                "Admin Login"
        );

        dialog.setContentText(
                "Enter your password:"
        );

        Optional<String> result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String password =
                result.get();

        if (password.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Login",
                    "Password cannot be empty."
            );

            return;
        }

        try {

            LibraryManger manager =
                    LibraryManger.getInstance();

            Admin admin =
                    manager.loginAdmin(
                            password
                    );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Login Successful",
                    "Welcome Admin!"
            );

            /*
             * Admin Dashboard
             * will be opened here.
             */

        } catch (
                IllegalArgumentException |
                IllegalStateException e
        ) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Failed",
                    e.getMessage()
            );
        }
    }


    private void openUserDashboard(ActionEvent event, NormalUser user) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ir/ac/kntu/Javafx/UserDashboard.fxml"));

            Parent root = loader.load();

            UserDashboardController controller = loader.getController();

            controller.setCurrentUser(user);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            showAlert(Alert.AlertType.ERROR, "Error", "Could not open User Dashboard.");
        }
    }

    private void openSupporterDashboard(ActionEvent event, Supporter supporter) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ir/ac/kntu/Javafx/SupporterDashboard.fxml"));

            Parent root = loader.load();

            SupporterDashboardController controller = loader.getController();

            controller.setSupporter(supporter);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            showAlert(Alert.AlertType.ERROR, "Error", "Could not open User Dashboard.");
        }
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

            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to Main Menu.");
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
