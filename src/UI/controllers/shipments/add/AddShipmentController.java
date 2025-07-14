
package UI.controllers.shipments.add;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import BackEnd.Service;
import BackEnd.Basic.Date;
import BackEnd.Exceptions.ExceedingBudgetLimitsException;
import BackEnd.Exceptions.IDException;
import BackEnd.Exceptions.PastDatesException;
import BackEnd.Store.Data;
import BackEnd.Store.Freight;
import BackEnd.Store.Orders;
import UI.controllers.MainUIController;

public class AddShipmentController {

    @FXML
    private TextField idField;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker receiptDatePicker;
    @FXML
    private VBox ordersContainer;

    @FXML
    public void initialize() {
        for (Orders order : Data.orders.getHeap()) {
            HBox orderRow = new HBox(10);

            CheckBox orderCheckBox = new CheckBox(order.getName());
            Button detailButton = new Button("DETAIL :");

            detailButton.setOnAction(e -> showOrderDetails(order));

            orderRow.getChildren().addAll(orderCheckBox, detailButton);
            ordersContainer.getChildren().add(orderRow);
        }

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

        receiptDatePicker.setValue(LocalDate.now());

    }

    private void showOrderDetails(Orders order) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Detail");
        alert.setHeaderText("Order Include : ");

        alert.setContentText(order.toString());
        alert.showAndWait();
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

    @FXML
    private void handleSave() {
        int ID;
        try {

            ID = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            showConfirmation("ERROR", "ID must containe numeric part just", () -> {
            }, AlertType.ERROR);
            return;
        }

        String destination = destinationField.getText();
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

        try {
            Service.addFreight(new Freight(ID, destination, date, selectedOrders));
        } catch (IDException e) {
            showConfirmation("ERROR", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        } catch (ExceedingBudgetLimitsException e) {
            showConfirmation("ERROR", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        }

        showConfirmation("CONFIRME", "Done Save", () -> {
            idField.setText("");
            destinationField.setText("");
            receiptDatePicker.setValue(LocalDate.now());

            for (Node node : ordersContainer.getChildren()) {
                if (node instanceof HBox hbox) {
                    for (Node child : hbox.getChildren()) {
                        if (child instanceof CheckBox checkBox && checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    }
                }
            }

        }, AlertType.CONFIRMATION);

    }

    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/MainUI.fxml"));
            Parent root = loader.load();

            MainUIController controller = loader.getController();
            controller.selectShipmentsTab();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
