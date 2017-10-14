package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import in.optho.opthoremedies.Fragments.Design1;
import in.optho.opthoremedies.Fragments.Design2;
import in.optho.opthoremedies.Fragments.Design3;
import in.optho.opthoremedies.Fragments.Design4;
import in.optho.opthoremedies.Fragments.Design5;
import in.optho.opthoremedies.Fragments.Design6;
import in.optho.opthoremedies.Fragments.Design7;
import in.optho.opthoremedies.Fragments.SidePaneFragment;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

public class ProductActivity extends AppCompatActivity  {

    Fragment fragment;
    SlidingPaneLayout pane;
    ImageView next;
    ImageView prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        pane = (SlidingPaneLayout) findViewById(R.id.slidingpane);
        pane.setPanelSlideListener(new PaneListener());
        Bundle extras = new Bundle();
        final Product product = getIntent().getParcelableExtra("PRODUCT"); // Parcelable
        extras.putParcelable("PRODUCT", product);
 //       Product product = (Product) extras.getSerializable("PRODUCT");

        next = (ImageView) findViewById(R.id.next);
        prev = (ImageView) findViewById(R.id.prev);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product p=product;
                int id=product.getId();
                p=null;
                id++;
                for (Product temp: MainListActivity.sortedList) {
                    if(temp.getId()==id){

                        p = temp;

                    }

                }
                if(p==null){
                    Toast.makeText(ProductActivity.this, "Last Product", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
                    intent.putExtra("PRODUCT", p);
                    startActivity(intent);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product p=product;
                int id=product.getId();
                p=null;
                id++;
                for (Product temp: MainListActivity.sortedList) {
                    if(temp.getId()+2==id){

                        p = temp;

                    }

                }
                if(p==null){
                    Toast.makeText(ProductActivity.this, "First Product", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ProductActivity.this, ProductActivity.class);
                    intent.putExtra("PRODUCT", p);
                    startActivity(intent);
                }
            }
        });


        String category=String.valueOf(product.getDesign());
        switch (category){
            case "1":
                fragment = new Design1();
                break;
            case "2":
                fragment = new Design2();
                break;
            case "3":
                fragment = new Design3();
                break;
            case "4":
                fragment = new Design4();
                break;
            case "5":
                fragment = new Design5();
                break;
            case "6":
                fragment = new Design6();
                break;
            case "7":
                fragment = new Design7();
                break;
            default:
                fragment = new Design1();

        }

        fragment.setArguments(extras);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.nothing, R.anim.slide_down);
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();


        fragment = new SidePaneFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.setCustomAnimations(R.anim.nothing, R.anim.slide_down);
        ft1.replace(R.id.leftpane, fragment);
        ft1.addToBackStack(null);
        ft1.commit();

    }

    private class PaneListener implements SlidingPaneLayout.PanelSlideListener {

        @Override
        public void onPanelClosed(View view) {
            System.out.println("Panel closed");
        }

        @Override
        public void onPanelOpened(View view) {
            System.out.println("Panel opened");
        }

        @Override
        public void onPanelSlide(View view, float arg1) {
            System.out.println("Panel sliding");
        }

    }

    @Override
    public void onBackPressed() {

        if (pane.isOpen()){
            pane.closePane();
        }
        else startActivity(new Intent(ProductActivity.this,MainListActivity.class));

    }

}
