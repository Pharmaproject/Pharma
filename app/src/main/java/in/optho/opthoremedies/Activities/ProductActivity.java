package in.optho.opthoremedies.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import in.optho.opthoremedies.Fragments.Design1;
import in.optho.opthoremedies.Fragments.Design2;
import in.optho.opthoremedies.Fragments.Design3;
import in.optho.opthoremedies.Fragments.Design4;
import in.optho.opthoremedies.Fragments.Design5;
import in.optho.opthoremedies.Fragments.Design6;
import in.optho.opthoremedies.Fragments.Design7;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

public class ProductActivity extends AppCompatActivity {

    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Bundle extras = new Bundle();
        Product product = getIntent().getParcelableExtra("PRODUCT"); // Parcelable
        extras.putParcelable("PRODUCT", product);
 //       Product product = (Product) extras.getSerializable("PRODUCT");

        String category=String.valueOf(product.getCategory());
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

    }
}
