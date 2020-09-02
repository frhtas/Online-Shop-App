package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

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


// Market görevlisine gelen siparişleri gösteren menünün oluşturulduğu Fragment sınıfı
public class OrdersFragment extends Fragment {
    RecyclerView recyclerView_orders;
    ArrayList<Order> orders;
    OrderAdapter orderAdapter;

    public OrdersFragment(ArrayList<Order> allOrders) {
        this.orders = allOrders;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (orders.size() == 0) {
            view = inflater.inflate(R.layout.fragment_orders_null, container, false);
            return view;
        }
        else
            view = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView_orders = (RecyclerView) view.findViewById(R.id.recyclerView_orders);
        orderAdapter = new OrderAdapter(getActivity(), orders);
        recyclerView_orders.setAdapter(orderAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_orders.setLayoutManager(layoutManager);

        return view;
    }

}
