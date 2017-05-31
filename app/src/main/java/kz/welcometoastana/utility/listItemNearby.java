package kz.welcometoastana.utility;

/**
 * Created by nurdaulet on 5/31/17.
 */

public class listItemNearby {
    private String firstName;
    private String secondName;
    private String firstImg;
    private String secondImg;
    private String category;

    public listItemNearby(String firstName, String secondName, String firstImg, String secondImg, String category) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstImg = firstImg;
        this.secondImg = secondImg;
        this.category = category;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public String getSecondImg() {
        return secondImg;
    }

    public String getCategory() {
        return category;
    }
}


