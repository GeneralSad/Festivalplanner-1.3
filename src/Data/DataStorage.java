package Data;

import java.io.*;

public class DataStorage
{

    public static void saveSchedule(String filePath, Schedule schedule)
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

    public static Schedule loadSchedule(String filePath)
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
