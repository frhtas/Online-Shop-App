package ce.yildiz.edu.tr.sanalmarketotomasyonu.classes;

import java.io.Serializable;


// Sipariş bilgilerini tutmak için oluşturulan sınıf
public class Order implements Serializable {
    private Cart cart;
    private Customer customer;
    private int orderID, orderStatu, orderRating;
    private String orderDate;

    public Order(Cart cart, Customer customer, int orderStatu) {
        this.cart = cart;
        this.customer = customer;
        this.orderStatu = orderStatu;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderStatu() {
        return orderStatu;
    }

    public void setOrderStatu(int orderStatu) {
        this.orderStatu = orderStatu;
    }

    public int getOrderRating() {
        return orderRating;
    }

    public void setOrderRating(int orderRating) {
        this.orderRating = orderRating;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalOrderPrice() {
        return cart.getTotalPrice();
    }
}
