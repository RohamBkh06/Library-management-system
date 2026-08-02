
package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.*;
import ir.ac.kntu.util.Pagination;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SupporterDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label openRequestsLabel;

    @FXML
    private Label handledTodayLabel;

    @FXML
    private LineChart<String, Number> weeklyRequestChart;

    @FXML
    private Button fineManagementButton;


    private Supporter supporter;


    public void setSupporter(Supporter supporter) {

        this.supporter = supporter;

        if (supporter != null) {

            welcomeLabel.setText(
                    "Welcome " +
                            supporter.getFirstName() +
                            " " +
                            supporter.getLastName()
            );

            loadDashboardStatistics();
        }
    }


    // =========================================================
    // DASHBOARD STATISTICS
    // =========================================================

    private void loadDashboardStatistics() {

        Task<DashboardStatistics> task =
                new Task<>() {

                    @Override
                    protected DashboardStatistics call() {

                        List<SupportTicket> tickets =
                                LibraryManger.getInstance()
                                        .getAllTickets();


                        long openRequests =
                                tickets.stream()
                                        .filter(ticket ->
                                                !ticket.isAnswered()
                                        )
                                        .filter(ticket ->
                                                supporter
                                                        .getDepartments()
                                                        .contains(
                                                                ticket.getDepartment()
                                                        )
                                        )
                                        .count();


                        LocalDate today =
                                LocalDate.now();


                        long handledToday =
                                tickets.stream()
                                        .filter(SupportTicket::isAnswered)
                                        .filter(ticket ->
                                                supporter
                                                        .getDepartments()
                                                        .contains(
                                                                ticket.getDepartment()
                                                        )
                                        )
                                        .filter(ticket ->
                                                today.equals(
                                                        ticket.getAnsweredDate()
                                                )
                                        )
                                        .count();


                        Map<LocalDate, Long> weeklyRequests =
                                tickets.stream()
                                        .filter(ticket ->
                                                supporter
                                                        .getDepartments()
                                                        .contains(
                                                                ticket.getDepartment()
                                                        )
                                        )
                                        .filter(ticket ->
                                                ticket.getCreatedDate() != null &&
                                                !ticket
                                                        .getCreatedDate()
                                                        .isBefore(
                                                                today.minusDays(6)
                                                        )
                                        )
                                        .collect(
                                                Collectors.groupingBy(
                                                        SupportTicket::getCreatedDate,
                                                        Collectors.counting()
                                                )
                                        );


                        return new DashboardStatistics(
                                openRequests,
                                handledToday,
                                weeklyRequests
                        );
                    }
                };


        task.setOnSucceeded(event -> {

            DashboardStatistics statistics =
                    task.getValue();


            openRequestsLabel.setText(
                    String.valueOf(
                            statistics.openRequests()
                    )
            );


            handledTodayLabel.setText(
                    String.valueOf(
                            statistics.handledToday()
                    )
            );


            updateWeeklyChart(
                    statistics.weeklyRequests()
            );
        });


        task.setOnFailed(event -> {

            Throwable exception =
                    task.getException();

            showError(
                    "Dashboard Error",
                    exception == null
                            ? "Could not load dashboard statistics."
                            : exception.getMessage()
            );
        });


        Thread statisticsThread =
                new Thread(task);

        statisticsThread.setDaemon(true);

        statisticsThread.start();
    }


    private void updateWeeklyChart(
            Map<LocalDate, Long> weeklyRequests) {

        weeklyRequestChart
                .getData()
                .clear();


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        series.setName("Requests");


        LocalDate today =
                LocalDate.now();


        IntStream.range(0, 7)
                .mapToObj(today::minusDays)
                .sorted()
                .forEach(date -> {

                    long count =
                            weeklyRequests.getOrDefault(
                                    date,
                                    0L
                            );

                    series.getData().add(
                            new XYChart.Data<>(
                                    date.toString(),
                                    count
                            )
                    );
                });


        weeklyRequestChart
                .getData()
                .add(series);
    }


    // =========================================================
    // USERS
    // =========================================================

    @FXML
    private void handleUsers() {

        List<NormalUser> users =
                LibraryManger
                        .getInstance()
                        .getUserById()
                        .values()
                        .stream()
                        .toList();


        if (users.isEmpty()) {

            showMessage(
                    "Users",
                    "No users found."
            );

            return;
        }


        Pagination<NormalUser> pagination =
                new Pagination<>(users);


        VBox content =
                new VBox(10);

        Label title =
                new Label("All Users");


        VBox userList =
                new VBox(8);


        Runnable refresh =
                () -> {

                    userList
                            .getChildren()
                            .clear();


                    pagination
                            .getCurrentPage()
                            .forEach(user -> {

                                Label userLabel =
                                        new Label(
                                                user.toString() +
                                                        "\nActive Borrows: " +
                                                        user.activeBorrows() +
                                                        " | Fines: " +
                                                        user.getFines().size()
                                        );

                                userLabel
                                        .setWrapText(true);

                                userList
                                        .getChildren()
                                        .add(userLabel);
                            });
                };


        Button previous =
                new Button("Previous");

        Button next =
                new Button("Next");

        Button close =
                new Button("Close");


        Label pageLabel =
                new Label();


        previous.setOnAction(event -> {

            pagination.previousPage();

            refresh.run();

            pageLabel.setText(
                    "Page " +
                            pagination.getCurrentPageNumber() +
                            "/" +
                            pagination.gerTotalPageNumber()
            );
        });


        next.setOnAction(event -> {

            pagination.nextPage();

            refresh.run();

            pageLabel.setText(
                    "Page " +
                            pagination.getCurrentPageNumber() +
                            "/" +
                            pagination.gerTotalPageNumber()
            );
        });


        close.setOnAction(event ->
                ((Stage) close
                        .getScene()
                        .getWindow())
                        .close()
        );


        refresh.run();


        pageLabel.setText(
                "Page " +
                        pagination.getCurrentPageNumber() +
                        "/" +
                        pagination.gerTotalPageNumber()
        );


        content
                .getChildren()
                .addAll(
                        title,
                        userList,
                        previous,
                        next,
                        pageLabel,
                        close
                );


        showWindow(
                "All Users",
                content,
                600,
                500
        );
    }


    // =========================================================
    // RECENT BORROWS
    // =========================================================

    @FXML
    private void handleRecentBorrows() {

        List<Borrowed> borrows =
                LibraryManger
                        .getInstance()
                        .getRecentBorrows();


        if (borrows.isEmpty()) {

            showMessage(
                    "Recent Borrows",
                    "No borrows found."
            );

            return;
        }


        Pagination<Borrowed> pagination =
                new Pagination<>(borrows);


        VBox content =
                new VBox(10);

        VBox borrowList =
                new VBox(8);

        Label pageLabel =
                new Label();


        Runnable refresh =
                () -> {

                    borrowList
                            .getChildren()
                            .clear();


                    pagination
                            .getCurrentPage()
                            .forEach(borrowed -> {

                                Label label =
                                        new Label(
                                                borrowed.toString()
                                        );

                                label.setWrapText(true);

                                borrowList
                                        .getChildren()
                                        .add(label);
                            });


                    pageLabel.setText(
                            "Page " +
                                    pagination.getCurrentPageNumber() +
                                    "/" +
                                    pagination.gerTotalPageNumber()
                    );
                };


        Button previous =
                new Button("Previous");

        Button next =
                new Button("Next");

        Button close =
                new Button("Close");


        previous.setOnAction(event -> {

            pagination.previousPage();

            refresh.run();
        });


        next.setOnAction(event -> {

            pagination.nextPage();

            refresh.run();
        });


        close.setOnAction(event ->
                ((Stage) close
                        .getScene()
                        .getWindow())
                        .close()
        );


        refresh.run();


        content
                .getChildren()
                .addAll(
                        new Label("Recent Borrows"),
                        borrowList,
                        previous,
                        next,
                        pageLabel,
                        close
                );


        showWindow(
                "Recent Borrows",
                content,
                600,
                500
        );
    }


    // =========================================================
    // FINE MANAGEMENT
    // =========================================================

    @FXML
    private void handleFineManagement() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/FineManagement.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            Stage stage =
                    (Stage) fineManagementButton
                            .getScene()
                            .getWindow();


            stage.setScene(
                    new Scene(root)
            );


            stage.setTitle(
                    "Fine Management"
            );


            stage.show();

        } catch (IOException e) {

            showError(
                    "Error",
                    "Could not open Fine Management."
            );
        }
    }


    // =========================================================
    // ADD ITEM
    // =========================================================

    @FXML
    private void handleAddItem() {


        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/AddItem.fxml"
                            )
                    );

            Parent root =
                    loader.load();


            AddItemController controller =
                    loader.getController();

            controller.setSupporter(
                    supporter
            );


            Stage stage =
                    (Stage) fineManagementButton
                            .getScene()
                            .getWindow();


            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(
                    "Add Library Item"
            );

            stage.show();

        } catch (IOException e) {

            showError(
                    "Error",
                    "Could not open Add Item page."
            );
        }

    }



    // =========================================================
    // SUPPORT REQUESTS
    // =========================================================

    @FXML
    private void handleSupportRequests() {


        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/SupportRequests.fxml"
                            )
                    );

            Parent root =
                    loader.load();


            SupportRequestsController controller =
                    loader.getController();

            controller.setSupporter(
                    supporter
            );


            Stage stage =
                    (Stage) fineManagementButton
                            .getScene()
                            .getWindow();


            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(
                    "Support Requests"
            );

            stage.show();

        } catch (IOException e) {

            showError(
                    "Error",
                    "Could not open Support Requests."
            );
        }

    }



    // =========================================================
    // LOGOUT
    // =========================================================

    @FXML
    private void handleLogout() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/MainMenu.fxml"
                            )
                    );


            Parent root =
                    loader.load();


            Stage stage =
                    (Stage) welcomeLabel
                            .getScene()
                            .getWindow();


            stage.setScene(
                    new Scene(root)
            );


            stage.setTitle(
                    "Library Management System"
            );


            stage.show();

        } catch (IOException e) {

            showError(
                    "Error",
                    "Could not return to main menu."
            );
        }
    }


    // =========================================================
    // WINDOW HELPER
    // =========================================================

    private void showWindow(
            String title,
            Parent root,
            double width,
            double height) {

        Stage stage =
                new Stage();

        stage.setTitle(title);

        stage.setScene(
                new Scene(
                        root,
                        width,
                        height
                )
        );

        stage.show();
    }


    private void showWindow(
            String title,
            VBox root,
            double width,
            double height) {

        Stage stage =
                new Stage();

        stage.setTitle(title);

        stage.setScene(
                new Scene(
                        root,
                        width,
                        height
                )
        );

        stage.show();
    }


    // =========================================================
    // ALERTS
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

        Platform.runLater(() -> {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setTitle(title);

            alert.setHeaderText(null);

            alert.setContentText(message);

            alert.showAndWait();
        });
    }


    // =========================================================
    // DASHBOARD DATA
    // =========================================================

    private record DashboardStatistics(
            long openRequests,
            long handledToday,
            Map<LocalDate, Long> weeklyRequests
    ) {
    }
}

