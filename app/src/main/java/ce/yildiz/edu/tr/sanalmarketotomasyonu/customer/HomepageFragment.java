package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import android.content.Context;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;


// Müşteriye Ana Sayfa'da ürünleri gösteren menünün oluşturulduğu Fragment sınıfı
public class HomepageFragment extends Fragment {
    EditText editText_search;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Product> productList;
    Context myContext;


    //----------------Sepette kaç adet ürünün bulunduğunu bildirim olarak göstermek için oluşturulan interface------
    private FragmentListener fragmentListener;
    public interface FragmentListener {
        void onRefreshBadge(int badgeCount);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (FragmentListener) context;
    }
    //------------------------------------------------------------------------------------------------------------


    public HomepageFragment(Context context, ArrayList<Product> productList) {
        this.productList = productList;
        this.myContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_product);
        productAdapter = new ProductAdapter(getActivity(), productList);
        recyclerView.setAdapter(productAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

        editText_search = (EditText) view.findViewById(R.id.editText_search);
        editText_search.addTextChangedListener(new TextWatcher() {
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


        productAdapter.setListener(new ProductAdapter.AdapterListener() {
            @Override
            public void onClick(int badgeCount) {
                fragmentListener.onRefreshBadge(badgeCount);
            }
        });

        return view;
    }


    // Müşteri ürün aramak istediğinde ürünleri filtreleyen ve yapılan aramaya göre güncelleyen metod
    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();

        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }

}
