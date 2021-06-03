import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * <h3>Measurement</h3>
 * This holds the measurement values
 * @version 1.0
 */
public class Measurement
{
    int terrariumId;
    Timestamp time;
    int Temperature;
    int Humidity;
    int CO2Level;
    int lux;
    public Measurement(int tid, Timestamp time, int temperature, int humidity, int CO2Level, int lux)
    {
        terrariumId = tid;
        this.time = time;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getTemperature()
    {
        return Temperature;
    }

    public void setTemperature(int temperature)
    {
        Temperature = temperature;
    }

    public int getHumidity()
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
                ", Temperature=" + Temperature +
                ", Humidity=" + Humidity +
                ", CO2Level=" + CO2Level +
                ", Lux=" + lux +
                '}';
    }
}