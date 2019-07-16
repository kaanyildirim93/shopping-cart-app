package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Campaign {

    /** category of campaign */
    private Category category;

    /** rate or amount for discount */
    private double rate;

    /** amount of least item number to realize discount */
    private int itemCount;

    /** discount type for campaign */
    private DiscountType discountType;

}
