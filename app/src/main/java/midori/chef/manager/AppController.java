package midori.chef.manager;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import midori.kitchen.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by BimoV on 3/9/2017.
 */

public class AppController extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    //get context
    public static Context getContext(){
        return context;
    }

    //check connection
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                Log.d("Network Info", info.getExtraInfo());
                return true;
            }
        }
        return false;
    }

    // Show snackbar error
    public static void showErrorConnection(View view) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, "Please check your internet connection", Snackbar.LENGTH_SHORT);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    // Show snackbar info
    public static void showInfo(View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


}
