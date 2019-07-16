package util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.ShoppingCart;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryCostCalculator {

    private double costPerDelivery;

    private double costPerProduct;

    private double fixedCost;

    public double calculateFor(ShoppingCart cart) {
        return (costPerDelivery * cart.getNumberOfDeliveries()) + (costPerProduct * cart.getNumberOfProducts());
    }


}
