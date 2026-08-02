package ir.ac.kntu.Javafx;

import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.Pagination;
import ir.ac.kntu.util.PaymentService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class UserDashboardController {


    private static final int PAGE_SIZE = 10;

    private NormalUser currentUser;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label capacityLabel;

    private final AtomicLong searchVersion = new AtomicLong(0);


// =========================================================
// User
// =========================================================

    public void setCurrentUser(NormalUser user) {
        this.currentUser = user;
        updateUserInformation();
    }


    private void updateUserInformation() {
        if (currentUser == null) {
            return;
        }
        welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());

        idLabel.setText("Member ID: " + currentUser.getId());

        emailLabel.setText("Email: " + currentUser.getEmail());

        phoneLabel.setText("Phone: " + currentUser.getPhoneNum());

        statusLabel.setText("Status: " + (currentUser.isActive() ? "Active" : "Inactive"));
        updateCapacity();
    }


    private void updateCapacity() {

        int availableCapacity = currentUser.getBorrowLimit() - currentUser.activeBorrows();

        capacityLabel.setText("Available Borrow Capacity: " + availableCapacity);
    }


// =========================================================
// Wallet
// =========================================================

    @FXML
    private void handleWallet(ActionEvent event) {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Wallet");

        VBox content = new VBox(15);

        content.setPadding(new Insets(20));

        Label walletLabel = new Label(currentUser.getWallet().toString());

        Button chargeButton = new Button("Charge Wallet");

        Button transactionsButton = new Button("See Transactions");

        Button finesButton = new Button("See Fines");

        chargeButton.setOnAction(e -> chargeWallet());

        transactionsButton.setOnAction(e -> showTransactions());

        finesButton.setOnAction(e -> showFines());

        content.getChildren().addAll(walletLabel, chargeButton, transactionsButton, finesButton);

        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


    private void chargeWallet() {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Charge Wallet");

        dialog.setHeaderText("Enter the amount");

        dialog.setContentText("Amount:");

        dialog.showAndWait()
                .ifPresent(value -> {

                    try {

                        int amount = Integer.parseInt(value);

                        PaymentService.chargeWallet(amount, currentUser);

                        showAlert(Alert.AlertType.INFORMATION, "Success", "Wallet charged successfully.");

                    } catch (IllegalArgumentException e) {

                        showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                    }
                });
    }


    private void showTransactions() {

        List<?> transactions = currentUser.getWallet().getTransactions();

        showPaginatedDialog("Transactions", transactions, null);
    }


// =========================================================
// Borrowed Items
// =========================================================

    @FXML
    private void handleBorrowedItems(ActionEvent event) {
        showBorrowedItems();
    }


    private void showBorrowedItems() {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("My Borrowed Items");

        VBox content = new VBox(10);

        content.setPadding(new Insets(15));

        Label capacity = new Label();

        updateBorrowCapacityLabel(capacity);

        ListView<Borrowed> listView = new ListView<>();

        Pagination<Borrowed> pagination = new Pagination<>(currentUser.getBorrowedList());

        Label pageLabel = new Label();

        Button previousButton = new Button("Previous");

        Button nextButton = new Button("Next");

        Button borrowButton = new Button("Borrow New Item");

        Button extendButton = new Button("Extend Borrow");

        Button returnButton = new Button("Return Borrow");


        Runnable updatePage = () -> {
            listView.getItems().setAll(pagination.getCurrentPage());
            updatePaginationControls(pagination, pageLabel, previousButton, nextButton);
        };


        previousButton.setOnAction(event -> {
                    pagination.previousPage();
                    updatePage.run();
                }
        );


        nextButton.setOnAction(
                event -> {

                    pagination.nextPage();

                    updatePage.run();
                }
        );


        borrowButton.setOnAction(
                event -> {

                    borrowNewItem();

                    refreshBorrowedList(
                            pagination
                    );

                    updatePage.run();

                    updateBorrowCapacityLabel(
                            capacity
                    );

                    updateCapacity();
                }
        );


        extendButton.setOnAction(
                event -> {

                    Borrowed selected =
                            listView
                                    .getSelectionModel()
                                    .getSelectedItem();

                    extendBorrow(
                            selected
                    );

                    updatePage.run();
                }
        );


        returnButton.setOnAction(
                event -> {

                    Borrowed selected =
                            listView
                                    .getSelectionModel()
                                    .getSelectedItem();

                    returnBorrow(
                            selected
                    );

                    refreshBorrowedList(
                            pagination
                    );

                    updatePage.run();

                    updateBorrowCapacityLabel(
                            capacity
                    );

                    updateCapacity();
                }
        );


        HBox navigation =
                new HBox(
                        10,
                        previousButton,
                        pageLabel,
                        nextButton
                );

        navigation.setAlignment(
                Pos.CENTER
        );


        HBox actions =
                new HBox(
                        10,
                        borrowButton,
                        extendButton,
                        returnButton
                );

        actions.setAlignment(
                Pos.CENTER
        );


        content.getChildren().addAll(
                capacity,
                listView,
                navigation,
                actions
        );


        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(
                        ButtonType.CLOSE
                );


        updatePage.run();

        dialog.showAndWait();
    }


    private void refreshBorrowedList(
            Pagination<Borrowed> pagination
    ) {

        List<Borrowed> updatedList =
                currentUser
                        .getBorrowedList();

        pagination =
                new Pagination<>(
                        updatedList
                );
    }


    private void updateBorrowCapacityLabel(
            Label label
    ) {

        int capacity =
                currentUser.getBorrowLimit()
                        - currentUser.activeBorrows();

        label.setText(
                "Available Capacity: "
                        + capacity
        );
    }


    private void borrowNewItem() {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Borrow Item"
        );

        dialog.setHeaderText(
                "Enter item title"
        );

        dialog.setContentText(
                "Title:"
        );

        dialog.showAndWait()
                .ifPresent(title -> {

                    try {

                        LibraryItem item =
                                Catalog
                                        .getInstance()
                                        .getItem(title);

                        if (item == null) {

                            throw new IllegalArgumentException(
                                    "Item not found."
                            );
                        }

                        currentUser
                                .borrowItem(
                                        item
                                );

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Success",
                                "Item borrowed successfully."
                        );

                    } catch (RuntimeException e) {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Error",
                                e.getMessage()
                        );
                    }
                });
    }


    private void extendBorrow(
            Borrowed borrowed
    ) {

        if (borrowed == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select a borrow record."
            );

            return;
        }


        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Extend Borrow"
        );

        dialog.setHeaderText(
                "Enter number of days"
        );

        dialog.setContentText(
                "Days:"
        );


        dialog.showAndWait()
                .ifPresent(value -> {

                    try {

                        long days =
                                Long.parseLong(
                                        value
                                );

                        borrowed
                                .extendBorrowTime(
                                        days
                                );

                        showAlert(
                                Alert.AlertType.INFORMATION,
                                "Success",
                                "Borrow time extended successfully."
                        );

                    } catch (IllegalArgumentException e) {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Error",
                                e.getMessage()
                        );
                    }
                });
    }


    private void returnBorrow(
            Borrowed borrowed
    ) {

        if (borrowed == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select a borrow record."
            );

            return;
        }


        try {

            currentUser
                    .returnItem(
                            borrowed
                    );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Item returned successfully."
            );

        } catch (RuntimeException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }


// =========================================================
// Reservations
// =========================================================

    @FXML
    private void handleReservations(
            ActionEvent event
    ) {

        List<Reservation> reservations =
                currentUser
                        .getReservationById()
                        .values()
                        .stream()
                        .toList();

        showReservations(
                reservations
        );
    }


    private void showReservations(
            List<Reservation> reservations
    ) {

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle(
                "My Reservations"
        );

        VBox content =
                new VBox(10);

        content.setPadding(
                new Insets(15)
        );


        ListView<Reservation> listView =
                new ListView<>();


        Pagination<Reservation> pagination =
                new Pagination<>(
                        reservations
                );


        Label pageLabel =
                new Label();

        Button previousButton =
                new Button(
                        "Previous"
                );

        Button nextButton =
                new Button(
                        "Next"
                );

        Button borrowButton =
                new Button(
                        "Borrow Reservation"
                );

        Button cancelButton =
                new Button(
                        "Cancel Reservation"
                );


        Runnable updatePage =
                () -> {

                    listView.getItems()
                            .setAll(
                                    pagination
                                            .getCurrentPage()
                            );

                    updatePaginationControls(
                            pagination,
                            pageLabel,
                            previousButton,
                            nextButton
                    );
                };


        previousButton.setOnAction(
                event -> {

                    pagination.previousPage();

                    updatePage.run();
                }
        );


        nextButton.setOnAction(
                event -> {

                    pagination.nextPage();

                    updatePage.run();
                }
        );


        borrowButton.setOnAction(
                event -> {

                    Reservation selected =
                            listView
                                    .getSelectionModel()
                                    .getSelectedItem();

                    borrowReservation(
                            selected
                    );
                }
        );


        cancelButton.setOnAction(
                event -> {

                    Reservation selected =
                            listView
                                    .getSelectionModel()
                                    .getSelectedItem();

                    cancelReservation(
                            selected
                    );

                    updatePage.run();
                }
        );


        HBox navigation =
                new HBox(
                        10,
                        previousButton,
                        pageLabel,
                        nextButton
                );

        navigation.setAlignment(
                Pos.CENTER
        );


        HBox actions =
                new HBox(
                        10,
                        borrowButton,
                        cancelButton
                );

        actions.setAlignment(
                Pos.CENTER
        );


        content.getChildren().addAll(
                listView,
                navigation,
                actions
        );


        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(
                        ButtonType.CLOSE
                );


        updatePage.run();

        dialog.showAndWait();
    }


    private void borrowReservation(
            Reservation reservation
    ) {

        if (reservation == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select a reservation."
            );

            return;
        }


        try {

            currentUser
                    .borrowItem(
                            reservation.getItem()
                    );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Reservation borrowed successfully."
            );

        } catch (RuntimeException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }


    private void cancelReservation(
            Reservation reservation
    ) {

        if (reservation == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select a reservation."
            );

            return;
        }


        try {

            reservation
                    .cancelReservation();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Reservation canceled successfully."
            );

        } catch (RuntimeException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }


// =========================================================
// Fines
// =========================================================

    @FXML
    private void handleFines(
            ActionEvent event
    ) {

        showFines();
    }


    private void showFines() {

        List<Fine> fines =
                currentUser
                        .getFines();


        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle(
                "My Fines"
        );


        VBox content =
                new VBox(10);

        content.setPadding(
                new Insets(15)
        );


        ListView<Fine> listView =
                new ListView<>();


        Pagination<Fine> pagination =
                new Pagination<>(
                        fines
                );


        Label pageLabel =
                new Label();


        Button previousButton =
                new Button(
                        "Previous"
                );

        Button nextButton =
                new Button(
                        "Next"
                );

        Button payButton =
                new Button(
                        "Pay Fine"
                );


        Runnable updatePage =
                () -> {

                    listView.getItems()
                            .setAll(
                                    pagination
                                            .getCurrentPage()
                            );

                    updatePaginationControls(
                            pagination,
                            pageLabel,
                            previousButton,
                            nextButton
                    );
                };


        previousButton.setOnAction(
                event -> {

                    pagination.previousPage();

                    updatePage.run();
                }
        );


        nextButton.setOnAction(
                event -> {

                    pagination.nextPage();

                    updatePage.run();
                }
        );


        payButton.setOnAction(
                event -> {

                    Fine selected =
                            listView
                                    .getSelectionModel()
                                    .getSelectedItem();

                    payFine(
                            selected
                    );

                    updatePage.run();
                }
        );


        HBox navigation =
                new HBox(
                        10,
                        previousButton,
                        pageLabel,
                        nextButton
                );

        navigation.setAlignment(
                Pos.CENTER
        );


        content.getChildren().addAll(
                listView,
                navigation,
                payButton
        );


        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(
                        ButtonType.CLOSE
                );


        updatePage.run();

        dialog.showAndWait();
    }


    private void payFine(
            Fine fine
    ) {

        if (fine == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select a fine."
            );

            return;
        }


        try {

            PaymentService
                    .payFine(
                            fine,
                            currentUser
                    );

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Fine paid successfully."
            );

        } catch (RuntimeException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }


// =========================================================
// Generic Pagination
// =========================================================

    private void updatePaginationControls(
            Pagination<?> pagination,
            Label pageLabel,
            Button previousButton,
            Button nextButton
    ) {

        pageLabel.setText(
                "Page "
                        + pagination.getCurrentPageNumber()
                        + " / "
                        + pagination.gerTotalPageNumber()
        );

        previousButton.setDisable(
                !pagination.hasPreviousPage()
        );

        nextButton.setDisable(
                !pagination.hasNextPage()
        );
    }


    private void showPaginatedDialog(
            String title,
            List<?> items,
            Consumer<Object> action
    ) {

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle(
                title
        );


        VBox content =
                new VBox(10);

        content.setPadding(
                new Insets(15)
        );


        ListView<Object> listView =
                new ListView<>();


        Pagination<Object> pagination =
                new Pagination<>(
                        items.stream()
                                .map(item ->
                                        (Object) item)
                                .toList()
                );


        Label pageLabel =
                new Label();


        Button previousButton =
                new Button(
                        "Previous"
                );

        Button nextButton =
                new Button(
                        "Next"
                );


        Runnable updatePage =
                () -> {

                    listView.getItems()
                            .setAll(
                                    pagination
                                            .getCurrentPage()
                            );

                    updatePaginationControls(
                            pagination,
                            pageLabel,
                            previousButton,
                            nextButton
                    );
                };


        previousButton.setOnAction(
                event -> {

                    pagination.previousPage();

                    updatePage.run();
                }
        );


        nextButton.setOnAction(
                event -> {

                    pagination.nextPage();

                    updatePage.run();
                }
        );


        HBox navigation =
                new HBox(
                        10,
                        previousButton,
                        pageLabel,
                        nextButton
                );

        navigation.setAlignment(
                Pos.CENTER
        );


        content.getChildren().addAll(
                listView,
                navigation
        );


        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(
                        ButtonType.CLOSE
                );


        updatePage.run();

        dialog.showAndWait();
    }


// =========================================================
// Settings / Support
// =========================================================

    @FXML
    private void handleSettings(ActionEvent event) {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Settings");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Button firstNameButton = new Button("Change First Name");
        Button lastNameButton = new Button("Change Last Name");
        Button emailButton = new Button("Change Email");
        Button phoneButton = new Button("Change Phone Number");
        Button passwordButton = new Button("Change Password");

        firstNameButton.setMaxWidth(Double.MAX_VALUE);
        lastNameButton.setMaxWidth(Double.MAX_VALUE);
        emailButton.setMaxWidth(Double.MAX_VALUE);
        phoneButton.setMaxWidth(Double.MAX_VALUE);
        passwordButton.setMaxWidth(Double.MAX_VALUE);

        firstNameButton.setOnAction(e -> {
            TextInputDialog inputDialog =
                    new TextInputDialog(currentUser.getFirstName());

            inputDialog.setTitle("Change First Name");
            inputDialog.setHeaderText("Enter your new first name");
            inputDialog.setContentText("First Name:");

            inputDialog.showAndWait().ifPresent(value -> {
                try {
                    currentUser.setFirstName(value);
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Data updated successfully."
                    );
                    updateUserInformation();
                } catch (IllegalArgumentException ex) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            ex.getMessage()
                    );
                }
            });
        });

        lastNameButton.setOnAction(e -> {
            TextInputDialog inputDialog =
                    new TextInputDialog(currentUser.getLastName());

            inputDialog.setTitle("Change Last Name");
            inputDialog.setHeaderText("Enter your new last name");
            inputDialog.setContentText("Last Name:");

            inputDialog.showAndWait().ifPresent(value -> {
                try {
                    currentUser.setLastName(value);
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Data updated successfully."
                    );
                    updateUserInformation();
                } catch (IllegalArgumentException ex) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            ex.getMessage()
                    );
                }
            });
        });

        emailButton.setOnAction(e -> {
            TextInputDialog inputDialog =
                    new TextInputDialog(currentUser.getEmail());

            inputDialog.setTitle("Change Email");
            inputDialog.setHeaderText("Enter your new email");
            inputDialog.setContentText("Email:");

            inputDialog.showAndWait().ifPresent(value -> {
                try {
                    currentUser.setEmail(value);
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Data updated successfully."
                    );
                    updateUserInformation();
                } catch (IllegalArgumentException ex) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            ex.getMessage()
                    );
                }
            });
        });

        phoneButton.setOnAction(e -> {
            TextInputDialog inputDialog =
                    new TextInputDialog(currentUser.getPhoneNum());

            inputDialog.setTitle("Change Phone Number");
            inputDialog.setHeaderText("Enter your new phone number");
            inputDialog.setContentText("Phone Number:");

            inputDialog.showAndWait().ifPresent(value -> {
                try {
                    currentUser.setPhoneNum(value);
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Data updated successfully."
                    );
                    updateUserInformation();
                } catch (IllegalArgumentException ex) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            ex.getMessage()
                    );
                }
            });
        });

        passwordButton.setOnAction(e -> {
            TextInputDialog inputDialog =
                    new TextInputDialog();

            inputDialog.setTitle("Change Password");
            inputDialog.setHeaderText("Enter your new password");
            inputDialog.setContentText("Password:");

            inputDialog.showAndWait().ifPresent(value -> {
                try {
                    currentUser.setPassword(value);
                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Data updated successfully."
                    );
                } catch (IllegalArgumentException ex) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            ex.getMessage()
                    );
                }
            });
        });

        content.getChildren().addAll(
                firstNameButton,
                lastNameButton,
                emailButton,
                phoneButton,
                passwordButton
        );

        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


    @FXML
    private void handleSupport(ActionEvent event) {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Support");
        dialog.setHeaderText("Submit a Support Request");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        ComboBox<Department> departmentComboBox =
                new ComboBox<>();

        departmentComboBox.getItems().addAll(
                Department.REPORT_PROBLEM,
                Department.REQUEST_ITEM,
                Department.FINANCIAL_AFFAIRS,
                Department.RESERVE_ITEM
        );

        departmentComboBox.setPromptText("Select ticket type");

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Enter your message...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(5);

        Button submitButton = new Button("Submit Request");

        submitButton.setOnAction(e -> {

            Department type =
                    departmentComboBox.getValue();

            String message =
                    messageArea.getText().trim();

            if (type == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Input",
                        "Please select a ticket type."
                );
                return;
            }

            if (message.isEmpty()) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Input",
                        "Please enter a message."
                );
                return;
            }

            try {

                currentUser.requestSupport(
                        message,
                        type
                );

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Support request submitted successfully."
                );

                dialog.close();

            } catch (RuntimeException ex) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        ex.getMessage()
                );
            }
        });

        content.getChildren().addAll(
                new Label("Ticket Type:"),
                departmentComboBox,
                new Label("Message:"),
                messageArea,
                submitButton
        );

        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CLOSE);

        dialog.showAndWait();
    }


    @FXML
    private void handleSupportTickets(ActionEvent event) {

        List<SupportTicket> tickets =
                currentUser.getTicketList();

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("My Support Tickets");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        ListView<SupportTicket> listView =
                new ListView<>();

        Pagination<SupportTicket> pagination =
                new Pagination<>(tickets);

        Label pageLabel = new Label();

        Button previousButton =
                new Button("Previous");

        Button nextButton =
                new Button("Next");

        Runnable updatePage = () -> {

            listView.getItems().setAll(
                    pagination.getCurrentPage()
            );

            updatePaginationControls(
                    pagination,
                    pageLabel,
                    previousButton,
                    nextButton
            );
        };

        previousButton.setOnAction(e -> {

            pagination.previousPage();

            updatePage.run();
        });

        nextButton.setOnAction(e -> {

            pagination.nextPage();

            updatePage.run();
        });

        HBox navigation =
                new HBox(
                        10,
                        previousButton,
                        pageLabel,
                        nextButton
                );

        navigation.setAlignment(
                Pos.CENTER
        );

        content.getChildren().addAll(
                listView,
                navigation
        );

        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CLOSE);

        updatePage.run();

        dialog.showAndWait();
    }


// =========================================================
// Browse Library
// =========================================================

    @FXML
    private void handleBrowseLibrary(ActionEvent event) {
        showLibraryItems(Catalog.getInstance().getItems());
    }


    private void showLibraryItems(List<LibraryItem> items) {

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Library Items");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        // =====================================================
        // Search Field
        // =====================================================

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title...");

        // =====================================================
        // ListView
        // =====================================================

        ListView<LibraryItem> listView = new ListView<>();

        // =====================================================
        // Pagination
        // =====================================================

        Pagination<LibraryItem> pagination =
                new Pagination<>(items);

        Label pageLabel = new Label();

        Button previousButton = new Button("Previous");
        Button nextButton = new Button("Next");

        // =====================================================
        // Filter Buttons
        // =====================================================

        Button categoryFilterButton =
                new Button("Filter by Category");

        Button yearFilterButton =
                new Button("Filter by Year");

        Button clearFilterButton =
                new Button("Show All");

        // =====================================================
        // Action Buttons
        // =====================================================

        Button borrowButton =
                new Button("Borrow");

        Button reserveButton =
                new Button("Reserve");


        // =====================================================
        // Update Page
        // =====================================================

        Runnable updatePage = () -> {

            listView.getItems().setAll(
                    pagination.getCurrentPage()
            );

            updatePaginationControls(
                    pagination,
                    pageLabel,
                    previousButton,
                    nextButton
            );
        };


        // =====================================================
        // Previous Page
        // =====================================================

        previousButton.setOnAction(event -> {

            pagination.previousPage();

            updatePage.run();
        });


        // =====================================================
        // Next Page
        // =====================================================

        nextButton.setOnAction(event -> {

            pagination.nextPage();

            updatePage.run();
        });


        // =====================================================
        // Search
        // =====================================================

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    searchLibraryItems(
                            newValue,
                            pagination,
                            updatePage
                    );
                }
        );


        // =====================================================
        // Filter By Category
        // =====================================================

        categoryFilterButton.setOnAction(event -> {

            filterLibraryByCategory(
                    pagination,
                    updatePage
            );
        });


        // =====================================================
        // Filter By Year
        // =====================================================

        yearFilterButton.setOnAction(event -> {

            filterLibraryByYear(
                    pagination,
                    updatePage
            );
        });


        // =====================================================
        // Show All
        // =====================================================

        clearFilterButton.setOnAction(event -> {

            // Invalidate old search tasks
            searchVersion.incrementAndGet();

            // Clear search field
            searchField.clear();

            // Load all library items
            List<LibraryItem> allItems =
                    Catalog.getInstance().getItems();

            pagination.setItems(allItems);

            // Refresh ListView
            updatePage.run();
        });


        // =====================================================
        // Borrow Selected Item
        // =====================================================

        borrowButton.setOnAction(event -> {

            LibraryItem selectedItem =
                    listView
                            .getSelectionModel()
                            .getSelectedItem();

            borrowLibraryItem(selectedItem);
        });


        // =====================================================
        // Reserve Selected Item
        // =====================================================

        reserveButton.setOnAction(event -> {

            LibraryItem selectedItem =
                    listView
                            .getSelectionModel()
                            .getSelectedItem();

            reserveLibraryItem(selectedItem);
        });


        // =====================================================
        // Navigation
        // =====================================================

        HBox navigation =
                new HBox(
                        10,
                        previousButton,
                        pageLabel,
                        nextButton
                );

        navigation.setAlignment(Pos.CENTER);


        // =====================================================
        // Filters
        // =====================================================

        HBox filters =
                new HBox(
                        10,
                        categoryFilterButton,
                        yearFilterButton,
                        clearFilterButton
                );

        filters.setAlignment(Pos.CENTER);


        // =====================================================
        // Actions
        // =====================================================

        HBox actions =
                new HBox(
                        10,
                        borrowButton,
                        reserveButton
                );

        actions.setAlignment(Pos.CENTER);


        // =====================================================
        // Add Components
        // =====================================================

        content.getChildren().addAll(
                searchField,
                listView,
                navigation,
                filters,
                actions
        );


        // =====================================================
        // Dialog
        // =====================================================

        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CLOSE);


        // =====================================================
        // Display First Page
        // =====================================================

        updatePage.run();

        dialog.showAndWait();
    }


// =========================================================
// Filter By Category
// =========================================================

    private void filterLibraryByCategory(
            Pagination<LibraryItem> pagination,
            Runnable updatePage
    ) {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle("Filter by Category");

        dialog.setHeaderText(
                "Enter category"
        );

        dialog.setContentText(
                "Category:"
        );


        dialog.showAndWait()
                .ifPresent(category -> {

                    String normalizedCategory =
                            category.trim();

                    if (normalizedCategory.isEmpty()) {

                        showAlert(
                                Alert.AlertType.WARNING,
                                "Invalid Input",
                                "Please enter a category."
                        );

                        return;
                    }


                    runInBackground(

                            () ->
                                    Catalog
                                            .getInstance()
                                            .filteredSearch(
                                                    item ->
                                                            item.getCategory()
                                                                    != null
                                                                    &&
                                                                    item.getCategory()
                                                                            .trim()
                                                                            .equalsIgnoreCase(
                                                                                    normalizedCategory
                                                                            )
                                            ),

                            result -> {

                                searchVersion.incrementAndGet();

                                pagination.setItems(result);

                                updatePage.run();


                                if (result.isEmpty()) {

                                    showAlert(
                                            Alert.AlertType.INFORMATION,
                                            "No Results",
                                            "No library items were found for this category."
                                    );
                                }
                            }
                    );
                });
    }


// =========================================================
// Filter By Year
// =========================================================

    private void filterLibraryByYear(
            Pagination<LibraryItem> pagination,
            Runnable updatePage
    ) {

        Dialog<String> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Filter by Publish Year"
        );


        TextField fromField =
                new TextField();

        fromField.setPromptText(
                "From year"
        );


        TextField toField =
                new TextField();

        toField.setPromptText(
                "To year"
        );


        VBox content =
                new VBox(
                        10,
                        fromField,
                        toField
                );

        content.setPadding(
                new Insets(15)
        );


        dialog.getDialogPane()
                .setContent(content);


        ButtonType filterButton =
                new ButtonType(
                        "Filter",
                        ButtonBar.ButtonData.OK_DONE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        filterButton,
                        ButtonType.CANCEL
                );


        dialog.setResultConverter(button -> {

            if (button == filterButton) {

                return fromField.getText()
                        + ":"
                        + toField.getText();
            }

            return null;
        });


        dialog.showAndWait()
                .ifPresent(range -> {

                    try {

                        String[] values =
                                range.split(":");


                        int from =
                                Integer.parseInt(
                                        values[0].trim()
                                );


                        int to =
                                Integer.parseInt(
                                        values[1].trim()
                                );


                        if (from > to) {

                            throw new IllegalArgumentException(
                                    "From year must be less than or equal to To year."
                            );
                        }


                        runInBackground(

                                () ->
                                        Catalog
                                                .getInstance()
                                                .filteredSearch(
                                                        item ->
                                                                item.getPublishYear()
                                                                        >= from
                                                                        &&
                                                                        item.getPublishYear()
                                                                                <= to
                                                ),

                                result -> {

                                    searchVersion.incrementAndGet();

                                    pagination.setItems(result);

                                    updatePage.run();


                                    if (result.isEmpty()) {

                                        showAlert(
                                                Alert.AlertType.INFORMATION,
                                                "No Results",
                                                "No library items were found in the selected year range."
                                        );
                                    }
                                }
                        );


                    } catch (NumberFormatException e) {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Invalid Input",
                                "Please enter valid years."
                        );


                    } catch (IllegalArgumentException e) {

                        showAlert(
                                Alert.AlertType.ERROR,
                                "Invalid Range",
                                e.getMessage()
                        );
                    }
                });
    }


// =========================================================
// Search Library Items
// =========================================================

    private void searchLibraryItems(
            String searchText,
            Pagination<LibraryItem> pagination,
            Runnable updatePage
    ) {

        // Create a unique ID for this search
        long currentSearchVersion =
                searchVersion.incrementAndGet();


        String query =
                searchText
                        .trim()
                        .toLowerCase();


        Task<List<LibraryItem>> task =
                new Task<>() {

                    @Override
                    protected List<LibraryItem> call() {

                        return Catalog
                                .getInstance()
                                .getItems()
                                .stream()
                                .filter(
                                        item ->
                                                query.isEmpty()
                                                        ||
                                                        (
                                                                item.getTitle() != null
                                                                        &&
                                                                        item.getTitle()
                                                                                .toLowerCase()
                                                                                .contains(query)
                                                        )
                                )
                                .toList();
                    }
                };


        // =====================================================
        // Search Completed
        // =====================================================

        task.setOnSucceeded(event -> {

            // Ignore old search results
            if (currentSearchVersion
                    != searchVersion.get()) {

                return;
            }


            List<LibraryItem> result =
                    task.getValue();


            // Update Pagination
            pagination.setItems(result);


            // Refresh ListView
            updatePage.run();
        });


        // =====================================================
        // Search Failed
        // =====================================================

        task.setOnFailed(event -> {

            if (currentSearchVersion
                    != searchVersion.get()) {

                return;
            }


            Throwable exception =
                    task.getException();


            showAlert(
                    Alert.AlertType.ERROR,
                    "Search Error",
                    exception == null
                            ? "Unknown search error."
                            : exception.getMessage()
            );
        });


        // =====================================================
        // Start Background Thread
        // =====================================================

        Thread searchThread =
                new Thread(task);


        searchThread.setDaemon(true);

        searchThread.start();
    }


    private void borrowLibraryItem(
            LibraryItem item
    ) {


        if (item == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select an item."
            );

            return;
        }


        try {

            currentUser.borrowItem(
                    item
            );


            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Item borrowed successfully."
            );


        } catch (
                RuntimeException e
        ) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Cannot Borrow Item",
                    e.getMessage()
            );
        }


    }

    private void reserveLibraryItem(
            LibraryItem item
    ) {


        if (item == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please select an item."
            );

            return;
        }


        try {

            currentUser.reserveItem(
                    item
            );


            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Item reserved successfully."
            );


        } catch (
                RuntimeException e
        ) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Cannot Reserve Item",
                    e.getMessage()
            );
        }


    }


// =========================================================
// Logout
// =========================================================

    @FXML
    private void handleLogout(
            ActionEvent event
    ) {

        try {

            Parent root =
                    javafx.fxml.FXMLLoader
                            .load(
                                    getClass()
                                            .getResource(
                                                    "/ir/ac/kntu/Javafx/MainMenu.fxml"
                                            )
                            );


            Stage stage =
                    (Stage)
                            ((Node) event
                                    .getSource())
                                    .getScene()
                                    .getWindow();


            stage.setScene(
                    new Scene(root)
            );

            stage.show();


        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Could not logout."
            );
        }
    }


// =========================================================
// Background Task
// =========================================================


    private <T> void runInBackground(
            BackgroundOperation<T> operation,
            BackgroundResult<T> resultHandler
    ) {


        Task<T> task =
                new Task<>() {

                    @Override
                    protected T call()
                            throws Exception {

                        return operation.run();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    resultHandler.accept(
                            task.getValue()
                    );
                }
        );


        task.setOnFailed(
                event -> {

                    Throwable exception =
                            task.getException();


                    Platform.runLater(
                            () -> {

                                showAlert(
                                        Alert.AlertType.ERROR,
                                        "Operation Error",
                                        exception == null
                                                ? "Operation failed."
                                                : exception.getMessage()
                                );
                            }
                    );
                }
        );


        Thread thread =
                new Thread(
                        task
                );


        thread.setDaemon(
                true
        );


        thread.start();


    }

    @FunctionalInterface
    private interface BackgroundOperation<T> {


        T run()
                throws Exception;


    }

    @FunctionalInterface
    private interface BackgroundResult<T> {


        void accept(
                T result
        );


    }


// =========================================================
// Alert
// =========================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        type
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


}
