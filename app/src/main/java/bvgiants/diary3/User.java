package bvgiants.diary3;

/**
 * Created by kenst on 3/05/2016.
 * Simple class to create a user object.
 *
 */
public class User {

    public String email;
    public String alias;
    public String password;
    public String team;

    public int id;
    public String firstName;
    public String lastName;
    public int height;
    public int weight;
    public int age;
    public String gender;

    public int sugarGoal;
    public int stepGoal;
    public int kilojoulesGoal;
    public int calorieGoal;
    
    public User (int id, String email, String password,String alias, String team){
        this.id = id;
        this.email = email;
        this.password = password;
        this.alias = alias;
        this.team = team;
    }

    //Polymorphic user object to help deal with creating user variables
    public User (int id,String firstName, String lastName, int height, int weight, int age,
                 String gender){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
    }

    public User (int id, int sugarGoal, int stepGoal, int kilojoulesGoal, int calorieGoal){
        this.id = id;
        this.sugarGoal = sugarGoal;
        this.stepGoal = stepGoal;
        this.kilojoulesGoal = kilojoulesGoal;
        this.calorieGoal = calorieGoal;
    }



    public String toString(){
        return email + " " + alias;
    }

    //Method to return a string capable of being stored in the UserTraitsTxtFile

    public String userGoals(){
        return id + " " + sugarGoal + " " + stepGoal + " " + kilojoulesGoal + " " + calorieGoal + "\n";
    }

    public String userTraits(){
        return id + " " + firstName + " " + lastName + " " + height + " " + weight + " " + age + " " + gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getSugarGoal() {
        return sugarGoal;
    }

    public void setSugarGoal(int sugarGoal) {
        this.sugarGoal = sugarGoal;
    }

    public int getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public int getKilojoulesGoal() {
        return kilojoulesGoal;
    }

    public void setKilojoulesGoal(int kilojoulesGoal) {
        this.kilojoulesGoal = kilojoulesGoal;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }
}
