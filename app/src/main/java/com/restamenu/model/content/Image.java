package com.restamenu.model.content;

/**
 * @author Roodie
 */

public class Image {

    private String image;
    private String link;

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Image{");
        sb.append("image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
