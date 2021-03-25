package LocationSystem;

import Simulator.LocationSystem.LocationDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocationDatabaseTest
{
    /**
     * Luuk van Berkel 2169248 OGP2 testopdracht voor proftaak
     */

    /**
     * tests if the correct amount of seats is found by the algorithm that searches the area
     * testvalue: (24, 32) as index of the map.
     * expectedResult: 17 seats
     */
    @Test
    void testCollectSeatsForCorrectAmountWith24and32Res17()
    {
        //Arrange
        LocationDatabase locationDatabase = new LocationDatabase();

        //Act
        int length = locationDatabase.collectSeats(24, 32).size();
        int expectedLength = 17;

        //Assert
        Assertions.assertEquals(length, expectedLength);
    }

    /**
     * tests if the correct amount of seats is found by the algorithm that searches the area
     * testvalue: (27, 32) as index of the map.
     * expectedResult: 9 seats
     */
    @Test
    void testCollectSeatsFromCorrectAmountWith27and32Res9()
    {
        //Arrange
        LocationDatabase locationDatabase = new LocationDatabase();

        //Act
        int length = locationDatabase.collectSeats(27, 32).size();
        int expectedLength = 9;

        //Assert
        Assertions.assertEquals(length, expectedLength);
    }

    /**
     * tests if the correct amount of seats is found by the algorithm that searches the area
     * testvalue: (26,3) as index of the map.
     * expectedResult: 0 seats
     */
    @Test
    void testCollectSeatsFromCorrectAmountWith26and3Res0(){
        //Arrange
        LocationDatabase locationDatabase = new LocationDatabase();

        //Act
        int length = locationDatabase.collectSeats(26, 3).size();
        int expectedLength = 0;

        //Assert
        Assertions.assertEquals(length, expectedLength);
    }


}