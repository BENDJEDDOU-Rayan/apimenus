package fr.univamu.iut.apimenus;

/**
 * DTO utilisé pour la requête création de menu
 */
public class MenuCreationRequest {

    private String title;
    private String description;
    private float price;

    /**
     * Constructeur par défaut
     */
    public MenuCreationRequest() {
    }

    /**
     * Méthode permettant d'avoir le titre du menu
     *
     * @return String titre du menu
     */
    public String getTitle() {
        return title;
    }

    /**
     * Méthode permettant de définir le titre du menu
     *
     * @param title String titre du menu à définir
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Méthode permettant d'avoir la description du menu
     *
     * @return String description du menu
     */
    public String getDescription() {
        return description;
    }

    /**
     * Méthode qui permet de définir la description du menu
     *
     * @param description String description à définir
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Méthode qui permet d'avoir le prix du menu
     *
     * @return float prix du menu
     */
    public float getPrice() {
        return price;
    }

    /**
     * Méthode qui permet de définir le prix du menu
     *
     * @param price float prix du menu à définir
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
