package fr.univamu.iut.apimenus;

import java.util.List;

public class MenuListPlatDTO {

    private int id_menu;
    private List<Integer> listPlatId;

    public MenuListPlatDTO() {}

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public List<Integer> getListPlatId() {
        return listPlatId;
    }

    public void setListPlatId(List<Integer> listPlatId) {
        this.listPlatId = listPlatId;
    }
}
