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
     * @param id_menu id du menu recherché
     * @return les informations du menu recherché au format JSON
     */
    @GET
    @Path("get/{id_menu}")
    @Produces("application/json")
    public String getMenu(@PathParam("id_menu") int id_menu) {

        String result = service.getMenuJSON(id_menu);

        // si le menu n'a pas été trouvé
        if (result == null)
            throw new NotFoundException();

        return result;
    }

    /**
     * Endpoint permettant de mettre à jours un menu
     * (la requête patch doit fournir le nouveau statut sur menu, les autres informations sont ignorées)
     *
     * @param id_menu   la référence du menu dont il faut changer le statut
     * @param menu le menu transmis en HTTP au format JSON et convertit en objet Menu
     * @return une réponse "updated" si la mise à jour a été effectuée, une erreur NotFound sinon
     */
    @PUT
    @Path("update/{id_menu}")
    @Consumes("application/json")
    public Response updateMenu(@PathParam("id_menu") int id_menu, Menu menu) {

        // si le menu n'a pas été trouvé
        if (!service.updateMenu(id_menu, menu))
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
     * @param id_menu int identifiant du menu à supprimer
     * @return une erreur si l'id n'existe pas, un message si ça a bien fonctionnée
     */
    @DELETE
    @Path("delete/{id_menu}")
    public Response deleteMenu(@PathParam("id_menu") int id_menu){
        if(!service.deleteMenu(id_menu)){
            throw new NotFoundException();
        } else {
            return Response.ok("Le menu " + id_menu + " a bien été supprimé !").build();
        }
    }

    /**
     * Endpoint permettant la récupération de tous les plats associés à un menu
     * @param id_menu int id du menu
     * @return String chaîne de caractère contenant tous les plats associés à un menu
     */
    @GET
    @Path("/get-all-plat-from-menu/{id_menu}")
    @Produces("application/json")
    public String getAllPlatFromMenu(@PathParam("id_menu") int id_menu) {
        String result = service.getAllPlatFromMenuJson(id_menu);

        if(result == null){
            throw new NotFoundException();
        }

        return result;
    }

    /**
     * Endpoint permettant d'associer un plat à un menu
     * @param menuPlatDTO DTO qui contient l'id du menu et l'id du plat à associer
     * @return Une réponse OK
     * @throws NotFoundException si menu introuvable
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

    /**
     * Endpoint permettant d'associer plusieurs plats à un menu
     * @param menuListPlatDTO DTO qui contient l'id du menu ainsi qu'une liste d'id de plats
     * @return Une réponse OK
     * @throws NotFoundException si le menu est introuvable
     */
    @PUT
    @Path("/add-all-plat-to-menu")
    @Consumes("application/json")
    public Response addAllPlatToMenu(MenuListPlatDTO menuListPlatDTO) {
        if(!service.addAllPlatToMenu(menuListPlatDTO.getId_menu(), menuListPlatDTO.getListPlatId())){
            throw new NotFoundException();
        } else {
            return Response.ok("Ces id viennent d'être associés au plat N°" + menuListPlatDTO.getId_menu() + " !").build();
        }
    }

    /**
     * Endpoint permettant de dissocier un plat d'un menu
     * @param id_menu id du menu
     * @param id_plat id du plat à dissocier
     * @return Un message de bon fonctionnement
     * @throws NotFoundException si menu introuvable
     */
    @DELETE
    @Path("/remove-plat-from-menu/{id_menu}/{id_plat}")
    public Response removePlatFromMenu(@PathParam("id_menu") int id_menu, @PathParam("id_plat") int id_plat) {
        if(!service.removePlatFromMenu(id_menu, id_plat)) {
            throw new NotFoundException();
        } else {
            return Response.ok("Le plat N°" + id_plat + " a bien été supprimé du menu N°" + id_menu).build();
        }
    }

    /**
     * Endpoint permettant de dissocier tous les plats associés à un menu
     * @param id_menu id du menu cible
     * @return Un message de bon fonctionnement
     * @throws NotFoundException si menu introuvable
     */
    @DELETE
    @Path("/remove-all-plats-from-menu/{id_menu}")
    public Response removeAllPlatsFromMenu(@PathParam("id_menu") int id_menu) {
        if(!service.removeAllPlatsFromMenu(id_menu)){
            throw new NotFoundException();
        } else {
            return Response.ok("Tous les plats du menu N°" + id_menu + " ont été supprimés !").build();
        }
    }

}
