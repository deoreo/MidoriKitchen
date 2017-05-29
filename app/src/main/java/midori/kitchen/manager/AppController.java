package midori.kitchen.manager;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import midori.kitchen.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class AppController extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MultiDex.install(this);
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

}
