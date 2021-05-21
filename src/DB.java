/*-----------------------------------------------------------------------------------------------------------*/
//          Developer 1: Kunal Buty                                                                          //
/*-----------------------------------------------------------------------------------------------------------*/

import java.sql.*;

public class DB {
    private static String dbUrl = "jdbc:mysql://localhost:3306/SolarCar?allowPublicKeyRetrieval=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    /*-----------------------------------------------------------------------------------------------------------/*
    //  NOTE: These credentials should be stored in a text file that is not in GitHub for security purposes     //
    /*----------------------------------------------------------------------------------------------------------*/
    private static String dbUsername = "solarCar";
    private static String dbPassword = "zot";


    //initialize a connection to MySQL DB
    private static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            return con;
        }
        catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }
    //Terminate connection to MySQL DB
    private static void endConnection(Connection con) {
        try {
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    //Upload data into MySQL DB
    public static void upload(DataEntry entry) {
        try {
            Connection con=DB.getConnection();
            String query = "INSERT INTO Telemetry (id, Battery_Temperature,Battery_Voltage_Out,Speed,Drive_Number,Time_Stamp,Battery_Charge) VALUES (?,?,?,?,?,NOW(),?) ;";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, entry.rowID);
            preparedStmt.setFloat(2, entry.batteryTemp);
            preparedStmt.setFloat(3, entry.batteryVOut);
            preparedStmt.setInt(4, entry.speed);
            preparedStmt.setInt(5, entry.driveNum);
            preparedStmt.setInt(6, entry.batteryCharge);
            preparedStmt.executeUpdate();
            DB.endConnection(con);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // deletes test entries
    public static void deleteTestEntries(int driveNum){
        try {
            Connection con=DB.getConnection();
            String query = "DELETE FROM Telemetry WHERE Drive_Number = " + driveNum;
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.executeUpdate();
            DB.endConnection(con);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // returns -1 if error
    // returns 0 if doesn't exist yet
    public static int entryExists(int driveNum, int rowID){
        try{
            Connection con=DB.getConnection();
            String query = "SELECT count(*) FROM Telemetry WHERE Drive_Number = ? AND id = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, Integer.toString(driveNum));
            preparedStmt.setString(2, Integer.toString(rowID));
            ResultSet result = preparedStmt.executeQuery();

            if (result.next()){
                return result.getInt(1);
            }
            DB.endConnection(con);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    //query database and get the number of rows of data that you want from a particular drive
    // driveNum = drive ID
    static void getData(int numRows, int driveNum, int[][] results) throws SQLException {
        //this will update the results array such that index results[][0]=batteryTemp, [][1]=batteryVout,[][1]=speed
        Connection con=DB.getConnection();
        Statement stmt = con.createStatement();
        String query = "SELECT Battery_Temperature,Battery_Voltage_Out,Speed,Battery_Charge From Telemetry WHERE Drive_Number =" + driveNum +" ORDER BY id LIMIT " + numRows +";";
        ResultSet rs = stmt.executeQuery(query);
        int i=0;
        while(rs.next()) {
            results[0][i]=rs.getInt("Battery_Temperature");
            results[1][i]=rs.getInt("Battery_Voltage_Out");
            results[2][i]=rs.getInt("Speed");
            results[3][i]=rs.getInt("Battery_Charge");
            i++;
        }
        DB.endConnection(con);
    }


}





