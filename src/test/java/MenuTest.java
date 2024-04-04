import fr.univamu.iut.apimenus.Menu;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Timestamp;

public class MenuTest {

    private static Menu menu;

    @BeforeClass
    public static void setUp() {
        menu = new Menu(1, "Rayan", "Menu pizza", "Pizza 4 fromages", 8.99f, new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testGetId() {
        Assert.assertEquals(1, menu.getId());
    }

    @Test
    public void testGetTitle() {
        Assert.assertEquals("Menu pizza", menu.getTitle());
    }

    @Test
    public void testGetPrice() {
        Assert.assertEquals(8.99f, menu.getPrice(), 0.0f);
    }

    @Test
    public void testSetId() {
        menu.setId(2);
        Assert.assertEquals(2, menu.getId());
    }

    @Test
    public void testSetTitle() {
        menu.setTitle("Menu burger");
        Assert.assertEquals("Menu burger", menu.getTitle());
    }

    @Test
    public void testSetDescription() {
        menu.setDescription("Burger 2 steaks");
        Assert.assertEquals("Burger 2 steaks", menu.getDescription());
    }

    @Test
    public void testSetPrice() {
        menu.setPrice(9.99f);
        Assert.assertEquals(9.99f, menu.getPrice(), 0.0f);
    }
}
