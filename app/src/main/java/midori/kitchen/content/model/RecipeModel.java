package midori.kitchen.content.model;

/**
 * Created by M. Asrof Bayhaqqi on 3/11/2017.
 */

public class RecipeModel {

    private String id;
    private String recipe;
    private String description;
    private String photo;
    private String owner;
    private int calories;

    public RecipeModel() {
    }

    public RecipeModel(String id, String recipe, String description, String photo, String owner, int calories) {
        this.id = id;
        this.recipe = recipe;
        this.description = description;
        this.photo = photo;
        this.owner = owner;
        this.calories = calories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
