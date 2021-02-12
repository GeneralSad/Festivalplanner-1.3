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
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(schedule);
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
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            return (Schedule) objectInputStream.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null; //Is dit het beste om te doen?
    }

}
