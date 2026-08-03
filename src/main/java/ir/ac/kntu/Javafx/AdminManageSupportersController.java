package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.modules.Department;
import ir.ac.kntu.modules.Supporter;
import ir.ac.kntu.util.Pagination;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminManageSupportersController {


    @FXML
    private Label adminLabel;

    @FXML
    private ListView<String> supportersListView;

    @FXML
    private Label pageLabel;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;


    private Admin admin;

    private Pagination<Supporter> pagination;

    private List<Supporter> allSupporters;


// =========================================================
// INITIALIZE
// =========================================================

    @FXML
    private void initialize() {

        loadSupporters();

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
// LOAD SUPPORTERS
// =========================================================

    private void loadSupporters() {

        allSupporters =
                LibraryManger
                        .getInstance()
                        .getSupporterByPassword()
                        .values()
                        .stream()
                        .toList();


        pagination =
                new Pagination<>(
                        allSupporters
                );


        updateSupporterList();

    }


// =========================================================
// UPDATE LIST
// =========================================================

    private void updateSupporterList() {

        List<String> supporters =
                pagination
                        .getCurrentPage()
                        .stream()
                        .map(Supporter::toString)
                        .toList();


        supportersListView.setItems(
                FXCollections.observableArrayList(
                        supporters
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

        pagination.nextPage();

        updateSupporterList();

    }


// =========================================================
// PREVIOUS PAGE
// =========================================================

    @FXML
    private void handlePreviousPage() {

        pagination.previousPage();

        updateSupporterList();

    }


// =========================================================
// ADD SUPPORTER
// =========================================================

    @FXML
    private void handleAddSupporter() {

        Dialog<ButtonType> dialog =
                new Dialog<>();


        dialog.setTitle(
                "Add Supporter"
        );


        ButtonType addButton =
                new ButtonType(
                        "Create",
                        ButtonBar.ButtonData.OK_DONE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        addButton,
                        ButtonType.CANCEL
                );


        TextField firstNameField =
                new TextField();

        TextField lastNameField =
                new TextField();

        TextField usernameField =
                new TextField();

        TextField passwordField =
                new TextField();


        VBox content =
                new VBox(
                        10,

                        new Label("First Name:"),
                        firstNameField,

                        new Label("Last Name:"),
                        lastNameField,

                        new Label("Username:"),
                        usernameField,

                        new Label("Password:"),
                        passwordField
                );


        content.setPadding(
                new javafx.geometry.Insets(20)
        );


        dialog.getDialogPane()
                .setContent(content);


        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == addButton) {

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
                                    && lastName.isBlank()
                                    && username.isBlank()
                                    && password.isBlank()) {

                                return;

                            }


                            Set<Department> departments =
                                    chooseDepartments();


                            Supporter supporter =
                                    new Supporter(
                                            firstName,
                                            lastName,
                                            username,
                                            password,
                                            departments
                                    );


                            LibraryManger
                                    .getInstance()
                                    .addSupporter(
                                            supporter
                                    );


                            loadSupporters();


                            showMessage(
                                    "Success",
                                    "Supporter Created Successfully."
                            );


                        } catch (Exception e) {

                            showError(
                                    "Error",
                                    e.getMessage()
                            );

                        }

                    }

                });

    }


// =========================================================
// CHOOSE DEPARTMENTS
// =========================================================

    private Set<Department> chooseDepartments() {

        Dialog<ButtonType> dialog =
                new Dialog<>();


        dialog.setTitle(
                "Choose Departments"
        );


        ButtonType okButton =
                new ButtonType(
                        "OK",
                        ButtonBar.ButtonData.OK_DONE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        okButton,
                        ButtonType.CANCEL
                );


        CheckBox reportProblem =
                new CheckBox(
                        "REPORT_PROBLEM"
                );


        CheckBox requestItem =
                new CheckBox(
                        "REQUEST_ITEM"
                );


        CheckBox financialAffairs =
                new CheckBox(
                        "FINANCIAL_AFFAIRS"
                );


        CheckBox reserveItem =
                new CheckBox(
                        "RESERVE_ITEM"
                );


        VBox content =
                new VBox(
                        10,

                        reportProblem,
                        requestItem,
                        financialAffairs,
                        reserveItem
                );


        content.setPadding(
                new javafx.geometry.Insets(20)
        );


        dialog.getDialogPane()
                .setContent(content);


        Set<Department> departments =
                new HashSet<>();


        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == okButton) {

                        if (reportProblem.isSelected()) {
                            departments.add(
                                    Department.REPORT_PROBLEM
                            );
                        }

                        if (requestItem.isSelected()) {
                            departments.add(
                                    Department.REQUEST_ITEM
                            );
                        }

                        if (financialAffairs.isSelected()) {
                            departments.add(
                                    Department.FINANCIAL_AFFAIRS
                            );
                        }

                        if (reserveItem.isSelected()) {
                            departments.add(
                                    Department.RESERVE_ITEM
                            );
                        }

                    }

                });


        return departments;

    }


// =========================================================
// EDIT SUPPORTER
// =========================================================

    @FXML
    private void handleEditSupporter() {

        int selectedIndex =
                supportersListView
                        .getSelectionModel()
                        .getSelectedIndex();


        if (selectedIndex == -1) {

            showMessage(
                    "Edit Supporter",
                    "Please select a supporter first."
            );

            return;

        }


        Supporter supporter =
                pagination
                        .getCurrentPage()
                        .get(selectedIndex);


        showEditDialog(
                supporter
        );

    }


// =========================================================
// EDIT DIALOG
// =========================================================

    private void showEditDialog(
            Supporter supporter) {

        Dialog<ButtonType> dialog =
                new Dialog<>();


        dialog.setTitle(
                "Edit Supporter"
        );


        ButtonType saveButton =
                new ButtonType(
                        "Save",
                        ButtonBar.ButtonData.OK_DONE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        ButtonType.CANCEL
                );


        TextField firstNameField =
                new TextField(
                        supporter.getFirstName()
                );


        TextField lastNameField =
                new TextField(
                        supporter.getLastName()
                );


        TextField usernameField =
                new TextField(
                        supporter.getUserName()
                );


        TextField passwordField =
                new TextField(
                        supporter.getPassword()
                );


        Button stateButton =
                new Button(
                        supporter.isActive()
                                ? "Deactivate"
                                : "Activate"
                );


        VBox content =
                new VBox(
                        10,

                        new Label("First Name:"),
                        firstNameField,

                        new Label("Last Name:"),
                        lastNameField,

                        new Label("Username:"),
                        usernameField,

                        new Label("Password:"),
                        passwordField,

                        stateButton
                );


        content.setPadding(
                new javafx.geometry.Insets(20)
        );


        stateButton.setOnAction(event -> {

            supporter.changeState();

            stateButton.setText(
                    supporter.isActive()
                            ? "Deactivate"
                            : "Activate"
            );

        });


        dialog.getDialogPane()
                .setContent(content);


        dialog.showAndWait()
                .ifPresent(result -> {

                    if (result == saveButton) {

                        try {

                            String oldPassword =
                                    supporter.getPassword();


                            supporter.setFirstName(
                                    firstNameField
                                            .getText()
                            );


                            supporter.setLastName(
                                    lastNameField
                                            .getText()
                            );


                            supporter.setUserName(
                                    usernameField
                                            .getText()
                            );


                            String newPassword =
                                    passwordField
                                            .getText();


                            if (!oldPassword.equals(
                                    newPassword
                            )) {

                                LibraryManger.getInstance()
                                        .updateSupporterPassword(supporter, newPassword);

                            }


                            loadSupporters();


                            showMessage(
                                    "Success",
                                    "Supporter Updated Successfully."
                            );


                        } catch (Exception e) {

                            showError(
                                    "Error",
                                    e.getMessage()
                            );

                        }

                    }

                });

    }


// =========================================================
// REMOVE SUPPORTER
// =========================================================

    @FXML
    private void handleRemoveSupporter() {

        int selectedIndex =
                supportersListView
                        .getSelectionModel()
                        .getSelectedIndex();


        if (selectedIndex == -1) {

            showMessage(
                    "Remove Supporter",
                    "Please select a supporter first."
            );

            return;

        }


        Supporter supporter =
                pagination
                        .getCurrentPage()
                        .get(selectedIndex);


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirmation.setTitle(
                "Remove Supporter"
        );


        confirmation.setHeaderText(
                "Remove Selected Supporter?"
        );


        confirmation.setContentText(
                supporter.getFirstName() +
                        " " +
                        supporter.getLastName()
        );


        confirmation.showAndWait()
                .ifPresent(result -> {

                    if (result ==
                            ButtonType.OK) {

                        LibraryManger
                                .getInstance()
                                .removeSupporter(
                                        supporter
                                );


                        loadSupporters();


                        showMessage(
                                "Success",
                                "Supporter Removed Successfully."
                        );

                    }

                });

    }


// =========================================================
// DEPARTMENTS
// =========================================================

    @FXML
    private void handleDepartments() {

        int selectedIndex =
                supportersListView
                        .getSelectionModel()
                        .getSelectedIndex();


        if (selectedIndex == -1) {

            showMessage(
                    "Departments",
                    "Please select a supporter first."
            );

            return;

        }


        Supporter supporter =
                pagination
                        .getCurrentPage()
                        .get(selectedIndex);


        showDepartmentsDialog(
                supporter
        );

    }


// =========================================================
// DEPARTMENT DIALOG
// =========================================================

    private void showDepartmentsDialog(
            Supporter supporter) {

        Dialog<ButtonType> dialog =
                new Dialog<>();


        dialog.setTitle(
                "Supporter Departments"
        );


        ButtonType closeButton =
                new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .add(
                        closeButton
                );


        ListView<Department> departmentListView =
                new ListView<>();


        departmentListView.setItems(
                FXCollections.observableArrayList(
                        supporter.getDepartments()
                )
        );


        ComboBox<Department> departmentComboBox =
                new ComboBox<>();


        departmentComboBox.setItems(
                FXCollections.observableArrayList(
                        Department.values()
                )
        );


        Button addButton =
                new Button(
                        "Add Department"
                );


        Button removeButton =
                new Button(
                        "Remove Department"
                );


        addButton.setOnAction(event -> {

            Department department =
                    departmentComboBox
                            .getValue();


            if (department == null) {

                return;

            }


            supporter.addDepartment(
                    department
            );


            departmentListView.setItems(
                    FXCollections.observableArrayList(
                            supporter.getDepartments()
                    )
            );

        });


        removeButton.setOnAction(event -> {

            Department department =
                    departmentListView
                            .getSelectionModel()
                            .getSelectedItem();


            if (department == null) {

                return;

            }


            supporter.removeDepartment(
                    department
            );


            departmentListView.setItems(
                    FXCollections.observableArrayList(
                            supporter.getDepartments()
                    )
            );

        });


        VBox content =
                new VBox(
                        10,

                        new Label(
                                "Current Departments:"
                        ),

                        departmentListView,

                        departmentComboBox,

                        addButton,

                        removeButton
                );


        content.setPadding(
                new javafx.geometry.Insets(20)
        );


        dialog.getDialogPane()
                .setContent(content);


        dialog.showAndWait();

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


            controller.setAdmin(
                    admin
            );


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

            showError(
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


    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();

    }


}
