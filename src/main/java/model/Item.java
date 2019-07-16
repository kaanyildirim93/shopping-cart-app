package model;

import lombok.Getter;
import lombok.Setter;

/**
 * represents an item in the shopping cart
 */
@Getter
@Setter
public class Item {

    private Product product;

    /** quantity of product */
    private int quantity;

    /** total amount for item (without discount) */
    private double totalAmount;

    /** amount of campaign discount */
    private double totalCampaignDiscount;

    public Item(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object){
            return true;
        }

        if(object == null || this.getClass() != object.getClass()){
            return false;
        }

        Item item = (Item) object;

        return (this.product.equals(item.getProduct()));
    }

}

