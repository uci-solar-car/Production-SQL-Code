/*-----------------------------------------------------------------------------------------------------------*/
//          Developer 1: Kunal Buty                                                                          //
/*-----------------------------------------------------------------------------------------------------------*/

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;

/*----------------------------------------------------------------------------------------------------------*/
//  NOTE: This currently assumes that there are 10 or more rows of data for a given driveNum.               //
//        Exceptions to this need to be handled                                                             //
//                                                                                                          //
//  NOTE: DriveNum is currently hardcoded to 1. In the future it needs to create a new DriveNum on startup  //
//                                                                                                          //
/*----------------------------------------------------------------------------------------------------------*/
public class Graph {

    //builds a LineChart and returns it
    public static LineChart<Number,Number> getChart(int numPoints,String dataType) {

        String title,yAxisTitle;
        int column;

        //set all graph variables depending on what graph is requested
        if(dataType.equals("batteryTemp")) {
            column=0;       //col 0 holds battery Temp data (from DB.getData)
            title="Battery Temperature";
            yAxisTitle="Temperature (C)";
        }
        else if(dataType.equals("batteryVoltage")) {
            column=1;       //col 1 holds battery voltage data (from DB.getData)
            title="Battery Voltage";
            yAxisTitle="Volts (V)";

        }
        else if(dataType.equals("carSpeed")){
            column=2;       //col 2 holds car speed data (from DB.getData)
            title="Car Speed";
            yAxisTitle="Speed (mph)";
        }
        else {
            column=3;       //col 3 holds battery charge data (from DB.getData)
            title="Battery Charge";
            yAxisTitle="Charge (%)";
        }

        //set up axes and title
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        yAxis.setLabel(yAxisTitle);
        LineChart graph = new LineChart<>(xAxis, yAxis);
        graph.setTitle(title);

        //create graph
        XYChart.Series graphData = new XYChart.Series();
        graphData.setName(title);

        //get data from sql DB
        try {
            int[][] data=new int[4][numPoints];
            //Note DriveNum is hardcoded to 1 here
            DB.getData(numPoints,1,data);
            for(int i=0;i<numPoints;i++) {
                graphData.getData().add(new XYChart.Data<>(i,data[column][i]));
                //System.out.println(data[i][0]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //add series to graph
        graph.getData().add(graphData);
        return graph;
    }
}
