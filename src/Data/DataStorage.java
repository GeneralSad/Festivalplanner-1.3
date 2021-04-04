package Data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Auteurs: Leon
 * <p>
 * Deze code zorgt ervoor dat een schedule ingeladen en opgeslagen kan worden
 */

public class DataStorage
{

    public static Schedule loadSchedule(String filePath)
    {

        try
        {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            DataObject dataObject = (DataObject) objectInputStream.readObject();

            return (new Schedule(dataObject.getLessons(), dataObject.getTeachers(), dataObject.getGroups(), dataObject.getClassrooms()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void saveSchedule(String filePath, Schedule schedule)
    {
        DataObject dataObject = new DataObject(schedule.getLessonArrayList(), schedule.getTeacherArrayList(), schedule.getGroupArrayList(), schedule.getClassroomArrayList());

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(dataObject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
