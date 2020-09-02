package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.CustomerReport;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;


// Market tarafında müşteri görüşlerinin gösterilmesini sağlayan aktivite
public class CustomerReportsActivity extends AppCompatActivity {
    ListView listView_allReports;
    TextView textView_dayOpinion, textView_daySuggestion, textView_dayComplaint;
    Button button_dateChooserReports;
    String chosenDate;

    TextView textView_fromCustomer, textView_customerIdea_market;

    DatabaseHelper databaseHelper;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerreports);

        databaseHelper = new DatabaseHelper(this);

        listView_allReports = (ListView) findViewById(R.id.listView_allReports);
        textView_dayOpinion = (TextView) findViewById(R.id.textView_dayOpinion);
        textView_daySuggestion = (TextView) findViewById(R.id.textView_daySuggestion);
        textView_dayComplaint = (TextView) findViewById(R.id.textView_dayComplaint);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        getDayReports(currentDate);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        setDateTimeField();

        button_dateChooserReports = (Button) findViewById(R.id.button_dateChooserReports);
        button_dateChooserReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

    }


    // Verilen güne göre müşteri görüşlerini alan metod
    private void getDayReports(String currentDate) {
        ArrayList<String> reports = new ArrayList<>();
        final ArrayList<CustomerReport> reportsOfDay = databaseHelper.getCustomerReports(currentDate);
        String reportStr;
        int totalOpinion = 0;
        int totalSuggestion = 0;
        int totalComplaint = 0;

        for (CustomerReport customerReport : reportsOfDay) {
            reportStr = "Rapor ID: " + customerReport.getId() +
                    "\nMüşteri Adı: " + customerReport.getCustomerName() +
                    "\nRapor Tipi: " + customerReport.getReportType() +
                    "\nRapor Tarihi: " + customerReport.getReportDate();
            reports.add(reportStr);
            switch (customerReport.getReportType()) {
                case "Görüş": totalOpinion++; break;
                case "Öneri": totalSuggestion++; break;
                case "Şikayet": totalComplaint++; break;
            }
        }
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reports);
        listView_allReports.setAdapter(listAdapter);
        listView_allReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedReport = (String) parent.getItemAtPosition(position);
                String reportID = selectedReport.substring(selectedReport.indexOf("ID: ") + 3, selectedReport.indexOf("Müşteri Adı: ")).trim();

                CustomerReport selectedCustomerReport = new CustomerReport();
                for ( CustomerReport customerReport : reportsOfDay) {
                    if (String.valueOf(customerReport.getId()).equals(reportID))
                        selectedCustomerReport = customerReport;
                }

                Dialog myDialog = createReportsDialog(view.getContext(), selectedCustomerReport);
                myDialog.show();
                Log.i("Tag", reportID, null);
            }
        });

        textView_dayOpinion.setText(String.valueOf(totalOpinion));
        textView_daySuggestion.setText(String.valueOf(totalSuggestion));
        textView_dayComplaint.setText(String.valueOf(totalComplaint));
    }


    // Müşterinin Görüş, Öneri veya Şikayetini yazabileceği bir pencere oluşturan metod
    public Dialog createReportsDialog(Context context, CustomerReport customerReport) {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_reports_market, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        textView_fromCustomer = (TextView) dialogLayout.findViewById(R.id.textView_fromCustomer);
        String fromCustomer = customerReport.getCustomerName() + " tarafından gelen " + customerReport.getReportType();
        textView_fromCustomer.setText(fromCustomer);

        textView_customerIdea_market = (TextView) dialogLayout.findViewById(R.id.textView_customerIdea_market);
        textView_customerIdea_market.setText(customerReport.getReport());

        builder.setView(dialogLayout)
                .setPositiveButton("Çık", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }


    // Marketin tarihe göre müşteri görüşlerine bakması için oluşturulan tarih seçme dialogunun oluşturulduğu metod
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Panel, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                chosenDate = dateFormatter.format(newDate.getTime());
                Toast.makeText(CustomerReportsActivity.this, chosenDate, Toast.LENGTH_SHORT).show();
                getDayReports(chosenDate);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}
