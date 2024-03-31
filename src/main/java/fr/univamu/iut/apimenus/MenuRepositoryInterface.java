package fr.univamu.iut.apimenus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Interface d'accès aux données des menus
 */
public interface MenuRepositoryInterface {

    /**
     * Méthode fermant le dépôt où sont stockées les informations sur les menus
     */
    void close();

    /**
     * Méthode qui retourne un objet Menu en fonction de l'id passé en paramètre
     *
     * @param id_menu int id du menu
     * @return Objet Menu
     */
    Menu getMenu(int id_menu);

    /**
     * Méthode qui retourne une ArrayList de tous les objets Menu
     * @return ArrayList d'objet Menu
     */
    ArrayList<Menu> getAllMenu();

    /**
     * Méthode qui met à jours tous les attributs d'un objet Menu dont l'id est passé en paramètre
     *
     * @param id_menu          id de l'objet Menu à mettre à jours
     * @param title       String nouveau titre
     * @param description String nouvelle description
     * @param price       float nouveau prix
     * @return true si le menu existe et la mise à jours a été faite, false sinon
     */
    boolean updateMenu(int id_menu, String title, String description, float price);

    /**
     * Méthode qui permet de créer un objet Menu
     *
     * @param title       String titre du menu à créer
     * @param description String description du menu à créer
     * @param price       float prix du menu à créer
     * @return true si le menu a été crée, false sinon
     */
    boolean createMenu(String title, String description, float price);

    /**
     * Méthode qui permet de supprimer un menu en passant l'id en paramètre
     *
     * @param id_menu int référence du menu à supprimer
     * @return true si le menu a été supprimé, false sinon
     */
    boolean deleteMenu(int id_menu);

    ArrayList<MenuPlatDTO> getAllPlatFromMenu(int id_menu);

    /**
     * Méthode qui permet d'associer un plat à un menu
     * @param id_menu id du menu
     * @param id_plat id du plat à associer
     * @return true si l'association s'est bien déroulée, false si non
     */
    boolean addPlatToMenu(int id_menu, int id_plat);

    boolean addAllPlatToMenu(int id_menu, List<Integer> listPlatId);

    boolean removePlatFromMenu(int id_menu, int id_plat);

    boolean removeAllPlatsFromMenu(int id_menu);
}