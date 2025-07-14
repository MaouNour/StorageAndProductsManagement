package UI.controllers.orders.search;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;

import BackEnd.Store.Data;
import BackEnd.Store.Orders;
import UI.controllers.MainUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchOrderController {

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
        int ID;
        try {
            ID = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showError("ID must containe numeric part just");
            return;
        }

        if (ID > Data.orders.getHeap().size()) {
            showError("ID not Exist. Pls, Enter Again Another ID");
            return;
        }

        handleOpenEdit(Data.orders.getHeap().get(ID - 1));
        showAllSimilarList();

        searchIdField.setText("");

    }

    private void showAllSimilarList() {
        ObservableList<String> nearby = FXCollections.observableArrayList();

        for (int i = 1; i <= Data.orders.getHeap().size(); i++) {
            nearby.add("ID: " + i + " - " + Data.orders.getHeap().get(i - 1).getName());
        }

        similarProductsList.setItems(nearby);

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
            controller.selectOrdersTab();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenEdit(Orders order) {
        if (order != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION); 
            alert.setTitle("order Detail");
            alert.setHeaderText("Details for order Name: " + order.getName());

            Label label = new Label(order.toString());
            label.setWrapText(true);

            ScrollPane scrollPane = new ScrollPane(label);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefSize(400, 250); 

            alert.getDialogPane().setContent(scrollPane);

            alert.showAndWait();
        }
    }

}
