
package UI.controllers.orders.add;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import BackEnd.Service;
import BackEnd.Basic.Priority;
import BackEnd.DS.NodeAVL;
import BackEnd.Exceptions.ExceedingBudgetLimitsException;
import BackEnd.Exceptions.IDException;
import BackEnd.Exceptions.IllegalAvailableAmountException;
import BackEnd.Store.Data;
import BackEnd.Store.Orders;
import BackEnd.Store.Product;
import UI.controllers.MainUIController;

public class AddOrderController {

    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    @FXML
    private VBox ordersContainer;
    @FXML
    private TextField orderNameField;

    @FXML
    public void initialize() {
        for (Product product : getAllProducts()) {
            HBox row = new HBox(10);
            row.setStyle("-fx-alignment: CENTER_LEFT;");

            CheckBox checkBox = new CheckBox(product.getName());
            Button detailButton = new Button("Detail");

            detailButton.setOnAction(e -> showProductDetails(product));

            row.getChildren().addAll(checkBox, detailButton);
            ordersContainer.getChildren().add(row);
        }

        priorityChoiceBox.getItems().addAll(Priority.getArrStringValues());
        priorityChoiceBox.setValue(Priority.getArrStringValues()[0]);
    }

    private ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        collectProducts(Data.goods.getRoot(), products);
        return products;
    }

    private void collectProducts(NodeAVL<Product> node, ArrayList<Product> list) {
        if (node == null)
            return;
        collectProducts(node.getLeft(), list);
        list.add(node.getItem());
        collectProducts(node.getRight(), list);
    }

    private void showProductDetails(Product product) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Detail");
        alert.setHeaderText("Details for Product ID: " + product.getID());
        alert.setContentText(
                "Name: " + product.getName() +
                        "\nPrice: " + product.getPrice() +
                        "\nAvailable Amount: " + product.getAvailableAmount());
        alert.showAndWait();
    }

    @FXML
    private void handleSave() {
        int priority = priorityChoiceBox.getSelectionModel().getSelectedIndex();
        String orderName = orderNameField.getText();
        ArrayList<Product> selectedProducts = new ArrayList<>();

        int i = 0;
        for (Node node : ordersContainer.getChildren()) {
            if (node instanceof HBox hbox) {
                for (Node child : hbox.getChildren()) {
                    if (child instanceof CheckBox checkBox && checkBox.isSelected()) {
                        Product p = getAllProducts().get(i);
                        selectedProducts.add(p);
                    }
                }
                i++;
            }
        }

        if (orderName.equals("")) {
            showConfirmation("Error", "Pls,Name is Empty", () -> {
            }, AlertType.ERROR);
            return;
        }
        if (selectedProducts.isEmpty()) {
            showConfirmation("Error", "Pls, select products for order", () -> {
            }, AlertType.ERROR);
            return;
        }
        try {
            Service.addNewOrderWithPriority(
                    new Orders(orderName, Priority.valueOf(Priority.getArrStringValues()[priority]),
                            selectedProducts));
        } catch (IllegalAvailableAmountException e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        } catch (IDException e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        } catch (ExceedingBudgetLimitsException e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        }
        showConfirmation("CONFIRME", "Save Done ✅", () -> {

            priorityChoiceBox.setValue(Priority.getArrStringValues()[0]);
            orderNameField.setText("");

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
            controller.selectOrdersTab();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
