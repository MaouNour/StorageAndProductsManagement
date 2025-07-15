package UI.controllers.AnalyticalReports;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;

import BackEnd.DS.NodeAVL;
import BackEnd.Store.Data;
import BackEnd.Store.Freight;
import UI.controllers.MainUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchHeightLevelShipment {

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
        showAllSimilarList(null);
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String input = searchIdField.getText().trim();
        Double level = null;
        if (!(input.replaceAll("[^\\d]", "").equals(input))) {
            showError("ID must containe numeric part just");
            return;
        } else if (input.equals("")) {
            showError("ID is Empty");
            return;
        } else
            level = Double.parseDouble(input);

        showAllSimilarList(level);
    }

    private void showAllSimilarList(Double level) {
        ObservableList<String> nearby = FXCollections.observableArrayList();
        showAllSimilarList(Data.freight.getRoot(), nearby, level);
        similarProductsList.setItems(nearby);

    }

    private void showAllSimilarList(NodeAVL<Freight> node, ObservableList<String> nearby, Double level) {
        if (node == null)
            return;
        showAllSimilarList(node.getLeft(), nearby, level);
        if (level == null || node.getItem().getCost() >= level) {
            nearby.add("ID: " + (node.getItem().getID()) + " - " + node.getItem().getDestination());
        }
        showAllSimilarList(node.getRight(), nearby, level);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/MainUI.fxml"));
            Parent root = loader.load();

            MainUIController controller = loader.getController();
            controller.selectAnalyticalReports();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenEdit(Freight freight) {
        Freight foundProduct = freight;
        if (foundProduct != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Product Add");
            alert.setContentText(freight.toString());
            alert.showAndWait();
        }
    }

}
