package fr.univamu.iut.apimenus.dto;

/**
 * DTO servant à associer un plat à un menu
 */
public class MenuPlatDTO {

    private int id_menu;
    private int id_plat;

    /**
     * Constructeur par défaut
     */
    public MenuPlatDTO() {}

    /**
     * Méthode permettant de récupérer l'id du menu
     * @return int id du menu
     */
    public int getId_menu() {
        return id_menu;
    }

    /**
     * Méthode permettant de définir l'id du menu
     * @param id_menu int nouveau id du menu
     */
    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    /**
     * Méthode permettant de récupérer l'id du plat
     * @return int id du plat
     */
    public int getId_plat() {
        return id_plat;
    }

    /**
     * Méthode permettant de définir l'id du plat
     * @param id_plat int nouveau id du plat
     */
    public void setId_plat(int id_plat) {
        this.id_plat = id_plat;
    }
}
