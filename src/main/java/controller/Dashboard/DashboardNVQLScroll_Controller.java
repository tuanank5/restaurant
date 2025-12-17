package controller.Dashboard;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class DashboardNVQLScroll_Controller {
	@FXML
	public ScrollPane pane_Dashboard;
	
	@FXML
    public void initialize(){
        loadTabContent(pane_Dashboard, "/view/fxml/Dashboard/DashboardNVQL.fxml");
    }

    private void loadTabContent(ScrollPane pane, String fxmlFile){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node content = loader.load();
            pane.setContent(content);
            pane.setFitToHeight(true);
            pane.setFitToWidth(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
