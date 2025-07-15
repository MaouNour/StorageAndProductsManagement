package UI.controllers.products.search;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;

import BackEnd.DS.NodeAVL;
import BackEnd.Exceptions.IDException;
import BackEnd.Store.Data;
import BackEnd.Store.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchProductController {

    @FXML
    private TextField searchIdField;
    @FXML
    private ListView<String> similarProductsList;
    @FXML
    private Label errorLabel;
    @FXML
    private Button okButton;

    @FXML
    public void initialize() {
        showAllSimilarList();
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

        NodeAVL<Product> ndoeProduct;
        Product product = null;
        try {
            ndoeProduct = Data.goods.search(ID);
            if (ndoeProduct != null)
                product = ndoeProduct.getItem();
        } catch (IDException e) {
            showError(e.getMessage());
            return;
        }
        handleOpenEdit(product);
        showAllSimilarList();
    }

    private void showAllSimilarList() {
        ObservableList<String> nearby = FXCollections.observableArrayList();
        showAllSimilarList(Data.goods.getRoot(), nearby);
        similarProductsList.setItems(nearby);
    }

    private void showAllSimilarList(NodeAVL<Product> node, ObservableList<String> nearby) {
        if (node == null)
            return;
        showAllSimilarList(node.getLeft(), nearby);
        nearby.add("ID: " + (node.getItem().getID()) + " - " + node.getItem().getName());
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
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Product Add");
            alert.setContentText("\nID : " + product.getID() + "\n name:"  + product.getName()+ "\navailable amount : " + product.getAvailableAmount() + "\nprice : " + product.getPrice() + "\nbuy from Product : " + product.getAmountFromProduct());
            alert.showAndWait();
        }
    }

}
