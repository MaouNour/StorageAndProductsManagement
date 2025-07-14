package UI.controllers.orders;

import java.io.IOException;

import BackEnd.Store.Data;
import BackEnd.Store.Orders;
import UI.controllers.orders.update.UpdateOrderController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrderSectionController {

    @FXML
    public void handleAdd(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/orders/add/AddOrder.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/orders/update/UpdateSearchView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/orders/search/SearchOrderView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void GetHighPriorityOrder(ActionEvent event) {

        showAlert("INFORNATION", "High Priority Order :  \n" + Data.orders.getHeap().get(0));

    }

    @FXML
    private void openEditWindow(Orders order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/orders/update/UpdateOrderView.fxml"));
            Parent root = loader.load();

            UpdateOrderController controller = loader.getController();
            controller.initializeWithOrder(order);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
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
}
