package in.optho.opthoremedies.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.optho.opthoremedies.Adapters.MyGridLayoutAdapter;
import in.optho.opthoremedies.Adapters.MyListLayoutAdapter;
import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

public class MainListActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private MyGridLayoutAdapter gridAdapter;
    private MyListLayoutAdapter listAdapter;

    ArrayList<Product> productdb=new ArrayList<>();

    private boolean isGridView=true;

    private void initialise() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainListActivity.this);
        gridLayoutManager =new GridLayoutManager(MainListActivity.this,2);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_read_activity, menu);

        MenuItem item = menu.findItem(R.id.layoutButton);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.layoutButton) {
            if(isGridView){
                Toast.makeText(this, "List Enabled", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(linearLayoutManager);
//                MyListLayoutAdapter gridAdapter = new MyListLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(listAdapter);
                isGridView = false;
                item.setIcon(R.drawable.grid_icon);


            }else{
                Toast.makeText(this, "Grid enabled", Toast.LENGTH_SHORT).show();
                recyclerView.setLayoutManager(gridLayoutManager);
                //MyGridLayoutAdapter gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(gridAdapter);
                isGridView = true;
                item.setIcon(R.drawable.list_icon);

            }
            return true;

        }
        if(id==R.id.sortAlphabet){
//            Toast.makeText(this, "sort clicked", Toast.LENGTH_SHORT).show();
       //     Collections.sort(gridAdapter);

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return product.getName().compareTo(t1.getName());
                }
            });
            gridAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();
//            recyclerView.setAdapter(gridAdapter);
            Toast.makeText(this, "sorted Alphabetically", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortDefault){
//            Toast.makeText(this, "sort clicked", Toast.LENGTH_SHORT).show();
            //     Collections.sort(gridAdapter);

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return product.getpDefault().compareTo(t1.getpDefault());
                }
            });
            gridAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();

            Toast.makeText(this, "sorted Alphabetically", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortCategory){
//            Toast.makeText(this, "sort clicked", Toast.LENGTH_SHORT).show();
            //     Collections.sort(gridAdapter);

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return product.getCategory().compareTo(t1.getCategory());
                }
            });
            gridAdapter.notifyDataSetChanged();
            Toast.makeText(this, "sorted Alphabetically", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortMostFreqUsed){
            Toast.makeText(this, "feature not availabe", Toast.LENGTH_SHORT).show();
        }



        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        //Initialise all variables here
        initialise();



        //fetching the data from the database in ArrayList
        ProductDatabaseHelper db = new ProductDatabaseHelper(this);
        productdb=db.getProductList();







//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
        listAdapter = new MyListLayoutAdapter(MainListActivity.this, productdb);
        recyclerView.setAdapter(gridAdapter);

        isGridView = true;

    }

}
