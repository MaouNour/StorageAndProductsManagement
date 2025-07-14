// src/controllers/ProductManagementController.java
package UI.controllers.products.add;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import BackEnd.Service;
import BackEnd.Exceptions.IDException;
import BackEnd.Exceptions.IllegalAvailableAmountException;
import BackEnd.Exceptions.NegativePriceException;
import BackEnd.Store.Product;

public class AddProductController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    public void handleAddProduct(ActionEvent event) {
        int ID = -1;
        if (!(idField.getText().trim().replaceAll("[^\\d]", "").equals(idField.getText().trim()))) {
            showConfirmation("Error", "ID must containe numeric part just", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else if (idField.getText() == "") {
            showConfirmation("Error", "ID is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else
            ID = Integer.parseInt(idField.getText());

        if (nameField.getText() == "") {
            showConfirmation("Error", "Name is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;
        }

        double price = -1;
        if (!(priceField.getText().trim().replaceAll("[^\\d]", "").equals(priceField.getText().trim()))) {
            showConfirmation("Error", "Price must containe numeric part just", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else if (priceField.getText() == "") {
            showConfirmation("Error", "Price is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;

        } else {
            price = Double.parseDouble(priceField.getText());
        }

        int availableAmount = -1;
        if (!(quantityField.getText().trim().replaceAll("[^\\d]", "").equals(quantityField.getText().trim()))) {
            showConfirmation("Error", "Available must containe numeric part just", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else if (quantityField.getText() == "") {
            showConfirmation("Error", "Amount is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;

        } else {
            availableAmount = Integer.parseInt(quantityField.getText());
        }

        try {
            Service.addNewProduct(new Product(ID, nameField.getText(), price, availableAmount));
        } catch (NegativePriceException e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, Alert.AlertType.ERROR);
            return;
        } catch (IDException e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, Alert.AlertType.ERROR);
            return;
        } catch (IllegalAvailableAmountException e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, Alert.AlertType.ERROR);
            return;
        }

        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");

        showConfirmation("CONFIRME", "Done ✅", () -> {
        }, Alert.AlertType.CONFIRMATION);
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

    public void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/MainUI.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

}
