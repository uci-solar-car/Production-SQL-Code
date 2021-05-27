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

public class GUI extends Application implements Initializable{
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

    public void launchGUI(String[] args) {
        launch(args);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.numPoints=10;
        this.WINDOW_SIZE = 10;
        int driveNum = 1;

        //create graphs
        batteryTempGraph = Graph.getChart(numPoints,"batteryTemp", driveNum);
        batteryVoltageGraph = Graph.getChart(numPoints,"batteryVoltage", driveNum);
        carSpeedGraph = Graph.getChart(numPoints,"carSpeed", driveNum);
        batteryChargeGraph = Graph.getChart(numPoints,"batteryCharge", driveNum);

        //add graphs to first column of gui
        col1.getChildren().add(batteryTempGraph);
        col1.getChildren().add(batteryVoltageGraph);

        //add graphs to second column of gui
        col2.getChildren().add(carSpeedGraph);
        col2.getChildren().add(batteryChargeGraph);

        System.out.println("initialized");


    }

    public void refresh(int driveNum) {
        //this function is triggered when a certain button is clicked (check fxml file to see which one)
        //Code below shows how to add a graph to the GUI.


        LineChart<Number,Number> batteryTempGraph;
        batteryTempGraph = Graph.getChart(this.numPoints,"batteryTemp", driveNum);
        col1.getChildren().clear();
        col1.getChildren().add(batteryTempGraph);

        LineChart<Number,Number> voltageGraph;
        voltageGraph = Graph.getChart(this.numPoints,"batteryVoltage", driveNum);
        col1.getChildren().add(voltageGraph);

        LineChart<Number,Number> speed;
        speed = Graph.getChart(this.numPoints,"speed", driveNum);
        col2.getChildren().clear();
        col2.getChildren().add(speed);

        LineChart<Number,Number> chargeGraph;
        chargeGraph = Graph.getChart(this.numPoints,"batteryCharge", driveNum);
        col2.getChildren().add(chargeGraph);

    }

    /*
    public void refresh(int driveNum) {
        //this function is triggered when a certain button is clicked (check fxml file to see which one)
        //Code below shows how to add a graph to the GUI.

        System.out.println("refresh attempted");

        this.col1.getChildren().clear();
        this.col2.getChildren().clear();

        LineChart<Number,Number> batteryTempGraph;
        batteryTempGraph = Graph.getChart(numPoints,"batteryTemp", driveNum);
        this.col1.getChildren().add(batteryTempGraph);


        /*
        // initialize
        batteryTempGraph = Graph.getChart(numPoints,"batteryTemp", driveNum);
        batteryVoltageGraph = Graph.getChart(numPoints,"batteryVoltage", driveNum);
        carSpeedGraph = Graph.getChart(numPoints,"carSpeed", driveNum);
        batteryChargeGraph = Graph.getChart(numPoints,"batteryCharge", driveNum);


            // put random number with current time
            if (batteryTempGraph.getData().size() > WINDOW_SIZE)
                batteryTempGraph.getData().remove(0);
            if (batteryVoltageGraph.getData().size() > WINDOW_SIZE)
                batteryVoltageGraph.getData().remove(0);
            if (carSpeedGraph.getData().size() > WINDOW_SIZE)
                carSpeedGraph.getData().remove(0);
            if (batteryChargeGraph.getData().size() > WINDOW_SIZE)
                batteryChargeGraph.getData().remove(0);


            try {
                int[][] data=new int[4][numPoints];
                //Note DriveNum is hardcoded to 1 here
                DB.getData(numPoints,driveNum,data);

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

                batteryTempGraph.setData(ol1);
                batteryVoltageGraph.setData(ol2);
                carSpeedGraph.setData(ol3);
                batteryChargeGraph.setData(ol4);
            } catch (SQLException e) {
                e.printStackTrace();
            }



    }
*/

}
