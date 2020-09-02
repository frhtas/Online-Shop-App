package ce.yildiz.edu.tr.sanalmarketotomasyonu.customer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.R;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Order;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.database.DatabaseHelper;


// Müşterinin Siparişlerim kısmında gördüğü siparişlerin kartlar şeklinde gösterilmesi için oluşturulan Adapter sınıfı
public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.OrderHolder> {
    private Order myOrder;
    private ArrayList<Order> orders;
    private LayoutInflater inflater;

    DatabaseHelper databaseHelper;

    public CustomerOrderAdapter(Context context, ArrayList<Order> orders) {
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;

        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_customerorder_card, parent, false);
        OrderHolder holder = new OrderHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order selectedOrder = orders.get(position);
        holder.setData(selectedOrder, position);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }



    class OrderHolder extends RecyclerView.ViewHolder {
        TextView textView_onSent, textView_onReady, textView_onRoad, textView_delivered, textView_totalPriceCustomerOrder, textView_customerOrderDate;
        Button button_rateOrder;
        RatingBar ratingBar_order;
        ListView listView_customerorder;
        SeekBar seekBar_orderStatu;
        LinearLayout linearLayout;
        CardView itemCustomerOrderCard;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            textView_onSent = (TextView) itemView.findViewById(R.id.textView_onSent);
            textView_onReady = (TextView) itemView.findViewById(R.id.textView_onReady);
            textView_onRoad = (TextView) itemView.findViewById(R.id.textView_onRoad);
            textView_delivered = (TextView) itemView.findViewById(R.id.textView_delivered);
            textView_totalPriceCustomerOrder = (TextView) itemView.findViewById(R.id.textView_totalPriceCustomerOrder);
            textView_customerOrderDate = (TextView) itemView.findViewById(R.id.textView_customerOrderDate);
            listView_customerorder = (ListView) itemView.findViewById(R.id.listView_customerorder);
            button_rateOrder = (Button) itemView.findViewById(R.id.button_rateOrder);
            ratingBar_order = (RatingBar) itemView.findViewById(R.id.ratingBar_order);
            seekBar_orderStatu = (SeekBar) itemView.findViewById(R.id.seekBar_orderStatu);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_customerOrder);
            itemCustomerOrderCard = (CardView) itemView.findViewById(R.id.item_customerorder_card);
        }

        public void setData(final Order selectedOrder, final int position) {
            textView_totalPriceCustomerOrder.setText(String.valueOf(selectedOrder.getTotalOrderPrice()) + " TL");
            textView_customerOrderDate.setText(selectedOrder.getOrderDate());
            ratingBar_order.setRating(selectedOrder.getOrderRating());

            // Siparişte bulunan ürün bilgilerinin ListView'da gösterilmesi
            List<Product> products = selectedOrder.getCart().getProducts();
            ArrayList<String> productsOnCart = new ArrayList<>();
            for (Product p : products) {
                String info = "Ürün Adı: " + p.getName() + " (" + p.getAmount_str() + ")" +
                            "\nMiktar: " + p.getNumberOf() +
                            "       Fiyat: " + selectedOrder.getCart().getProductPrice(p) + " TL";
                productsOnCart.add(info);
            }
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(itemView.getContext(), R.layout.simple_list_item, productsOnCart);
            listView_customerorder.setAdapter(listAdapter);

            seekBar_orderStatu.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            // Sipariş durumuna göre sepetin yerinin ve durumun Siparişlerim ekranında değişmesi
            int orderStatu = selectedOrder.getOrderStatu();
            switch (orderStatu) {
                case 0:
                    textView_onSent.setVisibility(View.VISIBLE); // Sipariş Verildi
                    seekBar_orderStatu.setProgress(0);
                    break;
                case 1:
                    textView_onReady.setVisibility(View.VISIBLE); // Sipariş Hazırlanıyor
                    seekBar_orderStatu.setProgress(1);
                    break;
                case 2:
                    textView_onRoad.setVisibility(View.VISIBLE); // Sipariş Yolda
                    seekBar_orderStatu.setProgress(2);
                    break;
                case 3:
                    textView_delivered.setVisibility(View.VISIBLE); // Sipariş Teslim Edildi
                    seekBar_orderStatu.setProgress(3);
                    ratingBar_order.setVisibility(View.VISIBLE);
                    if (ratingBar_order.getRating() != 0) {
                        button_rateOrder.setVisibility(View.INVISIBLE);
                        ratingBar_order.setIsIndicator(true);
                        ratingBar_order.setProgressTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.colorBackground_app)));
                        break;
                    }
                    button_rateOrder.setVisibility(View.VISIBLE);
                    break;
            }


            button_rateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOrder.setOrderRating((int) ratingBar_order.getRating());
                    databaseHelper.updateOrder(selectedOrder);
                    button_rateOrder.setVisibility(View.INVISIBLE);
                    ratingBar_order.setIsIndicator(true);
                    ratingBar_order.setProgressTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.colorBackground_app)));
                    Toast.makeText(view.getContext(), "Değerlendirmeniz Alındı!", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

}
