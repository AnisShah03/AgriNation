package com.world.agrination.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.world.agrination.R;

public class SellerProductCategoryActivity extends AppCompatActivity {


    private ImageView Crops, Agri_Hardware, Fruits, Vegitables;
    private ImageView Seeds, Fertilizer, Pesticides;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

        /////////////////////

        Crops = (ImageView) findViewById(R.id.Crops);
        Agri_Hardware = (ImageView) findViewById(R.id.Agri_Hardware);
        Fruits = (ImageView) findViewById(R.id.Fruits);
        Vegitables = (ImageView) findViewById(R.id.Vegitables);

        ///////////////////////

        Seeds = (ImageView) findViewById(R.id.Seeds);
        Fertilizer = (ImageView) findViewById(R.id.Fertilizer);
        Pesticides = (ImageView) findViewById(R.id.Pesticides);


        ///////////////////////

        Crops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Crops");
                startActivity(intent);
            }
        });

        Agri_Hardware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Agri_Hardware");
                startActivity(intent);
            }
        });


        Fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Fruits");
                startActivity(intent);
            }
        });

        Vegitables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Vegitables");
                startActivity(intent);
            }
        });


//////////////////////////////////


        Seeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Home11");
                startActivity(intent);
            }
        });

        Fertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Home12");
                startActivity(intent);
            }
        });


        Pesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("Category", "Home13");
                startActivity(intent);
            }
        });


    }
}