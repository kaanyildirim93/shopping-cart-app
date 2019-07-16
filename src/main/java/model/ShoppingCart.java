package model;

import exception.InvalidCouponException;
import exception.InvalidQuantityException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {

    /** item list in shopping cart */
    private List<Item> items;

    /** total amount of shopping cart */
    private double totalAmount;

    /** total amount after discount */
    private double totalAmountAfterDiscount;

    /** total campaign discount */
    private double totalCampaignDiscountAmount;

    /** total coupon amount */
    private double totalCouponDiscountAmount;

    /** applied campaign */
    private Campaign appliedCampaign;

    /** applied coupon */
    private Coupon appliedCoupon;

    /** is campaign applied or not (default value false) */
    private boolean isCampaignDiscountApplied;

    /** add items(product, quantity) to shopping cart */
    public void addItem (Product product, int quantity) throws InvalidQuantityException {
        if(quantity == 0){
            throw new InvalidQuantityException("Invalid quantity");
        }

        if(items == null){
            items = new ArrayList<>();
        }

        boolean isExist = false;

        for(Item item : items) {
            if(item.getProduct().equals(product)){
                item.setQuantity(item.getQuantity() + quantity);
                isExist = true;
                break;
            }
        }

        if(!isExist){
            items.add(new Item(product, quantity));
        }

    }

    /** applies campaign discount to shopping cart */
    public void applyDiscounts(Campaign campaign1, Campaign campaign2, Campaign campaign3) throws InvalidCouponException {

        isCampaignDiscountApplied = true;

        // calculates total amounts of items and shopping cards without discounts
        calculateTotalAmount();

        // selects best campaign and calculates total amount of card after campaign discount
        selectBestCampaign(campaign1, campaign2, campaign3);

        // apply discount for cart each item according to campaign
        applyCampaignDiscounts();

    }

    /** calculates total amount of items in shopping cart (without discount or coupon) */
    public double calculateTotalAmount() {
        items.forEach( ((item) -> {
            double totalAmtForItem = item.getProduct().getPrice() * item.getQuantity(); // total amount of item
            item.setTotalAmount(totalAmtForItem); // for item
            totalAmount += totalAmtForItem; // for shopping cart
        }));
        return totalAmount;
    }

    /** selects best campaign */
    public Campaign selectBestCampaign(Campaign campaign1, Campaign campaign2, Campaign campaign3) {

        // adds campaigns
        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(campaign1);
        campaigns.add(campaign2);
        campaigns.add(campaign3);

        double maxAmount = 0;

        for(Campaign campaign : campaigns) {
            if(campaign != null) {
                double tempAmount = 0;

                List<Item> appliedItems = items
                        .stream()
                        .filter((i) -> campaign.getCategory() == i.getProduct().getCategory())
                        .collect(Collectors.toList());

                for (Item item : appliedItems) {
                    if (item.getQuantity() > campaign.getItemCount()) {
                        if (campaign.getDiscountType() == DiscountType.Rate) {
                            tempAmount = (item.getProduct().getPrice() * item.getQuantity()) * (campaign.getRate() / 100);
                        } else {
                            tempAmount = campaign.getRate() * item.getQuantity();
                        }
                    }
                }

                if (tempAmount > maxAmount) {
                    maxAmount = tempAmount;
                    appliedCampaign = campaign;
                }
            }
        }

        return appliedCampaign;
    }

    /**
     *  applies campaign discounts to items and calculates amount of campaign discount
     */
    public double applyCampaignDiscounts() {

        items.forEach((item -> {
                    double tempAmount = 0;
                    if(appliedCampaign != null && appliedCampaign.getCategory() == item.getProduct().getCategory() &&  item.getQuantity() > appliedCampaign.getItemCount()){
                        if(appliedCampaign.getDiscountType() == DiscountType.Rate){
                            tempAmount = (item.getProduct().getPrice() * item.getQuantity()) * (appliedCampaign.getRate() / 100);
                        } else {
                            tempAmount = appliedCampaign.getRate();
                        }
                        totalCampaignDiscountAmount += tempAmount;
                    }
                    item.setTotalCampaignDiscount(tempAmount);
                }
                ));
        totalAmountAfterDiscount = totalAmount - totalCampaignDiscountAmount;

        return totalAmountAfterDiscount;
    }


    /**
     * calculates discount coupon
     */
    public double applyCoupon(Coupon coupon) throws InvalidCouponException {
        if (!isCampaignDiscountApplied)
            throw new InvalidCouponException("Coupon can't be applied to shopping cart");
        else {
            if(totalAmountAfterDiscount > coupon.getMinAmount()) {
                appliedCoupon = coupon;
                totalCouponDiscountAmount = (totalAmountAfterDiscount * (coupon.getRate() / 100));
                totalAmountAfterDiscount -= totalCouponDiscountAmount;
            }
        }
        return totalAmountAfterDiscount;
    }

    /**
     * @return number of different categories
     * */
    public int getNumberOfDeliveries() {

        return (int) items
                .stream()
                .filter(distinctByKey(i -> i.getProduct().getCategory()))
                .count();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public int getNumberOfProducts() {

        return items.size();
    }

    public void print() {

        //grouping by category
        Map<Category, List<Item>> categoryItemList = groupByCategory();

        // prints according to category
        categoryItemList.entrySet().forEach((entry) -> {
            System.out.println(entry.getKey().getTitle().toUpperCase());
            entry.getValue().forEach((item) ->
                    System.out.println("  Product Name: " + item.getProduct().getTitle() +
                            " || Quantity: "  + item.getQuantity() +
                            " || Unit Price: "  + item.getProduct().getPrice() +
                            " || Total Price: "  + item.getTotalAmount() +
                            " || Total Discount: "  + item.getTotalCampaignDiscount())
            );
        });

        System.out.println("--------------------------------------------------------" +
                "--------------------------------------------------------");
        System.out.println("Total Amount                    : " + totalAmount + " ₺");
        System.out.println("Total Campaign Discount Amount  : " + totalCampaignDiscountAmount + " ₺");
        System.out.println("Total Coupon Discount Amount    : " + totalCouponDiscountAmount + " ₺");
        System.out.println("Total Amount After Discounts    : " + totalAmountAfterDiscount + " ₺");
    }

    private Map<Category, List<Item>> groupByCategory(){
        return items
                .stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getCategory()));
    }

}
