/*-----------------------------------------------------------------------------------------------------------*/
//          Developer 1: Kunal Buty                                                                          //
/*-----------------------------------------------------------------------------------------------------------*/


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GUI extends Application implements Initializable {
    public VBox col1;
    public VBox col2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //set initial scene and initialize gui
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Solar Car Telemetry");
        primaryStage.setScene(new Scene(root, 1500, 1000));
        primaryStage.show();

    }

    void launchGUI(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int numPoints=10;
        LineChart<Number,Number> batteryTempGraph;
        LineChart<Number,Number> carSpeedGraph;
        LineChart<Number,Number> batteryVoltageGraph;
        LineChart<Number,Number> batteryChargeGraph;

        //create graphs
        batteryTempGraph = Graph.getChart(numPoints,"batteryTemp");
        batteryVoltageGraph = Graph.getChart(numPoints,"batteryVoltage");
        carSpeedGraph = Graph.getChart(numPoints,"carSpeed");
        batteryChargeGraph = Graph.getChart(numPoints,"batteryCharge");

        //add graphs to first column of gui
        col1.getChildren().add(batteryTempGraph);
        col1.getChildren().add(batteryVoltageGraph);

        //add graphs to second column of gui
        col2.getChildren().add(carSpeedGraph);
        col2.getChildren().add(batteryChargeGraph);


    }

    public void refresh() {
        //this function is triggered when a certain button is clicked (check fxml file to see which one)
        //Code below shows how to add a graph to the GUI.

        /*
        LineChart<Number,Number> batteryTempGraph;
        batteryTempGraph = Graph.getChart(5,"batteryTemp");
        col1.getChildren().add(batteryTempGraph);
        */
    }


}
