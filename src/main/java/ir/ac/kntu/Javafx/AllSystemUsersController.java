package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.modules.Entity;
import ir.ac.kntu.util.Pagination;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.List;

public class AllSystemUsersController {

    @FXML
    private ListView<String> usersListView;

    @FXML
    private Label pageLabel;

    @FXML
    private TextField searchField;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;


    private Pagination<Entity> pagination;

    private List<Entity> allEntities;

    private Admin admin;

    public void setAdmin(Admin admin) { this.admin = admin; }


    @FXML
    private void initialize() {

        loadAllEntities();

    }


    private void loadAllEntities() {

        allEntities =
                LibraryManger
                        .getInstance()
                        .getAllEntities();

        pagination =
                new Pagination<>(allEntities);

        updateList();

    }


    private void updateList() {

        List<String> currentPage =
                pagination
                        .getCurrentPage()
                        .stream()
                        .map(Entity::toString)
                        .toList();


        usersListView.setItems(
                FXCollections.observableArrayList(
                        currentPage
                )
        );


        pageLabel.setText(
                "Page " +
                        pagination.getCurrentPageNumber() +
                        " / " +
                        pagination.gerTotalPageNumber()
        );


        previousButton.setDisable(
                !pagination.hasPreviousPage()
        );


        nextButton.setDisable(
                !pagination.hasNextPage()
        );

    }


    @FXML
    private void handleNext() {

        if (pagination.hasNextPage()) {

            pagination.nextPage();

            updateList();

        }

    }


    @FXML
    private void handlePrevious() {

        if (pagination.hasPreviousPage()) {

            pagination.previousPage();

            updateList();

        }

    }


    @FXML
    private void handleSearchFirstName() {

        String value =
                searchField
                        .getText()
                        .trim();


        if (value.isEmpty()) {

            showMessage(
                    "Search",
                    "Please enter a first name."
            );

            return;

        }


        List<Entity> result =
                LibraryManger
                        .getInstance()
                        .filteredSearch(
                                entity ->
                                        entity
                                                .getFirstName()
                                                .toLowerCase()
                                                .contains(
                                                        value.toLowerCase()
                                                )
                        );


        showSearchResults(result);

    }


    @FXML
    private void handleSearchLastName() {

        String value =
                searchField
                        .getText()
                        .trim();


        if (value.isEmpty()) {

            showMessage(
                    "Search",
                    "Please enter a last name."
            );

            return;

        }


        List<Entity> result =
                LibraryManger
                        .getInstance()
                        .filteredSearch(
                                entity ->
                                        entity
                                                .getLastName()
                                                .toLowerCase()
                                                .contains(
                                                        value.toLowerCase()
                                                )
                        );


        showSearchResults(result);

    }


    @FXML
    private void handleSearchPassword() {

        String value =
                searchField
                        .getText()
                        .trim();


        if (value.isEmpty()) {

            showMessage(
                    "Search",
                    "Please enter a password."
            );

            return;

        }


        List<Entity> result =
                LibraryManger
                        .getInstance()
                        .filteredSearch(
                                entity ->
                                        entity
                                                .getPassword()
                                                .equals(value)
                        );


        showSearchResults(result);

    }


    private void showSearchResults(
            List<Entity> result) {


        if (result.isEmpty()) {

            showMessage(
                    "Search",
                    "No Result Found."
            );

            return;

        }


        List<String> resultList =
                result
                        .stream()
                        .map(Entity::toString)
                        .toList();


        usersListView.setItems(
                FXCollections.observableArrayList(
                        resultList
                )
        );


        pageLabel.setText(
                "Search Results: " +
                        result.size()
        );


        previousButton.setDisable(true);

        nextButton.setDisable(true);

    }


    @FXML
    private void handleClearSearch() {

        searchField.clear();

        loadAllEntities();

    }



    @FXML
    private void handleBack(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AdminDashboard.fxml"
                            )
                    );

            Parent root = loader.load();

            AdminDashboardController controller =
                    loader.getController();


            controller.setAdmin(admin);

            Stage stage =
                    (Stage)
                            ((Node) event.getSource())
                                    .getScene()
                                    .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            showMessage(
                    "Error",
                    "Could not return to Admin Dashboard."
            );

            e.printStackTrace();
        }
    }




    private void showMessage(
            String title,
            String message) {


        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();

    }

}