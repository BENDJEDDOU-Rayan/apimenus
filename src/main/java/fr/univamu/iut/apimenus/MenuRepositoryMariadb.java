package fr.univamu.iut.apimenus;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui permet d'exécuter les requêtes sql
 */
public class MenuRepositoryMariadb implements MenuRepositoryInterface, Closeable {

    /**
     * Accès à la base de données (session)
     */
    protected Connection dbConnection;

    private String apiPlatUrl = "http://localhost:8080/APII-1.0-SNAPSHOT/api/";

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
                String author = result.getString("author");
                String title = result.getString("title");
                String description = result.getString("description");
                float price = result.getFloat("price");
                Timestamp creationDate = result.getTimestamp("creationDate");

                // création et initialisation de l'objet Menu
                selectedMenu = new Menu(id, author, title, description, price, creationDate);
                ArrayList<PlatDTO> listPlat = fetchPlatDTOFromApi(selectedMenu.id);
                selectedMenu.setListPlat(listPlat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedMenu;
    }

    /**
     * Méthode qui permet de récupérer les plats d'un menu à partir de l'api plats & utilisateurs.
     * Les plats sont récupérés à travers des DTO.
     * @param id_menu id du menu
     * @return ArrayList<DTO> ArrayList contenant tous les plats
     */
    public ArrayList<PlatDTO> fetchPlatDTOFromApi(int id_menu) {
        String getAllPlatQuery = "SELECT id_plat FROM Plat_menu where id_menu=?";
        ArrayList<PlatDTO> listMenuPlatDTO = new ArrayList<>();
        int nbRowModified;
        try(PreparedStatement ps = dbConnection.prepareStatement(getAllPlatQuery)) {
            ps.setInt(1, id_menu);
            ResultSet listPlatId = ps.executeQuery();

            Client client = ClientBuilder.newClient();
            WebTarget apiPlatResource  = client.target(apiPlatUrl);

            while (listPlatId.next()){
                int idPlat = listPlatId.getInt("id_plat");
                WebTarget apiPlatEndpoint = apiPlatResource.path("plats/" + idPlat);
                Response response = apiPlatEndpoint.request(MediaType.APPLICATION_JSON).get();
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    PlatDTO platDTO = response.readEntity(PlatDTO.class);
                    listMenuPlatDTO.add(platDTO);
                }
            }
            client.close();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return listMenuPlatDTO;
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
                String author = result.getString("author");
                String title = result.getString("title");
                String description = result.getString("description");
                float price = result.getFloat("price");
                Timestamp creationDate = result.getTimestamp("creationDate");

                // création du menu courant
                Menu currentMenu = new Menu(id, author, title, description, price, creationDate);
                currentMenu.setListPlat(fetchPlatDTOFromApi(id));
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
    public boolean updateMenu(int id, String author, String title, String description, float price) {
        String query = "UPDATE Menu SET author=?, title=?, description=?, price=?  where id_menu=?";
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, author);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setFloat(4, price);
            ps.setInt(5, id);

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
     * @return true si la création s'est bien déroulée, false si non
     * @throws RuntimeException si il y a une erreur côté sql
     */
    @Override
    public boolean createMenu(String author, String title, String description, List<Integer> listPlat) {
        String query = "INSERT INTO Menu (author, title, description, price) VALUES (?, ?, ?, 0)";
        int createdIdMenu = 0;
        int nbRowModified;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, author);
            ps.setString(2, title);
            ps.setString(3, description);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()){
                    createdIdMenu = rs.getInt(1);
                }
            }

            if(listPlat.size() > 1) {
                for (Integer integer : listPlat) {
                    addPlatToMenu(createdIdMenu, integer);
                }
            } else {
                addPlatToMenu(createdIdMenu, listPlat.get(0));
            }

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
        String queryInitialPrice = "SELECT price FROM Menu where id_menu=?";
        String queryAddPlat = "INSERT INTO Plat_menu (id_menu, id_plat) VALUES (?, ?)";
        String queryUpdatePrice = "UPDATE Menu SET price=?  where id_menu=?";

        int nbRowModified;
        int nbRowModified2;

        // création d'un client
        Client client = ClientBuilder.newClient();
        // définition de l'adresse de la ressource
        WebTarget apiPlatResource  = client.target(apiPlatUrl);
        // définition du point d'accès
        WebTarget apiPlatEndpoint = apiPlatResource.path("plats/price/" + id_plat);
        // envoi de la requête et récupération de la réponse
        Response response = apiPlatEndpoint.request(MediaType.APPLICATION_JSON).get();
        // transformation de la réponse en un DTO utilisable dans le code
        MenuUpdatePriceDTO parsedPlatPrice = response.readEntity(MenuUpdatePriceDTO.class);
        float parsedMenuPrice = 0;
        client.close();

        // construction et exécution d'une requête préparée
        try (PreparedStatement psAddPlat = dbConnection.prepareStatement(queryAddPlat);
            PreparedStatement psInitialPrice = dbConnection.prepareStatement(queryInitialPrice);
            PreparedStatement psUpdatePrice = dbConnection.prepareStatement(queryUpdatePrice)) {
            psAddPlat.setInt(1, id_menu);
            psAddPlat.setInt(2, id_plat);

            // exécution de la requête qui associe le plat au menu
            nbRowModified = psAddPlat.executeUpdate();

            psInitialPrice.setInt(1, id_menu);
            // exécution de la requête qui récupère le prix initial du menu
            ResultSet rs = psInitialPrice.executeQuery();
            if(rs.next()){
                parsedMenuPrice = rs.getFloat("price");
            }

            psUpdatePrice.setFloat(1, parsedMenuPrice + parsedPlatPrice.getPrice());
            psUpdatePrice.setInt(2, id_menu);
            // exécution de la requête qui récupère le prix initial du menu
            nbRowModified2 = psUpdatePrice.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0 && nbRowModified2 != 0);
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
            if(!addPlatToMenu(id_menu, listPlatId.get(i))){
                return false;
            }
        }
        return true;
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
        String queryInitialPrice = "SELECT price FROM Menu where id_menu=?";
        String queryUpdatePrice = "UPDATE Menu SET price=?  where id_menu=?";

        int nbRowModified;
        int nbRowModified2;

        // création d'un client
        Client client = ClientBuilder.newClient();
        // définition de l'adresse de la ressource
        WebTarget apiPlatResource  = client.target(apiPlatUrl);
        // définition du point d'accès
        WebTarget apiPlatEndpoint = apiPlatResource.path("plats/price/" + id_plat);
        // envoi de la requête et récupération de la réponse
        Response response = apiPlatEndpoint.request(MediaType.APPLICATION_JSON).get();
        MenuUpdatePriceDTO parsedPlatPrice = response.readEntity(MenuUpdatePriceDTO.class);
        float parsedMenuPrice = 0;
        client.close();

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query);
        PreparedStatement psInitialPrice = dbConnection.prepareStatement(queryInitialPrice);
        PreparedStatement psUpdatePrice = dbConnection.prepareStatement(queryUpdatePrice)) {
            ps.setInt(1, id_menu);
            ps.setInt(2, id_plat);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();

            psInitialPrice.setInt(1, id_menu);
            // exécution de la requête qui récupère le prix initial du menu
            ResultSet rs = psInitialPrice.executeQuery();
            if(rs.next()){
                parsedMenuPrice = rs.getFloat("price");
            }

            psUpdatePrice.setFloat(1, parsedMenuPrice - parsedPlatPrice.getPrice());
            psUpdatePrice.setInt(2, id_menu);
            // exécution de la requête qui récupère le prix initial du menu
            nbRowModified2 = psUpdatePrice.executeUpdate();
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
        String queryUpdatePrice = "UPDATE Menu SET price=0  where id_menu=?";
        int nbRowModified;
        int nbRowModified2;

        // construction et exécution d'une requête préparée
        try (PreparedStatement ps = dbConnection.prepareStatement(query);
        PreparedStatement psUpdatPrice = dbConnection.prepareStatement(queryUpdatePrice)) {
            ps.setInt(1, id_menu);

            // exécution de la requête
            nbRowModified = ps.executeUpdate();

            psUpdatPrice.setInt(1, id_menu);
            // exécution de la requête qui réinitialise le prix
            nbRowModified2 = psUpdatPrice.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0 && nbRowModified2 != 0);
    }
}