package ce.yildiz.edu.tr.sanalmarketotomasyonu.classes;

import java.io.Serializable;
import java.util.ArrayList;


// Müşterinin oluşturacağı sepet bilgilerini tutmak ve taşımak amacıyla oluşturulan Sepet sınıfı
public class Cart implements Serializable {
    ArrayList<Product> products;
    float productPrice;

    public Cart(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }


    public float getProductPrice(Product product) {
        float productPrice = product.getPrice()*product.getNumberOf();
        return productPrice;
    }

    public void setProductPrice(Integer productID) {
        Product product = products.get(productID);
        this.productPrice = product.getPrice()*product.getNumberOf();
    }

    public float getTotalPrice() {
        ArrayList<Product> products = getProducts();
        float totalPrice = 0;
        for (Product product : products) {
            totalPrice += getProductPrice(product);
        }
        return totalPrice;
    }

    public int getCartSize() {
        return products.size();
    }
}
