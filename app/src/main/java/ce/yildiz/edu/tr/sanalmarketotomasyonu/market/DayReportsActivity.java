package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import androidx.appcompat.app.AppCompatActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Order;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


// Market tarafında günlük raporların gösterilmesini sağlayan aktivite
public class DayReportsActivity extends AppCompatActivity {
    ListView listView_allOrders;
    TextView textView_dayPrice, textView_dayRating;
    Button button_dateChooser;
    String chosenDate;

    DatabaseHelper databaseHelper;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayreports);

        databaseHelper = new DatabaseHelper(this);

        listView_allOrders = (ListView) findViewById(R.id.listView_allOrders);
        textView_dayPrice = (TextView) findViewById(R.id.textView_dayPrice);
        textView_dayRating = (TextView) findViewById(R.id.textView_dayRating);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        getDayReports(currentDate);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        setDateTimeField();

        button_dateChooser = (Button) findViewById(R.id.button_dateChooser);
        button_dateChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });


    }

    // Verilen güne göre sipariş bilgilerini alan metod
    private void getDayReports(String currentDate) {
        ArrayList<String> orders = new ArrayList<>();
        ArrayList<Order> ordersOfDay = databaseHelper.getOrdersByDay(currentDate);
        String orderStr;
        int totalRating = 0, numberOfRating = 0;
        float dayRating = 0;
        float dayPrice = 0;

        for (Order order : ordersOfDay) {
            orderStr = "Sipariş ID: " + order.getOrderID() +
                    "\nMüşteri Adı: " + order.getCustomer().getName() +
                    "\nSipariş Puanı: " + order.getOrderRating() +
                    "\nSipariş Ücreti: " + order.getTotalOrderPrice() + " TL" +
                    "\nSipariş Tarihi: " + order.getOrderDate();
            orders.add(orderStr);
            totalRating += order.getOrderRating();
            if (order.getOrderRating() != 0)
                numberOfRating++;
            dayPrice += order.getTotalOrderPrice();
        }
        if (numberOfRating != 0)
            dayRating = (float) totalRating/numberOfRating;
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orders);
        listView_allOrders.setAdapter(listAdapter);

        textView_dayPrice.setText(String.valueOf(dayPrice) + " TL");
        textView_dayRating.setText(String.valueOf(dayRating));
    }


    // Marketin tarihe göre raporlara bakması için oluşturulan tarih seçme dialogunun oluşturulduğu metod
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Panel, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                chosenDate = dateFormatter.format(newDate.getTime());
                Toast.makeText(DayReportsActivity.this, chosenDate, Toast.LENGTH_SHORT).show();
                getDayReports(chosenDate);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


}
