package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import androidx.appcompat.app.AppCompatActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Customer;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


// Müşteri kaydının yapılmasını sağlayan aktivite
public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText_name, editText_email, editText_phone, editText_username_signup;
    EditText editText_password_signup, editText_bdate, editText_address;
    Button button_signup;

    String name, email, phone, username, password, bdate, address;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private DatabaseHelper databaseHelper;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        editText_username_signup = (EditText) findViewById(R.id.editText_username_signup);
        editText_password_signup = (EditText) findViewById(R.id.editText_password_signup);
        editText_bdate = (EditText) findViewById(R.id.editText_bdate);
        editText_address = (EditText) findViewById(R.id.editText_address);
        button_signup = (Button) findViewById(R.id.button_signup);

        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        setDateTimeField();
        editText_bdate.setOnClickListener(this);
        button_signup.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
        customer = new Customer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_signup: // Kayıt Ol butonuna basılırsa giriş yapma denemesi
                name = editText_name.getText().toString().trim();
                email = editText_email.getText().toString().trim();
                phone = editText_phone.getText().toString().trim();
                username = editText_username_signup.getText().toString().trim();
                password = editText_password_signup.getText().toString().trim();
                bdate = editText_bdate.getText().toString().trim();
                address = editText_address.getText().toString().trim();
                if (!email.equals("") && !username.equals("") && !password.equals("") && !name.equals("") && !phone.equals("") && !bdate.equals("") && !address.equals("") ) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // Girilen e-mail adresinin geçerli olup olmadığının kontrol edilmesi
                        Toast.makeText(SignupActivity.this, "Geçersiz E-mail!", Toast.LENGTH_SHORT).show();
                        editText_email.setText("");
                        return;
                    }
                    tryToSignup(email, username);
                    return;
                }
                Toast.makeText(SignupActivity.this, "Lütfen kayıt olmak için boş alanları doldurun!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.editText_bdate:
                datePickerDialog.show();
                break;
        }
    }


    // Kullanıcıdan doğum tarihini takvimden almak için oluşturulan metod
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(2000, 0, 1);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Panel, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText_bdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    //  Girilen bilgileri veritabanında kontrol eden daha önce aynı e- mail ya da Kullanıcı Adı alınmamışsa onaylayan metod
    private void tryToSignup(String email, String username) {
        if (!databaseHelper.checkUserSignup(email, username)) {

            customer.setName(name);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setUsername(username);
            customer.setPassword(password);
            customer.setBdate(bdate);
            customer.setAddress(address);
            databaseHelper.addUser(customer);

            Toast.makeText(this, "Başarıyla kayıt olundu!", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else {
            Toast.makeText(this, "Kullanıcı Adı ya da Mail zaten alınmış.\nLütfen tekrar deneyin!!", Toast.LENGTH_SHORT).show();
            editText_email.setText("");
            editText_username_signup.setText("");
            editText_password_signup.setText("");
        }
    }

}
