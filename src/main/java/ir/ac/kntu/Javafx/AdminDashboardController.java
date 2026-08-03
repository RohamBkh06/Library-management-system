package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.modules.Borrowed;
import ir.ac.kntu.modules.LibraryItem;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.modules.Transaction;
import ir.ac.kntu.modules.TransactionType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Label fineRevenueLabel;

    @FXML
    private ListView<String> popularItemsListView;

    @FXML
    private LineChart<String, Number> activityChart;


    private Admin admin;


    // =========================================================
    // SET ADMIN
    // =========================================================

    public void setAdmin(Admin admin) {

        this.admin = admin;

        if (admin != null) {

            welcomeLabel.setText(
                    "Welcome " +
                            admin.getFirstName() +
                            " " +
                            admin.getLastName()
            );

            loadDashboardStatistics();
        }
    }


    // =========================================================
    // DASHBOARD STATISTICS
    // =========================================================

    private void loadDashboardStatistics() {

        LibraryManger manager =
                LibraryManger.getInstance();


        // -----------------------------------------------------
        // TOTAL USERS
        // -----------------------------------------------------

        long totalUsers =
                manager
                        .getUserById()
                        .values()
                        .stream()
                        .count();


        totalUsersLabel.setText(
                String.valueOf(totalUsers)
        );


        // -----------------------------------------------------
        // TOTAL LIBRARY ITEMS
        // -----------------------------------------------------

        long totalItems =
                ir.ac.kntu.modules.Catalog
                        .getInstance()
                        .getItems()
                        .stream()
                        .count();


        totalItemsLabel.setText(
                String.valueOf(totalItems)
        );


        // -----------------------------------------------------
        // TOTAL FINE REVENUE
        // -----------------------------------------------------

        double totalFineRevenue =
                manager
                        .getUserById()
                        .values()
                        .stream()
                        .flatMap(user ->
                                user.getWallet()
                                        .getTransactions()
                                        .stream()
                        )
                        .filter(transaction ->
                                transaction.getType()
                                        == TransactionType.FINE_PAYMENT
                        )
                        .mapToDouble(
                                Transaction::getAmount
                        )
                        .sum();


        fineRevenueLabel.setText(
                String.format(
                        "%.2f",
                        totalFineRevenue
                )
        );


        // -----------------------------------------------------
        // MOST POPULAR ITEMS
        // -----------------------------------------------------

        Map<String, Long> popularItems =
                manager
                        .getUserById()
                        .values()
                        .stream()
                        .flatMap(user ->
                                user.getBorrowedList()
                                        .stream()
                        )
                        .collect(
                                Collectors.groupingBy(
                                        borrowed ->
                                                borrowed
                                                        .getItem()
                                                        .getTitle(),
                                        Collectors.counting()
                                )
                        );


        List<String> popularItemsList =
                popularItems
                        .entrySet()
                        .stream()
                        .sorted(
                                Map.Entry
                                        .<String, Long>
                                                comparingByValue()
                                        .reversed()
                        )
                        .limit(5)
                        .map(entry ->
                                entry.getKey() +
                                        " - " +
                                        entry.getValue() +
                                        " borrows"
                        )
                        .toList();


        popularItemsListView.setItems(
                FXCollections.observableArrayList(
                        popularItemsList
                )
        );


        // -----------------------------------------------------
        // SYSTEM ACTIVITY
        // -----------------------------------------------------

        updateActivityChart();
    }


    // =========================================================
    // ACTIVITY CHART
    // =========================================================

    private void updateActivityChart() {

        activityChart
                .getData()
                .clear();


        LibraryManger manager =
                LibraryManger.getInstance();


        LocalDate today =
                LocalDate.now();


        Map<LocalDate, Long> activity =
                manager
                        .getUserById()
                        .values()
                        .stream()
                        .flatMap(user ->
                                user.getBorrowedList()
                                        .stream()
                        )
                        .filter(borrowed ->
                                !borrowed
                                        .getBorrowDate()
                                        .isBefore(
                                                today.minusDays(6)
                                        )
                        )
                        .collect(
                                Collectors.groupingBy(
                                        Borrowed::getBorrowDate,
                                        Collectors.counting()
                                )
                        );


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        series.setName(
                "Borrow Activity"
        );


        IntStream
                .range(0, 7)
                .mapToObj(
                        today::minusDays
                )
                .sorted()
                .forEach(date -> {

                    long count =
                            activity.getOrDefault(
                                    date,
                                    0L
                            );


                    series
                            .getData()
                            .add(
                                    new XYChart.Data<>(
                                            date.toString(),
                                            count
                                    )
                            );
                });


        activityChart
                .getData()
                .add(series);
    }


    // =========================================================
    // ADMIN OPTIONS
    // =========================================================

    @FXML
    private void handleManageUsers(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AdminManageUsers.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            AdminManageUsersController controller =
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
                    "Could not open Manage Users page."
            );

            e.printStackTrace();

        }

    }


    @FXML
    private void handleManageSupporters(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AdminManageSupporters.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            AdminManageSupportersController controller =
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

            showMessage(
                    "Error",
                    "Could not open Manage Supporters page."
            );

            e.printStackTrace();

        }

    }


    @FXML
    private void handleManageAdmins() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AdminManageAdmins.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            AdminManageAdminsController controller =
                    loader.getController();


            controller.setAdmin(
                    admin
            );


            Stage stage =
                    (Stage) welcomeLabel
                            .getScene()
                            .getWindow();


            stage.setScene(
                    new Scene(root)
            );


            stage.show();


        } catch (IOException e) {

            showMessage(
                    "Error",
                    "Could not open Manage Admins page."
            );
        }

    }



    @FXML
    private void handleAllSystemUsers(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AllSystemUsers.fxml"
                            )
                    );

            Parent root = loader.load();

            AllSystemUsersController controller =
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
                    "Could not open All System Users page."
            );

            e.printStackTrace();
        }
    }


    @FXML
    private void handleSystemSettings(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/SystemSettings.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            SystemSettingsController controller =
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
                    "Could not open System Settings page."
            );

            e.printStackTrace();

        }

    }

    @FXML
    private void handleLogout(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/Login.fxml"
                            )
                    );

            Parent root = loader.load();

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
                    "Could not logout."
            );

            e.printStackTrace();

        }

    }


    // =========================================================
    // ALERT
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