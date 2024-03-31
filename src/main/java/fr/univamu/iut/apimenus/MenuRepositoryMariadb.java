package fr.univamu.iut.apimenus;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe qui permet d'exécuter les requêtes sql
 */
public class MenuRepositoryMariadb implements MenuRepositoryInterface, Closeable {

    /**
     * Accès à la base de données (session)
     */
    protected Connection dbConnection;

    /**
     * Constructeur de la classe
     *
     * @param infoConnection chaîne de caractères avec les informations de connexion
     *                       (p.ex. jdbc:mariadb://mysql-[compte].alwaysdata.net/[compte]_library_db
     * @param user           chaîne de caractères contenant l'identifiant de connexion à la base de données
     * @param pwd            chaîne de caractères contenant le mot de passe à utiliser
     */
    public MenuRepositoryMariadb(String infoConnection, String user, String pwd) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    /**
     * Méthode void permettant la fermeture de la connection avec la base de données
     */
    @Override
    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Méthode permettant de récupérer un menu grâce à son id
     * @param id int id du menu
     * @return Menu menu trouvé, si non une exception sql
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public Menu getMenu(int id) {

        Menu selectedMenu = null;

        String query = "SELECT * FROM Menu WHERE id_menu=?";

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, String.valueOf(id));

            // exécution de la requête
            ResultSet result = ps.executeQuery();

            // récupération du premier (et seul) tuple résultat
            // (si la référence du menu est valide)
            if (result.next()) {
                String title = result.getString("title");
                String description = result.getString("description");
                float price = result.getFloat("price");

                // création et initialisation de l'objet Menu
                selectedMenu = new Menu(id, title, description, price);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedMenu;
    }

    /**
     * Méthode permettant de récupérer tous les menus de la base de données
     * @return ArrayList<Menu> une arraylist contenant tous les menus trouvés
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public ArrayList<Menu> getAllMenu() {
        ArrayList<Menu> listMenu;

        String query = "SELECT * FROM Menu";

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            // exécution de la requête
            ResultSet result = ps.executeQuery();

            listMenu = new ArrayList<>();

            // récupération du premier (et seul) tuple résultat
            while (result.next()) {
                int id = result.getInt("id_menu");
                String title = result.getString("title");
                String description = result.getString("description");
                float price = result.getFloat("price");

                // création du menu courant
                Menu currentMenu = new Menu(id, title, description, price);

                listMenu.add(currentMenu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listMenu;
    }

    /**
     * Méthode permettant de mettre à jours un menu existant
     * @param id          id de l'objet Menu à mettre à jours
     * @param title       String nouveau titre
     * @param description String nouvelle description
     * @param price       float nouveau prix
     * @return true si la mise à jours a été faite, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean updateMenu(int id, String title, String description, float price) {
        String query = "UPDATE Menu SET title=?, description=?, price=?  where id_menu=?";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setFloat(3, price);
            ps.setInt(4, id);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Méthode permettant de créer un menu
     * @param title       String titre du menu à créer
     * @param description String description du menu à créer
     * @param price       float prix du menu à créer
     * @return true si la création s'est bien déroulée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean createMenu(String title, String description, float price) {
        String query = "INSERT INTO Menu (title, description, price) VALUES (?, ?, ?)";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setFloat(3, price);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Méthode permettant de supprimer un menu de la base de données
     * @param id int référence du menu à supprimer
     * @return true si la suppression s'est bien déroulée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean deleteMenu(int id) {
        String query = "DELETE FROM Menu WHERE id_menu=?";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Méthode permettant de récupérer tous les plats associés à un menu
     * @param id_menu int id du menu
     * @return true si l'opération s'est bien passée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public ArrayList<MenuPlatDTO> getAllPlatFromMenu(int id_menu) {
        ArrayList<MenuPlatDTO> listPlats;

        String query = "SELECT * FROM Plat_menu where id_menu=?";

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id_menu);


            // exécution de la requête
            ResultSet result = ps.executeQuery();

            listPlats = new ArrayList<>();

            // récupération du premier (et seul) tuple résultat
            while (result.next()) {
                int id = result.getInt("id_menu");
                int id_plat = result.getInt("id_plat");

                // Création du DTO courant
                MenuPlatDTO courant = new MenuPlatDTO();
                courant.setId_menu(id);
                courant.setId_plat(id_plat);

                listPlats.add(courant);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listPlats;
    }

    /**
     * Méthode qui permet d'associer un plat à un menu
     * @param id_menu int id du menu
     * @param id_plat int id du plat à associer
     * @return true si l'association s'est effectuée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean addPlatToMenu(int id_menu, int id_plat) {
        String query = "INSERT INTO Plat_menu (id_menu, id_plat) VALUES (?, ?)";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id_menu);
            ps.setInt(2, id_plat);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Méthode permettant d'associers plusieurs plats à un menu
     * @param id_menu int id du menu
     * @param listPlatId List<Integer> liste des id à associer au menu
     * @return true si les associations se sont bien déroulées, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean addAllPlatToMenu(int id_menu, List<Integer> listPlatId) {
        String query = "INSERT INTO Plat_menu (id_menu, id_plat) VALUES (?, ?)";
        int nbRowModified = 0;
        for(int i = 0; i < listPlatId.size(); ++i){
            try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
                ps.setInt(1, id_menu);
                ps.setInt(2, listPlatId.get(i));
                // exécution de la requête
                nbRowModified = ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return (nbRowModified != 0);
    }

    /**
     * Méthode qui permet de dissocier un plat d'un menu
     * @param id_menu int id du menu cible
     * @param id_plat int id du plat à dissocier
     * @return true si la disassociation s'est bien déroulée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean removePlatFromMenu(int id_menu, int id_plat) {
        String query = "DELETE FROM Plat_menu WHERE id_menu=? AND id_plat=?";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id_menu);
            ps.setInt(2, id_plat);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Méthode qui permet de dissocier tous les plats associés à un menu
     * @param id_menu int id du menu cible
     * @return true si l'opération s'est bien déroulée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean removeAllPlatsFromMenu(int id_menu) {
        String query = "DELETE FROM Plat_menu WHERE id_menu=?";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id_menu);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }
}