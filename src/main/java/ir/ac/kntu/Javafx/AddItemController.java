package ir.ac.kntu.Javafx;

import ir.ac.kntu.main.LibraryManger;
import ir.ac.kntu.modules.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddItemController {


    @FXML
    private ComboBox<String> itemTypeComboBox;

    @FXML
    private GridPane formGrid;

    @FXML
    private Button addButton;

    @FXML
    private Label statusLabel;


    private Supporter supporter;

    private final List<TextField> fields =
            new ArrayList<>();


    @FXML
    public void initialize() {

        itemTypeComboBox.getItems().addAll(
                "Book",
                "Magazine",
                "Ebook",
                "AudioBook"
        );
    }


    public void setSupporter(Supporter supporter) {

        this.supporter = supporter;
    }


    @FXML
    private void handleItemTypeChanged() {

        String selectedType =
                itemTypeComboBox.getValue();

        if (selectedType == null) {
            return;
        }

        clearForm();

        switch (selectedType) {

            case "Book" ->
                    createBookForm();

            case "Magazine" ->
                    createMagazineForm();

            case "Ebook" ->
                    createEbookForm();

            case "AudioBook" ->
                    createAudioBookForm();
        }

        addButton.setDisable(false);

        statusLabel.setText(
                "Enter the information for the selected item."
        );
    }


    private void createBookForm() {

        addField("Title");
        addField("ID");
        addField("Publish Year");
        addField("Category");
        addField("Author");
        addField("Page Count");
        addField("Available Copies");
        addField("ISBN");
    }


    private void createMagazineForm() {

        addField("Title");
        addField("ID");
        addField("Publish Year");
        addField("Category");
        addField("Available Copies");
        addField("Publication Frequency");
        addField("ISSN");
    }


    private void createEbookForm() {

        addField("Title");
        addField("ID");
        addField("Publish Year");
        addField("Category");
        addField("Digital Format");
        addField("Size");
        addField("Download URL");
        addField("Page Count");
    }


    private void createAudioBookForm() {

        addField("Title");
        addField("ID");
        addField("Publish Year");
        addField("Category");
        addField("Digital Format");
        addField("Size");
        addField("Download URL");
        addField("Page Count");
    }


    private void addField(String labelText) {

        int row =
                fields.size();

        Label label =
                new Label(labelText + ":");

        TextField field =
                new TextField();

        field.setPromptText(
                "Enter " + labelText
        );

        formGrid.add(
                label,
                0,
                row
        );

        formGrid.add(
                field,
                1,
                row
        );

        fields.add(field);
    }


    @FXML
    private void handleAddItem() {

        if (itemTypeComboBox.getValue() == null) {

            showError(
                    "Invalid Selection",
                    "Please select an item type."
            );

            return;
        }


        if (fields.stream().anyMatch(
                field -> field.getText().isBlank()
        )) {

            showError(
                    "Incomplete Form",
                    "Please fill in all fields."
            );

            return;
        }


        try {

            String selectedType =
                    itemTypeComboBox.getValue();


            switch (selectedType) {

                case "Book" ->
                        addBook();

                case "Magazine" ->
                        addMagazine();

                case "Ebook" ->
                        addEbook();

                case "AudioBook" ->
                        addAudioBook();

                default ->
                        throw new IllegalArgumentException(
                                "Invalid item type."
                        );
            }


            showSuccess(
                    "Item added successfully."
            );

            clearAll();

        } catch (NumberFormatException e) {

            showError(
                    "Invalid Number",
                    "Please enter valid numbers in numeric fields."
            );

        } catch (RuntimeException e) {

            showError(
                    "Could Not Add Item",
                    e.getMessage()
            );
        }
    }


    private void addBook() {

        Catalog.getInstance().addItem(

                new Book(

                        getValue(0),
                        getValue(1),
                        Integer.parseInt(getValue(2)),
                        getValue(3),
                        getValue(4),
                        Integer.parseInt(getValue(5)),
                        Long.parseLong(getValue(6)),
                        getValue(7)
                )
        );
    }


    private void addMagazine() {

        Catalog.getInstance().addItem(

                new Magazine(

                        getValue(0),
                        getValue(1),
                        Integer.parseInt(getValue(2)),
                        getValue(3),
                        Long.parseLong(getValue(4)),
                        getValue(6),
                        PublicationFrequency.fromString(
                                getValue(5)
                        )
                )
        );
    }


    private void addEbook() {

        Catalog.getInstance().addItem(

                new Ebook(

                        getValue(0),
                        getValue(1),
                        Integer.parseInt(getValue(2)),
                        getValue(3),
                        DigitalFormat.fromString(
                                getValue(4)
                        ),
                        Integer.parseInt(getValue(5)),
                        getValue(6),
                        Integer.parseInt(getValue(7))
                )
        );
    }


    private void addAudioBook() {

        Catalog.getInstance().addItem(

                new AudioBook(

                        getValue(0),
                        getValue(1),
                        Integer.parseInt(getValue(2)),
                        getValue(3),
                        DigitalFormat.fromString(
                                getValue(4)
                        ),
                        Integer.parseInt(getValue(5)),
                        getValue(6),
                        Integer.parseInt(getValue(7))
                )
        );
    }


    private String getValue(int index) {

        return fields
                .get(index)
                .getText()
                .trim();
    }


    @FXML
    private void handleClear() {

        for (TextField field : fields) {
            field.clear();
        }

        statusLabel.setText("");
    }


    private void clearForm() {

        formGrid
                .getChildren()
                .clear();

        fields.clear();

        statusLabel.setText("");

        addButton.setDisable(true);
    }


    private void clearAll() {

        itemTypeComboBox
                .getSelectionModel()
                .clearSelection();

        clearForm();
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
                    (Stage) addButton
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


    private void showSuccess(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Success"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }


    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message == null
                        ? "Unknown error."
                        : message
        );

        alert.showAndWait();
    }


}
