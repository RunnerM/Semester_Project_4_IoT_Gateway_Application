import java.sql.Date;
import java.sql.Time;

public class Measurement
{
    int terrariumId;
    Time time;
    Date date;
    int Temperature;
    int Humidity;
    int CO2Level;
    int lux;
    public Measurement(int tid,Time time, Date date, int temperature, int humidity, int CO2Level, int lux)
    {
        terrariumId = tid;
        this.time = time;
        this.date = date;
        Temperature = temperature;
        Humidity = humidity;
        this.CO2Level = CO2Level;
        this.lux=lux;

    }

    public int getTerrariumId() {
        return terrariumId;
    }

    public void setTerrariumId(int terrariumId) {
        this.terrariumId = terrariumId;
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

    public void setTemperature(int temperature)
    {
        Temperature = temperature;
    }

    public float getHumidity()
    {
        return Humidity;
    }

    public void setHumidity(int humidity)
    {
        Humidity = humidity;
    }

    public int getCO2Level()
    {
        return CO2Level;
    }

    public void setCO2Level(int CO2Level)
    {
        this.CO2Level = CO2Level;
    }

    public int getLux() {
        return lux;
    }

    public void setLux(int lux) {
        this.lux = lux;
    }

    @Override
    public String toString()
    {
        return "Measurement{" +
                ", TerrariumID=" + terrariumId +
                ", time=" + time +
                ", date=" + date +
                ", Temperature=" + Temperature +
                ", Humidity=" + Humidity +
                ", CO2Level=" + CO2Level +
                ", Lux=" + lux +
                '}';
    }
}