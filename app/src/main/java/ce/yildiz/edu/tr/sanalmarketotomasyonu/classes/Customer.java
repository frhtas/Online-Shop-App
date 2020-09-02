package ce.yildiz.edu.tr.sanalmarketotomasyonu.classes;

import java.io.Serializable;


// Müşteriye ait bilgileri tutmak ve taşımak amacıyla oluşturulan Müşteri sınıfı
public class Customer implements Serializable {
    private String name, email, phone, username, password, bdate, address;

    public Customer() {
    }

    public Customer(String name, String email, String phone, String username, String password, String bdate, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.bdate = bdate;
        this.address = address;
    }

    public Customer(String username, String name, String address) {
        this.username = username;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
