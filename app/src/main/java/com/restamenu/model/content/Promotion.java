package com.restamenu.model.content;

/**
 * Created by Alexandr.
 */

public class Promotion {

    private int promo_id;
    private String name;
    private String image;
    private String link;

    public int getPromo_id() {
        return promo_id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Promotion{");
        sb.append("promo_id=").append(promo_id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
