package kz.welcometoastana.Pamyatka;

/**
 * Created by nurdaulet on 5/13/17.
 */

public class PamyatkaListItem {
    private String name;
    private String summary;
    private String imageUrl;
    private String category;




    public PamyatkaListItem(String name, String description, String imageUrl, String category){
        this.name = name;
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;

    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSummary() {
        return summary;
    }


    public String getCategory() {


        return category;
    }
}
