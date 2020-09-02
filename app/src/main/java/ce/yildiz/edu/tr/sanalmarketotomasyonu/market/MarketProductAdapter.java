package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.shawnlin.numberpicker.NumberPicker;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;

// Market görevlisinin Ürünler menüsünde gördüğü ürünlerin kartlar şeklinde gösterilmesi için oluşturulan Adapter sınıfı
public class MarketProductAdapter extends RecyclerView.Adapter<MarketProductAdapter.ProductHolder> {
    private ArrayList<Product> productList;
    private LayoutInflater inflater;

    Context myContext;
    DatabaseHelper databaseHelper;

    public MarketProductAdapter(Context context, ArrayList<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.productList = products;
        this.myContext = context;

        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_marketproduct_card, parent, false);
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
        EditText editText_marketProductName, editText_marketProductPrice;
        ImageView imageView_marketProductImage;
        NumberPicker numberPicker_amount;
        Button button_updateMarketProduct;
        LinearLayout linearLayout;
        CardView itemMarketProductCard;


        public ProductHolder(View itemView) {
            super(itemView);
            editText_marketProductName = (EditText) itemView.findViewById(R.id.editText_marketProductName);
            editText_marketProductPrice = (EditText) itemView.findViewById(R.id.editText_marketProductPrice);
            numberPicker_amount = (NumberPicker) itemView.findViewById(R.id.numberPicker_amount);
            imageView_marketProductImage = (ImageView) itemView.findViewById(R.id.imageView_marketProductImage);
            button_updateMarketProduct = (Button) itemView.findViewById(R.id.button_updateMarketProduct);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutMarketProduct);
            itemMarketProductCard = (CardView) itemView.findViewById(R.id.item_marketproduct_card);
        }


        public void setData(final Product selectedProduct, int position) {
            editText_marketProductName.setText(selectedProduct.getName());
            editText_marketProductPrice.setText(selectedProduct.getPrice().toString());
            //editText_marketProductAmount.setText(String.valueOf(selectedProduct.getAmount()));
            imageView_marketProductImage.setImageBitmap(selectedProduct.getImage());
            numberPicker_amount.setMinValue(0);
            numberPicker_amount.setMaxValue(200);
            numberPicker_amount.setValue(selectedProduct.getAmount());

            numberPicker_amount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldAmount, int newAmount) {
                    selectedProduct.setAmount(newAmount);
                }
            });

            button_updateMarketProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newName = editText_marketProductName.getText().toString();
                    float newPrice = Float.parseFloat(editText_marketProductPrice.getText().toString());
                    int newAmount = numberPicker_amount.getValue();
                    selectedProduct.setName(newName);
                    selectedProduct.setPrice(newPrice);
                    selectedProduct.setAmount(newAmount);
                    databaseHelper.updateProduct(selectedProduct);
                    Toast.makeText(myContext, "Ürün Güncelleme Başarılı!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}
