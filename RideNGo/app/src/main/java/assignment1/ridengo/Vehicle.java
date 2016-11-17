package assignment1.ridengo;

/**
 * Created by resta on 2016-11-17.
 */

public class Vehicle {
    private String plateNum;
    private int vYear;
    private String vMake;
    private String vModel;
    private String vColor;
    public void Vehicle(String pNum,int year,String make,String model,String color){
        this.plateNum = pNum;
        this.vYear = year;
        this.vMake = make;
        this.vModel = model;
        this.vColor = color;
    }

    public String getPlateNum(){
        return this.plateNum;
    }

    public int getYear(){
        return this.vYear;
    }

    public String getMake(){
        return this.vMake;
    }

    public String getModel(){
        return this.vModel;
    }

    public String getColor(){
        return this.vColor;
    }

    @Override
    public String toString() {
        return this.vYear+" "+this.vMake+" "+this.vModel;
    }
}
