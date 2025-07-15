package UI.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MainUIController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private AnchorPane productsTab, shipmentsTab, ordersTab, AnalyticalReports;

    public void selectShipmentsTab() {
        if (mainTabPane != null && shipmentsTab != null) {
            mainTabPane.getSelectionModel().select(2);
        }
    }

    public void selectOrdersTab() {
        if (mainTabPane != null && shipmentsTab != null) {
            mainTabPane.getSelectionModel().select(1);
        }
    }

    public void selectAnalyticalReports() {
        if (mainTabPane != null && shipmentsTab != null) {
            mainTabPane.getSelectionModel().select(2);
        }
    }

    @FXML
    public void initialize() {
        try {
            productsTab.getChildren()
                    .add(FXMLLoader.load(getClass().getResource("/UI/views/products/ProductSection.fxml")));
            shipmentsTab.getChildren()
                    .add(FXMLLoader.load(getClass().getResource("/UI/views/shipments/ShipmentSection.fxml")));
            ordersTab.getChildren().add(FXMLLoader.load(getClass().getResource("/UI/views/orders/OrderSection.fxml")));
            AnalyticalReports.getChildren().add(FXMLLoader.load(getClass().getResource("/UI/views/AnalyticalReports/AnalyticalReportsView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
