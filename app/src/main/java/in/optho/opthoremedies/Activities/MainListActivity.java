package in.optho.opthoremedies.Activities;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import in.optho.opthoremedies.Adapters.MyGridLayoutAdapter;
import in.optho.opthoremedies.Adapters.MyListLayoutAdapter;
import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

public class MainListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    int cx,cy,dx,dy;
    float finalRadius;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
//    private GridLayoutManager gridLayoutManager;
    private RecyclerView.LayoutManager gridLayoutManager;
    Animator animator;
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
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        ArrayList<Product> newList = new ArrayList<>();
        for (Product product : productdb) {
            String name = product.getName().toLowerCase();
            if (name.contains(newText)) {
                newList.add(product);
            }
            gridAdapter.setFilter(newList);
            listAdapter.setFilter(newList);
        }

        return false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.layoutButton) {
            if(isGridView){

                animateframe();

                recyclerView.setLayoutManager(linearLayoutManager);
//                MyListLayoutAdapter gridAdapter = new MyListLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(listAdapter);
                isGridView = false;
                item.setIcon(R.drawable.grid_icon);
                recyclerView.setBackgroundColor(getResources().getColor(R.color.transparent));
                Snackbar.make(getWindow().getDecorView(), "List View enabled", Snackbar.LENGTH_SHORT).show();



            }else{
                animateframe();
                recyclerView.setLayoutManager(gridLayoutManager);
                //MyGridLayoutAdapter gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(gridAdapter);
                isGridView = true;
                item.setIcon(R.drawable.list_icon);
                recyclerView.setBackground(getResources().getDrawable(R.drawable.wallbg));
                Snackbar.make(getWindow().getDecorView(), "Shelves View enabled", Snackbar.LENGTH_SHORT).show();


            }
            return true;

        }
        if(id==R.id.sortAlphabet){

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return product.getName().compareTo(t1.getName());
                }
            });
            gridAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();
            Toast.makeText(this, "sorted Alphabetically", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortDefault){

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return product.getpDefault().compareTo(t1.getpDefault());
                }
            });
            gridAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();

            Toast.makeText(this, "sorted by Default", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortCategory){

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return product.getCategory().compareTo(t1.getCategory());
                }
            });
            gridAdapter.notifyDataSetChanged();
            Toast.makeText(this, "sorted by Category", Toast.LENGTH_LONG).show();
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

        recyclerView.setLayoutManager(gridLayoutManager);

        gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
        listAdapter = new MyListLayoutAdapter(MainListActivity.this, productdb);

        recyclerView.setAdapter(gridAdapter);
        recyclerView.setHasFixedSize(true);


        isGridView = true;


    }


    public void animateframe(){

         cx = (recyclerView.getLeft() + recyclerView.getRight()) / 2;
         cy = (recyclerView.getTop() + recyclerView.getBottom()) / 2;

        // get the final radius for the clipping circle
         dx = Math.max(cx, recyclerView.getWidth() - cx);
         dy = Math.max(cy, recyclerView.getHeight() - cy);
        finalRadius = (float) Math.hypot(dx, dy);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            animator = ViewAnimationUtils.createCircularReveal(recyclerView, cx, cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(1000);
            animator.start();
        }else{
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(1000);
            animator.start();
        }
    }





}
