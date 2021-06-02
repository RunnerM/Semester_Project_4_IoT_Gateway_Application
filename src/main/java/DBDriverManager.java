import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Formatter;

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
    public boolean[] getActionStates(String EUI, Timestamp time) throws SQLException
    {
        boolean[] results = {true,true};
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
        String fiveMinsAgo, now;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        now = "'" +dateFormat.format(new Date(time.getTime())) + "'";
        fiveMinsAgo = "'" +dateFormat.format(new Date(time.getTime()-300000))+ "'";


        query = "SELECT togglevent, togglelight FROM tasks WHERE terrariumid = " + terrariumId + "AND time <= " + now + "AND time >" + fiveMinsAgo  ;
        statement = connection.prepareStatement(query);
        rs = statement.executeQuery();
        results[0] = rs.getBoolean("togglevent");
        results[1] = rs.getBoolean("togglelight");




        return results;
    }

}
