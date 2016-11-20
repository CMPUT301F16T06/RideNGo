package assignment1.ridengo.UnitTesting;

import junit.framework.TestCase;

import assignment1.ridengo.Vehicle;

/**
 * Created by Rui on 2016-11-19.
 */
public class VehicleTest extends TestCase {

    public void testGetPlateNum() throws Exception {
        Vehicle vehicle = new Vehicle("1", 2016,"Rolls Royce", "Ghost", "Black");
        assertTrue(vehicle.getPlateNum().trim().equals("1"));
    }

    public void testGetYear() throws Exception {
        Vehicle vehicle = new Vehicle("Plate", 2016,"Rolls Royce", "Ghost", "Black");
        assertTrue(vehicle.getYear() == 2016);
    }

    public void testGetMake() throws Exception {
        Vehicle vehicle = new Vehicle("Plate", 2016,"Rolls Royce", "Ghost", "Black");
        assertTrue(vehicle.getMake().trim().equals("Rolls Royce"));
    }

    public void testGetModel() throws Exception {
        Vehicle vehicle = new Vehicle("Plate", 2016,"Rolls Royce", "Ghost", "Black");
        assertTrue(vehicle.getModel().equals("Ghost"));
    }

    public void testGetColor() throws Exception {
        Vehicle vehicle = new Vehicle("Plate", 2016,"Rolls Royce", "Ghost", "Black");
        assertTrue(vehicle.getColor().equals("Black"));
    }

}