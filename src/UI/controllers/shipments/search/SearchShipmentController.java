package UI.controllers.shipments.search;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;

import BackEnd.Service;
import BackEnd.DS.NodeAVL;
import BackEnd.Exceptions.IDException;
import BackEnd.Store.Data;
import BackEnd.Store.Freight;
import UI.controllers.MainUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchShipmentController {

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
        int ID;
        try {
            ID = Integer.parseInt(searchIdField.getText().trim());
        } catch (NumberFormatException e) {
            showError("ID must containe numeric part just");
            return;
        }

        Freight freight = null;
        try {
            NodeAVL<Freight> nodeFreight = Service.search(Data.freight, ID);
            if (nodeFreight != null)
                freight = nodeFreight.getItem();

        } catch (IDException e) {
            showError(e.getMessage());
            return;
        }
        handleOpenEdit(freight);

        similarProductsList = new ListView<String>();
        showAllSimilarList();
    }

    private void showAllSimilarList() {
        ObservableList<String> nearby = FXCollections.observableArrayList();
        showAllSimilarList(Data.freight.getRoot(), nearby);
        similarProductsList.setItems(nearby);
    }

    private void showAllSimilarList(NodeAVL<Freight> node, ObservableList<String> nearby) {
        if (node == null)
            return;
        showAllSimilarList(node.getLeft(), nearby);
        nearby.add("ID: " + (node.getItem().getID()) + " - Destination: " + node.getItem().getDestination());
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

    @FXML
    private void handleOpenEdit(Freight freight) {
        if (freight != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Freight Detail");
            alert.setHeaderText("Details for Freight ID: " + freight.getID());

            Label label = new Label(freight.toString());
            label.setWrapText(true);

            ScrollPane scrollPane = new ScrollPane(label);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefSize(400, 250);

            alert.getDialogPane().setContent(scrollPane);

            alert.showAndWait();
        }
    }

}
