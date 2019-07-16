import model.*;
import org.junit.Before;
import org.junit.Test;
import util.DeliveryCostCalculator;

import static junit.framework.TestCase.assertEquals;

public class DeliveryCostCalculatorTest {

    ShoppingCart shoppingCart;
    DeliveryCostCalculator deliveryCostCalculator;

    @Before
    public void setUp() throws Exception {
        Category foodCategory = new Category("food");
        Category electronicCategory = new Category("electronic");
        Product apple = new Product("Apple", 100, foodCategory);
        Product almond = new Product("Almond", 50, foodCategory);
        Product tablet = new Product("Apple Tablet", 5000, electronicCategory);
        shoppingCart = new ShoppingCart();
        shoppingCart.addItem(apple, 5);
        shoppingCart.addItem(almond, 5);
        shoppingCart.addItem(tablet, 5);
        deliveryCostCalculator = new DeliveryCostCalculator(12.0, 10, 2.99);
    }

    @Test
    public void testCalculateFor(){
        assertEquals(54.0, deliveryCostCalculator.calculateFor(shoppingCart));
    }

}
