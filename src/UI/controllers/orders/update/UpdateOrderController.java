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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateOrderController {

    @FXML
    private Button saveButton, discardButton;
    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    @FXML
    private ListView<CheckBox> similarProductsList;

    @FXML
    private TextField nameField;

    private Orders selectedOrder;
    private Runnable onOrderUpdated;

    @FXML
    public void initialize() {
        priorityChoiceBox.getItems().addAll(Priority.getArrStringValues());
        priorityChoiceBox.setValue(Priority.getArrStringValues()[0]);

        for (Product product : getAllProducts()) {
            CheckBox checkBox = new CheckBox("ID : " + product.getID() + " - " + product.getName());
            checkBox.setUserData(product);
            similarProductsList.getItems().add(checkBox);
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

        for (CheckBox checkBox : similarProductsList.getItems()) {
            Product p = (Product) checkBox.getUserData();
            for (Product selected : order.getContentOrder()) {
                if (p.getID() == selected.getID()) {
                    checkBox.setSelected(true);
                    break;
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

        for (CheckBox checkBox : similarProductsList.getItems()) {
            if (checkBox.isSelected()) {
                Product p = (Product) checkBox.getUserData();
                selectedProducts.add(p);
            }
        }

        if (selectedProducts.isEmpty()) {
            showAlert("Error", "Please select at least one product.");
            return;
        }

        Priority pri = Priority.valueOf(selectedPriority.toUpperCase());
        boolean reAdd = !(selectedOrder.getPriority() == pri);

        selectedOrder.setName(newName);
        selectedOrder.setPriority(Priority.valueOf(selectedPriority.toUpperCase()));
        selectedOrder.setContentOrder(selectedProducts);

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
