package com.restamenu.model.content;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class Category implements Parcelable {
    private int category_id;
    private int currency_id;
    private int language_id;
    private String name;
    private String background;
    private String image;
    private List<Product> products;
    private String date; //Дата и время последнего обновления меню в формате Y-m-d H:i:s
    private String modified;


    public int geId() {
        return category_id;
    }

    public int getCurrencyId() {
        return currency_id;
    }

    public int getLanguageId() {
        return language_id;
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public String getImage() {
        return image;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Category{");
        sb.append("category_id=").append(category_id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.category_id);
        dest.writeInt(this.currency_id);
        dest.writeInt(this.language_id);
        dest.writeString(this.name);
        dest.writeString(this.background);
        dest.writeString(this.image);
        dest.writeList(this.products);
        dest.writeString(this.date);
        dest.writeString(this.modified);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.category_id = in.readInt();
        this.currency_id = in.readInt();
        this.language_id = in.readInt();
        this.name = in.readString();
        this.background = in.readString();
        this.image = in.readString();
        this.products = new ArrayList<Product>();
        in.readList(this.products, Product.class.getClassLoader());
        this.date = in.readString();
        this.modified = in.readString();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
