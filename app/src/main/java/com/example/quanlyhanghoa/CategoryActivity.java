package com.example.quanlyhanghoa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private ProductHelper productHelper;
    EditText txtName,txtId;
    Button btnAddCategory;
    private ListView lvCategory;
    private ArrayAdapter<Category> adapterCategory;
    private String idCategory;
    private ArrayAdapter arrayAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        productHelper = new ProductHelper(this);
        lvCategory = findViewById(R.id.lvAddCategory);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        txtName = findViewById(R.id.edtAddNameCategory);
        txtId = findViewById(R.id.edtAddIdCategory);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = txtId.getText().toString();
                String nameCategory = txtName.getText().toString();
                try{
                if (nameCategory.trim().length() > 0) {

                    Category cate = new Category(id, nameCategory);
                    productHelper.addCategory(cate);
                    Toast.makeText(CategoryActivity.this, "Category added", Toast.LENGTH_SHORT).show();

                }}
                catch (Exception e){
                    Toast.makeText(CategoryActivity.this, "Category added fail", Toast.LENGTH_SHORT).show();
                }
                txtId.setText("");
                txtName.setText("");
                loadCategory();
            }
        });
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle short click
                Category category = (Category) arrayAdapter.getItem(position);
                updateCategory(category);
            }
        });

        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle long click

                Category category = (Category) arrayAdapter.getItem(position);
                deleteCategory(category);

                return true; // indicate the event is handled
            }
        });


        loadCategory();
    }
    public void loadCategory(){
        ArrayList<Category> categories = productHelper.getAllCategories();
        // Create a new instance of ProductAdapter with the list of products
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,categories);
        // Set the array adapter to listView
        lvCategory.setAdapter(arrayAdapter);

    }
    public void deleteCategory(Category category) {
        // Create alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title of dialog
        builder.setTitle("Delete Category");
        // Set message of dialog
        builder.setMessage("Are you sure you want to delete this Category?");
        // Set positive button of dialog
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the student from the database
                productHelper.deleteCategory(category);
                // Show a toast message
                Toast.makeText(CategoryActivity.this, "Category deleted", Toast.LENGTH_SHORT).show();
                // Reload the list of Product
                loadCategory();
            }
        });
        // Set negative button of dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        // Create and show the dialog
        builder.create().show();
    }
    public void updateCategory(Category category){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title of dialog
        builder.setTitle("Update Category");
        // Set layout of dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_category, null);
        builder.setView(dialogView);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the dialog as an alert dialog
                AlertDialog alertDialog = (AlertDialog) dialog;
                try {
                    TextView editTextId = alertDialog.findViewById(R.id.editTextcategoryId);
                    EditText editTextName = alertDialog.findViewById(R.id.editTextcategoryName);

                    // Get the input values from the edit texts
                    String id = editTextId.getText().toString();
                    String name = editTextName.getText().toString();

                    // Set the input values to the category
                    category.setCategoryId(id);
                    category.setCategoryName(name);

                    // Update the category in the database
                    productHelper.updateCategory(category);
                    // Show a toast message
                    Toast.makeText(CategoryActivity.this, "Category updated", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(CategoryActivity.this, "Category update fail", Toast.LENGTH_SHORT).show();
                }// Reload the list of Product
                loadCategory();
            }
        });

        // Set negative button of dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Set the edit texts with the current values of the product
        TextView editTextId = alertDialog.findViewById(R.id.editTextcategoryId);
        EditText editTextName = alertDialog.findViewById(R.id.editTextcategoryName);

        editTextName.setText(category.getCategoryName());
        editTextId.setText(category.getCategoryId());

    }

}