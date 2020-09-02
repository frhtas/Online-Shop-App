package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Cart;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;


// Müşterinin Sepetim bilgilerini gösteren menünün oluşturulduğu Fragment sınıfı
public class ShopCartFragment extends Fragment {
    TextView textView_totalPrice;
    Button button_confirmCart;
    RecyclerView recyclerView_cart;
    CartAdapter cartAdapter;
    Cart myCart;
    ArrayList<Product> productsOnCart;
    DatabaseHelper databaseHelper;


    //----------------Sepette kaç adet ürünün bulunduğunu bildirim olarak göstermek için oluşturulan interface----
    private FragmentListener fragmentListener;
    public interface FragmentListener {
        void onRefresh(int badgeCount, int confirmCart);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (FragmentListener) context;
    }
    //------------------------------------------------------------------------------------------------------------


    public ShopCartFragment(ArrayList<Product> productsOnCart) {
        this.productsOnCart = productsOnCart;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (productsOnCart.size() == 0) {
            view = inflater.inflate(R.layout.fragment_shopcart_null, container, false);
            return view;
        }
        else
            view = inflater.inflate(R.layout.fragment_shopcart, container, false);

        databaseHelper = new DatabaseHelper(getActivity());

        myCart = new Cart(productsOnCart);
        recyclerView_cart = (RecyclerView) view.findViewById(R.id.recyclerView_cart);
        cartAdapter = new CartAdapter(getActivity(), myCart);
        recyclerView_cart.setAdapter(cartAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_cart.setLayoutManager(layoutManager);

        textView_totalPrice = (TextView) view.findViewById(R.id.textView_totalPrice);
        textView_totalPrice.setText(String.valueOf(myCart.getTotalPrice()) + " TL");


        cartAdapter.setListener(new CartAdapter.AdapterListener() {
            @Override
            public void onClick(ArrayList<Product> productsOnCart, float totalPrice, int badgeCount) {
                textView_totalPrice.setText(String.valueOf(totalPrice) + " TL");
                fragmentListener.onRefresh(badgeCount, 0);
                cartAdapter.updateList(productsOnCart);

                if (badgeCount == 0) {

                }
                //myCart = new Cart(productsOnCart);
            }
        });


        button_confirmCart = (Button) view.findViewById(R.id.button_confirmCart);
        // Sepeti Onayla butonuna basılınca Sipariş bilgilerinin gönderilmesi
        button_confirmCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // müşteri ve oluşturduğu sepetin sipariş bilgileri olarak markete gönderildiği kısım (onRefresh() metodunda sipariş bilgileri gönderiliyor)
                ArrayList<Product> products = new ArrayList<>();
                //cartAdapter.updateList(products);
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Sipariş Onayı")
                        .setMessage("Ödeme kapıda yapılacak.\nSiparişi onaylıyor musunuz?")
                        .setIcon(R.drawable.ic_shopping_basket_appcolor_24dp)
                        .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                textView_totalPrice.setText("0.0 TL");
                                fragmentListener.onRefresh(0, 1);
                            }})
                        .setNegativeButton("İptal", null).show();
            }
        });

        return view;
    }

}
