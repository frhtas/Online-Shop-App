package ce.yildiz.edu.tr.sanalmarketotomasyonu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Customer;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.CustomerReport;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Order;
import ce.yildiz.edu.tr.sanalmarketotomasyonu.classes.Product;


// Ürünlere, Müşterilere ve Siparişlere ait bilgilerin saklanması için oluşturulan Veritabanı sınıfı (SQLite kullanıldı)
public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/ce.yildiz.edu.tr.sanalmarketotomasyonu/databases/";
    private static String DB_NAME = "SanalMarket.db";
    private static int DB_VERSION = 5;
    public static String DATABASE_TABLE = "customer";

    private SQLiteDatabase myDatabase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }


    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (dbExist) {
            Log.i("tag", "dbExist: " + dbExist);
        }
        else {
            Log.i("tag", "dbNotExist: " + dbExist);
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    // Uygulamanın ilk açılışında önceden oluşturulan veritabanının uygulamada güncellenebilmesi için kopyalama işlemi yapıldı.
    private void copyDatabase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
            return true;
        }
        return false;
    }

    public void openDatabase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    // Müşterinin kayıt olduktan sonra bilgilerinin veritabanına alınması için oluşturulan metod
    public void addUser(Customer customer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", customer.getName());
        values.put("email", customer.getEmail());
        values.put("phone", customer.getPhone());
        values.put("username", customer.getUsername());
        values.put("password", customer.getPassword());
        values.put("bdate", customer.getBdate());
        values.put("address", customer.getAddress());

        db.insert("customer", null, values);
        db.close();
    }


    // Kayıt olmak isteyen müşterininin bilgilerinin veritabanında kontrol edildiği metod, aynı email ya da Kullanıcı Adı'nı kullanan başka biri var mı diye...
    public boolean checkUserSignup(String Email, String Username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT email, username FROM customer WHERE email = '" + Email + "' OR username = '" + Username + "' ";
        Cursor cursor = db.rawQuery(query, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }


    // Müşteri giriş ekranında kullanıcının girdiği Kullanıcı Adı ve Şifre'nin veritabanında kontrol edilmesi için oluştutulan metod
    public boolean checkUserLogin(String Username, String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " SELECT username, password FROM customer WHERE username = '" + Username + "' AND password = '" + Password + "' ";
        Cursor cursor = db.rawQuery(query, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }


    // Market giriş ekranında kullanıcının girdiği Kullanıcı Adı ve Şifre'nin veritabanında kontrol edilmesi için oluştutulan metod
    public boolean checkUserLoginMarket(String Username, String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " SELECT username, password FROM employee WHERE username = '" + Username + "' AND password = '" + Password + "' ";
        Cursor cursor = db.rawQuery(query, null);

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }


    // Kullanıcı Adı'na göre veritabanından müşteri bilgilerinin alındığı metod, gerekli yerde kullanılmak amacıyla
    public Customer getCustomer(String Username) {
        String name = null, address = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name, address FROM customer WHERE username = '" + Username + "' ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while(cursor.moveToNext()) {
                name = cursor.getString(0);
                address = cursor.getString(1);
            }
        }
        cursor.close();
        db.close();
        return new Customer(Username, name, address);
    }


    // Kullanıcı Adı'na göre veritabanından müşterinin bütün bilgilerinin alındığı metod, Profilim kısmında göstermek amacıyla
    public Customer getCustomerInfos(String Username) {
        SQLiteDatabase db = getWritableDatabase();
        String name = null, email = null, phone = null, username = null, password = null, bdate = null, address = null;
        String query = "SELECT * FROM customer WHERE username = '" + Username + "' ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while(cursor.moveToNext()) {
                name = cursor.getString(1);
                email = cursor.getString(2);
                phone = cursor.getString(3);
                username = cursor.getString(4);
                password = cursor.getString(5);
                bdate = cursor.getString(6);
                address = cursor.getString(7);
            }
        }
        cursor.close();
        db.close();
        return new Customer(name, email, phone, username, password, bdate, address);
    }

    // Veritabanında müşterinin bilgilerinin güncellendiği metod
    public void updateCustomerInfos(Customer customer) {
        String Username = customer.getUsername();
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE customer SET password = ?, phone = ?, address = ? WHERE username = '" + Username + "' ";
        db.execSQL(query, new String[] {customer.getPassword(), customer.getPhone(), customer.getAddress()});
        db.close();
    }


    // Ürün bilgilerini veritabanından alan metod
    public Cursor getProducts() {
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM product", null);
        return cursor;
    }


    // Sepette bulunan ürünlerin bilgilerini veritabanından alan metod
    public Cursor getProductsOnCart() {
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM product WHERE number_of > '" + 0 + "'";
        cursor = db.rawQuery(query, null);
        return cursor;
    }


    // Ürün bilgilerini veritabanında güncelleyen metod, sepete eklendikten sonra vs.
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE product SET name = ?, price = ?, amount = ?, number_of = ? WHERE _id = ?";
        db.execSQL(query, new String[]{product.getName(), String.valueOf(product.getPrice()), String.valueOf(product.getAmount()), String.valueOf(product.getNumberOf()), String.valueOf(product.getId())});
        db.close();
    }

    // Ürün bilgilerini müşteri sepeti onayladıktan sonra veritabanında güncelleyen metod, sepeti boşaltmak için
    public void updateProducts(ArrayList<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE product SET number_of = ? WHERE _id = ?";
        for (Product product : products) {
            db.execSQL(query, new String[]{String.valueOf(0), String.valueOf(product.getId())});
        }
        db.close();
    }

    // Ürün bilgilerini (kalan miktar) ürün teslim edildikten sonra veritabanında güncelleyen metod, sepeti boşaltmak için
    public void updateProductsAmount(ArrayList<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String query = "UPDATE product SET amount = ? WHERE _id = ?";
        for (Product product : products) {
            cursor = db.rawQuery("SELECT amount FROM product WHERE _id = '" + product.getId() + "' ", null);
            while (cursor.moveToNext())
                db.execSQL(query, new String[]{String.valueOf(cursor.getInt(0)-product.getNumberOf()), String.valueOf(product.getId())});
        }
        Objects.requireNonNull(cursor).close();
        db.close();
    }

    // Markette yeterince ürün bulunup bulunmadığını kontrol eden metod
    public boolean enoughProduct(Product myProduct) {
        int productNumberOf = myProduct.getNumberOf();
        int productID = myProduct.getId();
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM product WHERE amount < '" + productNumberOf + "' AND _id = '" + productID + "' ";
        cursor = db.rawQuery(query, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount == 0)
            return true;
        return false;
    }



    // Sipariş objesini veritabanında saklayan metod, müşteri onayladıktan sonra kullanılacak
    public void storeOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put("order_", gson.toJson(order).getBytes());
        values.put("orderstatu", 0);
        values.put("date", order.getOrderDate());

        db.insert("orders", null, values);
        db.close();
    }

    // Sipariş objesini veritabanından alan metod, market görevlisinin Siparişler kısmında görmesi için kullanılacak
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM orders WHERE orderstatu < 3";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                byte[] blob = cursor.getBlob(1);
                String json = new String(blob);
                Gson gson = new Gson();
                Order order = gson.fromJson(json, new TypeToken<Order>() {}.getType());
                order.setOrderID(cursor.getInt(0));
                order.setOrderStatu(cursor.getInt(2));
                order.setOrderDate(cursor.getString(3));
                order.setOrderRating(cursor.getInt(4));
                orders.add(order);
            }
        }

        cursor.close();
        db.close();
        return orders;
    }

    // Spesifik bir müşteriye ait siparişlerin veritabanından alındığı metod, müşterinin Siparişlerim ksımında görebilmesi için
    public ArrayList<Order> getCustomerOrders(Customer customer) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM orders";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                byte[] blob = cursor.getBlob(1);
                String json = new String(blob);
                Gson gson = new Gson();
                Order order = gson.fromJson(json, new TypeToken<Order>() {}.getType());
                order.setOrderID(cursor.getInt(0));
                order.setOrderStatu(cursor.getInt(2));
                order.setOrderDate(cursor.getString(3));
                order.setOrderRating(cursor.getInt(4));
                if (order.getCustomer().getUsername().equals(customer.getUsername()))
                    orders.add(order);
            }
        }
        cursor.close();
        db.close();

        return orders;
    }

    // Sipariş objesini güncelleyen metod, market görevlisinin sipariş durumunu (hazırlanıyor vs.) müşteriye aktarabilmesi için oluşturuldu
    public void updateOrder(Order order) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE orders SET orderstatu = ?, date = ?, rating = ? WHERE _id = ?";
        db.execSQL(query, new String[]{String.valueOf(order.getOrderStatu()), order.getOrderDate(), String.valueOf(order.getOrderRating()), String.valueOf(order.getOrderID())});
        db.close();
    }


    // Verilen güne ait siparişleri getiren metod
    public ArrayList<Order> getOrdersByDay(String currentDate) {
        ArrayList<Order> ordersOfDay = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM orders WHERE date LIKE '%" + currentDate + "%' ";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                byte[] blob = cursor.getBlob(1);
                String json = new String(blob);
                Gson gson = new Gson();
                Order order = gson.fromJson(json, new TypeToken<Order>() {}.getType());
                order.setOrderID(cursor.getInt(0));
                order.setOrderStatu(cursor.getInt(2));
                order.setOrderDate(cursor.getString(3));
                order.setOrderRating(cursor.getInt(4));
                ordersOfDay.add(order);
            }
        }
        cursor.close();
        db.close();

        return ordersOfDay;
    }


    // Müşterinin Görüş, Öneri ya da Şikayetini veritabanına kaydeden metod
    public void storeReport(CustomerReport customerReport) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("customer_name", customerReport.getCustomerName());
        values.put("type", customerReport.getReportType());
        values.put("report", customerReport.getReport());
        values.put("date", customerReport.getReportDate());

        db.insert("customer_reports", null, values);
        db.close();
    }

    // Verilen güne ait müşteri raporlarını veritabanından alan metod
    public ArrayList<CustomerReport> getCustomerReports(String currentDate) {
        ArrayList<CustomerReport> customerReports = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM customer_reports WHERE date LIKE '%" + currentDate + "%' ";
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                CustomerReport customerReport = new CustomerReport();
                customerReport.setId(cursor.getInt(0));
                customerReport.setCustomerName(cursor.getString(1));
                customerReport.setReportType(cursor.getString(2));
                customerReport.setReport(cursor.getString(3));
                customerReport.setReportDate(cursor.getString(4));

                customerReports.add(customerReport);
            }
        }
        cursor.close();
        db.close();

        return customerReports;
    }
}
