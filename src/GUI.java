/*-----------------------------------------------------------------------------------------------------------*/
//          Developer 1: Kunal Buty                                                                          //
/*-----------------------------------------------------------------------------------------------------------*/


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GUI extends Application implements Initializable {
    public VBox col1;
    public VBox col2;

    public int numPoints;
    public int WINDOW_SIZE;

    public LineChart<Number,Number> batteryTempGraph;
    public LineChart<Number,Number> carSpeedGraph;
    public LineChart<Number,Number> batteryVoltageGraph;
    public LineChart<Number,Number> batteryChargeGraph;

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
        this.numPoints=10;
        this.WINDOW_SIZE = 10;

        //create graphs
        this.batteryTempGraph = Graph.getChart(numPoints,"batteryTemp");
        this.batteryVoltageGraph = Graph.getChart(numPoints,"batteryVoltage");
        this.carSpeedGraph = Graph.getChart(numPoints,"carSpeed");
        this.batteryChargeGraph = Graph.getChart(numPoints,"batteryCharge");

        //add graphs to first column of gui
        col1.getChildren().add(this.batteryTempGraph);
        col1.getChildren().add(this.batteryVoltageGraph);

        //add graphs to second column of gui
        col2.getChildren().add(this.carSpeedGraph);
        col2.getChildren().add(this.batteryChargeGraph);

        System.out.println("initialized");

    }

    public void refresh(int driveNum) {
        //this function is triggered when a certain button is clicked (check fxml file to see which one)
        //Code below shows how to add a graph to the GUI.

        /*
        LineChart<Number,Number> batteryTempGraph;
        batteryTempGraph = Graph.getChart(5,"batteryTemp");
        col1.getChildren().add(batteryTempGraph);
        */

        Platform.runLater(() -> {
            // put random number with current time
            if (this.batteryTempGraph.getData().size() > this.WINDOW_SIZE)
                this.batteryTempGraph.getData().remove(0);
            if (this.batteryVoltageGraph.getData().size() > this.WINDOW_SIZE)
                this.batteryVoltageGraph.getData().remove(0);
            if (this.carSpeedGraph.getData().size() > this.WINDOW_SIZE)
                this.carSpeedGraph.getData().remove(0);
            if (this.batteryChargeGraph.getData().size() > this.WINDOW_SIZE)
                this.batteryChargeGraph.getData().remove(0);


            try {
                int[][] data=new int[4][this.numPoints];
                //Note DriveNum is hardcoded to 1 here
                DB.getData(this.numPoints,driveNum,data);

                ObservableList ol1 = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
                for(int i=0;i<numPoints;i++) {
                    ol1.add(new XYChart.Data<>(i,data[0][i]));
                    //System.out.println(data[i][0]);
                }

                ObservableList ol2 = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
                for(int i=0;i<numPoints;i++) {
                    ol1.add(new XYChart.Data<>(i,data[1][i]));
                    //System.out.println(data[i][0]);
                }

                ObservableList ol3 = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
                for(int i=0;i<numPoints;i++) {
                    ol1.add(new XYChart.Data<>(i,data[2][i]));
                    //System.out.println(data[i][0]);
                }

                ObservableList ol4 = FXCollections.<XYChart.Series<Number, Number>>observableArrayList();
                for(int i=0;i<numPoints;i++) {
                    ol1.add(new XYChart.Data<>(i,data[3][i]));
                    //System.out.println(data[i][0]);
                }

                this.batteryTempGraph.setData(ol1);
                this.batteryVoltageGraph.setData(ol2);
                this.carSpeedGraph.setData(ol3);
                this.batteryChargeGraph.setData(ol4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


}
