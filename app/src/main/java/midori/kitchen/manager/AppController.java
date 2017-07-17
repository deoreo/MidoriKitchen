package midori.kitchen.manager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import midori.kitchen.R;
import midori.kitchen.content.activity.HomeActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by M. Asrof Bayhaqqi on 3/10/2017.
 */

public class AppController extends Application {

    private static Context context;
    private Application application;
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

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this))
                .init();
    }
    //get context
    public static Context getContext(){
        return context;
    }


    public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        private Application application;

        public MyNotificationOpenedHandler(Application application) {
            this.application = application;
        }

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            // Get custom datas from notification
            JSONObject data = result.notification.payload.additionalData;
            if (data != null) {
                String myCustomData = data.optString("key", null);
            }

            // React to button pressed
            OSNotificationAction.ActionType actionType = result.action.type;
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            // Launch new activity using Application object
            startApp();
        }

        private void startApp() {
            Intent intent = new Intent(application, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }
    }
}
