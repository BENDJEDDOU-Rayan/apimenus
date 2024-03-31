package fr.univamu.iut.apimenus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

/**
 * Ressource associée aux menus
 * (point d'accès de l'API REST)
 */
@Path("/menus")
@ApplicationScoped
public class MenuResource {

    /**
     * Service utilisé pour accéder aux données des menus et récupérer/modifier leurs informations
     */
    private MenuService service;

    /**
     * Constructeur par défaut
     */
    public MenuResource() {
    }

    /**
     * Constructeur permettant d'initialiser le service avec une interface d'accès aux données
     *
     * @param menuRepo objet implémentant l'interface d'accès aux données
     */
    public @Inject MenuResource(MenuRepositoryInterface menuRepo) {
        this.service = new MenuService(menuRepo);
    }

    /**
     * Constructeur permettant d'initialiser le service d'accès aux menus
     */
    public MenuResource(MenuService service) {
        this.service = service;
    }

    /**
     * Endpoint permettant de publier de tous les menus enregistrés
     *
     * @return la liste des menus (avec leurs informations) au format JSON
     */
    @GET
    @Produces("application/json")
    public String getAllMenus() {
        return service.getAllMenusJSON();
    }

    /**
     * Endpoint permettant de récupurer les informations d'un menu dont l'id est passé en paramètre dans le chemin
     *
     * @param id id du menu recherché
     * @return les informations du menu recherché au format JSON
     */
    @GET
    @Path("get/{id}")
    @Produces("application/json")
    public String getMenu(@PathParam("id") int id) {

        String result = service.getMenuJSON(id);

        // si le menu n'a pas été trouvé
        if (result == null)
            throw new NotFoundException();

        return result;
    }

    /**
     * Endpoint permettant de mettre à jours un menu
     * (la requête patch doit fournir le nouveau statut sur menu, les autres informations sont ignorées)
     *
     * @param id   la référence du menu dont il faut changer le statut
     * @param menu le menu transmis en HTTP au format JSON et convertit en objet Menu
     * @return une réponse "updated" si la mise à jour a été effectuée, une erreur NotFound sinon
     */
    @PUT
    @Path("update/{id}")
    @Consumes("application/json")
    public Response updateMenu(@PathParam("id") int id, Menu menu) {

        // si le menu n'a pas été trouvé
        if (!service.updateMenu(id, menu))
            throw new NotFoundException();
        else
            return Response.ok("Le menu vient d'être mis à jours !").build();
    }

    /**
     * Endpoint permettant la création d'un menu
     * @param request DTO contenant le titre, description et prix du menu
     * @return message d'erreur si la création du menu échoue
     */
    @PUT
    @Path("/create")
    @Consumes("application/json")
    public Response createMenu(MenuCreationRequest request) {
        if (!service.createMenu(request.getTitle(), request.getDescription(), request.getPrice()))
            return Response.ok("Une erreur est survenue lors de la création du menu " + request.getTitle() + " !").build();
        else
            return Response.ok("Le menu " + request.getTitle() + " a été crée !").build();
    }

    /**
     * Endpoint permettant la suppression d'un menu
     * @param id int identifiant du menu à supprimer
     * @return une erreur si l'id n'existe pas, un message si ça a bien fonctionnée
     */
    @DELETE
    @Path("delete/{id}")
    public Response deleteMenu(@PathParam("id") int id){
        if(!service.deleteMenu(id)){
            throw new NotFoundException();
        } else {
            return Response.ok("Le menu " + id + " a bien été supprimé !").build();
        }
    }

    /**
     * Endpoint permettant d'associer un plat à un menu
     * @param menuPlatDTO DTO qui sert à récupérer le contenu json
     * @return une erreur si le menu n'est pas trouvé, un message si il est bien trouvé
     */
    @PUT
    @Path("/add-plat-to-menu")
    @Consumes("application/json")
    public Response addPlatToMenu(MenuPlatDTO menuPlatDTO) {
        if(!service.addPlatToMenu(menuPlatDTO.getId_menu(), menuPlatDTO.getId_plat())) {
            throw new NotFoundException();
        } else {
            return Response.ok("Le plat N°" + menuPlatDTO.getId_plat() + " a bien été ajouté au menu N°" + menuPlatDTO.getId_menu()).build();
        }
    }

}
