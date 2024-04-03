package fr.univamu.iut.apimenus;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilisée pour récupérer les informations nécessaires à la ressource
 */
public class MenuService {

    /**
     * Objet permettant d'accéder au dépôt où sont stockées les informations sur les menus
     */
    protected MenuRepositoryInterface menuRepo;

    /**
     * Constructeur permettant d'injecter l'accès aux données
     *
     * @param menuRepo objet implémentant l'interface d'accès aux données
     */
    public @Inject MenuService(MenuRepositoryInterface menuRepo) {
        this.menuRepo = menuRepo;
    }

    /**
     * Méthode retournant les informations sur les menus au format JSON
     *
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getAllMenusJSON() {

        ArrayList<Menu> allMenus = menuRepo.getAllMenu();

        // création du json et conversion de la liste de menus
        String result = null;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(allMenus);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * Méthode retournant au format JSON les informations sur un menu recherché
     *
     * @param id la référence du menu recherché
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getMenuJSON(int id) {
        String result = null;
        Menu myMenu = menuRepo.getMenu(id);

        // si le menu a été trouvé
        if (myMenu != null) {

            // création du json et conversion du menus
            try (Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(myMenu);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    public String getAllPlatFromMenuJson(int id_menu) {
        ArrayList<MenuPlatDTO> listPlatDTO = menuRepo.getAllPlatFromMenu(id_menu);

        // création du json et conversion de la liste des plats associées au menu
        String result = null;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(listPlatDTO);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * Méthode permettant de mettre à jours les informations d'un menu
     *
     * @param id   référence du menu à mettre à jours
     * @param menu les nouvelles informations a utiliser
     * @return true si le menu a pu être mis à jours, false si non
     */
    public boolean updateMenu(int id, Menu menu) {
        return menuRepo.updateMenu(id, menu.title, menu.description, menu.price);
    }

    /**
     * Méthode permettant de créer un nouveau menu
     * @param title String titre du menu
     * @param description String description du menu
     * @param listPlat list de plats à associer
     * @return true si la création s'est bien déroulée, false si non
     */
    public boolean createMenu(String title, String description, List<Integer> listPlat) {
        return menuRepo.createMenu(title, description, listPlat);
    }

    /**
     * Méthode permettant de supprimer un menu
     * @param id int identifiant du menu à supprimer
     * @return true si la suppression s'est bien déroulée, false si non
     */
    public boolean deleteMenu(int id) {
        return menuRepo.deleteMenu(id);
    }

    /**
     * Méthode permettant d'associer un plat à un menu
     * @param id_menu int id du menu
     * @param id_plat int id du plat à associer
     * @return true si l'association s'est bien déroulée, false si non
     */
    public boolean addPlatToMenu(int id_menu, int id_plat) {
        return menuRepo.addPlatToMenu(id_menu, id_plat);
    }

    /**
     * Méthode permettant d'associer plusieurs plats à un menu
     * @param id_menu int id du menu
     * @param listPlatId List<Integer> liste des id des plats à associer au menu
     * @return true si les associations se sont bien déroulées, false si non
     */
    public boolean addAllPlatToMenu(int id_menu, List<Integer> listPlatId){
        return menuRepo.addAllPlatToMenu(id_menu, listPlatId);
    }

    /**
     * Méthode permettant de dissocier un plat d'un menu
     * @param id_menu int id du menu
     * @param id_plat int id du plat à associer
     * @return true si la disassociation s'est bien déroulée, false si non
     */
    public boolean removePlatFromMenu(int id_menu, int id_plat) {
        return menuRepo.removePlatFromMenu(id_menu, id_plat);
    }

    /**
     * Méthode permettant de dissocier tous les plats d'un menu
     * @param id_menu int id du menu
     * @return true si les disassociation se sont bien déroulées, false si non
     */
    public boolean removeAllPlatsFromMenu(int id_menu) {
        return menuRepo.removeAllPlatsFromMenu(id_menu);
    }

}
