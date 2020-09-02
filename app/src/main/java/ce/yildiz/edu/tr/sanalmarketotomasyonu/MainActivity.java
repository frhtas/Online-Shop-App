package ce.yildiz.edu.tr.sanalmarketotomasyonu;

import androidx.appcompat.app.AppCompatActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.customer.LoginActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.market.MarketLoginActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.IOException;


// Uygulama ilk açıldığında gelen ekran, bu aktivite sayesinde Müşteri uygulamasına ya da Market uygulamasına gidebiliyoruz
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_customer, button_market;
    SharedPreferences sharedPreferences;
    int countCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        countCopy = sharedPreferences.getInt("countcopy", 0);
        if (countCopy == 0) {
            copyDatabaseFromAssets();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("countcopy", 1);
            editor.apply();
        }

        button_customer = (Button) findViewById(R.id.button_customer);
        button_market = (Button) findViewById(R.id.button_market);

        button_customer.setOnClickListener(this);
        button_market.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_customer: // Müşteri butonuna basılırsa Müşteri uygulamasının giriş ekranına gidilecek
                Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                break;

            case R.id.button_market: // Market butonuna basılırsa Market uygulamasının giriş ekranına gidilecek
                Intent marketLoginActivity = new Intent(MainActivity.this, MarketLoginActivity.class);
                startActivity(marketLoginActivity);
                break;
        }
    }


    private void copyDatabaseFromAssets() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        try {
            databaseHelper.createDatabase();
        } catch (IOException e) {
            throw new Error("Unable to create database!");
        }
        try {
            databaseHelper.openDatabase();
        } catch (SQLException e) {
            throw new Error("Unable to open database");
        }
    }
}
