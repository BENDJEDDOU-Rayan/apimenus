API qui gère les menus pour une application de livraison de repas (BUT Info. 2ème année)

## Liste des endpoints disponibles 

### ```/api/menus```
* GET
* Endpoint qui permet de récupérer tous les menus existants
* retourne List\<Menu\> listMenu : liste de menus

### ```/api/menus/get/{id_menu}```
* GET
* Endpoint qui permet de récupérer un menu en particulier
* int id_menu : id du menu

### ```/api/menus/create```
* POST
* Endpoint qui permet de créer un menu
* String title : titre du menu
* String description : description du menu
* List\<Integer\> : liste des id des plats à ajouter au menu

### ```/api/menus/update/{id_menu}```
* PATCH
* Endpoint qui permet de mettre à jours un menu déjà existant
* int id_menu : id du menu

### ```/api/menus/delete/{id_menu}```
* DELETE
* Endpoint qui permet de supprimer un menu
* int id_menu : id du menu

### ```/api/menus/get-all-plat-from-menu/{id_menu}```
* GET
* Endpoint qui permet de récupérer tous les plats associés à un mebnu
* int id_menu : id du menu

### ```/api/menus/add-plat-to-menu```
* POST
* Endpoint qui permet d'associer un plat à un menu
* prend en paramètre un DTO qui contient l'id du menu et l'id du plat

### ```/api/menus/add-all-plat-to-menu```
* POST
* Endpoint qui permet d'associer plusieurs plats à un menu
* prend en paramètre un DTO qui contient l'id du menu ainsi qu'une List\<Integer\> contenant les id des plats

### ```/api/menus/remove-plat-from-menu/{id_menu}/{id_plat}```
* DELETE
* Endpoint qui permet de supprimer un plat d'un menu
* int id_menu : id du menu
* int id_plat : id du plat à supprimer

### ```/api/menus/remove-all-plats-from-menu/{id_menu}```
* DELETE
* Endpoint qui permet de supprimer tous les plats d'un menu
* int id_menu : id du menu
