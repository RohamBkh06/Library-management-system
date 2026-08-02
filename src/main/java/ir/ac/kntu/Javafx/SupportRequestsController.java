package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.Department;
import ir.ac.kntu.modules.SupportTicket;
import ir.ac.kntu.modules.Supporter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SupportRequestsController {


    @FXML
    private Label departmentLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<SupportTicket> ticketTable;

    @FXML
    private TableColumn<SupportTicket, String> idColumn;

    @FXML
    private TableColumn<SupportTicket, Department> departmentColumn;

    @FXML
    private TableColumn<SupportTicket, String> messageColumn;

    @FXML
    private TableColumn<SupportTicket, String> statusColumn;

    @FXML
    private TextArea answerTextArea;

    @FXML
    private Button answerButton;

    private Supporter supporter;


    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        departmentColumn.setCellValueFactory(
                new PropertyValueFactory<>("department")
        );

        messageColumn.setCellValueFactory(
                new PropertyValueFactory<>("message")
        );

        statusColumn.setCellValueFactory(
                cellData -> {

                    SupportTicket ticket =
                            cellData.getValue();

                    String status =
                            ticket.isAnswered()
                                    ? "Answered"
                                    : "Open";

                    return new javafx.beans.property.SimpleStringProperty(
                            status
                    );
                }
        );

        ticketTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue == null) {
                                statusLabel.setText(
                                        "Select a request to answer."
                                );
                                return;
                            }

                            if (newValue.isAnswered()) {

                                statusLabel.setText(
                                        "This request has already been answered."
                                );

                            } else {

                                statusLabel.setText(
                                        "Selected request: "
                                                + newValue.getId()
                                );
                            }
                        }
                );
    }


    public void setSupporter(Supporter supporter) {

        this.supporter = supporter;

        if (supporter == null) {
            return;
        }

        String departments =
                supporter.getDepartments()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.joining(", "));

        departmentLabel.setText(
                "Your Departments: " + departments
        );

        loadTickets();
    }


    private void loadTickets() {

        Task<List<SupportTicket>> task =
                new Task<>() {

                    @Override
                    protected List<SupportTicket> call() {

                        return LibraryManger
                                .getInstance()
                                .getAllTickets()
                                .stream()
                                .filter(ticket ->
                                        supporter
                                                .getDepartments()
                                                .contains(
                                                        ticket.getDepartment()
                                                )
                                )
                                .collect(
                                        Collectors.toList()
                                );
                    }
                };


        task.setOnSucceeded(event -> {

            ticketTable
                    .getItems()
                    .setAll(task.getValue());

            statusLabel.setText(
                    "Showing "
                            + task.getValue().size()
                            + " support request(s)."
            );
        });


        task.setOnFailed(event -> {

            Throwable exception =
                    task.getException();

            showError(
                    "Error",
                    exception == null
                            ? "Could not load support requests."
                            : exception.getMessage()
            );
        });


        Thread thread =
                new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }


    @FXML
    private void handleAnswer() {

        SupportTicket selectedTicket =
                ticketTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedTicket == null) {

            showError(
                    "No Request Selected",
                    "Please select a support request first."
            );

            return;
        }


        if (selectedTicket.isAnswered()) {

            showError(
                    "Already Answered",
                    "This support request has already been answered."
            );

            return;
        }


        String answer =
                answerTextArea
                        .getText()
                        .trim();


        if (answer.isBlank()) {

            showError(
                    "Invalid Answer",
                    "Please enter an answer."
            );

            return;
        }


        answerButton.setDisable(true);

        statusLabel.setText(
                "Processing answer..."
        );


        Task<Void> task =
                new Task<>() {

                    @Override
                    protected Void call() {

                        supporter.answer(
                                selectedTicket,
                                answer
                        );

                        return null;
                    }
                };


        task.setOnSucceeded(event -> {

            answerTextArea.clear();

            answerButton.setDisable(false);

            statusLabel.setText(
                    "Ticket answered successfully."
            );

            loadTickets();
        });


        task.setOnFailed(event -> {

            answerButton.setDisable(false);

            Throwable exception =
                    task.getException();

            showError(
                    "Error",
                    exception == null
                            ? "Could not answer the request."
                            : exception.getMessage()
            );
        });


        Thread thread =
                new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }


    @FXML
    private void handleRefresh() {

        if (supporter != null) {
            loadTickets();
        }
    }


    @FXML
    private void handleBack() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/ir/ac/kntu/Javafx/SupporterDashboard.fxml"
                            )
                    );

            Parent root =
                    loader.load();


            SupporterDashboardController controller =
                    loader.getController();

            controller.setSupporter(
                    supporter
            );


            Stage stage =
                    (Stage) ticketTable
                            .getScene()
                            .getWindow();


            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(
                    "Supporter Dashboard"
            );

            stage.show();

        } catch (IOException e) {

            showError(
                    "Error",
                    "Could not return to Supporter Dashboard."
            );
        }
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


}
