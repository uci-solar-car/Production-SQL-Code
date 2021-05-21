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





    public static void main (String[] args) throws IOException {
        // Launch GUI
        GUI gui = new GUI();

        GUIThread gt = new GUIThread(gui, args);
        gt.start();

        // probably going to need threads

        // test wirelesscom
        WirelessComThread wct = new WirelessComThread(gui);
        wct.start();

        // clean up test (Deletes entries with DriveNum == 0)
        //DB.deleteTestEntries(44);
    }
}
