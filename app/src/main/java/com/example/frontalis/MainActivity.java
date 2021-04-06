package com.example.frontalis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.frontalis.Adapter.CategoryAdapter;
import com.example.frontalis.Common.SpaceDecoration;
import com.example.frontalis.DBHelper.DBHelper;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recycler_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Frontalis 2020");
        setSupportActionBar(toolbar);

        recycler_category = findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(this, 2));

        CategoryAdapter adapter = new CategoryAdapter(MainActivity.this, DBHelper.getInstance(this).getAllCategories());
        int spaceInPixel = 8;
        recycler_category.addItemDecoration(new SpaceDecoration(spaceInPixel));
        recycler_category.setAdapter(adapter);

    }
}
