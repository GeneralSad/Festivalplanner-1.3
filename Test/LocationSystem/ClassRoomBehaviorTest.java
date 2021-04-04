package LocationSystem;

import Data.Group;
import Data.Student;
import Simulator.LocationSystem.ClassRoomBehavior;
import Simulator.LocationSystem.Seat;
import Simulator.NPC.NPC;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

class ClassRoomBehaviorTest
{
    /**
     * Luuk van Berkel 2169248 ogp2 tests
     */

    /**
     * checks if it throws an error when there are more npc trying to sit then there are seats
     * addedNPC = 20
     * numberOfSeats = 0
     * result should be true
     */
    @Test
    void testClaimEmptySeatWithNPCOverflowErrorWorkingCheckReturnTrue()
    {
        //__________________________________________________________________
        //Arrange
        //boolean
        boolean succefull = false;
        //Classroom
        ArrayList<Seat> studentSeats = new ArrayList<>();
        Seat teacherSeat = new Seat(null, null, 0);
        ClassRoomBehavior classroom = new ClassRoomBehavior(studentSeats, teacherSeat, null, 0, null);

        //npc
        ArrayList<NPC> npcs = new ArrayList<>();
        String name = "";
        for (int i = 0; i < 20; i++)
        {
            name = name + i;
            npcs.add(new NPC(new Student(name, 0, new Group(""))));
        }

        //__________________________________________________________________
        //act
        try{
            for (int i = 0; i < npcs.size(); i++)
            {
                classroom.claimEmptySeat(npcs.get(i));
            }
        } catch (IllegalArgumentException e){
            succefull = true;
        }

        //__________________________________________________________________
        //assert
        Assert.assertTrue(succefull);
    }


    /**
     * adding check to check if the students get asigent to there correct seat
     * number seats = 13
     * number npc = 20
     * res 13 means true
     */
    @Test
    void testClaimEmptySeatWithTestSubjectsInput13Seats20NPCRes13()
    {
        //__________________________________________________________________
        //Arrange
        //adding seats
        ArrayList<Seat> studentSeats = new ArrayList<>();
        for (int i = 0; i < 13; i++)
        {
            studentSeats.add(new Seat(new Point2D.Double(i, 0), null, 0));
        }
        //adding npc's
        ArrayList<NPC> npcs = new ArrayList<>();
        String name = "";
        for (int i = 0; i < 20; i++)
        {
            name = name + i;
            npcs.add(new NPC(new Student(name, 0, new Group(""))));
        }
        //making lists
        Seat teacherSeat = new Seat(null, null, 0);
        ClassRoomBehavior classroom = new ClassRoomBehavior(studentSeats, teacherSeat, null, 0, null);

        //__________________________________________________________________
        //Act
        try{
            for (int i = 0; i < npcs.size(); i++)
            {
                classroom.claimEmptySeat(npcs.get(i));
            }
        } catch (IllegalArgumentException e){
        }
        int result = classroom.seats.size();
        int expected = 13;

        //__________________________________________________________________
        //assert
        Assert.assertEquals(result, expected);
    }


    /**
     * test if the npc are actually deleted
     * seats = 13
     * npc added = 10
     * npc removed = 3
     * res - 7
     */
    @Test
    void removeStudentTestWith3NPCand13Seats(){
        //__________________________________________________________________
        //Arrange
        //adding seats
        ArrayList<Seat> studentSeats = new ArrayList<>();
        for (int i = 0; i < 13; i++)
        {
            studentSeats.add(new Seat(new Point2D.Double(i, 0), null, 0));
        }
        //adding npc's
        ArrayList<NPC> npcs = new ArrayList<>();
        String name = "";
        for (int i = 0; i < 10; i++)
        {
            name = name + i;
            npcs.add(new NPC(new Student(name, 0, new Group(""))));
        }
        //making lists
        Seat teacherSeat = new Seat(null, null, 0);
        ClassRoomBehavior classroom = new ClassRoomBehavior(studentSeats, teacherSeat, null, 0, null);
        //filling seat
        for (int i = 0; i < npcs.size(); i++)
        {
            classroom.claimEmptySeat(npcs.get(i));
        }


        //_____________________________________________________________
        //Act
        //leaving seat
        for (int i = 0; i < 3; i++)
        {
            classroom.leaveFilledSeat(npcs.get(i));
        }

        //checks students
        int result= 0;
        for (int i = 0; i < studentSeats.size(); i++)
        {
            if (studentSeats.get(i).getStudent() != null){
                result++;
            }

        }

        int expected = 7;


        //______________________________________________________________
        //assert
        Assert.assertEquals(expected, result);
    }

    /**
     * test if there is a error message that is deliberately displayed so the programmer is informed of a remove to much
     * seats = 0
     * removes = 3
     */
    @Test
    void removeStudentTestWithErrorMessageWith0SeatsAnd3NPCResTrue(){
        //__________________________________________________________________
        //Arrange
        //boolean
        boolean succefull = false;
        //Classroom
        ArrayList<Seat> studentSeats = new ArrayList<>();
        Seat teacherSeat = new Seat(null, null, 0);
        ClassRoomBehavior classroom = new ClassRoomBehavior(studentSeats, teacherSeat, null, 0, null);

        //npc
        ArrayList<NPC> npcs = new ArrayList<>();
        String name = "";
        for (int i = 0; i < 3; i++)
        {
            name = name + i;
            npcs.add(new NPC(new Student(name, 0, new Group(""))));
        }

        //__________________________________________________________________
        //act
        try{
            for (int i = 0; i < npcs.size(); i++)
            {
                classroom.leaveFilledSeat(npcs.get(i));
            }
        } catch (IllegalArgumentException e){
            succefull = true;
        }

        //__________________________________________________________________
        //assert
        Assert.assertTrue(succefull);
    }


}