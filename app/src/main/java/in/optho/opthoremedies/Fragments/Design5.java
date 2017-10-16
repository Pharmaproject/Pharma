package in.optho.opthoremedies.Fragments;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.ImageSetter;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Design5 extends Fragment {

    private ImageView opl1;
    private ImageView brand1;
    private ImageView Science1;
    private ImageView cartoon1;
    private ImageView indication1;
    private ImageView cpl1;
    ProductDatabaseHelper db;

    private byte[] brand;
    private byte[] openpunch;
    private byte[] graphic;
    private byte[] carton;
    private byte[] indication;
    private byte[] description;
    private byte[] closepunch;
    private byte[] customicon;
    ImageSetter bit;

    Product product;
    public Design5() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ProductDatabaseHelper(getActivity());
        bit = new ImageSetter();
        Bundle bundle = getArguments();
        product = bundle.getParcelable("PRODUCT");
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
        View view = inflater.inflate(R.layout.fragment_design5, container, false);
        Context c= getActivity().getApplicationContext();

        final RelativeLayout layout =(RelativeLayout) view.findViewById(R.id.background);

        opl1 = (ImageView) view.findViewById(R.id.opl1);
        brand1 = (ImageView) view.findViewById(R.id.brand1);
        Science1 = (ImageView) view.findViewById(R.id.Science1);
        cartoon1 = (ImageView) view.findViewById(R.id.cartoon1);
        indication1 = (ImageView) view.findViewById(R.id.indication1);
        cpl1 = (ImageView) view.findViewById(R.id.cpl1);
        ImageView expandedImageView = (ImageView) view.findViewById(R.id.expanded_image);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(graphic, 0, graphic.length));
        bitmapDrawable.setAlpha(180);
        layout.setBackground(bitmapDrawable);


        bit.SetImage(brand1,brand,c,view,expandedImageView);
        bit.SetImage(opl1,openpunch,c,view,expandedImageView);
        bit.SetImage(Science1,description,c,view,expandedImageView);
        bit.SetImage(cartoon1,carton,c,view,expandedImageView);
        bit.SetImage(indication1,indication,c,view,expandedImageView);
        bit.SetImage(cpl1,closepunch,c,view,expandedImageView);


        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(product.getName());


    }

}
