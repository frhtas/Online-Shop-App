package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import androidx.appcompat.app.AppCompatActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


// Market girişinin yapılmasını sağlayan aktivite
public class MarketLoginActivity extends AppCompatActivity {
    EditText editText_username_market, editText_password_market;
    Button button_login_market;

    String username, password;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_login);

        databaseHelper = new DatabaseHelper(this);

        editText_username_market = (EditText) findViewById(R.id.editText_username_market);
        editText_password_market = (EditText) findViewById(R.id.editText_password_market);
        button_login_market = (Button) findViewById(R.id.button_login_market);

        // Giriş Yap butonuna basılırsa giriş yapma denemesi
        button_login_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editText_username_market.getText().toString().trim();
                password = editText_password_market.getText().toString().trim();
                if (!username.equals("") && !password.equals("")) {
                    tryToLogin(username, password);
                    return;
                }
                Toast.makeText(MarketLoginActivity.this, "Lütfen Kullanıcı Adı ve Şifre'yi giriniz!!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //  Girilen Kullanıcı Adı ve Şifre'yi veritabanında kontrol eden ve doğruysa giriş yaptıran metod
    public void tryToLogin(String username, String password) {
        if (databaseHelper.checkUserLoginMarket(username, password)) {
            Toast.makeText(MarketLoginActivity.this, "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(MarketLoginActivity.this, MarketHomeActivity.class);
            startActivity(homeIntent);
        }
        else
            Toast.makeText(MarketLoginActivity.this, "Kullanıcı Adı ya da Şifre yanlış.\nLütfen tekrar deneyin!", Toast.LENGTH_SHORT).show();
    }

}
