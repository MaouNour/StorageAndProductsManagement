
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
        ArrayList<Product> allProducts = getAllProducts();

        for (Product product : allProducts) {
            HBox row = new HBox(10);
            row.setStyle("-fx-alignment: CENTER_LEFT;");

            CheckBox checkBox = new CheckBox(product.getName());
            Button detailButton = new Button("Detail");

            Button minusButton = new Button("-");
            Label quantityLabel = new Label("1");
            Button plusButton = new Button("+");
            HBox counterBox = new HBox(5, minusButton, quantityLabel, plusButton);
            counterBox.setVisible(false); // يبدأ مخفياً
            counterBox.setManaged(false); // لا يشغل مساحة

            detailButton.setOnAction(e -> showProductDetails(product));

            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                counterBox.setVisible(newVal);
                counterBox.setManaged(newVal);
            });

            final int[] quantity = { 1 };
            plusButton.setOnAction(e -> {
                quantity[0]++;
                quantityLabel.setText(String.valueOf(quantity[0]));
            });

            minusButton.setOnAction(e -> {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    quantityLabel.setText(String.valueOf(quantity[0]));
                }
            });

            checkBox.setUserData(new Object[] { product, quantity });

            row.getChildren().addAll(checkBox, detailButton, counterBox);
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

        if (orderName.equals("")) {
            showConfirmation("Error", "Pls, Name is Empty", () -> {
            }, AlertType.ERROR);
            return;
        }

        for (Node node : ordersContainer.getChildren()) {
            if (node instanceof HBox hbox) {
                for (Node child : hbox.getChildren()) {
                    if (child instanceof CheckBox checkBox && checkBox.isSelected()) {
                        Object[] data = (Object[]) checkBox.getUserData();
                        Product product = (Product) data[0];
                        int quantity = ((int[]) data[1])[0];
                        if (product.getAvailableAmount() <= quantity) {
                            showConfirmation("ERROR", "Can't add amount from product bigger than available amount",
                                    () -> {
                                    }, AlertType.ERROR);
                            return;
                        }
                        try {
                            product.updateAvailableAmount(product.getAvailableAmount() - quantity);
                        } catch (IllegalAvailableAmountException e) {
                            return;
                        }
                        product.updateAmountFromProduct(quantity);
                        selectedProducts.add(product);
                    }
                }
            }
        }

        if (selectedProducts.isEmpty()) {
            showConfirmation("Error", "Pls, select products for order", () -> {
            }, AlertType.ERROR);
            return;
        }

        try {
            Service.addNewOrderWithPriority(
                    new Orders(orderName, Priority.valueOf(Priority.getArrStringValues()[priority]), selectedProducts));
        } catch (Exception e) {
            showConfirmation("Error", e.getMessage(), () -> {
            }, AlertType.ERROR);
            return;
        }

        showConfirmation("CONFIRMED", "Save Done ✅", () -> {
            priorityChoiceBox.setValue(Priority.getArrStringValues()[0]);
            orderNameField.setText("");

            for (Node node : ordersContainer.getChildren()) {
                if (node instanceof HBox hbox) {
                    for (Node child : hbox.getChildren()) {
                        if (child instanceof CheckBox checkBox) {
                            checkBox.setSelected(false);
                            Object[] data = (Object[]) checkBox.getUserData();
                            int[] quantity = (int[]) data[1];
                            quantity[0] = 1;
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
