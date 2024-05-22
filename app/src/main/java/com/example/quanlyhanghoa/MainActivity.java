package com.example.quanlyhanghoa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ProductHelper object to create and access data
    private ProductHelper productHelper;

    // ListView object to display list of Product
    private ListView listView,lvCategory;
    String arr[];
    final int RESQUEST_TAKE_PHOTO = 123;
    final int RESQUEST_CHOOSE_PHOTO = 321;

    Spinner spinnerCategory;
    ImageView imgAddProduct;

    private ArrayAdapter<String> adapterCategory;
    private String idProduct;
    private Boolean checkChooseImage = false;

    // ArrayAdapter object to connect data with ListView
    private ProductAdapter arrayAdapter;

    // Button object to add new Product
    private Button button,sort,sum,category;

    // Create user interface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign values to fields
        productHelper = new ProductHelper(this);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);
        sum = findViewById(R.id.sum);
        sort = findViewById(R.id.sort);
        category=findViewById(R.id.cate);

        // Register listener for button
        category.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Call add product method
                editcategory();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call add product method
//                try{
                addProduct();
//                }
//                catch (Exception e){
//                    Toast.makeText(MainActivity.this, "Product added fail", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call sum product method
                Sum();
            }
        });
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call add product method
                sortProduct();
            }
        });
        // Register listener for listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                loadProduc0t();
                try{
                // Get the product at the clicked position
                Product product = (Product) arrayAdapter.getItem(position);
                // Call updateproduct method with the Product as argument
                updateProduct(product);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Product update fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Register listener for listView
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                loadProduct();
                // Get the product at the long clicked position
                Product product = (Product) arrayAdapter.getItem(position);
                // Call delete product method with the product as argument
                deleteProduct(product);
                // Return true to indicate the event is handled
                return true;
            }
        });

        // Call loadProduct method
        loadProduct();
    }

    public void loadProduct() {
        // Get list of Product from database
        ArrayList<Product> products = productHelper.getAllProducts();
        // Create a new instance of ProductAdapter with the list of products
        arrayAdapter = new ProductAdapter(products, this);
        // Set the array adapter to listView
        listView.setAdapter(arrayAdapter);
    }

    public void editcategory(){
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
    public void loadCategory() {
        // Assuming that productHelper.getAllCategories() returns an ArrayList<Category>
        ArrayList<String> categoryNames = productHelper.getAllNameCategories();


        if (spinnerCategory != null) {
            adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
            adapterCategory.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spinnerCategory.setAdapter(adapterCategory);
        } else {
            Log.e("MainActivity", "Spinner is null");
        }
    }


    @SuppressLint("MissingInflatedId")
    public void sortProduct() {
        // Create a dialog builder for sorting options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort Products");

        // Inflate the custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_sort, null);
        builder.setView(dialogView);

        // Get the radio buttons and sort button from the dialog view

        RadioButton radioSortByCategory = dialogView.findViewById(R.id.radioSortByCategory);
        RadioButton radioSortAll = dialogView.findViewById(R.id.radioSortALL);
        Button btnSort = dialogView.findViewById(R.id.btnSort);
        Spinner spinner = dialogView.findViewById(R.id.spinselection);
        ArrayList<String> categoryNames = productHelper.getAllNameCategories();
        if (spinner != null) {
            adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
            adapterCategory.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spinner.setAdapter(adapterCategory);
        } else {
            Log.e("MainActivity", "Spinner is null");
        }
        // Create a new dialog to show sorting options
        AlertDialog sortDialog = builder.create();

        // Set a click listener for the "Sort" button
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioSortByCategory.isChecked()) {
                    // Sort by Category
                    // Retrieve the products from your data source (e.g., database)
                    ArrayList<Product> products = productHelper.getAllProducts();
                    ArrayList<Product> sortproducts = new ArrayList<>();
                    // Sort the products by Category

                    String category = spinner.getSelectedItem().toString();
                    for (Product product:products){
                        if (product.getProductCategory()==category)
                            sortproducts.add(product);
                    }
                    // Update the ListView with the sorted products
                    arrayAdapter = new ProductAdapter(sortproducts, MainActivity.this);
                    // Set the array adapter to listView
                    listView.setAdapter(arrayAdapter);
                }
                else if(radioSortAll.isChecked()) {
                   loadProduct();
                }
                // Dismiss the sorting dialog
                sortDialog.dismiss();
            }
        });

        // Show the sorting dialog
        sortDialog.show();
    }




    public void Sum() {
        ArrayList<Product> products = arrayAdapter.getListData();
        int totalQuantity = 0;
        double totalValue;
        totalValue = 0.0;

        for (Product product : products) {
            totalQuantity += product.getProductQuantity();
            totalValue += product.getProductPrice() * product.getProductQuantity();
        }

        // Create and show a dialog to display the totals
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Total Summary");

        // Create a view to display the totals
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_summary, null);
        TextView quantityTextView = dialogView.findViewById(R.id.totalQuantity);
        TextView valueTextView = dialogView.findViewById(R.id.totalValue);

        quantityTextView.setText("Total Quantity: " + totalQuantity);
        valueTextView.setText("Total Value: " + totalValue + "VNĐ");

        builder.setView(dialogView);

        // Set a button to close the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog with the summary
        AlertDialog summaryDialog = builder.create();
        summaryDialog.show();
    }


    // Add new product
    public void addProduct() {
        // Create alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title of dialog
        builder.setTitle("Add Product");

        // Set layout of dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout_add, null);
        builder.setView(dialogView);

        // Get the imgAddProduct ImageView from the dialog view
        ImageView imgAddProduct = dialogView.findViewById(R.id.imgAddProduct);
        spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        loadCategory();
        // Set the click listener for imgAddProduct
        imgAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageSelection(imgAddProduct);
            }
        });

        // Set positive button of dialog
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the dialog as an alert dialog
                AlertDialog alertDialog = (AlertDialog) dialog;
                try {
                    // Get the edit texts from the dialog
                    EditText editTextId = alertDialog.findViewById(R.id.editTextId);
                    EditText editTextName = alertDialog.findViewById(R.id.editTextName);


                    EditText editprice = alertDialog.findViewById(R.id.editTextproductPrice);
                    EditText quan = alertDialog.findViewById(R.id.editTextproductQuantity);
                    EditText editproduction = alertDialog.findViewById(R.id.editTextproduction);

                    // Get the input values from the edit texts
                    String id = editTextId.getText().toString();
                    String name = editTextName.getText().toString();
                    byte[] img = convertImageViewToByte(imgAddProduct);
                    String category = spinnerCategory.getSelectedItem().toString();
                    Double price = Double.parseDouble(editprice.getText().toString());
                    int quantity = Integer.parseInt(quan.getText().toString());
                    String production = editproduction.getText().toString();

                    // Create a new product with the input values
                    Product product = new Product(id, name, img, category, price, quantity, production);
                    // Add the product to the database
                    productHelper.addProduct(product);
                    // Show a toast message
                    Toast.makeText(MainActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Product add fail", Toast.LENGTH_SHORT).show();
                }

                // Reload the list of Products
                loadProduct();
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
        builder.create().show();
    }



    // Function to handle image selection
    private void handleImageSelection(ImageView imageView) {
        Log.d("MyApp", "handleImageSelection called");
        // Create an AlertDialog to choose between Gallery and Camera
        CharSequence options[] = new CharSequence[]{"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Choose from Gallery
                    choosePhoto(imageView);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    // Update existing product
    public void updateProduct(Product product) {
        // Create alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title of dialog
        builder.setTitle("Update Product");
        // Set layout of dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        loadCategory();
        // Get the imgAddProduct ImageView from the dialog view
        ImageView imgAddProduct = dialogView.findViewById(R.id.imgAddProduct);
        spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        loadCategory();
        // Set the click listener for imgAddProduct
        imgAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence image[] = new CharSequence[]{MainActivity.this.getText(R.string.simple_photo), MainActivity.this.getText(R.string.simple_camera)};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(MainActivity.this.getText(R.string.simple_chooseImage));
                builder.setItems(image, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            handleImageSelection(imgAddProduct);
                        } else if (which == 1) {
                            takePicture();
                        }
                    }
                });
                builder.show();
            }
        });

        // Set positive button of dialog
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the dialog as an alert dialog
                AlertDialog alertDialog = (AlertDialog) dialog;
                try {
                    // Get the edit texts from the dialog

                    EditText editTextName = alertDialog.findViewById(R.id.editTextName);


                    EditText editprice = alertDialog.findViewById(R.id.editTextproductPrice);
                    EditText quan = alertDialog.findViewById(R.id.editTextproductQuantity);
                    EditText editproduction = alertDialog.findViewById(R.id.editTextproduction);

                    // Get the input values from the edit texts

                    String name = editTextName.getText().toString();
                    byte[] img = convertImageViewToByte(imgAddProduct);
                    String category = spinnerCategory.getSelectedItem().toString();
                    Double price = Double.parseDouble(editprice.getText().toString());
                    int quantity = Integer.parseInt(quan.getText().toString());
                    String production = editproduction.getText().toString();

                    // Set the input values to the product
                    product.setProductName(name);
                    product.setProductImage(img);
                    product.setProductCategory(category);
                    product.setProductPrice(price);
                    product.setProductQuantity(quantity);
                    product.setProduction(production);
                    // Update the product in the database
                    productHelper.updateProduct(product);
                    // Show a toast message
                    Toast.makeText(MainActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Product update fail", Toast.LENGTH_SHORT).show();
                }// Reload the list of Product
                loadProduct();
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

        // Set neutral button of dialog
        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the Product from the database
                productHelper.deleteProduct(product);
                // Show a toast message
                Toast.makeText(MainActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                // Reload the list of Product
                loadProduct();
            }
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText editTextId = alertDialog.findViewById(R.id.editTextId);
        EditText editTextName = alertDialog.findViewById(R.id.editTextName);
        byte[] img = convertImageViewToByte(imgAddProduct);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        EditText editprice = alertDialog.findViewById(R.id.editTextproductPrice);
        EditText quan = alertDialog.findViewById(R.id.editTextproductQuantity);
        EditText editproduction = alertDialog.findViewById(R.id.editTextproduction);

        editTextName.setText(product.getProductName());
        imgAddProduct.setImageBitmap(bitmap);
        editTextId.setText(product.getProductId());

        // Ensure that loadCategory() has completed before using spinnerCategory
        loadCategory();

        // Use spinnerCategory after it has been initialized
        spinnerCategory = alertDialog.findViewById(R.id.spinnerCategory);
        if (spinnerCategory != null) {
            spinnerCategory.setSelection(adapterCategory.getPosition(product.getProductCategory()));
        } else {
            Log.e("MainActivity", "Spinner is null");
        }

        editprice.setText(product.getProductPrice().toString());
        quan.setText(String.valueOf(product.getProductQuantity()));
        editproduction.setText(product.getProduction());

    }

    // Delete existing product
    public void deleteProduct(Product product) {
        // Create alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title of dialog
        builder.setTitle("Delete Product");
        // Set message of dialog
        builder.setMessage("Are you sure you want to delete this Product?");
        // Set positive button of dialog
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the student from the database
                productHelper.deleteProduct(product);
                // Show a toast message
                Toast.makeText(MainActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                // Reload the list of Product
                loadProduct();
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
    private byte[] convertImageViewToByte(ImageView img){
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(ImageView imageView) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESQUEST_CHOOSE_PHOTO);

        // Store the ImageView reference for later use
        imgAddProduct = imageView;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgAddProduct.setImageBitmap(bitmap);
                    checkChooseImage = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAddProduct.setImageBitmap(bitmap);
                checkChooseImage = true;
            }
        }
    }
}