
import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class WirelessCom {
    private SerialPort port;

    private InputStream inputstream;
    private OutputStream outputstream;

    // init
    public WirelessCom(String portName) throws IOException {
        port = SerialPort.getCommPort(portName);

        System.out.println("Port Opened: " + port);

        port.setComPortParameters(9600, 8, 1, 0);

        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        // attempts to open port, will continue looping if it's unable to connect
        while (!port.isOpen()) {
            System.out.println("not connected");
            port.openPort();
            delay(1000);
        }
        System.out.println("Port successfully opened :)");

        // sets up input/output streams for data exchange
        inputstream = port.getInputStream();

        // clear InputStream of any unread data from last time
        inputstream.skip(inputstream.available());

        outputstream = port.getOutputStream();

    }

    // close port when done
    public void closePort() {
        port.closePort();
    }

    // waits 'ms' milliseconds
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    // no stop mechanism implemented yet
    public void receiveData() throws IOException {
        while (true) {

            // stores the msg char by char
            String msg = "";

            //System.out.println(inputstream.available());

            if (inputstream.available() > 0) {
                // prints & uploads message to database
                try {
                    while (inputstream.available() > 0)
                        msg += (char) inputstream.read();
                    inputstream.close();

                    System.out.println("msg received: " + msg);

                    ArrayList<Integer> ids = uploadData(msg);

                    // send back a list of all the ids received
                    outputstream.write(generateReturnMsg(ids).getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            WirelessCom.delay(5000); // read every minute (later)
        }
    }

    private boolean validateChecksum(String data) {
        // strEntries[6] is checksum
        return true; // placeholder
    }

    private int generateChecksum(String msg){
        return 1; // placeholder
    }

    // returns numIdsReceived, ids ... , checksum
    private String generateReturnMsg(ArrayList<Integer> ids){
        if (ids.size() == 0){
            return "";
        }

        String returnMsg = Integer.toString(ids.size()) + ";";


        Iterator<Integer> iter = ids.iterator();
        while(iter.hasNext()){
            returnMsg += "," + iter.next();
        }

        returnMsg += ";" + generateChecksum(returnMsg) + "\n"; // need the "\n" so xbee can call readline()

        return returnMsg;
    }

    // purely for testing
    public void testRun() throws IOException {
        // test without serial port
        uploadData("1,1,1,0,1\n1,2,3,0,1");

        // looks for a single data entry from serial port
        while (true) {
            // if it keeps coming out as 0's then no data is being received
            System.out.println(inputstream.available());

            if (inputstream.available() > 0) {
                String msg = "";
                // prints message
                try {
                    while (inputstream.available() > 0)
                        msg += (char) inputstream.read();
                    inputstream.close();

                    System.out.println("msg: " + msg);
                    uploadData(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // informing arduino that data has been received
                // can turn this into an ack packet
                // later imitate ack system in computer networks
                outputstream.write("received".getBytes());
                break;
            }

            delay(2000);
        }

    }


    // returns whether or not the string was properly formatted for upload
    // change this to return the list of ids (integers?)
    // rowID ; battery_temperature, Battery_voltage_out, Battery_charge, speed, driveNum; checksum
    //    0       1                        2                      3       4,     5            last
    // returns the ids received
    private ArrayList<Integer> uploadData(String data) {
        // remove \n from data, split by ;
        String[] rows = data.replace("\n", "").split(";");

        ArrayList<Integer> ids = new ArrayList(); // records ids successfully received

        if (rows.length < 3){ // numRows, data, checksum
            // no substantial data received
            return ids;
        }

        // validate checksum (would require resend of all data)
        if (!validateChecksum(rows[rows.length - 1])) {
            System.out.println("Checksum failed for data: " + data);
            return ids;
        }

        // iterates through the data-entries to enter each one
        // for now, only takes one row
        for (int r = 1; r < rows.length - 1; ++r) {
            System.out.println("Parsing row from Serial Port: " + rows[r]);

            String[] strEntries = rows[r].split(",");

            // Problem with entry
            if (strEntries.length != 5) {
                System.out.println("Incorrect entry format: " + rows[r]);
                continue;
            }

            try{ // enter data
                DataEntry entry = parseData(rows[0], strEntries);

                int check = DB.entryExists(entry.driveNum, entry.rowID);

                // check db for dupe rowID
                if (check == 0){
                    DB.upload(entry);
                }
                else if (check == -1){ // error
                    System.out.println("error in checking-dupe, not uploaded: " + rows[r]);
                    continue;
                }
                // else: exists already => record rowID to inform xbee it no longer
                // needs to send this entry

                // record IDs for successful entries
                ids.add(entry.rowID);

                System.out.println("Entry - " + '"' + rows[r] + '"' + " uploaded successfully (or dupe row_id)\n");
            }
            catch(Exception e){
                System.out.println("Error in entry of: " + rows[r]);
            }
        }
        return ids;
    }

    // parses a single row of data, returns in DataEntry format
    private DataEntry parseData(String row_id, String[] strEntries) {
        DataEntry entry = new DataEntry();

        entry.rowID = Integer.parseInt(row_id);
        entry.batteryTemp = Float.parseFloat(strEntries[0]);
        entry.batteryVOut = Float.parseFloat(strEntries[1]);
        entry.speed = Integer.parseInt(strEntries[2]);
        entry.driveNum = Integer.parseInt(strEntries[3]);
        entry.batteryCharge = Integer.parseInt(strEntries[4]);
        // 6 is checksum, not uploaded to db

        return entry;
    }
}

