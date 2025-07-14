package UI.controllers.AnalyticalReports;

import java.io.IOException;
import java.util.Optional;

import BackEnd.FileHandling.Statistics;
import BackEnd.Store.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class AnalyticalReportsController {
    @FXML
    private void inventoryValue() {
        showConfirmation("Storage : ", "Storage : \n" + Statistics.getStatistics() + " $.", AlertType.INFORMATION,
                () -> {
                });
                String stats = Statistics.getStatistics()+"\n\ntotal order cost:"+Data.orders.costOrders();
                Statistics.writeToFile(stats);
                
    }

    @FXML
    private void HighCostShipments(ActionEvent event) {

        try {
            switchScene(event,"/UI/views/AnalyticalReports/SearchHeightLevelShipment.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Data.freight.printHeightCostFreight(Data.freight.getRoot(), 500);

    }

    @FXML
    private void TotalOrderCost() {

        showConfirmation("Total Order Cost", "Total Order Cost : " + Data.orders.costOrders() + " $.",
                AlertType.INFORMATION,
                () -> {
                });

    }

    private void showConfirmation(String title, String message, AlertType alertType, Runnable onConfirm) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirm.run();
        }
    }

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

}
