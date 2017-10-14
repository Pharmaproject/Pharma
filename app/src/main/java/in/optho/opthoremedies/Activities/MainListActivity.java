package in.optho.opthoremedies.Activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
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


    //
    public static ArrayList<Product> tempList=new ArrayList<>();







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
    private int column;

    private static long back_pressed;

    ArrayList<Product> productdb=new ArrayList<>();

    private boolean isGridView=true;

    private void initialise() {

        column =calculateNoOfColumns(MainListActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainListActivity.this);
        gridLayoutManager =new GridLayoutManager(MainListActivity.this,column);


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


            tempList = newList;
//            setDataChange(tempList);


            /*gridAdapter.setFilter(tempList);
            listAdapter.setFilter(tempList);
*/

//            gridAdapter.notifyDataSetChanged();
//            listAdapter.notifyDataSetChanged();
            gridAdapter.setFilter(tempList);
            listAdapter.setFilter(tempList);
//
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
                Snackbar.make(getWindow().getDecorView(), "List View enabled", Snackbar.LENGTH_SHORT).show();



            }else{
                animateframe();
                recyclerView.setLayoutManager(gridLayoutManager);
                //MyGridLayoutAdapter gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, productdb);
                recyclerView.setAdapter(gridAdapter);
                isGridView = true;
                item.setIcon(R.drawable.list_icon);
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
            tempList = productdb;


            /*gridAdapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();
            */


            setDataChange(tempList);

            Toast.makeText(this, "Sorted Alphabetically", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortDefault){

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return String.valueOf(product.getPriority()).compareTo(String.valueOf((t1.getPriority())));
                }
            });
            tempList = productdb;
//            gridAdapter.notifyDataSetChanged();
//            listAdapter.notifyDataSetChanged();


            setDataChange(tempList);


            Toast.makeText(this, "Sorted by Default", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortCategory){

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return String.valueOf(product.getCategory()).compareTo(String.valueOf(t1.getCategory()));
                }
            });

            tempList = productdb;
//            gridAdapter.notifyDataSetChanged();
            setDataChange(tempList);
            Toast.makeText(this, "sorted by Category", Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.sortMostFreqUsed){

            Collections.sort(productdb, new Comparator<Product>() {
                @Override
                public int compare(Product product, Product t1) {
                    return String.valueOf(product.getCounter()).compareTo(String.valueOf(t1.getCounter()));
                }
            });
            tempList = productdb;
//            gridAdapter.notifyDataSetChanged();
            setDataChange(tempList);



            Toast.makeText(this, "sorted by Most frequently used", Toast.LENGTH_LONG).show();
            return true;
//            Toast.makeText(this, "feature not availabe", Toast.LENGTH_SHORT).show();
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


        tempList = productdb;


        recyclerView.setLayoutManager(gridLayoutManager);

//        setDataChange(tempList);
        gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, tempList);
        listAdapter = new MyListLayoutAdapter(MainListActivity.this, tempList);

        recyclerView.setAdapter(gridAdapter);
        recyclerView.setHasFixedSize(true);


        isGridView = true;


    }

    void setDataChange(ArrayList<Product> tempList){
/*
        gridAdapter = new MyGridLayoutAdapter(MainListActivity.this, tempList);
        listAdapter = new MyListLayoutAdapter(MainListActivity.this, tempList);
*/

        gridAdapter.setFilter(tempList);
        listAdapter.setFilter(tempList);

    }
    @Override
    public void onBackPressed() {

                if (back_pressed + 2000 > System.currentTimeMillis()) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                    this.finishAffinity();
                }
                else {
                    Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
                    back_pressed = System.currentTimeMillis();
                }
            }


    public void animateframe(){

         cx = (recyclerView.getLeft() + recyclerView.getRight()) / 2;
         cy = (recyclerView.getTop() + recyclerView.getBottom()) / 2;

        // get the final radius for the clipping circle
         dx = Math.max(cx, recyclerView.getWidth() - cx);
         dy = Math.max(cy, recyclerView.getHeight() - cy);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            animator = ViewAnimationUtils.createCircularReveal(recyclerView, cx, cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(1000);
            animator.start();
        }else{/*
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(1000);
            animator.start();*/
        }
        finalRadius = (float) Math.hypot(dx, dy);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            column =calculateNoOfColumns(MainListActivity.this);
            recyclerView.setLayoutManager(new GridLayoutManager(MainListActivity.this, column));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            column =calculateNoOfColumns(MainListActivity.this);
            recyclerView.setLayoutManager(new GridLayoutManager(MainListActivity.this, column));
        }
    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }


}
