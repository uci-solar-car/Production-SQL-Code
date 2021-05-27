import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class WirelessComThread extends Thread{

    public static GUI gui;

    public WirelessComThread(GUI g){
        gui = g;
    }

    // check available port names
    public void getPortNames(){
        SerialPort[] ports = SerialPort.getCommPorts();

        for (int i = 0; i < ports.length; ++i)
        {
            System.out.println(ports[i].getSystemPortName());
        }
    }

    public void run() {

            getPortNames();

            // change to xbee portname
            String portName = "ttyUSB0";

            try{
                // edit the string to reflect your serial port name
                WirelessCom portConnection = new WirelessCom(portName, gui);

                // goes thru loop, sends data back and forth with serial port
                portConnection.receiveData();

                // close connection to port
                portConnection.closePort();
            }
            catch (IOException e){}
    }
}
