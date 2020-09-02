package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;


// Market tarafında günlük raporların ve müşteri görüşlerinin görüleceği Fragment sınıfı
public class MarketReportsFragment extends Fragment {
    Button button_dayReports, button_customerReports;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_marketreports, container, false);

        button_dayReports = (Button) view.findViewById(R.id.button_dayReports);
        button_dayReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dayReportsIntent = new Intent(getActivity(), DayReportsActivity.class);
                startActivity(dayReportsIntent);
            }
        });


        button_customerReports = (Button) view.findViewById(R.id.button_customerReports);
        button_customerReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customerReportsIntent = new Intent(getActivity(), CustomerReportsActivity.class);
                startActivity(customerReportsIntent);
            }
        });

        return view;
    }

}

