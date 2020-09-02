package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Cart;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Customer;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Order;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// Müşteri arayüzünün oluşturulduğu Ana Aktivite. Ana Sayfa, Sepetim, Siparişlerim, Profilim kısımlarını kapsayan ve kontrol eden aktivite
public class HomeActivity extends AppCompatActivity implements ShopCartFragment.FragmentListener, HomepageFragment.FragmentListener {
    BottomNavigationView bottomNavigation;
    DatabaseHelper databaseHelper;
    Customer customer;
    String username;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        customer = databaseHelper.getCustomer(username);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomepageFragment(getApplicationContext(), getProducts(0))).commit();
    }


    // Müşteri hangi menü tuşuna basarsa oraya götüren metod
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomepageFragment(getApplicationContext(), getProducts(0));
                    break;

                case R.id.nav_shopcart:
                    selectedFragment = new ShopCartFragment(getProducts(1));
                    break;

                case R.id.nav_customer_orders:
                    selectedFragment = new CustomerOrdersFragment(getCustomerOrders());
                    break;

                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment(getCustomerInfos(username));
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };


    // Veritabanında bulunan ürünlerin bilgilerini alan metod, cart=1 ise sadece sepette bulunan ürünlerin bilgisi alınacak
    private ArrayList<Product> getProducts(int cart) {
        ArrayList<Product> products = new ArrayList<Product>();
        int productId, productAmount, productNumberOf;
        String productName = null;
        Float productPrice = null;
        String productAmountStr = null;
        Bitmap productImage = null;

        Cursor cursor;

        if (cart == 1)  // If usage for ShopCartFragment, for get products which on the cart
            cursor = databaseHelper.getProductsOnCart();
        else            // If usage for HomepageFragment, for get all products
            cursor = databaseHelper.getProducts();

        while (cursor.moveToNext()) {
            productId = cursor.getInt(0);
            productName = cursor.getString(1);
            productPrice = cursor.getFloat(2);
            productAmountStr = cursor.getString(3);
            productAmount = cursor.getInt(4);
            byte[] image = cursor.getBlob(5);
            productImage = BitmapFactory.decodeByteArray(image, 0, image.length);
            productNumberOf = cursor.getInt(6);

            Product newProduct = new Product(productId, productName, productPrice, productAmountStr, productAmount, productImage, productNumberOf);
            products.add(newProduct);
        }

        return products;
    }


    // Müşterinin şimdiye yapmış olduğu siparişleri ve onların durumlarını veritabanından alan metod
    private ArrayList<Order> getCustomerOrders() {
        ArrayList<Order> orders = databaseHelper.getCustomerOrders(customer);
        return orders;
    }

    // Kullanıcı Adı'na göre müşterinin bütün bilgilerinin alındığı metod, Profilim kısmında göstermek amacıyla
    private Customer getCustomerInfos(String username) {
        Customer customer = databaseHelper.getCustomerInfos(username);
        return customer;
    }


    //----------------Sepette kaç adet ürünün bulunduğunu bildirim olarak göstermek için oluşturulan metodlar-------------------------------------
    @Override
    public void onRefresh(int badgeCount, int confirmCart) {
        if (confirmCart == 1) {
            if (!enoughProductsAmount(getProducts(1))) // Markette yeterince ürün yoksa sipariş iptali
                return;


            order = new Order(new Cart(getProducts(1)), customer, 0);  // Sipariş objesi yaratılarak veritabanına yollandı
            String currentDate = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss", Locale.getDefault()).format(new Date());
            order.setOrderDate(currentDate);
            order.setOrderRating(0);
            databaseHelper.updateProducts(getProducts(1));
            databaseHelper.storeOrder(order);

            Fragment customerOrdersFragment = new CustomerOrdersFragment(getCustomerOrders());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, customerOrdersFragment).commit();
            bottomNavigation.setSelectedItemId(R.id.nav_customer_orders);

            badgeUpdate(badgeCount);
            return;
        }
        badgeUpdate(badgeCount);
    }

    @Override
    public void onRefreshBadge(int badgeCount) {
        badgeUpdate(badgeCount);
    }

    public void badgeUpdate(int badgeCount) {
        BadgeDrawable badgeShopcart = bottomNavigation.getOrCreateBadge(R.id.nav_shopcart);
        badgeShopcart.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackground_app));
        badgeShopcart.setBadgeTextColor(Color.WHITE);
        badgeShopcart.setMaxCharacterCount(5);
        badgeShopcart.setVisible(true);
        if (badgeCount == 0)
            badgeShopcart.setVisible(false);
        badgeShopcart.setNumber(badgeCount);
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------


    // Markette yeterince ürün bulunup bulunmadığını kontrol eden metod
    public boolean enoughProductsAmount(ArrayList<Product> productsOnCart) {
        for (Product product: productsOnCart) {
            if (!databaseHelper.enoughProduct(product)) {
                Toast.makeText(this, "Üzgünüz! Şu an bakkAL'da yeterince " + product.getName() + " bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

}
