import exception.InvalidCouponException;
import exception.InvalidQuantityException;
import model.*;
import org.junit.Before;
import org.junit.Test;
import util.DeliveryCostCalculator;

import static junit.framework.TestCase.*;

public class ShoppingCartTest {

    ShoppingCart shoppingCart;
    Category foodCategory;
    Category electronicCategory;
    Product apple;
    Product almond;
    Product tablet;
    Campaign campaign1;
    Campaign campaign2;
    Coupon coupon;
    DeliveryCostCalculator deliveryCostCalculator;

    @Before
    public void setUp() throws Exception {
        foodCategory = new Category("food");
        electronicCategory = new Category("electronic");
        apple = new Product("Apple", 100, foodCategory);
        almond = new Product("Almond", 50, foodCategory);
        tablet = new Product("Apple Tablet", 5000, electronicCategory);
        shoppingCart = new ShoppingCart();
        shoppingCart.addItem(apple, 5);
        shoppingCart.addItem(almond, 5);
        shoppingCart.addItem(tablet, 5);
        campaign1 = new Campaign(foodCategory, 20.0, 3, DiscountType.Rate);
        campaign2 = new Campaign(electronicCategory, 50.0, 5, DiscountType.Rate);
        coupon = new Coupon(100, 10, DiscountType.Rate);
        deliveryCostCalculator = new DeliveryCostCalculator(12.0, 10, 2.99);
    }

    @Test
    public void testIsItemListCreated(){
        assertFalse(shoppingCart.getItems() == null);
        assertFalse(shoppingCart.getItems().isEmpty());
    }

    @Test
    public void testNumberOfProducts(){
        assertEquals(3, shoppingCart.getNumberOfProducts());
    }


    @Test
    public void testApplyDiscounts() throws Exception{
        assertEquals(25750.0, shoppingCart.calculateTotalAmount());
        assertEquals(campaign1, shoppingCart.selectBestCampaign(campaign1, campaign2, null));
        assertEquals(25600.0, shoppingCart.applyCampaignDiscounts());
    }

    @Test
    public void testApplyCoupon() throws Exception{
        shoppingCart.applyDiscounts(campaign1, campaign2, null);
        assertEquals(23040.0, shoppingCart.applyCoupon(coupon));
        assertEquals(shoppingCart.getTotalAmount(), (
                shoppingCart.getTotalCampaignDiscountAmount() +
                shoppingCart.getTotalCouponDiscountAmount() +
                shoppingCart.getTotalAmountAfterDiscount()));
        shoppingCart.print();
        assertEquals(54.0, deliveryCostCalculator.calculateFor(shoppingCart));
    }

    @Test(expected = InvalidCouponException.class)
    public void testApplyCouponDirectly() throws InvalidCouponException{
        shoppingCart.applyCoupon(coupon);
    }

    @Test
    public void testNumberOfDeliveries() throws Exception{
        shoppingCart.addItem(apple, 5);
        shoppingCart.addItem(tablet, 1);
        assertEquals(2, shoppingCart.getNumberOfDeliveries());
    }

    @Test(expected = InvalidQuantityException.class)
    public void testAddItemWithQuantity() throws Exception{
        shoppingCart.addItem(apple, 0);
        assertEquals(2, shoppingCart.getNumberOfDeliveries());
    }

}
