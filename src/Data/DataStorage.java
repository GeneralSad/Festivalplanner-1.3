package Data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataStorage
{

    public void saveSchedule(String filePath, Schedule schedule) throws Exception
    {

        try
        {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(schedule);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public Schedule loadSchedule(String filePath) throws Exception
    {

        try
        {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (Schedule) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null; //Is dit het beste om te doen?
    }

}
