package model;

import lombok.*;

@Getter
@Setter
public class Category{

    /** title of category */
    private String title;

    /** category of category */
    private Category category;

    public Category(String title) {
        this.title = title;
    }
}
