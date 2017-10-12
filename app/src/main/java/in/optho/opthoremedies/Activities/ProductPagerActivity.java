package in.optho.opthoremedies.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.optho.opthoremedies.Database.ProductDatabaseHelper;
import in.optho.opthoremedies.ImageSetter;
import in.optho.opthoremedies.Models.Product;
import in.optho.opthoremedies.R;

public class ProductPagerActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    String received="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = new Bundle();
        Product product = getIntent().getParcelableExtra("PRODUCT"); // Parcelable


        int pos = product.getId();



        String category=String.valueOf(product.getCategory());
        switch (category){
            /*case "1":
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
*/
        }


        // Create the adapter that will return a fragment for each of the list of
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(pos);



    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
//        private Parcelable product;

        public PlaceholderFragment() {
        }


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

        int id;

        Product product;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_design1, container, false);
            final int i = getArguments().getInt(ARG_SECTION_NUMBER);

            Context c= getActivity().getApplicationContext();



            db = new ProductDatabaseHelper(getActivity());
            bit = new ImageSetter();
            Bundle bundle = getArguments();
            product = bundle.getParcelable("PRODUCT");
            id = MainListActivity.tempList.get(i).getId();

            brand =db.getBrand(id);
            openpunch = db.getOpenpunch(id);
            graphic = db.getGraphic(id);
            carton = db.getCarton(id);
            indication = db.getIndication(id);
            description= db.getDesc(id);
            closepunch= db.getClosepunch(id);
            customicon  = db.getCustomicon(id);






            final RelativeLayout layout =(RelativeLayout) view.findViewById(R.id.background);

            opl1 = (ImageView) view.findViewById(R.id.opl1);
            brand1 = (ImageView) view.findViewById(R.id.brand1);
            Science1 = (ImageView) view.findViewById(R.id.Science1);
            cartoon1 = (ImageView) view.findViewById(R.id.cartoon1);
            indication1 = (ImageView) view.findViewById(R.id.indication1);
            cpl1 = (ImageView) view.findViewById(R.id.cpl1);

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(graphic, 0, graphic.length));
            layout.setBackground(bitmapDrawable);

            bit.SetImage(brand1,brand,c);
            bit.SetImage(opl1,openpunch,c);
            bit.SetImage(Science1,description,c);
            bit.SetImage(cartoon1,carton,c);
            bit.SetImage(indication1,indication,c);
            bit.SetImage(cpl1,closepunch,c);


            return view;
        }


/*
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //you can set the title for your toolbar here for different fragments different titles
            getActivity().setTitle(product.getName());


        }
*/

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
//            return MainActivity.list.size();
            return MainListActivity.tempList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String s = String.valueOf(position);
            return s;
            /*switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }*/


        }
    }



    @Override
    public void onBackPressed() {

        startActivity(new Intent(ProductPagerActivity.this,MainListActivity.class));

    }
}
