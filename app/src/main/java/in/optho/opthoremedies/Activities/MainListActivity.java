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

    ArrayList<String> productname=new ArrayList<>();
    ArrayList<String> productcategory=new ArrayList<>();
    ArrayList<String> productdefault=new ArrayList<>();

    ArrayList<Product> productdb=new ArrayList<>();


    private MyGridLayoutAdapter adapter;

    ArrayList<Product> productList=new ArrayList<>();
    private boolean isGridView=false;

    private void initialise() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainListActivity.this);


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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainListActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                MyListLayoutAdapter adapter = new MyListLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(adapter);
                isGridView = false;
                item.setIcon(R.drawable.grid_icon);


            }else{
                Toast.makeText(this, "Grid enabled", Toast.LENGTH_SHORT).show();

                gridLayoutManager =new GridLayoutManager(MainListActivity.this,2);
                recyclerView.setLayoutManager(gridLayoutManager);
                MyGridLayoutAdapter adapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(adapter);
                isGridView = true;
                item.setIcon(R.drawable.list_icon);

            }
            return true;

        }
        if(id==R.id.menuSortAlphabet){
//            Toast.makeText(this, "sort clicked", Toast.LENGTH_SHORT).show();
  //          Collections.sort(adapter.toString());
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

        for (Product p : productdb){
            productname.add(p.getName()); // <-- add it to your List<Item>.
            productcategory.add(p.getCategory());
            productdefault.add(p.getpDefault());

        }







//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
        recyclerView.setAdapter(adapter);


    }

}
