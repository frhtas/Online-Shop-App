package ce.yildiz.edu.tr.sanalmarketotomasyonu.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
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


// Market görevlisinin Siparişler kısmında gördüğü siparişlerin kartlar şeklinde gösterilmesi için oluşturulan Adapter sınıfı
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Order myOrder;
    private ArrayList<Order> orders;
    private LayoutInflater inflater;

    DatabaseHelper databaseHelper;

    public OrderAdapter(Context context, ArrayList<Order> orders) {
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;

        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order_card, parent, false);
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
        TextView textView_orderDate, textView_totalPriceOrder, textView_customerName, textView_customerAddress;
        Button button_confirmOrder;
        ListView listView_order;
        RadioGroup radioGroup_orderStatus;
        LinearLayout linearLayout;
        CardView itemOrderCard;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            textView_orderDate = (TextView) itemView.findViewById(R.id.textView_orderDate);
            textView_totalPriceOrder = (TextView) itemView.findViewById(R.id.textView_totalPriceOrder);
            textView_customerName = (TextView) itemView.findViewById(R.id.textView_customerName);
            textView_customerAddress = (TextView) itemView.findViewById(R.id.textView_customerAddress);
            button_confirmOrder = (Button) itemView.findViewById(R.id.button_confirmOrder);
            listView_order = (ListView) itemView.findViewById(R.id.listView_order);
            radioGroup_orderStatus = (RadioGroup) itemView.findViewById(R.id.radioGroup_orderStatus);
            radioGroup_orderStatus.setEnabled(false);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout_order);
            itemOrderCard = (CardView) itemView.findViewById(R.id.item_order_card);
        }

        public void setData(final Order selectedOrder, final int position) {
            textView_orderDate.setText(selectedOrder.getOrderDate());
            textView_customerName.setText(selectedOrder.getCustomer().getName());
            textView_customerAddress.setText(selectedOrder.getCustomer().getAddress());
            textView_totalPriceOrder.setText("Toplam: " + String.valueOf(selectedOrder.getTotalOrderPrice()) + " TL");

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
            listView_order.setAdapter(listAdapter);

            // Müşteriyi sipariş durumu hakkında bilgilendirmek için RadioButton'lar kullanıldı
            switch (selectedOrder.getOrderStatu()) {
                case 0:
                    radioGroup_orderStatus.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    button_confirmOrder.setVisibility(View.INVISIBLE);
                    radioGroup_orderStatus.check(R.id.radioButton_orderOnReady);
                    break;
                case 2:
                    button_confirmOrder.setVisibility(View.INVISIBLE);
                    radioGroup_orderStatus.check(R.id.radioButton_orderOnRoad);
                    break;
            }

            // Sipariş onaylanması
            button_confirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioGroup_orderStatus.setVisibility(View.VISIBLE);
                    radioGroup_orderStatus.setEnabled(true);
                    radioGroup_orderStatus.check(R.id.radioButton_orderOnReady);
                    ArrayList<Product> soldProducts = new ArrayList<>(selectedOrder.getCart().getProducts());
                    databaseHelper.updateProductsAmount(soldProducts);
                    button_confirmOrder.setVisibility(View.INVISIBLE);
                }
            });

            // RadioGroup'un dinlenmesi ve konuma göre sipariş durumunun göncellenmesi
            radioGroup_orderStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radioButton_orderOnReady: // Sipariş Hazırlanıyor
                            selectedOrder.setOrderStatu(1);
                            databaseHelper.updateOrder(selectedOrder);
                            databaseHelper.close();
                            Toast.makeText(itemView.getContext(), "Sipariş Hazırlanıyor!", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.radioButton_orderOnRoad: // Sipariş Yolda
                            selectedOrder.setOrderStatu(2);
                            databaseHelper.updateOrder(selectedOrder);
                            databaseHelper.close();
                            Toast.makeText(itemView.getContext(), "Sipariş Yolda!", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.radioButton_orderDelivered: // Sipariş Teslim Edildi
                            selectedOrder.setOrderStatu(3);
                            databaseHelper.updateOrder(selectedOrder);
                            databaseHelper.close();
                            Toast.makeText(itemView.getContext(), "Sipariş Teslim Edildi!", Toast.LENGTH_SHORT).show();
                            removeAt(getAdapterPosition());
                            break;
                    }
                }
            });

        }

        public void removeAt(int position) {
            orders.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orders.size());
        }
    }


}
