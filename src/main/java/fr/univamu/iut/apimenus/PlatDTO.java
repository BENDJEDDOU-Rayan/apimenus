package fr.univamu.iut.apimenus;

/**
 * DTO Servant à fetch un plat depuis l'api plats & utilisateurs
 */
public class PlatDTO {

    private int id;
    private String name;
    private String description;
    private float price;

    /**
     * Constructeur par défaut
     */
    public PlatDTO() {}

    /**
     * Méthode permettant de récupérer l'id du plat
     * @return int id du plat
     */
    public int getId() {
        return id;
    }

    /**
     * Méthode permettant de définir l'id du plat
     * @param id int nouveau id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Méthode permettant de récupérer le nom du plat
     * @return String nom du plat
     */
    public String getName() {
        return name;
    }

    /**
     * Méthode permettant de définir le nom du plat
     * @param name String nouveau nom à définir
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Méthode permettant de récupérer la description du plat
     * @return String description du plat
     */
    public String getDescription() {
        return description;
    }

    /**
     * Méthode permettant de définir la description du plat
     * @param description String description du plat
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Méthode permettant de récupérer le prix du plat
     * @return float prix du plat
     */
    public float getPrice() {
        return price;
    }

    /**
     * Méthode permettant de définir le prix du plat
     * @param price float nouveau prix du plat
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
