package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Product {

    /** title of product */
    private String title;

    /** unit price of product */
    private double price;

    /** category of product */
    private Category category;

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }

        if(object == null || this.getClass() != object.getClass()){
            return false;
        }

        Product product = (Product) object;

        return (this.title.equals(product.title) && this.category.getTitle().equals(product.getCategory().getTitle()));
    }

}
