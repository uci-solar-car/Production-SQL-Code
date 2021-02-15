/*-----------------------------------------------------------------------------------------------------------*/
//          Developer 1: Kunal Buty                                                                          //
//          Developer 2: Sharon Xia
/*-----------------------------------------------------------------------------------------------------------*/

// Setup Modules
// * remember to go to project modules
//   (Right click folder -> open module settings -> dependencies)
//   and add mysql-connector-java/jserialcomm.jar

// ==> Launch GUI
// so far, we're just taking the data that's already
// in the database and displaying it as a graph

// testWirelessCom()
// you can edit/add new methods in WirelessCom to test here

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class Main {

    // check available port names
    public static void getPortNames(){
        SerialPort[] ports = SerialPort.getCommPorts();

        for (int i = 0; i < ports.length; ++i)
        {
            System.out.println(ports[i].getSystemPortName());
        }
    }

    public static void testWirelessCom() throws IOException {
        //getPortNames();

    	// edit the string to reflect your serial port name
        WirelessCom portConnection = new WirelessCom("COM3");

        // vv uncomment the method you want to run or add a new one

        // smaller test no loop
        //portConnection.testRun();

        // goes thru loop, sends data back and forth with serial port
         portConnection.receiveData();

        // close connection to port
        portConnection.closePort();
    }

    public static void main (String[] args) throws IOException {
        // Launch GUI
        //GUI gui=new GUI();
        //gui.launchGUI(args);


        // test wirelesscom
        testWirelessCom();

        // clean up test (Deletes entries with DriveNum == 0)
        //DB.deleteTestEntries(44);
    }
}
