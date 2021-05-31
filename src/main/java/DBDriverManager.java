import java.sql.*;

public class DBDriverManager
{
    private final String url = "";
    private final String user = "";
    private final String password = "";
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
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    public void insertMeasurements(String EUI,Measurement measurement) throws SQLException
    {
        //idk if the following works
        String query = "INSERT into MotherBoardData(time,temprature,humidity,co2,lightlevel,eui)" + "VALUeS(?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setTimestamp(1,measurement.getTime());
        statement.setInt(2,measurement.getTemperature());
        statement.setInt(3,measurement.getHumidity());
        statement.setInt(4,measurement.getCO2Level());
        statement.setInt(5,measurement.getLux());
        statement.setString(6,EUI);
        statement.executeBatch();
        /*Statement st = connection.createStatement();
        st.executeUpdate(query);
        st.close();
        connection.close();*/
    }
    public boolean getServoState(String EUI) throws SQLException
    {
        String query = "";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        boolean result =rs.
        return result;
    }
    public boolean getLightState(String EUI) throws SQLException
    {
        String query = "";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        boolean result =rs.
        return result;
    }
}
