package ir.ac.kntu.Javafx;

import ir.ac.kntu.modules.NormalUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDashboardController {


    private NormalUser currentUser;

    @FXML
    private Label welcomeLabel;


    public void setCurrentUser(NormalUser user) {

        this.currentUser = user;

        welcomeLabel.setText("Welcome, " + user.getFirstName() + " " + user.getLastName());
    }


    @FXML
    private void handleProfile(ActionEvent event) {

        showAlert(Alert.AlertType.INFORMATION, "My Profile", currentUser.toString());
    }


    @FXML
    private void handleBrowseLibrary(ActionEvent event) {

        showAlert(Alert.AlertType.INFORMATION, "Browse Library", "Library browsing will be implemented here.");
    }


    @FXML
    private void handleBorrowedItems(ActionEvent event) {

        int activeBorrows = currentUser.activeBorrows();

        showAlert(Alert.AlertType.INFORMATION, "My Borrowed Items", "Active borrowed items: " + activeBorrows);
    }


    @FXML
    private void handleReservations(ActionEvent event) {

        int reservations = currentUser.getWaitingReservations().size();

        showAlert(Alert.AlertType.INFORMATION, "My Reservations", "Waiting reservations: " + reservations);
    }


    @FXML
    private void handleFines(ActionEvent event) {

        int unpaidFines = currentUser.getFines().size();

        showAlert(Alert.AlertType.INFORMATION, "My Fines", "Unpaid fines: " + unpaidFines);
    }


    @FXML
    private void handleLogout(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ir/ac/kntu/Javafx/MainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            showAlert(Alert.AlertType.ERROR, "Error", "Could not logout.");
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
