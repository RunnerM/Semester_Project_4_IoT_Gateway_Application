import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
/**
 * <h3>Database driver</h3>
 * It is fresponsible for database access, serves as a layer between the general java code and JDBC
 * @author  Ali
 * @version 0.9
 */
public class DBDriverManager
{
    private final String Url = "jdbc:postgresql://sep4terrariumnewinstance.ctbb2v6dqmr8.eu-central-1.rds.amazonaws.com/TerrariumDB";
    private final String User = "postgres";
    private final String Password = "postgres123";
    private Connection connection;
    private static DBDriverManager instance = new DBDriverManager();

    public static DBDriverManager getInstance()
    {
        if(instance == null)
        {
            instance = new DBDriverManager();
        }
        return instance;
    }
    public DBDriverManager()
    {
        getConnection();
    }
    public void getConnection()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(Url, User, Password);
            if(connection.isValid(2000)) System.out.println("works");
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    //onText()
    /**
     * This method is called to insert the received measurements into the database
     * @param EUI the EUI of the device where the measurements are coming from
     * @param measurement the measurement data itself
     * @exception SQLException on failed connection, wrong query or other issues stemming from an sql operation
     */
    public void insertMeasurements(String EUI,Measurement measurement) throws SQLException
    {
        String query = "INSERT into MotherBoardData(time,temperature,humidity,co2,lightlevel,eui)" + "VALUeS(?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setTimestamp(1,measurement.getTime());
        statement.setInt(2,measurement.getTemperature());
        statement.setInt(3,measurement.getHumidity());
        statement.setInt(4,measurement.getCO2Level());
        statement.setInt(5,measurement.getLux());
        statement.setString(6,EUI);
        statement.execute();
    }
    //onText()
    /**
     * This method is called to insert the received measurements into the database
     * @param EUI the EUI of the device that we need to retreive the tasks for
     * @param time the time which is considered now, it defines the time window of the task that is retrieved
     * @return boolean[] an array of three booleans, the first entry represents the state of the vent, the second the state of the light, and the third indicates if any tasks existed in the specified time frame, if yes, is set to true, it is false by default
     * @exception SQLException on failed connection, wrong query or other issues stemming from an sql operation
     */
    public boolean[] getActionStates(String EUI, Timestamp time) throws SQLException
    {
        boolean[] results = {false,false,false};
        int terrariumId = -1;
        /*String query = "SELECT motherboardid " + "FROM MotherBoardData" + "WHERE EUI = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,EUI);
        ResultSet rs = statement.executeQuery();
        mbid = rs.getInt("motherboardid");*/
        String query = "SELECT terrariumid FROM terrarium WHERE EUI = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,EUI);
        ResultSet rs = statement.executeQuery();
        rs.next();
        terrariumId = rs.getInt("terrariumid");
        String fiveMinsAgo, now;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        now = "'" +dateFormat.format(new Date(time.getTime())) + "'";
        Date tdate = new Date(time.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(tdate);
        c.add(Calendar.MINUTE, -500);
        fiveMinsAgo = "'" +dateFormat.format(c.getTime())+ "'";
        //+ "ORDER BY tasktime LIMIT 1"
        query = "SELECT togglevent, togglelight FROM tasks WHERE terrariumid = " + terrariumId + " AND tasktime BETWEEN " + fiveMinsAgo + " AND " + now;
        statement = connection.prepareStatement(query);
        rs = statement.executeQuery();
        rs.next();
        if(rs.next())
        {
            results[2] = true;
            results[0] = rs.getBoolean("togglevent");
            results[1] = rs.getBoolean("togglelight");
        }
        return results;
    }

}
