package UI.controllers.products.delet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;

import BackEnd.Service;
import BackEnd.DS.NodeAVL;
import BackEnd.Exceptions.IDException;
import BackEnd.Store.Data;
import BackEnd.Store.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeletProductController {

    @FXML
    private TextField searchIdField;
    @FXML
    private ListView<Product> similarProductsList;
    @FXML
    private Label errorLabel;
    @FXML
    private Button okButton;

    @FXML
    public void initialize() {
        showAllSimilarList();

        similarProductsList.setCellFactory(e -> new ListCell<>() {
            private final Label label = new Label();
            private final Button detailButton = new Button("Detail");
            private final HBox content = new HBox(10, label, detailButton);

            {
                detailButton.setOnAction(e -> {
                    Product product = getItem();
                    if (product != null) {
                        showProductDetails(product);
                    }
                });
            }

            private void showProductDetails(Product product) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Product Details");
                alert.setHeaderText("Details for Product ID: " + product.getID());
                alert.setContentText("Name: " + product.getName() +
                        "\nPrice: " + product.getPrice() +
                        "\nAvailable: " + product.getAvailableAmount());
                alert.showAndWait();
            }

            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setGraphic(null);
                } else {
                    label.setText("ID : " + product.getID() + " - " + product.getName());
                    setGraphic(content);
                }
            }
        });

    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String input = searchIdField.getText().trim();
        int ID = -1;
        if (!(input.replaceAll("[^\\d]", "").equals(input))) {
            showError("ID must containe numeric part just");
            return;
        } else if (input.equals("")) {
            showError("ID is Empty");
            return;
        } else
            ID = Integer.parseInt(input);

        Product product = null;
        try {
            product = Service.deletProduct(ID);
        } catch (IDException e) {
            showError(e.getMessage());
        }

        handleOpenEdit(product);

        showAllSimilarList();
    }

    private void showAllSimilarList() {
        ObservableList<Product> nearby = FXCollections.observableArrayList();
        showAllSimilarList(Data.goods.getRoot(), nearby);
        similarProductsList.setItems(nearby);
    }

    private void showAllSimilarList(NodeAVL<Product> node, ObservableList<Product> nearby) {
        if (node == null)
            return;
        showAllSimilarList(node.getLeft(), nearby);
        nearby.add(node.getItem());
        showAllSimilarList(node.getRight(), nearby);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        okButton.setVisible(true);
    }

    @FXML
    public void hideError() {
        errorLabel.setVisible(false);
        okButton.setVisible(false);
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/MainUI.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleOpenEdit(Product product) {
        Product foundProduct = product;
        if (foundProduct != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Delet Product");
            alert.setHeaderText("Delet Product has ID : " + product.getID() + "and name : " + product.getName());
            alert.setContentText("Done ✅");
            alert.showAndWait();
        }
    }

}
