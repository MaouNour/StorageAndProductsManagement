package UI.controllers.products;

import java.io.IOException;

import BackEnd.Store.Product;
import UI.controllers.products.update.UpdateProductController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductSectionController {

    @FXML
    public void handleAdd(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/products/add/AddProduct.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/products/update/UpdateSearchView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleSearch(ActionEvent event) {
        try {
            switchScene(event, "/UI/views/products/search/SearchProductView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {
  try {
            switchScene(event, "/UI/views/products/delet/DeletProductView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @FXML
    private void openEditWindow(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/views/products/update/UpdateProductView.fxml"));
            Parent root = loader.load();

            UpdateProductController controller = loader.getController();
            controller.initializeWithProduct(product);

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
