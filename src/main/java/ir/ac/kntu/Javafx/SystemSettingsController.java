
package ir.ac.kntu.Javafx;

import ir.ac.kntu.modules.Admin;
import ir.ac.kntu.util.SystemProperties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SystemSettingsController {

    @FXML
    private TextField borrowTimeField;

    @FXML
    private TextField reserveLimitField;

    @FXML
    private TextField reserveExpireDaysField;

    @FXML
    private TextField baseFineField;

    @FXML
    private TextField dailyFineField;


    private Admin admin;


    @FXML
    private void initialize() {

        loadCurrentValues();

    }


    public void setAdmin(Admin admin) {

        this.admin = admin;

        loadCurrentValues();

    }


    private void loadCurrentValues() {

        if (borrowTimeField == null) {
            return;
        }

        SystemProperties properties =
                SystemProperties.getInstance();


        borrowTimeField.setText(
                String.valueOf(
                        properties.getBaseBorrowTime()
                )
        );


        reserveLimitField.setText(
                String.valueOf(
                        properties.getReserveLimit()
                )
        );


        reserveExpireDaysField.setText(
                String.valueOf(
                        properties.getReserveExpireDays()
                )
        );


        baseFineField.setText(
                String.valueOf(
                        properties.getBaseFineRate()
                )
        );


        dailyFineField.setText(
                String.valueOf(
                        properties.getDailyFineRate()
                )
        );

    }


    @FXML
    private void handleUpdateBorrowTime() {

        try {

            int value =
                    Integer.parseInt(
                            borrowTimeField
                                    .getText()
                                    .trim()
                    );


            SystemProperties
                    .getInstance()
                    .setBaseBorrowTime(
                            admin,
                            value
                    );


            showMessage(
                    "Success",
                    "Borrow Time updated successfully."
            );


        } catch (Exception e) {

            showError(e.getMessage());

        }

    }


    @FXML
    private void handleUpdateReserveLimit() {

        try {

            int value =
                    Integer.parseInt(
                            reserveLimitField
                                    .getText()
                                    .trim()
                    );


            SystemProperties
                    .getInstance()
                    .setReserveLimit(
                            admin,
                            value
                    );


            showMessage(
                    "Success",
                    "Reserve Limit updated successfully."
            );


        } catch (Exception e) {

            showError(e.getMessage());

        }

    }


    @FXML
    private void handleUpdateReserveExpireDays() {

        try {

            int value =
                    Integer.parseInt(
                            reserveExpireDaysField
                                    .getText()
                                    .trim()
                    );


            SystemProperties
                    .getInstance()
                    .setReserveExpireDays(
                            admin,
                            value
                    );


            showMessage(
                    "Success",
                    "Reserve Expire Days updated successfully."
            );


        } catch (Exception e) {

            showError(e.getMessage());

        }

    }


    @FXML
    private void handleUpdateBaseFine() {

        try {

            double value =
                    Double.parseDouble(
                            baseFineField
                                    .getText()
                                    .trim()
                    );


            SystemProperties
                    .getInstance()
                    .setBaseFineRate(
                            admin,
                            value
                    );


            showMessage(
                    "Success",
                    "Base Fine updated successfully."
            );


        } catch (Exception e) {

            showError(e.getMessage());

        }

    }


    @FXML
    private void handleUpdateDailyFine() {

        try {

            double value =
                    Double.parseDouble(
                            dailyFineField
                                    .getText()
                                    .trim()
                    );


            SystemProperties
                    .getInstance()
                    .setDailyFineRate(
                            admin,
                            value
                    );


            showMessage(
                    "Success",
                    "Daily Fine updated successfully."
            );


        } catch (Exception e) {

            showError(e.getMessage());

        }

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


            Parent root =
                    loader.load();


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

            showError(
                    "Could not return to Admin Dashboard."
            );

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


    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle("Error");

        alert.setHeaderText(null);

        alert.setContentText(
                message == null
                        ? "An error occurred."
                        : message
        );

        alert.showAndWait();

    }

}

