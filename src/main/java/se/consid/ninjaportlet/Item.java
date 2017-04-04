package se.consid.ninjaportlet;

/**
 * Created by Tim on 2017-04-04.
 */
public class Item {

    private String name;
    private int id;
    private String batchCode;
    private double price;

    public String getName() {return name;}
    public int getId() {return id;}
    public String getBatchCode() {return batchCode;}
    public double getPrice() {return price;}

    public Item(String name, int id, String batchCode, double price) {
        this.name = name;
        this.id = id;
        this.batchCode = batchCode;
        this.price = price;
    }
}
