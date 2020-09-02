package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;

// Müşterinin ana sayfada gördüğü ürünlerin kartlar şeklinde gösterilmesi için oluşturulan Adapter sınıfı
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private ArrayList<Product> productList;
    private LayoutInflater inflater;

    Context myContext;
    DatabaseHelper databaseHelper;


    //----------------Sepette kaç adet ürünün bulunduğunu bildirim olarak göstermek için oluşturulan interface--------------------------
    private AdapterListener mListener;
    int badgeCount;
    public interface AdapterListener {
        void onClick(int badgeCount);
    }
    public void setListener(AdapterListener listener) {
        this.mListener = listener;
    }
    //---------------------------------------------------------------------------------------------------------------------------------


    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.productList = products;
        this.myContext = context;

        databaseHelper = new DatabaseHelper(context);

        badgeCount = 0;
        for (Product product : productList) {
            badgeCount += product.getNumberOf();
        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product_card, parent, false);
        ProductHolder holder = new ProductHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product selectedProduct = productList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(ArrayList<Product> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView textView_productName, textView_productPrice, textView_productAmountStr, textView_onCartCount;
        ImageView imageView_productImage;
        Button button_addToCart, button_oddFromCart;
        LinearLayout linearLayout;
        CardView itemProductCard;


        public ProductHolder(View itemView) {
            super(itemView);
            textView_productName = (TextView) itemView.findViewById(R.id.textView_productName);
            textView_productPrice = (TextView) itemView.findViewById(R.id.textView_productPrice);
            textView_productAmountStr = (TextView) itemView.findViewById(R.id.textView_productAmountStr);
            textView_onCartCount = (TextView) itemView.findViewById(R.id.textView_onCartCount);
            imageView_productImage = (ImageView) itemView.findViewById(R.id.imageView_productImage);
            button_addToCart = (Button) itemView.findViewById(R.id.button_addToCart);
            button_oddFromCart =(Button) itemView.findViewById(R.id.button_oddFromCart);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemProductCard = (CardView) itemView.findViewById(R.id.item_product_card);
        }


        public void setData(final Product selectedProduct, int position) {
            textView_productName.setText(selectedProduct.getName());
            textView_productPrice.setText(selectedProduct.getPrice().toString() + " TL");
            imageView_productImage.setImageBitmap(selectedProduct.getImage());
            textView_productAmountStr.setText(selectedProduct.getAmount_str());
            textView_onCartCount.setText(String.valueOf(selectedProduct.getNumberOf()));

            // Ana Sayfa kısmında ürünlerin + butonuna basılırsa sepete 1 adet daha eklendiği kısım
            button_addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    badgeCount++;
                    selectedProduct.setNumberOf(selectedProduct.getNumberOf() + 1);

                    mListener.onClick(badgeCount);
                    updateList(productList);
                    databaseHelper.updateProduct(selectedProduct);
                }
            });

            // Ana Sayfa kısmında ürünlerin - butonuna basılırsa sepetten 1 adet çıkarıldığı kısım
            button_oddFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedProduct.getNumberOf() == 0)
                        return;
                    badgeCount--;
                    selectedProduct.setNumberOf(selectedProduct.getNumberOf() - 1);

                    mListener.onClick(badgeCount);
                    updateList(productList);
                    databaseHelper.updateProduct(selectedProduct);
                }
            });

        }

    }

}
