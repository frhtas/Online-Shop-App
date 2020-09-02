package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Order;


// Müşterinin siparişlerini gösteren menünün oluşturulduğu Fragment sınıfı
public class CustomerOrdersFragment extends Fragment {
    RecyclerView recyclerView_customerOrders;
    ArrayList<Order> orders;
    CustomerOrderAdapter customerOrderAdapter;

    public CustomerOrdersFragment(ArrayList<Order> allOrders) {
        this.orders = allOrders;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customerorders, container, false);

        recyclerView_customerOrders = (RecyclerView) view.findViewById(R.id.recyclerView_customerOrders);
        customerOrderAdapter = new CustomerOrderAdapter(getActivity(), orders);
        recyclerView_customerOrders.setAdapter(customerOrderAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_customerOrders.setLayoutManager(layoutManager);

        return view;
    }
}
