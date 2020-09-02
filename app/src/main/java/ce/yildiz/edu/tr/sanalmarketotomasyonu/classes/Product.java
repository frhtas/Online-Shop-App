package ce.yildiz.edu.tr.sanalmarketotomasyonu.classes;

import android.graphics.Bitmap;
import java.io.Serializable;


// Ürünlere ait bilgileri tutmak ve taşımak amacıyla oluşturulan Ürün sınıfı
public class Product implements Serializable {
    private int id;
    private String name;
    private Float price;
    private String amount_str;
    private int amount;
    private Bitmap image;
    private int numberOf;

    public Product(int id, String name, Float price, String amount_str, int amount, Bitmap image, int numberOf) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount_str = amount_str;
        this.amount = amount;
        this.image = image;
        this.numberOf = numberOf;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getAmount_str() {
        return amount_str;
    }

    public int getAmount() {
        return amount;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getNumberOf() {
        return numberOf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setNumberOf(int numberOf) {
        this.numberOf = numberOf;
    }

    public float getPriceAll() {
        return numberOf*price;
    }

}
