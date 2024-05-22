package com.example.quanlyhanghoa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_NAME = "product";
    private static final String COLUMN_PRODUCTID = "productId";
    private static final String COLUMN_PRODUCTNAME = "productName";
    private static final String COLUMN_PRODUCTIMAGE = "productImage";
    private static final String COLUMN_PRODUCTCATEGORY = "productCategory";
    private static final String COLUMN_PRODUCTPRICE = "productPrice";
    private static final String COLUMN_PRODUCTQUANTITY = "productQuantity";
    private static final String COLUMN_PRODUCTTION = "production";

    private static final String TABLE_NAME2 = "category";
    private static final String COLUMN_CATEGORY_ID = "categoryId";
    private static final String COLUMN_CATEGORY_NAME = "categoryName";

    // Constructor
    public ProductHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_PRODUCTID + " VARCHAR PRIMARY KEY,"
                + COLUMN_PRODUCTNAME + " VARCHAR,"
                + COLUMN_PRODUCTIMAGE + " BLOB,"
                + COLUMN_PRODUCTCATEGORY + " VARCHAR,"
                + COLUMN_PRODUCTPRICE+" DOUBLE,"
                + COLUMN_PRODUCTQUANTITY+ " INTERGER,"
                + COLUMN_PRODUCTTION +  " VARCHAR" + ")";
        String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + "("
                + COLUMN_CATEGORY_ID + " VARCHAR PRIMARY KEY,"
                + COLUMN_CATEGORY_NAME + " VARCHAR" + ")";
        // Execute SQL statement
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
    }

    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SQL statement to drop table
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        // Execute SQL statement
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE2);
        // Create table again
        onCreate(db);
    }
    //Add category
    public void addCategory(Category category) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values to store data
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_ID, category.getCategoryId());
        values.put(COLUMN_CATEGORY_NAME, category.getCategoryName());
        // Insert data into table
        db.insert(TABLE_NAME2, null, values);
        // Close database
        db.close();
    }

    // Add Product
    public void addProduct(Product product) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values to store data
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTID, product.getProductId());
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRODUCTIMAGE,product.getProductImage());
        values.put(COLUMN_PRODUCTCATEGORY, product.getProductCategory());
        values.put(COLUMN_PRODUCTPRICE,product.getProductPrice());
        values.put(COLUMN_PRODUCTQUANTITY, product.getProductQuantity());
        values.put(COLUMN_PRODUCTTION,product.getProduction());

        // Insert data into table
        db.insert(TABLE_NAME, null, values);
        // Close database
        db.close();
    }
    //Get all Category
    @SuppressLint("Range")
    public ArrayList<Category> getAllCategories() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Create list to store Category
        ArrayList<Category> categories = new ArrayList<>();
        // SQL statement to select all records
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME2;
        // Execute SQL statement and get cursor
        Cursor cursor = db.query(TABLE_NAME2, null, null, null, null, null, null);
        // Loop through cursor and create category
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategoryId((cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setCategoryName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // Add category to list
                categories.add(category);
            } while (cursor.moveToNext());
        }
        // Close cursor and database
        cursor.close();
        db.close();
        // Return list of category
        return categories;
    }
    public ArrayList<String> getAllNameCategories() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Create list to store category names
        ArrayList<String> categoryNames = new ArrayList<>();
        // SQL statement to select all category names
        String SELECT_ALL_NAMES = "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_NAME2;
        // Execute SQL statement and get cursor
        Cursor cursor = db.rawQuery(SELECT_ALL_NAMES, null);
        // Loop through cursor and add category names to the list
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                categoryNames.add(categoryName);
            } while (cursor.moveToNext());
        }
        // Close cursor and database
        cursor.close();
        db.close();
        // Return list of category names
        return categoryNames;
    }


    // Get all Product
    @SuppressLint("Range")
    public ArrayList<Product> getAllProducts() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Create list to store Products
        ArrayList<Product> products = new ArrayList<>();
        // SQL statement to select all records
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
        // Execute SQL statement and get cursor
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        // Loop through cursor and create product
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setProductId((cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTID))));
                product.setProductName(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTNAME)));
                product.setProductImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_PRODUCTIMAGE)));
                product.setProductCategory(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTCATEGORY)));
                product.setProductPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRODUCTPRICE)));
                product.setProductQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCTQUANTITY)));
                product.setProduction(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCTTION)));

                // Add product to list
                products.add(product);
            } while (cursor.moveToNext());
        }
        // Close cursor and database
        cursor.close();
        db.close();
        // Return list of product
        return products;
    }
    // Delete category
    public void deleteCategory(Category category) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // SQL statement to delete record
        String DELETE = "DELETE FROM " + TABLE_NAME2 + " WHERE " + COLUMN_CATEGORY_ID + " = ?";
        // Execute SQL statement with student id as argument
        db.delete(TABLE_NAME2, COLUMN_CATEGORY_ID+ " = ?", new String[]{String.valueOf(category.getCategoryId())});
        // Close database
        db.close();
    }
    // Delete product
    public void deleteProduct(Product product) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // SQL statement to delete record
        String DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PRODUCTID + " = ?";
        // Execute SQL statement with student id as argument
        db.delete(TABLE_NAME, COLUMN_PRODUCTID + " = ?", new String[]{String.valueOf(product.getProductId())});
        // Close database
        db.close();
    }
    //Update Category
    public void updateCategory(Category category) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values to store data
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, category.getCategoryName());

        // SQL statement to update record
        String UPDATE = "UPDATE " + TABLE_NAME2 + " SET "
                + COLUMN_CATEGORY_NAME + " = ?"
                + " WHERE " + COLUMN_CATEGORY_ID + " = ?";
        // Execute SQL statement with product data and id as arguments
        db.update(TABLE_NAME2, values, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(category.getCategoryId())});

        // Close database
        db.close();
    }
     //Update product
    public void updateProduct(Product product) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values to store data
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRODUCTIMAGE,product.getProductImage());
        values.put(COLUMN_PRODUCTCATEGORY, product.getProductCategory());
        values.put(COLUMN_PRODUCTPRICE,product.getProductPrice());
        values.put(COLUMN_PRODUCTQUANTITY, product.getProductQuantity());
        values.put(COLUMN_PRODUCTTION,product.getProduction());
        // SQL statement to update record
        String UPDATE = "UPDATE " + TABLE_NAME + " SET "
                + COLUMN_PRODUCTNAME + " = ?,"
                + COLUMN_PRODUCTIMAGE + " = ?,"
                + COLUMN_PRODUCTCATEGORY + " = ?"
                + COLUMN_PRODUCTPRICE + " = ?"
                + COLUMN_PRODUCTQUANTITY + " = ?"
                + COLUMN_PRODUCTTION + " = ?"
                + " WHERE " + COLUMN_PRODUCTID + " = ?";
        // Execute SQL statement with product data and id as arguments
        db.update(TABLE_NAME, values, COLUMN_PRODUCTID + " = ?", new String[]{String.valueOf(product.getProductId())});

        // Close database
        db.close();
    }


}
