package fr.univamu.iut.apimenus;

/**
 * DTO servant aux calculs du prix d'un menu
 */
public class MenuUpdatePriceDTO {

    private float price;

    /**
     * Constructeur par défaut
     */
    public MenuUpdatePriceDTO() {}

    /**
     * Méthode permettant de récupérer le prix
     * @return float prix
     */
    public float getPrice() {
        return price;
    }

    /**
     * Méthode permettant définir le prix
     * @param price float nouveau prix
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
