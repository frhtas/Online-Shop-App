package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import androidx.appcompat.app.AppCompatActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


// Müşteri girişinin yapılmasını sağlayan aktivite
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText_username, editText_password;
    Button button_login;
    TextView textView_signup;

    String username, password;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);
        button_login = (Button) findViewById(R.id.button_login);
        textView_signup = (TextView) findViewById(R.id.textView_signup);

        button_login.setOnClickListener(this);
        textView_signup.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login: // Giriş Yap butonuna basılırsa giriş yapma denemesi
                username = editText_username.getText().toString().trim();
                password = editText_password.getText().toString().trim();
                if (!username.equals("") && !password.equals("")) {
                    tryToLogin(username, password);
                    break;
                }
                Toast.makeText(LoginActivity.this, "Lütfen Kullanıcı Adı ve Şifre'yi giriniz!!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.textView_signup: // // Kayıt Ol'a basılırsa müşteriyi Kayıt Olma aktivitesine gönderen kısım
                Intent signupActivity = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signupActivity);
                break;
        }
    }


    //  Girilen Kullanıcı Adı ve Şifre'yi veritabanında kontrol eden ve doğruysa giriş yaptıran metod
    public void tryToLogin(String username, String password) {
        if (databaseHelper.checkUserLogin(username, password)) {
            Toast.makeText(LoginActivity.this, "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
            homeIntent.putExtra("username", username);
            startActivity(homeIntent);
        }
        else
            Toast.makeText(LoginActivity.this, "Kullanıcı Adı ya da Şifre yanlış.\nLütfen tekrar deneyin!", Toast.LENGTH_SHORT).show();
    }


}
