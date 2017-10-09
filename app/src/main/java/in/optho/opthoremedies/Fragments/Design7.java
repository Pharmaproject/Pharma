package in.optho.opthoremedies.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Design7 extends Fragment {

    private ImageView opl1;
    private ImageView brand1;
    private ImageView Science1;
    private ImageView cartoon1;
    private ImageView indication1;
    private ImageView cpl1;




    Product product;
    public Design7() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        product = (Product) bundle.getSerializable("PRODUCT");

        /*for (String key : bundle.keySet()) {
            System.out.println("bundle keys= " + key);

        }
        name = bundle.getString("title");
        brand = bundle.getString("data");
*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_design7, container, false);




        opl1 = (ImageView) view.findViewById(R.id.opl1);
        brand1 = (ImageView) view.findViewById(R.id.brand1);
        Science1 = (ImageView) view.findViewById(R.id.Science1);
        cartoon1 = (ImageView) view.findViewById(R.id.cartoon1);
        indication1 = (ImageView) view.findViewById(R.id.indication1);
        cpl1 = (ImageView) view.findViewById(R.id.cpl1);






        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(product.getBrand());


    }

}
