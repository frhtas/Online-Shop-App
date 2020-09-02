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
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Cart;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;

// Müşterinin Sepetim kısmında gördüğü ürünlerin kartlar şeklinde gösterilmesi için oluşturulan Adapter sınıfı
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private Cart myCart;
    private ArrayList<Product> productsOnCart;
    private LayoutInflater inflater;

    DatabaseHelper databaseHelper;
    float totalPrice;


    //----------------Sepette kaç adet ürünün bulunduğunu bildirim olarak göstermek için oluşturulan interface------------
    private AdapterListener mListener;
    int badgeCount;
    public interface AdapterListener {
        void onClick(ArrayList<Product> updateList, float totalPrice, int badgeCount);
    }
    public void setListener(AdapterListener listener) {
        this.mListener = listener;
    }
    //-------------------------------------------------------------------------------------------------------------------


    public CartAdapter(Context context, Cart myCart) {
        this.inflater = LayoutInflater.from(context);
        this.productsOnCart = myCart.getProducts();
        this.myCart = myCart;
        this.totalPrice = myCart.getTotalPrice();

        databaseHelper = new DatabaseHelper(context);
        badgeCount = 0;
        for (Product product : productsOnCart) {
            badgeCount += product.getNumberOf();
        }
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cart_card, parent, false);
        CartAdapter.CartHolder holder = new CartAdapter.CartHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Product selectedProduct = productsOnCart.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return productsOnCart.size();
    }

    public void updateList(ArrayList<Product> updateList) {
        productsOnCart = updateList;
        notifyDataSetChanged();
    }


    class CartHolder extends RecyclerView.ViewHolder {
        TextView textView_productNameCart, textView_priceOfProducts, textView_productAmountCart, textView_onCountCart;
        ImageView imageView_productOnCart;
        Button button_add, button_odd;
        LinearLayout linearLayout;
        CardView itemCartCard;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            textView_productNameCart = (TextView) itemView.findViewById(R.id.textView_productNameCart);
            textView_priceOfProducts = (TextView) itemView.findViewById(R.id.textView_priceOfProducts);
            textView_productAmountCart = (TextView) itemView.findViewById(R.id.textView_productAmountCart);
            textView_onCountCart = (TextView) itemView.findViewById(R.id.textView_onCountCart);
            imageView_productOnCart = (ImageView) itemView.findViewById(R.id.imageView_productOnCart);
            button_add = (Button) itemView.findViewById(R.id.button_add);
            button_odd = (Button) itemView.findViewById(R.id.button_odd);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutCart);
            itemCartCard = (CardView) itemView.findViewById(R.id.item_cart_card);
        }

        public void setData(final Product selectedProduct, final int position) {
            imageView_productOnCart.setImageBitmap(selectedProduct.getImage());
            textView_productNameCart.setText(selectedProduct.getName());
            textView_productAmountCart.setText(selectedProduct.getAmount_str());
            textView_onCountCart.setText(String.valueOf(selectedProduct.getNumberOf()));
            textView_priceOfProducts.setText(String.valueOf(selectedProduct.getPriceAll()) + " TL");

            // Sepetim kısmında ürünlerin + butonuna basılırsa sepete 1 adet daha eklendiği kısım
            button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    badgeCount++;
                    selectedProduct.setNumberOf(selectedProduct.getNumberOf() + 1);

                    if (!productsOnCart.contains(selectedProduct))
                        productsOnCart.add(selectedProduct);

                    totalPrice += selectedProduct.getPrice();
                    mListener.onClick(productsOnCart, totalPrice, badgeCount);
                    databaseHelper.updateProduct(selectedProduct);
                }
            });

            // Sepetim kısmında ürünlerin - butonuna basılırsa sepetten 1 adet çıkarıldığı kısım
            button_odd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedProduct.getNumberOf() == 0)
                        return;
                    badgeCount--;
                    selectedProduct.setNumberOf(selectedProduct.getNumberOf() - 1);

                    if (selectedProduct.getNumberOf() == 0)
                        removeAt(getAdapterPosition());

                    totalPrice -= selectedProduct.getPrice();
                    mListener.onClick(productsOnCart, totalPrice, badgeCount);
                    databaseHelper.updateProduct(selectedProduct);
                }
            });

        }

        public void removeAt(int position) {
            productsOnCart.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productsOnCart.size());
        }
    }


}
