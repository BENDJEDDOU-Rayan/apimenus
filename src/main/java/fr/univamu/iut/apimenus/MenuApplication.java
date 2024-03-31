package fr.univamu.iut.apimenus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Main de l'api
 * @author Rayan BENDJEDDOU
 */
@ApplicationPath("/api")
@ApplicationScoped
public class MenuApplication extends Application {

    /**
     * Méthode appelée par l'API CDI pour injecter la connection à la base de données au moment de la création
     * de la ressource
     * @return un objet implémentant l'interface MenuRepositoryInterface utilisée
     *          pour accéder aux données des menus, voire les modifier
     */
    @Produces
    private MenuRepositoryInterface openDbConnection(){
        MenuRepositoryMariadb db = null;

        try{
            db = new MenuRepositoryMariadb("jdbc:mariadb://mysql-apimenus.alwaysdata.net/apimenus_db", "apimenus", "apirest2024");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Méthode permettant de fermer la connexion à la base de données lorsque l'application est arrêtée
     * @param menuRepo la connexion à la base de données instanciée dans la méthode @openDbConnection
     */
    private void closeDbConnection(@Disposes MenuRepositoryInterface menuRepo ) {
        menuRepo.close();
    }

}