package fr.univamu.iut.apimenus;

/**
 * Classe représentant un menu
 */
public class Menu {

    protected int id;
    protected String title;
    protected String description;
    protected float price;

    /**
     * Constructeur de l'objet Menu
     * @param id int référence du menu
     * @param title String titre du menu
     * @param description String description du menu
     * @param price float prix du menu
     */
    public Menu(int id, String title, String description, float price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public Menu() {}

    /**
     * Méthode permettant d'accéder à la référence du menu
     * @return un int qui représente la référence du menu
     */
    public int getId() {
        return id;
    }

    /**
     * Méthode permettant de modifier la référence du menu
     * @param id int à spécifier pour le remplacement
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Méthode permettant d'avoir le titre du menu
     * @return une chaîne de caractère avec le titre du menu
     */
    public String getTitle() {
        return title;
    }

    /**
     * Méthode permettant de changer le titre du menu
     * @param title chaîne de caractère avec le nouveau titre du menu
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Méthode permettant d'avoir la description du menu
     * @return chaîne de caractère avec la description du menu
     */
    public String getDescription() {
        return description;
    }

    /**
     * Méthode permettant de changer la description du menu
     * @param description chaîne de caractère avec la nouvelle description du menu
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Méthode permettant d'avoir le prix du menu
     * @return float le prix du menu
     */
    public float getPrice() {
        return price;
    }

    /**
     * Méthode permettant de changer le prix du menu
     * @param price float le nouveau prix du menu
     */
    public void setPrice(float price) {
        this.price = price;
    }
}