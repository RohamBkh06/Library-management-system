package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.util.Pagination;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminManageUsersController {


    @FXML
    private Label adminLabel;

    @FXML
    private ComboBox<String> searchTypeComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> usersListView;

    @FXML
    private Label pageLabel;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;


    private Admin admin;

    private Pagination<NormalUser> pagination;

    private List<NormalUser> allUsers;


// =========================================================
// INITIALIZE
// =========================================================

    @FXML
    private void initialize() {

        searchTypeComboBox.setItems(
                FXCollections.observableArrayList(
                        "First Name",
                        "Last Name",
                        "Password"
                )
        );

        searchTypeComboBox.getSelectionModel()
                .selectFirst();

        loadUsers();

    }


// =========================================================
// SET ADMIN
// =========================================================

    public void setAdmin(Admin admin) {

        this.admin = admin;

        if (admin != null) {

            adminLabel.setText(
                    "Admin: " +
                            admin.getFirstName() +
                            " " +
                            admin.getLastName()
            );
        }
    }


// =========================================================
// LOAD USERS
// =========================================================

    private void loadUsers() {

        allUsers =
                LibraryManger
                        .getInstance()
                        .getUserById()
                        .values()
                        .stream()
                        .toList();

        pagination =
                new Pagination<>(allUsers);

        updateUserList();

    }


// =========================================================
// UPDATE LIST
// =========================================================

    private void updateUserList() {

        List<String> users =
                pagination
                        .getCurrentPage()
                        .stream()
                        .map(NormalUser::toString)
                        .collect(Collectors.toList());

        usersListView.setItems(
                FXCollections.observableArrayList(users)
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


// =========================================================
// NEXT PAGE
// =========================================================

    @FXML
    private void handleNextPage() {

        pagination.nextPage();

        updateUserList();

    }


// =========================================================
// PREVIOUS PAGE
// =========================================================

    @FXML
    private void handlePreviousPage() {

        pagination.previousPage();

        updateUserList();

    }


// =========================================================
// SEARCH
// =========================================================

    @FXML
    private void handleSearch() {

        String searchValue =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        if (searchValue.isEmpty()) {

            showMessage(
                    "Search",
                    "Please enter a search value."
            );

            return;
        }


        String searchType =
                searchTypeComboBox
                        .getValue();


        List<NormalUser> result;


        switch (searchType) {

            case "First Name" ->

                    result =
                            allUsers
                                    .stream()
                                    .filter(user ->
                                            user.getFirstName()
                                                    .toLowerCase()
                                                    .contains(searchValue)
                                    )
                                    .toList();


            case "Last Name" ->

                    result =
                            allUsers
                                    .stream()
                                    .filter(user ->
                                            user.getLastName()
                                                    .toLowerCase()
                                                    .contains(searchValue)
                                    )
                                    .toList();


            case "Password" ->

                    result =
                            allUsers
                                    .stream()
                                    .filter(user ->
                                            user.getPassword()
                                                    .equals(searchValue)
                                    )
                                    .toList();


            default -> {

                return;

            }

        }


        if (result.isEmpty()) {

            showMessage(
                    "Search",
                    "No Result Found."
            );

            return;
        }


        pagination =
                new Pagination<>(result);

        updateUserList();

    }


// =========================================================
// SHOW ALL
// =========================================================

    @FXML
    private void handleShowAll() {

        searchField.clear();

        pagination =
                new Pagination<>(allUsers);

        updateUserList();

    }


// =========================================================
// EDIT USER
// =========================================================

    @FXML
    private void handleEditUser() {

        int selectedIndex =
                usersListView
                        .getSelectionModel()
                        .getSelectedIndex();


        if (selectedIndex == -1) {

            showMessage(
                    "Edit User",
                    "Please select a user first."
            );

            return;
        }


        NormalUser selectedUser =
                pagination
                        .getCurrentPage()
                        .get(selectedIndex);


        showEditUserDialog(selectedUser);

    }


// =========================================================
// EDIT USER DIALOG
// =========================================================

    private void showEditUserDialog(
            NormalUser user) {

        javafx.scene.control.Dialog<ButtonType> dialog =
                new javafx.scene.control.Dialog<>();

        dialog.setTitle("Edit User");

        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        ButtonType.CANCEL
                );


        TextField firstNameField =
                new TextField(
                        user.getFirstName()
                );

        TextField lastNameField =
                new TextField(
                        user.getLastName()
                );

        TextField passwordField =
                new TextField(
                        user.getPassword()
                );


        Button stateButton =
                new Button(
                        user.isActive()
                                ? "Deactivate User"
                                : "Activate User"
                );


        VBox content =
                new VBox(
                        10,
                        new Label("First Name:"),
                        firstNameField,

                        new Label("Last Name:"),
                        lastNameField,

                        new Label("Password:"),
                        passwordField,

                        stateButton
                );

        content.setPadding(
                new javafx.geometry.Insets(20)
        );


        stateButton.setOnAction(event -> {

            user.changeState();

            stateButton.setText(
                    user.isActive()
                            ? "Deactivate User"
                            : "Activate User"
            );

        });


        dialog.getDialogPane()
                .setContent(content);


        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == saveButton) {

                        try {

                            user.setFirstName(
                                    firstNameField
                                            .getText()
                            );

                            user.setLastName(
                                    lastNameField
                                            .getText()
                            );

                            user.setPassword(
                                    passwordField
                                            .getText()
                            );


                            updateUserList();


                            showMessage(
                                    "Success",
                                    "User Updated Successfully."
                            );


                        } catch (Exception e) {

                            showMessage(
                                    "Error",
                                    e.getMessage()
                            );

                        }

                    }

                });

    }


// =========================================================
// REMOVE USER
// =========================================================

    @FXML
    private void handleRemoveUser() {

        int selectedIndex =
                usersListView
                        .getSelectionModel()
                        .getSelectedIndex();


        if (selectedIndex == -1) {

            showMessage(
                    "Remove User",
                    "Please select a user first."
            );

            return;
        }


        NormalUser selectedUser =
                pagination
                        .getCurrentPage()
                        .get(selectedIndex);


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Remove User"
        );

        confirmation.setHeaderText(
                "Remove Selected User?"
        );

        confirmation.setContentText(
                selectedUser.getFirstName() +
                        " " +
                        selectedUser.getLastName()
        );


        confirmation.showAndWait()
                .ifPresent(result -> {

                    if (result ==
                            javafx.scene.control.ButtonType.OK) {

                        LibraryManger
                                .getInstance()
                                .removeUser(
                                        selectedUser
                                );


                        loadUsers();


                        showMessage(
                                "Success",
                                "User Removed Successfully."
                        );

                    }

                });

    }


// =========================================================
// BACK
// =========================================================

    @FXML
    private void handleBack(
            ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AdminDashboard.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            AdminDashboardController controller =
                    loader.getController();


            controller.setAdmin(admin);


            Stage stage =
                    (Stage)
                            ((Node)
                                    event.getSource())
                                    .getScene()
                                    .getWindow();


            stage.setScene(
                    new Scene(root)
            );

            stage.show();


        } catch (IOException e) {

            showMessage(
                    "Error",
                    "Could not open Admin Dashboard."
            );

        }

    }


// =========================================================
// MESSAGE
// =========================================================

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
