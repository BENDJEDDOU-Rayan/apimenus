package fr.univamu.iut.apimenus.dto;

import java.util.List;

/**
 * DTO servant à associer plusieurs plats à un menu
 */
public class MenuListPlatDTO {

    private int id_menu;
    private List<Integer> listPlatId;

    /**
     * Constructeur par défaut
     */
    public MenuListPlatDTO() {}

    /**
     * Méthode permettant d'avoir l'id du menu
     * @return int id du menu
     */
    public int getId_menu() {
        return id_menu;
    }

    /**
     * Méthode permettant de définir l'id du menu
     * @param id_menu int id du menu à définir
     */
    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    /**
     * Méthode permettant d'avoir la liste des plats
     * @return List<Integer> liste des plats (leur id)
     */
    public List<Integer> getListPlatId() {
        return listPlatId;
    }

    /**
     * Méthode permettant de définir la liste des plats
     * @param listPlatId List<Integer> liste des plats à définir
     */
    public void setListPlatId(List<Integer> listPlatId) {
        this.listPlatId = listPlatId;
    }
}
