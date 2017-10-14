package in.optho.opthoremedies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import in.optho.opthoremedies.Fragments.Design1;
import in.optho.opthoremedies.Fragments.Design6;
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


        int pos = getIntent().getIntExtra("POSITION",0);



        String category=String.valueOf(product.getCategory());



        // Create the adapter that will return a fragment for each of the list of
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(pos);



    }


/*
    *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
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


        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
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

            Toast.makeText(c, "Pager Fragment", Toast.LENGTH_SHORT).show();


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


    }*/

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
            int design=MainListActivity.tempList.get(position).getDesign();

            switch (design){
                case 1:
                    return Design1.newInstance(position);
                case 6:
                    return Design6.newInstance(position);
                    //break;
/*
                case 3:
                    return Design3.newInstance(position);
                    //break;
                case 4:
                    return Design4.newInstance(position);
                case 5:
                    return Design5.newInstance(position);
                case 6:
                    return Design6.newInstance(position);
                case 7:
                    return Design7.newInstance(position);
*/
                default:
                    return Design1.newInstance(position);

            }



//            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
//            return MainActivity.list.size();
            return MainListActivity.tempList.size();
//            return 3;
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
