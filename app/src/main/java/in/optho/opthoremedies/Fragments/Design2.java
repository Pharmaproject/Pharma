package in.optho.opthoremedies.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Design2 extends Fragment {

    Product product;

    ProductDatabaseHelper db;

    private byte[] brand;
    private byte[] openpunch;
    private byte[] graphic;
    private byte[] carton;
    private byte[] indication;
    private byte[] description;
    private byte[] closepunch;
    private byte[] customicon;

    public Design2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        product = (Product) bundle.getSerializable("PRODUCT");
        int id = product.getId();
        brand =db.getBrand(id);
        openpunch = db.getOpenpunch(id);
        graphic = db.getGraphic(id);
        carton = db.getCarton(id);
        indication = db.getIndication(id);
        description= db.getDesc(id);
        closepunch= db.getClosepunch(id);
        customicon  = db.getCustomicon(id);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_design2, container, false);





        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(product.getName());
    }

}
