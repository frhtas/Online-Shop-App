package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Customer;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.CustomerReport;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;


// Müşteriye Profilim kısmında bilgilerini gösteren ve Görüş, Öneri, Şikayet yapabilmesini sağlayan menünün oluşturulduğu Fragment sınıfı
public class ProfileFragment extends Fragment {
    EditText editText_customerUsername, editText_customerEmail, editText_customerName, editText_customerBdate;
    EditText editText_customerPassword, editText_customerPhone, editText_customerAddress;
    Button button_updateCustomer;
    TextView textView_reports;

    RadioGroup radioGroup_report;
    EditText editText_customerIdea;

    Customer customer;
    DatabaseHelper databaseHelper;

    public ProfileFragment(Customer customerInfos) {
        this.customer = customerInfos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(view.getContext());

        editText_customerUsername = (EditText) view.findViewById(R.id.editText_customerUsername);
        editText_customerUsername.setText(customer.getUsername());

        editText_customerEmail = (EditText) view.findViewById(R.id.editText_customerEmail);
        editText_customerEmail.setText(customer.getEmail());

        editText_customerName = (EditText) view.findViewById(R.id.editText_customerName);
        editText_customerName.setText(customer.getName());

        editText_customerBdate = (EditText) view.findViewById(R.id.editText_customerBdate);
        editText_customerBdate.setText(customer.getBdate());

        editText_customerPassword = (EditText) view.findViewById(R.id.editText_customerPassword);
        editText_customerPassword.setText(customer.getPassword());

        editText_customerPhone = (EditText) view.findViewById(R.id.editText_customerPhone);
        editText_customerPhone.setText(customer.getPhone());

        editText_customerAddress = (EditText) view.findViewById(R.id.editText_customerAddress);
        editText_customerAddress.setText(customer.getAddress());

        button_updateCustomer = (Button) view.findViewById(R.id.button_updateCustomer);
        button_updateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editText_customerPassword.getText().toString();
                String newPhone = editText_customerPhone.getText().toString();
                String newAddress = editText_customerAddress.getText().toString();

                customer.setPassword(newPassword);
                customer.setPhone(newPhone);
                customer.setAddress(newAddress);
                databaseHelper.updateCustomerInfos(customer);

                Toast.makeText(view.getContext(), "Bilgileriniz başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
            }
        });

        textView_reports = (TextView) view.findViewById(R.id.textView_reports);
        textView_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialog = createReportsDialog(view.getContext());
                myDialog.show();
            }
        });

        return view;
    }


    // Müşterinin Görüş, Öneri veya Şikayetini yazabileceği bir pencere oluşturan metod
    public Dialog createReportsDialog(Context context) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_reports, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogLayout)
                .setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        radioGroup_report = (RadioGroup) dialogLayout.findViewById(R.id.radioGroup_report);
                        editText_customerIdea = (EditText) dialogLayout.findViewById(R.id.editText_customerIdea);

                        int radioButtonID = radioGroup_report.getCheckedRadioButtonId();
                        if (radioButtonID != R.id.radioButton_opinion && radioButtonID != R.id.radioButton_suggestion && radioButtonID != R.id.radioButton_complaint) {
                            Toast.makeText(dialogLayout.getContext(), "Lütfen Görüş, Öneri ya da Şikayet'i işaretleyin!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String reportType = "";
                        switch (radioButtonID) {
                            case R.id.radioButton_opinion:
                                reportType = "Görüş";
                                Toast.makeText(dialogLayout.getContext(), "Görüşünüz başarıyla iletildi.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.radioButton_suggestion:
                                reportType = "Öneri";
                                Toast.makeText(dialogLayout.getContext(), "Öneriniz başarıyla iletildi.", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.radioButton_complaint:
                                reportType = "Şikayet";
                                Toast.makeText(dialogLayout.getContext(), "Şikayetiniz başarıyla iletildi.", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        String report = editText_customerIdea.getText().toString();
                        String reportDate = new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss", Locale.getDefault()).format(new Date());

                        CustomerReport customerReport = new CustomerReport(customer.getName(), reportType, report, reportDate);
                        databaseHelper.storeReport(customerReport);
                    }
                })
                .setNegativeButton("İptal", null);
        return builder.create();
    }
}
