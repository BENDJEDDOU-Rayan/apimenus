package fr.univamu.iut.apimenus;

import java.sql.Timestamp;
import java.util.List;

/**
 * DTO utilisé pour la requête création de menu
 */
public class MenuCreationRequest {

    private String author;
    private String title;
    private String description;
    private List<Integer> listPlat;

    private Timestamp creationDate;

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
     * @return List<Integer> liste de plats à associer au menu
     */
    public List<Integer> getListPlat() {
        return listPlat;
    }

    /**
     * Méthode qui permet de définir le prix du menu
     *
     * @param listPlat float prix du menu à définir
     */
    public void setListPlat(List<Integer> listPlat) {
        this.listPlat = listPlat;
    }

    /**
     * Méthode permettant de récupérer l'auteur du menu
     * @return String auteur du menu
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Méthode qui définit l'auteur d'un menu
     * @param author String auteur à définir
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Méthode qui récupère la date et heure de création du menu
     * @return Timestamp date et heure de création du menu
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * Méthode qui définit la date et heure de la création du menu
     * @param creationDate Timestamp nouvelle date et heure
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
