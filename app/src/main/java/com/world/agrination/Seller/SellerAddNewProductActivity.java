package com.world.agrination.Seller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.world.agrination.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {


    private ProgressDialog loadingBar;

    private String CategoryName, Descripton, Price, ProductName, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductBtn;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;


    private static final int GalleryPick = 1;

    private Uri ImageUri;
    private String ProductRandomKey, downloadImageURL;
    private StorageReference ProductImageRef;

    private DatabaseReference ProductRef, sellersRef;


    private String sName, sAddress, sEmail, sPhone, sID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);


        CategoryName = getIntent().getExtras().get("Category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");


        AddNewProductBtn = (Button) findViewById(R.id.add_new_product_btn);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);


        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        sellersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            sName = dataSnapshot.child("name").getValue().toString();
                            sAddress = dataSnapshot.child("address").getValue().toString();
                            sPhone = dataSnapshot.child("phone").getValue().toString();
                            sEmail = dataSnapshot.child("email").getValue().toString();
                            sID = dataSnapshot.child("sellerID").getValue().toString();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void OpenGallery() {
        Intent gallaeryIntent = new Intent();
        gallaeryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaeryIntent.setType("image/*");
        startActivityForResult(gallaeryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }


    }

    private void ValidateProductData() {
        Descripton = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        ProductName = InputProductName.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "Product Image Is Mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Descripton)) {
            Toast.makeText(this, "Enter Product Descrition...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Enter Product Price...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ProductName)) {
            Toast.makeText(this, "Enter Product Name...", Toast.LENGTH_SHORT).show();
        } else {

            StoreProductInfo();
        }
    }

    private void StoreProductInfo() {


        loadingBar.setTitle("Processing...");
        loadingBar.setMessage("Wait Until Process Not Completed.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");////// add underscore "_" for spacing in end yyy
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        ProductRandomKey = saveCurrentDate + saveCurrentTime;


        StorageReference filepath = ProductImageRef.child(ImageUri.getLastPathSegment() + ProductRandomKey + ".jpg");

        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Product Image Uploaded Successfully....", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadImageURL = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageURL = task.getResult().toString();

                            Toast.makeText(SellerAddNewProductActivity.this, "Got The Product Image Successfully To Database....", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();

                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put("pid", ProductRandomKey);
        productmap.put("date", saveCurrentDate);
        productmap.put("time", saveCurrentTime);
        productmap.put("description", Descripton);
        productmap.put("image", downloadImageURL);
        productmap.put("category", CategoryName);
        productmap.put("price", Price);
        productmap.put("pname", ProductName);


        productmap.put("SellerName", sName);
        productmap.put("SellerAddress", sAddress);
        productmap.put("SellerEmail", sEmail);
        productmap.put("SellerPhone", sPhone);
        productmap.put("sellerID", sID);
        productmap.put("productState", "Not Approved");

        ProductRef.child(ProductRandomKey).updateChildren(productmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellersHomeActivity.class);
                            startActivity(intent);


                            loadingBar.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product Is Added Successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


}