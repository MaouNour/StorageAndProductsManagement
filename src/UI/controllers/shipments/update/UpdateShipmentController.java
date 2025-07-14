package UI.controllers.shipments.update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import BackEnd.Service;
import BackEnd.Basic.Date;
import BackEnd.Exceptions.PastDatesException;
import BackEnd.Store.Data;
import BackEnd.Store.Freight;
import BackEnd.Store.Orders;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class UpdateShipmentController {

    @FXML
    private Button saveButton, discardButton;
    @FXML
    private TextField idField;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker receiptDatePicker;
    @FXML
    private VBox ordersContainer;

    private Runnable onShipmentUpdated;

    public void setOnShipmentUpdated(Runnable runnable) {
        onShipmentUpdated = runnable;
    }

    private Freight selectedFriehgt;

    @FXML
    public void initializeWithShipment(Freight friehgt) {
        this.selectedFriehgt = friehgt;
        idField.setText(String.valueOf(friehgt.getID()));
        destinationField.setText(friehgt.getDestination());

        receiptDatePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd-MM-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                receiptDatePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });

        Date extractDate = friehgt.getDateOfReceipt();
        receiptDatePicker.setValue(LocalDate.of(extractDate.getYear(), extractDate.getMonth(), extractDate.getDay()));

        ordersContainer.getChildren().clear();

        for (Orders order : Data.orders.getHeap()) {
            HBox orderRow = new HBox(10);
            orderRow.setStyle("-fx-alignment: CENTER_LEFT;");

            CheckBox checkBox = new CheckBox(order.getName());
            checkBox.setUserData(order);

            for (Orders selected : friehgt.getOrders()) {
                if (order.hashCode() == selected.hashCode()) {
                    checkBox.setSelected(true);
                    break;
                }
            }

            Button detailButton = new Button("DETAIL");
            detailButton.setOnAction(e -> showOrderDetails(order));

            orderRow.getChildren().addAll(checkBox, detailButton);
            ordersContainer.getChildren().add(orderRow);
        }
    }

    @FXML
    public void initialize() {
        for (Orders order : Data.orders.getHeap()) {
            HBox orderRow = new HBox(10);
            orderRow.setStyle("-fx-alignment: CENTER_LEFT;");

            CheckBox orderCheckBox = new CheckBox(order.getName());

            Button detailButton = new Button("DETAIL");
            detailButton.setOnAction(e -> showOrderDetails(order));

            orderRow.getChildren().addAll(orderCheckBox, detailButton);
            orderCheckBox.setUserData(order); 
            ordersContainer.getChildren().add(orderRow);
        }
    }

    private void showOrderDetails(Orders order) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Detail");
        alert.setHeaderText("Order Include :");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(400, 300);

        Label contentLabel = new Label(order.toString());
        contentLabel.setWrapText(true); 

        scrollPane.setContent(contentLabel);

        alert.getDialogPane().setContent(scrollPane);

        alert.showAndWait();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        
        String destination;
        if (!destinationField.getText().equals("") && destinationField.getText() != null)
            destination = destinationField.getText();
        else {
            showConfirmation("ERROR", "Destination is Empty", () -> {
            }, AlertType.ERROR);
            return;
        }
        Date date;
        try {
            date = new Date(receiptDatePicker.getValue().getYear(), receiptDatePicker.getValue().getMonthValue(),
                    receiptDatePicker.getValue().getDayOfMonth());
        } catch (PastDatesException e) {
            showConfirmation("ERROR", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        }
        ArrayList<Orders> selectedOrders = new ArrayList<>();

        int i = 0;
        for (Node node : ordersContainer.getChildren()) {
            if (node instanceof HBox hbox) {
                for (Node child : hbox.getChildren()) {
                    if (child instanceof CheckBox checkBox && checkBox.isSelected()) {
                        Orders o = Data.orders.getHeap().get(i); 
                        selectedOrders.add(o);
                    }
                }
                i++;
            }
        }

        Service.updateDateOfReceipt(new Freight(selectedFriehgt.getID(), destination, date, selectedOrders),
                selectedFriehgt);

        showConfirmation("CONFIRME", "Do you trust from save changement ?? ", () -> {

            if (onShipmentUpdated != null) {
                onShipmentUpdated.run();
            }

            ((Stage) saveButton.getScene().getWindow()).close();
        }, AlertType.CONFIRMATION);
    }

    @FXML
    private void handleDiscard(ActionEvent event) {
        showConfirmation("CONFIRME", "Do you trust from not save changement ?? ", () -> {
        }, AlertType.CONFIRMATION);
        ((Stage) discardButton.getScene().getWindow()).close();
    }

    private void showConfirmation(String title, String message, Runnable onConfirm, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirm.run();
        }
    }
}
