package UI.controllers.orders.update;

import java.util.ArrayList;
import java.util.Optional;

import BackEnd.Service;
import BackEnd.Basic.Priority;
import BackEnd.DS.NodeAVL;
import BackEnd.Store.Data;
import BackEnd.Store.Orders;
import BackEnd.Store.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UpdateOrderController {

    @FXML
    private Button saveButton, discardButton;
    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    @FXML
    private ListView<HBox> similarProductsList;
    @FXML
    private TextField nameField;

    private Orders selectedOrder;
    private Runnable onOrderUpdated;

    @FXML
    public void initialize() {
        priorityChoiceBox.getItems().addAll(Priority.getArrStringValues());
        priorityChoiceBox.setValue(Priority.getArrStringValues()[0]);

        for (Product product : getAllProducts()) {
            HBox row = new HBox(10);
            row.setStyle("-fx-alignment: CENTER_LEFT;");

            CheckBox checkBox = new CheckBox("ID : " + product.getID() + " - " + product.getName());

            Button minusButton = new Button("-");
            Label quantityLabel = new Label(String.valueOf(product.getAmountFromProduct()));
            Button plusButton = new Button("+");
            HBox counterBox = new HBox(5, minusButton, quantityLabel, plusButton);
            counterBox.setVisible(false);
            counterBox.setManaged(false);

            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                counterBox.setVisible(newVal);
                counterBox.setManaged(newVal);
            });

            final int[] quantity = { Integer.parseInt(quantityLabel.getText()) };
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

            row.getChildren().addAll(checkBox, counterBox);
            similarProductsList.getItems().add(row);
        }
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

    public void setOnOrderUpdated(Runnable callback) {
        this.onOrderUpdated = callback;
    }

    public void initializeWithOrder(Orders order) {
        this.selectedOrder = order;
        priorityChoiceBox.setValue(order.getPriority().name());
        nameField.setText(order.getName());

        for (HBox row : similarProductsList.getItems()) {
            for (javafx.scene.Node node : row.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    Object[] data = (Object[]) checkBox.getUserData();
                    Product p = (Product) data[0];
                    for (Product selected : order.getContentOrder()) {
                        if (p.getID() == selected.getID()) {
                            checkBox.setSelected(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String newName = nameField.getText();
        String selectedPriority = priorityChoiceBox.getValue();

        if (newName == null || newName.equals("")) {
            showAlert("Error", "Order name cannot be empty.");
            return;
        }

        ArrayList<Product> selectedProducts = new ArrayList<>();

        for (HBox row : similarProductsList.getItems()) {
            for (javafx.scene.Node node : row.getChildren()) {
                if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                    Object[] data = (Object[]) checkBox.getUserData();
                    Product product = (Product) data[0];
                    int quantity = ((int[]) data[1])[0];
                    product.updateAmountFromProduct(quantity);
                    selectedProducts.add(product);
                }
            }
        }

        if (selectedProducts.isEmpty()) {
            showAlert("Error", "Please select at least one product.");
            return;
        }
        selectedOrder.setContentOrder(selectedProducts);

        if (selectedOrder.getCostTotal() > Data.levelMoneySideOrders) {
            showAlert("ERROR", "Exceeding the budget limit of " + Data.levelMoneySideOrders + "$.");
            return;
        }
        Priority pri = Priority.valueOf(selectedPriority.toUpperCase());
        boolean reAdd = !(selectedOrder.getPriority() == pri);

        selectedOrder.setName(newName);
        selectedOrder.setPriority(pri);

        if (reAdd)
            Service.updatePriorityOrder(selectedOrder, Data.orders.getHeap().indexOf(selectedOrder));

        showConfirmation("Do you trust to save changes?", () -> {
            if (onOrderUpdated != null) {
                onOrderUpdated.run();
            }
            ((Stage) saveButton.getScene().getWindow()).close();
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDiscard(ActionEvent event) {
        showConfirmation("Do you trust from not save changement ?? ", () -> {
        });
        ((Stage) discardButton.getScene().getWindow()).close();
    }

    private void showConfirmation(String message, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRME");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirm.run();
        }
    }
}
