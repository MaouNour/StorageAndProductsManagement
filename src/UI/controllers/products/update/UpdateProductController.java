package UI.controllers.products.update;

import java.util.Optional;

import BackEnd.Service;
import BackEnd.Exceptions.IllegalAvailableAmountException;
import BackEnd.Exceptions.NegativePriceException;
import BackEnd.Store.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateProductController {

    @FXML
    private TextField idField, nameField, priceField, quantityField;
    @FXML
    private Button saveButton, discardButton;

    private Product selectedProduct;

    private Runnable onProductUpdated;

    public void setOnProductUpdated(Runnable callback) {
        this.onProductUpdated = callback;
    }

    @FXML
    public void initializeWithProduct(Product product) {
        this.selectedProduct = product;
        idField.setText(String.valueOf(product.getID()));
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getAvailableAmount()));

    }

    @FXML
    private void handleSave(ActionEvent event) {

        if (nameField.getText().equals("")) {
            showConfirmation("Error", "Name is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;
        }

        double price = -1;
        if (!(priceField.getText().trim().replaceAll("[^\\d, .]", "").equals(priceField.getText().trim()))) {
            showConfirmation("Error", "Price must containe numeric part just", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else if (priceField.getText().equals("")) {
            showConfirmation("Error", "Price is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else {
            price = Double.parseDouble(priceField.getText().trim());

        }

        double availableAmount = -1;
        if (!(quantityField.getText().trim().replaceAll("[^\\d, .]", "").equals(quantityField.getText().trim()))) {
            showConfirmation("Error", "Available must containe numeric part just", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } else if (quantityField.getText().equals("")) {
            showConfirmation("Error", "Amount is Empty", () -> {
            }, Alert.AlertType.ERROR);
            return;

        } else {
            availableAmount = Double.parseDouble(quantityField.getText());

        }

        try {
            Service.updateProduct(
                    new Product(selectedProduct.getID(), nameField.getText().trim(), price, availableAmount),
                    selectedProduct);
        } catch (IllegalAvailableAmountException e) {
            showConfirmation("Error", "The quantity exceeded the required limit.", () -> {
            }, Alert.AlertType.ERROR);
            return;
        } catch (NegativePriceException e) {
            showConfirmation("Error", "Price mustn't zero or negative number", () -> {
            }, Alert.AlertType.ERROR);
            return;
        }
        showConfirmation("CONFIRME", "Do you trust from save chengemnt ??", () -> {
            if (onProductUpdated != null) {
                onProductUpdated.run();
            }

            ((Stage) saveButton.getScene().getWindow()).close();
        }, AlertType.CONFIRMATION);

    }

    @FXML
    private void handleDiscard(ActionEvent event) {
        showConfirmation("CONFIRME", "Do you trust from not save chengemnt ??", () -> {
            ((Stage) discardButton.getScene().getWindow()).close();
        }, AlertType.CONFIRMATION);
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
