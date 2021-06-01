import java.sql.*;

public class DBDriverManager
{
    private final String Url = "";
    private final String User = "";
    private final String Password = "";
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
        int recordId = -1;
        int mbid = -1;
        int terrariumId = -1;
        int taskId = -1;
        String query = "SELECT motherboardid " + "FROM MotherBoardData" + "WHERE EUI = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,EUI);
        ResultSet rs = statement.executeQuery();
        mbid = rs.getInt("motherboardid");
        query = "SELECT terrariumid" + "FFROM Terrarium" + "WHERE motherboardid = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1,mbid);
        rs = statement.executeQuery();
        terrariumId = rs.getInt("terrariumid");
        query = "SELECT togglevent, togglelight" + "FROM tasks" +  "WHERE terrariumid = ?" + ;
        statement = connection.prepareStatement(query);
        statement.setInt(1,terrariumId);

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
