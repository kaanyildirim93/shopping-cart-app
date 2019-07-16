package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Coupon {

    /** min amount to realize coupon on shopping cart */
    private int minAmount;

    /** rate for coupon */
    private double rate;

    /** discount type for coupon */
    private DiscountType discountType;
}
