
import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
    public void closePort(){
        port.closePort();
    }

    // waits 'ms' milliseconds
    public static void delay(int ms){
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    // hmmm idk how we want to add a stop mechanism
    // can probably check console input for stop command

    public void receiveData() throws IOException{
        while (true){

            // stores the msg char by char
            String msg = "";

            //System.out.println(inputstream.available());

            if (inputstream.available() > 0){
                // prints & uploads message to database
                try
                {
                    while(inputstream.available() > 0)
                        msg += (char)inputstream.read();
                    inputstream.close();

                    uploadData(msg);
                } catch (Exception e) { e.printStackTrace(); }

                // informing arduino that data has been received
                // can turn this into an ack packet
                // later imitate ack system in computer networks
                outputstream.write("received".getBytes());
            }
            WirelessCom.delay(3000);

        }
    }


    public void testRun() throws IOException{
        // test without serial port
        uploadData("1,1,1,0,1\n1,2,3,0,1");

        // looks for a single data entry from serial port
        while (true){
            // if it keeps coming out as 0's then no data is being received
            System.out.println(inputstream.available());

            if (inputstream.available() > 0){
                String msg = "";
                // prints message
                try
                {
                    while(inputstream.available() > 0)
                        msg += (char)inputstream.read();
                    inputstream.close();

                    System.out.println("msg: " + msg);
                    uploadData(msg);
                } catch (Exception e) { e.printStackTrace(); }

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
    private boolean uploadData(String data){
        try{
            String[] rows = data.split("\n");

            // iterates through the rows and enters each one
            for (int r = 0; r < rows.length; ++r){
                System.out.println("Parsing row from Serial Port: " + rows[r]);

                String[] strEntries = rows[r].split(",");

                if (strEntries.length != 5){
                    throw new Exception("incorrect data-entry format: Length = " + strEntries.length);
                }

                // convert to array of integers
                int[] entries = new int[strEntries.length];

                for (int i = 0; i < strEntries.length; ++i){
                    entries[i] = Integer.parseInt(strEntries[i]);
                }

                DB.upload(entries[0], entries[1], entries[2], entries[3], entries[4]);

                System.out.println("Entry - " + '"' + rows[r] + '"' + " uploaded successfully\n");
            }
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}
