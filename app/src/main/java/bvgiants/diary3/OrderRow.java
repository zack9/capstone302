package bvgiants.diary3;

/**
 * Created by kenst on 4/05/2016.
 */
public class OrderRow {

    public int orderID;
    public int orderTypeCode;
    public String date;
    public String time;
    public int userID;
    public int foodId;
    public String location;

    public OrderRow (int orderID, int orderTypeCode, String date, int userID){
        this.orderID = orderID;
        this.orderTypeCode = orderTypeCode;
        this.date = date;
        this.userID = userID;
    }

    public OrderRow (String date, String time, int foodId, String location){
        this.date = date;
        this.time = time;
        this.foodId = foodId;
        this.location = location;
    }


    public String todaysFoodCheck(){
        return date + " " + time + " " + foodId + " " + location;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(int orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
