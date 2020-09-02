package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;


public class ProductsFragment extends Fragment {
    EditText editText_searchMarket;
    RecyclerView recyclerView;
    MarketProductAdapter marketProductAdapter;
    ArrayList<Product> productList;

    public ProductsFragment(ArrayList<Product> products) {
        this.productList = products;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_marketproducts);
        marketProductAdapter = new MarketProductAdapter(getActivity(), productList);
        recyclerView.setAdapter(marketProductAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        editText_searchMarket = (EditText) view.findViewById(R.id.editText_searchMarket);
        editText_searchMarket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                filter(text.toString());
            }
        });

        return view;
    }


    // Market görevlisi ürün aramak istediğinde ürünleri filtreleyen ve yapılan aramaya göre güncelleyen metod
    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();

        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        marketProductAdapter.updateList(filteredList);
    }
}
