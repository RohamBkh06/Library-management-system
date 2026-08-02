package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Fine;
import ir.ac.kntu.modules.NormalUser;
import ir.ac.kntu.util.Pagination;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FineManagementController {


    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<DebtorRow> fineTable;

    @FXML
    private TableColumn<DebtorRow, String> userIdColumn;

    @FXML
    private TableColumn<DebtorRow, String> userNameColumn;

    @FXML
    private TableColumn<DebtorRow, Integer> fineCountColumn;

    @FXML
    private TableColumn<DebtorRow, Long> totalDebtColumn;

    @FXML
    private TableColumn<DebtorRow, LocalDate> lastFineDateColumn;

    @FXML
    private Label pageLabel;


    private List<DebtorRow> currentRows =
            new ArrayList<>();

    private Pagination<DebtorRow> pagination;

    private boolean descendingSort = true;


    @FXML
    private void initialize() {

        userIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("userId")
        );

        userNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("userName")
        );

        fineCountColumn.setCellValueFactory(
                new PropertyValueFactory<>("fineCount")
        );

        totalDebtColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalDebt")
        );

        lastFineDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastFineDate")
        );


        loadFines();
    }


    private void loadFines() {

        Thread thread = new Thread(() -> {

            try {

                List<DebtorRow> rows =
                        createDebtorRows(
                                null,
                                null,
                                true
                        );


                Platform.runLater(() ->
                        updateTable(rows)
                );

            } catch (Exception e) {

                Platform.runLater(() ->
                        showError(
                                "Error",
                                e.getMessage()
                        )
                );
            }
        });


        thread.setDaemon(true);
        thread.start();
    }


    private List<DebtorRow> createDebtorRows(
            LocalDate fromDate,
            LocalDate toDate,
            boolean descending) {

        List<NormalUser> users =
                new ArrayList<>(
                        LibraryManger
                                .getInstance()
                                .getUserById()
                                .values()
                );


        /*
         * Stream is used to process all users.
         *
         * Each user provides his unpaid fines.
         */
        return users.stream()

                /*
                 * Keep only users who have at least
                 * one unpaid fine.
                 */
                .filter(user ->
                        !user.getFines().isEmpty()
                )

                /*
                 * Convert each user to a DebtorRow.
                 */
                .map(user -> {

                    /*
                     * Filter fines according to
                     * selected date range.
                     */
                    List<Fine> filteredFines =
                            user.getFines()
                                    .stream()

                                    .filter(fine -> {

                                        LocalDate date =
                                                fine.getCreatedDate();

                                        if (fromDate != null &&
                                                date.isBefore(fromDate)) {

                                            return false;
                                        }

                                        if (toDate != null &&
                                                date.isAfter(toDate)) {

                                            return false;
                                        }

                                        return true;
                                    })

                                    .toList();


                    /*
                     * If all fines were filtered out,
                     * this user should not appear.
                     */
                    if (filteredFines.isEmpty()) {
                        return null;
                    }


                    /*
                     * Calculate total debt using Stream.
                     */
                    long totalDebt =
                            (long) filteredFines.stream()

                                    .mapToDouble(
                                            Fine::getAmount
                                    )

                                    .sum();


                    /*
                     * Find latest fine date.
                     */
                    LocalDate lastFineDate =
                            filteredFines.stream()

                                    .map(
                                            Fine::getCreatedDate
                                    )

                                    .max(
                                            Comparator.naturalOrder()
                                    )

                                    .orElse(null);


                    return new DebtorRow(

                            user.getId(),

                            user.getFirstName()
                                    + " "
                                    + user.getLastName(),

                            filteredFines.size(),

                            totalDebt,

                            lastFineDate
                    );
                })

                /*
                 * Remove users that have no fines
                 * inside the selected date range.
                 */
                .filter(row ->
                        row != null
                )

                /*
                 * Sort users according to
                 * total debt.
                 */
                .sorted(
                        descending
                                ? Comparator.comparing(
                                DebtorRow::getTotalDebt
                        ).reversed()

                                : Comparator.comparing(
                                DebtorRow::getTotalDebt
                        )
                )

                .toList();
    }


    private void updateTable(
            List<DebtorRow> rows) {

        currentRows =
                new ArrayList<>(rows);

        pagination =
                new Pagination<>(currentRows);

        updateCurrentPage();
    }


    private void updateCurrentPage() {

        if (pagination == null) {
            return;
        }


        fineTable.setItems(
                FXCollections.observableArrayList(
                        pagination.getCurrentPage()
                )
        );


        pageLabel.setText(
                "Page "
                        + pagination.getCurrentPageNumber()
                        + " / "
                        + pagination.gerTotalPageNumber()
        );
    }


    @FXML
    private void handleFilter() {

        LocalDate from =
                fromDatePicker.getValue();

        LocalDate to =
                toDatePicker.getValue();


        if (from != null &&
                to != null &&
                from.isAfter(to)) {

            showError(
                    "Invalid Date Range",
                    "From date cannot be after To date."
            );

            return;
        }


        Thread thread = new Thread(() -> {

            List<DebtorRow> rows =
                    createDebtorRows(
                            from,
                            to,
                            descendingSort
                    );


            Platform.runLater(() ->
                    updateTable(rows)
            );
        });


        thread.setDaemon(true);
        thread.start();
    }


    @FXML
    private void handleClearFilter() {

        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);

        loadFines();
    }


    @FXML
    private void handleSortAscending() {

        descendingSort = false;

        sortCurrentData();
    }


    @FXML
    private void handleSortDescending() {

        descendingSort = true;

        sortCurrentData();
    }


    private void sortCurrentData() {

        Thread thread = new Thread(() -> {

            List<DebtorRow> sortedRows =
                    currentRows.stream()

                            .sorted(
                                    descendingSort
                                            ? Comparator
                                              .comparing(
                                                      DebtorRow
                                                      ::getTotalDebt
                                              )
                                              .reversed()

                                            : Comparator
                                              .comparing(
                                                      DebtorRow
                                                      ::getTotalDebt
                                              )
                            )

                            .toList();


            Platform.runLater(() ->
                    updateTable(sortedRows)
            );
        });


        thread.setDaemon(true);
        thread.start();
    }


    @FXML
    private void handleNextPage() {

        if (pagination != null &&
                pagination.hasNextPage()) {

            pagination.nextPage();

            updateCurrentPage();
        }
    }


    @FXML
    private void handlePreviousPage() {

        if (pagination != null &&
                pagination.hasPreviousPage()) {

            pagination.previousPage();

            updateCurrentPage();
        }
    }



    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ir/ac/kntu/Javafx/SupporterDashboard.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
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
