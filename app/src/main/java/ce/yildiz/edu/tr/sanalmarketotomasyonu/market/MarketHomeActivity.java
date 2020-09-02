package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Order;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.customer.HomeActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;


// Market arayüzünün oluşturulduğu Ana Aktivite. Siparişler, Ürünler, Raporlar kısımlarını kapsayan ve kontrol eden aktivite
public class MarketHomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation_market;
    DatabaseHelper databaseHelper;
    HomeActivity homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_home);

        databaseHelper = new DatabaseHelper(this);
        homeActivity = new HomeActivity();

        bottomNavigation_market = (BottomNavigationView) findViewById(R.id.bottom_navigation_market);
        bottomNavigation_market.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_market, new OrdersFragment(getAllOrders())).commit();

    }


    // Market görevlisi hangi menü tuşuna basarsa oraya götüren metod
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_orders:
                    selectedFragment = new OrdersFragment(getAllOrders());
                    break;

                case R.id.nav_products:
                    selectedFragment = new ProductsFragment(getProducts());
                    break;

                case R.id.nav_reports_market:
                    selectedFragment = new MarketReportsFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_market, selectedFragment).commit();

            return true;
        }
    };


    // Müşteri sepeti onayladıktan sonra Sipariş bilgilerinin market tarafına getirildiği metod
    private ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = databaseHelper.getAllOrders();
        return orders;
    }


    // Veritabanında bulunan ürünlerin bilgilerini alan metod
    private ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        int productId, productAmount, productNumberOf;
        String productName = null;
        Float productPrice = null;
        String productAmountStr = null;
        Bitmap productImage = null;

        Cursor cursor;

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

}
