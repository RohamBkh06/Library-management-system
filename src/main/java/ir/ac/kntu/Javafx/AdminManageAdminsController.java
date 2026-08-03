package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.util.Pagination;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

import java.util.List;

public class AdminManageAdminsController {


    @FXML
    private ListView<Admin> adminListView;

    @FXML
    private Label pageLabel;

    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;


    private Admin editor;

    private Admin selectedAdmin;

    private Pagination<Admin> pagination;

    private boolean editMode = false;


// =========================================================
// SET ADMIN
// =========================================================

    public void setAdmin(Admin admin) {

        this.editor = admin;

        loadAdmins();
    }


// =========================================================
// LOAD ADMINS
// =========================================================

    private void loadAdmins() {

        List<Admin> admins =
                LibraryManger
                        .getInstance()
                        .getAdminByPassword()
                        .values()
                        .stream()
                        .toList();

        pagination =
                new Pagination<>(admins);

        updateList();
    }


// =========================================================
// UPDATE LIST
// =========================================================

    private void updateList() {

        if (pagination == null) {
            return;
        }

        adminListView.setItems(
                FXCollections.observableArrayList(
                        pagination.getCurrentPage()
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


// =========================================================
// NEXT PAGE
// =========================================================

    @FXML
    private void handleNextPage() {

        if (pagination.hasNextPage()) {

            pagination.nextPage();

            updateList();
        }
    }


// =========================================================
// PREVIOUS PAGE
// =========================================================

    @FXML
    private void handlePreviousPage() {

        if (pagination.hasPreviousPage()) {

            pagination.previousPage();

            updateList();
        }
    }


// =========================================================
// CREATE ADMIN
// =========================================================

    @FXML
    private void handleCreateAdmin() {

        editMode = false;

        selectedAdmin = null;

        formTitleLabel.setText(
                "Create New Admin"
        );

        clearFields();

        saveButton.setText(
                "Create"
        );
    }


// =========================================================
// EDIT ADMIN
// =========================================================

    @FXML
    private void handleEditAdmin() {

        Admin target =
                adminListView
                        .getSelectionModel()
                        .getSelectedItem();


        if (target == null) {

            showError(
                    "Please select an admin first."
            );

            return;
        }


        try {

            if (!editor.canEdit(target)) {

                throw new IllegalStateException(
                        "You don't have permission to edit this admin."
                );
            }


            selectedAdmin = target;

            editMode = true;


            formTitleLabel.setText(
                    "Edit Admin"
            );


            firstNameField.setText(
                    target.getFirstName()
            );

            lastNameField.setText(
                    target.getLastName()
            );

            usernameField.setText(
                    target.getUserName()
            );

            passwordField.setText(
                    target.getPassword()
            );


            saveButton.setText(
                    "Update"
            );


        } catch (Exception e) {

            showError(
                    e.getMessage()
            );
        }
    }


// =========================================================
// SAVE
// =========================================================

    @FXML
    private void handleSave() {

        try {

            String firstName =
                    firstNameField
                            .getText()
                            .trim();

            String lastName =
                    lastNameField
                            .getText()
                            .trim();

            String username =
                    usernameField
                            .getText()
                            .trim();

            String password =
                    passwordField
                            .getText()
                            .trim();


            if (firstName.isBlank()
                    || lastName.isBlank()
                    || username.isBlank()
                    || password.isBlank()) {

                throw new IllegalArgumentException(
                        "All fields are required."
                );
            }


            LibraryManger manager =
                    LibraryManger.getInstance();


            // =================================================
            // CREATE
            // =================================================

            if (!editMode) {

                Admin newAdmin =
                        new Admin(
                                firstName,
                                lastName,
                                username,
                                password,
                                editor
                        );


                manager.addAdmin(
                        newAdmin
                );


                showInformation(
                        "Admin Created",
                        "Admin created successfully."
                );

            }


            // =================================================
            // EDIT
            // =================================================

            else {

                if (selectedAdmin == null) {

                    throw new IllegalStateException(
                            "No admin selected."
                    );
                }


                selectedAdmin.setFirstName(
                        firstName
                );

                selectedAdmin.setLastName(
                        lastName
                );

                selectedAdmin.setUserName(
                        username
                );


                if (!selectedAdmin
                        .getPassword()
                        .equals(password)) {

                    LibraryManger.getInstance().updateAdminPassword(selectedAdmin, password);
                }


                showInformation(
                        "Admin Updated",
                        "Admin updated successfully."
                );
            }


            clearFields();

            loadAdmins();

            editMode = false;

            selectedAdmin = null;


        } catch (Exception e) {

            showError(
                    e.getMessage()
            );
        }
    }


// =========================================================
// REMOVE ADMIN
// =========================================================

    @FXML
    private void handleRemoveAdmin() {

        Admin target =
                adminListView
                        .getSelectionModel()
                        .getSelectedItem();


        if (target == null) {

            showError(
                    "Please select an admin first."
            );

            return;
        }


        try {

            if (!editor.canEdit(target)) {

                throw new IllegalStateException(
                        "You don't have permission to remove this admin."
                );
            }


            boolean confirmed =
                    showConfirmation(
                            "Remove Admin",
                            "Are you sure you want to remove this admin?"
                    );


            if (!confirmed) {

                return;
            }


            LibraryManger
                    .getInstance()
                    .removeAdmin(
                            editor,
                            target
                    );


            showInformation(
                    "Admin Removed",
                    "Admin removed successfully."
            );


            loadAdmins();


        } catch (Exception e) {

            showError(
                    e.getMessage()
            );
        }
    }


// =========================================================
// CLEAR
// =========================================================

    @FXML
    private void handleClear() {

        clearFields();

        selectedAdmin = null;

        editMode = false;

        formTitleLabel.setText(
                "Admin Information"
        );

        saveButton.setText(
                "Save"
        );
    }


    private void clearFields() {

        firstNameField.clear();

        lastNameField.clear();

        usernameField.clear();

        passwordField.clear();
    }


// =========================================================
// BACK
// =========================================================

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

            controller.setAdmin(editor);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            showError(
                    "Could not return to Admin Dashboard."
            );
        }
    }


// =========================================================
// ALERTS
// =========================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Error"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    private void showInformation(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    private boolean showConfirmation(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );


        return alert
                .showAndWait()
                .orElse(null)
                == javafx.scene.control.ButtonType.OK;
    }


}
