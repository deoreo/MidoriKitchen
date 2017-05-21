package midori.kitchen.content.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import midori.kitchen.R;
import midori.kitchen.content.adapter.AdapterAlamat;
import midori.kitchen.content.adapter.AdapterSuggestion;
import midori.kitchen.content.fragment.BuyReviewFragment;
import midori.kitchen.content.model.GeocodeModel;
import midori.kitchen.database.DatabaseHandler;
import midori.kitchen.manager.AppData;
import midori.kitchen.manager.AppPrefManager;
import midori.kitchen.manager.ConfigManager;
import midori.kitchen.manager.GoogleAPIManager;
import midori.kitchen.content.model.PlaceModel;
import midori.kitchen.manager.JSONControl;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by User on 11/18/2015.
 */
public class LocationActivity extends FragmentActivity
        //implements GoogleMap.OnMapClickListener
{

    //private GoogleMap googleMap;
    private String strDetail;
    private final String TAG = "LocationActivity";
     //private Circle mCircle;
    //public static Marker markerTemp;
    //public static Boolean mTouchMap = false;
    //private Marker markerCurrent,markerFrom;
    private CameraUpdate cameraUpdate;
    private TextView txtFrom;
    private ImageView btnCurrent, btnBack;
    private RelativeLayout btnGunakan;
    private static Activity mActivity;
    private RelativeLayout layoutSuggestion, layoutRecent;
    //private LatLng posFrom, posTemp, mapCenter;
    //public static LinearLayout layoutMarkerFrom;
    private Button btnLocationFrom;
    private TextView txtLocationFrom, upLocation;
    private ProgressBar progressMapFrom;
    public Boolean isSearchCurrent = false;
    private CardView layoutfillForm;
    private ProgressBar progress;
    private FrameLayout itemCurrent;
    private ListView lvSuggestion, lvRecent;
    private List<PlaceModel> LIST_PLACE = null;
    private String description = "";
    private PlaceModel mPlace;
    Map<String, String> flurryParams = new HashMap<String, String>();
    private AdapterSuggestion mAdapter;
    private String strDetailFrom;
    private DatabaseHandler db;
    private AdapterAlamat adapterAlamat;
    private BroadcastReceiver changeAlamat;
    private boolean isRecent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        mActivity = this;
        db = new DatabaseHandler(mActivity);
        progress = (ProgressBar) findViewById(R.id.progressSuggestion);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        //btnCurrent = (ImageView) findViewById(R.id.btnCurrent);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnGunakan = (RelativeLayout) findViewById(R.id.btnGunakan);
        //layoutMarkerFrom = (LinearLayout) findViewById(R.id.layoutMarkerFrom);

        //upLocation = (TextView) findViewById(R.id.txtLocation);
        layoutfillForm = (CardView) findViewById(R.id.layoutfillForm);
        layoutSuggestion = (RelativeLayout) findViewById(R.id.layoutSuggestion);
        layoutRecent = (RelativeLayout) findViewById(R.id.layoutRecent);
        itemCurrent = (FrameLayout) findViewById(R.id.itemCurrent);
        lvSuggestion = (ListView) findViewById(R.id.lvSuggestion);
        lvRecent = (ListView) findViewById(R.id.lvRecent);


        changeAlamat = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Log.d("", "broadcast changeAlamat");
                isRecent = true;
                String message = intent.getStringExtra("message");
                String address = AppData.address;
                txtFrom.setText(message);
                AppPrefManager.getInstance(mActivity).setAlamat(message);
                new GetGeocode(mActivity, address).execute();
//                Intent j = new Intent(getBaseContext(), BuyActivity.class);
//                startActivity(j);
                finish();


            }
        };

        try {
            LIST_PLACE = db.loadPlace();
        }catch (Exception e){
            LIST_PLACE = new ArrayList<>();
        }
        adapterAlamat = new AdapterAlamat(mActivity, LIST_PLACE);
        lvRecent.setAdapter(adapterAlamat);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutfillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        txtFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isRecent = false;
                return false;
            }
        });

        txtFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (s.length() >= 3 && !isRecent) {
                    new GetSuggestion(mActivity, s.toString()).execute();
                } else if (s.length() == 0) {
                    layoutSuggestion.setVisibility(GONE);
                    layoutRecent.setVisibility(VISIBLE);
                }


            }
        });



    }



    @Override
    public void onBackPressed() {
//            Intent j = new Intent(getBaseContext(), BuyActivity.class);
//            startActivity(j);
            finish();



        super.onBackPressed();  // optional depending on your needs
    }


    public static void hideKeyboard() {
        View view = mActivity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            //layoutSuggestion.setVisibility(GONE);
            //handler.removeCallbacks(delayedAction);
        }

    }

    private void SendBroadcast(String typeBroadcast, String type) {
        Intent intent = new Intent(typeBroadcast);
        // add data
        intent.putExtra("message", type);
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
    }

    public class GetSuggestion extends AsyncTask<String, Void, JSONArray> {

        String address, tag;
        Activity activity;
        LatLng latLng;
        public GetSuggestion(Activity activity, String address) {
            this.address = address;
            this.tag = tag;
            this.activity = activity;
        }


        @Override
        protected void onPreExecute() {
            progress.setVisibility(VISIBLE);
            layoutSuggestion.setVisibility(VISIBLE);
            layoutRecent.setVisibility(GONE);
            itemCurrent.setVisibility(GONE);
            lvSuggestion.setVisibility(GONE);
            lvSuggestion.setAdapter(null);
        }

        @Override
        protected JSONArray doInBackground(String... arg) {
            JSONArray json = null;
            LIST_PLACE = new ArrayList<PlaceModel>();
            JSONControl JSONControl = new JSONControl();
            try {
                json = JSONControl.listPlace(address);
            } catch (Exception e) {
            }
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    String id = "";
                    description = "";
                    address = "";
                    String detail = "";
                    boolean status = true;
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        id = jsonObject.getString("place_id");
                        description = jsonObject.getString("description");
                        String[] descSplit = description.split(",");
                        address = descSplit[0];
                        detail = descSplit[1] + "," + descSplit[2];
                        status = true;

                    } catch (JSONException e) {
                    } catch (Exception e) {

                    }

                    if (status) {
                        mPlace = new PlaceModel(id, address, detail);
                        LIST_PLACE.add(mPlace);
                    }
                }
                try {
                    mAdapter = new AdapterSuggestion(activity, LIST_PLACE);
                } catch (NullPointerException e) {
                }
            } else {
                LIST_PLACE = null;
            }

            return json;
        }

        @Override
        protected void onPostExecute(final JSONArray json) {
            // TODO Auto-generated method stub
            super.onPostExecute(json);
            progress.setVisibility(GONE);
            lvSuggestion.setAdapter(mAdapter);
            lvSuggestion.setVisibility(VISIBLE);
            lvSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        PlaceModel selectedPlace = LIST_PLACE.get(position);

                        txtFrom.setText(selectedPlace.getAddress());
                        strDetailFrom = selectedPlace.getAddressDetail();
                        String alamat = selectedPlace.getAddress() + "," + selectedPlace.getAddressDetail();

                        new GetGeocode(activity,description).execute();

                        layoutSuggestion.setVisibility(GONE);
                        layoutRecent.setVisibility(VISIBLE);

                        AppPrefManager.getInstance(activity).setAlamat(selectedPlace.getAddress());
                        SendBroadcast("changeAlamat", selectedPlace.getAddress());
                        db.insertPlace(selectedPlace);

                        hideKeyboard();


                    } catch (Exception e) {
                        layoutSuggestion.setVisibility(GONE);
                        layoutRecent.setVisibility(VISIBLE);

                        hideKeyboard();
                    }
//                        Intent j = new Intent(getBaseContext(), BuyActivity.class);
//                        startActivity(j);

                        finish();

                }
            });

        }
    }

    public class GetGeocode extends AsyncTask<String, Void, GeocodeModel> {

        String address, tag;
        Activity activity;
        public GetGeocode(Activity activity, String address) {
            this.address = address;
            this.activity = activity;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected GeocodeModel doInBackground(String... arg) {
            GeocodeModel modelGeocode = GoogleAPIManager.geocode(address);

            return modelGeocode;
        }

        @Override
        protected void onPostExecute(final GeocodeModel modelGeocode) {
            // TODO Auto-generated method stub
            super.onPostExecute(modelGeocode);
            AppPrefManager.getInstance(activity).setGeocode(new LatLng(modelGeocode.getLat(), modelGeocode.getLon()));
            //AppData.posFrom = new LatLng(modelGeocode.getLat(), modelGeocode.getLon());
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onStart() {
       super.onStart();
//        FlurryAgent.onStartSession(this, ConfigManager.FLURRY_API_KEY);
//        FlurryAgent.logEvent("CHECKOUT_LOCATION", flurryParams, true);
    }

    public void onStop() {
        super.onStop();
//        FlurryAgent.endTimedEvent("CHECKOUT_LOCATION");
//        FlurryAgent.onEndSession(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(LocationActivity.this).registerReceiver(changeAlamat,
                new IntentFilter("changeAlamat"));

    }

}
