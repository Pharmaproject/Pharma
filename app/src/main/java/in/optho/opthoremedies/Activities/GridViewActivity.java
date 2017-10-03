package in.optho.opthoremedies.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import in.optho.opthoremedies.Adapters.MyGridLayoutAdapter;
import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.R;

public class GridViewActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    ArrayList<String> productList=new ArrayList<>();


    private SharedPreferences sharedpreferences;
    private boolean isGridView=false;

    MyGridLayoutAdapter adapter;

    private void initialise() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(GridViewActivity.this);
//        linearLayoutManager=new GridLayoutManager(GridViewActivity.this,2);
        sharedpreferences = getSharedPreferences("PRODUCT_ID", Context.MODE_PRIVATE);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_read_activity, menu);

        //for favourite Button
        MenuItem item = menu.findItem(R.id.layoutButton);
//        return true;
/*
        if(sharedpreferences.getBoolean("isGridView", false)){
            item.setIcon(R.drawable.list_icon);
            isGridView = true;
        }*/
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.layoutButton) {
            if(isGridView){
                Toast.makeText(this, "ListView Enabled", Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GridViewActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                MyGridLayoutAdapter adapter = new MyGridLayoutAdapter(GridViewActivity.this, productList);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("isGridView", false);
                isGridView = false;
                item.setIcon(R.drawable.grid_icon);


            }else{
                Toast.makeText(this, "GridView enabled", Toast.LENGTH_SHORT).show();
                linearLayoutManager =new GridLayoutManager(GridViewActivity.this,2);

                recyclerView.setLayoutManager(linearLayoutManager);
                MyGridLayoutAdapter adapter = new MyGridLayoutAdapter(GridViewActivity.this, productList);
                recyclerView.setAdapter(adapter);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("isGridView", true);
                isGridView = true;
                item.setIcon(R.drawable.list_icon);

            }
            return true;

        }
        if(id==R.id.menuSortAlphabet){
//            Toast.makeText(this, "sort clicked", Toast.LENGTH_SHORT).show();
            Collections.sort(productList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            Toast.makeText(this, "sorted Alphabetically", Toast.LENGTH_LONG).show();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        //Initialise all variables here
        initialise();


        //checking if product data is already saved or not
        boolean isDataSaved=sharedpreferences.getBoolean("isDataSaved", false);
        if(isDataSaved){
//            insertData();
        }else
            insertData();




        //fetching the data from the database in ArrayList
        ProductDatabaseHelper db = new ProductDatabaseHelper(this);
        productList=db.getProductList();


//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyGridLayoutAdapter(GridViewActivity.this, productList);
        recyclerView.setAdapter(adapter);


    }


    private void insertData() {
        ProductDatabaseHelper db = new ProductDatabaseHelper(GridViewActivity.this);


        db.insertData("Kumar");
        db.insertData("Leader");
        db.insertData("Bhuvan");
        db.insertData("Ankit");
        db.insertData("Madhav");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isDataSaved", true);
        editor.commit();

    }
}
