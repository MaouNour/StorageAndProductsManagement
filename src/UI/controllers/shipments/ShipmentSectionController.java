package UI.controllers.shipments;

import java.io.IOException;

import BackEnd.Store.Freight;
import UI.controllers.shipments.update.UpdateShipmentController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShipmentSectionController {
    

    @FXML
    public void handleAdd(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/shipments/add/AddShipment.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/shipments/update/UpdateSearchView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleSearch(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/shipments/search/SearchShipmentView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openEditWindow(Freight freight) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/shipments/update/UpdateShipmentView.fxml"));
            Parent root = loader.load();

            UpdateShipmentController controller = loader.getController();
            controller.initializeWithShipment(freight);

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
