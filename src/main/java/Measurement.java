import java.sql.Date;
import java.sql.Time;

public class Measurement
{
    int Gym_Id;
    Time time;
    Date date;
    float Temperature;
    float Humidity;
    float CO2Level;

    public Measurement(Time time, Date date, float temperature, float humidity, float CO2Level)
    {
        this.time = time;
        this.date = date;
        Temperature = temperature;
        Humidity = humidity;
        this.CO2Level = CO2Level;
    }


    public Time getTime()
    {
        return time;
    }

    public void setTime(Time time)
    {
        this.time = time;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public float getTemperature()
    {
        return Temperature;
    }

    public void setTemperature(float temperature)
    {
        Temperature = temperature;
    }

    public float getHumidity()
    {
        return Humidity;
    }

    public void setHumidity(float humidity)
    {
        Humidity = humidity;
    }

    public float getCO2Level()
    {
        return CO2Level;
    }

    public void setCO2Level(float CO2Level)
    {
        this.CO2Level = CO2Level;
    }

    @Override
    public String toString()
    {
        return "Measurement{" +
                ", time=" + time +
                ", date=" + date +
                ", Temperature=" + Temperature +
                ", Humidity=" + Humidity +
                ", CO2Level=" + CO2Level +
                '}';
    }
}