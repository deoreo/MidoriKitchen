package midori.chef.manager;

/**
 * Created by BimoV on 5/24/2017.
 */

import com.google.android.gms.maps.model.LatLng;

public class AppData {
    public static boolean refreshDataMenu = false;
    public static String TOKEN = "7f7cb3d190e243242834eff7aedc5272-6e10abda22870f81dcdb724a7231ea27.168b3f90f315be9aa222f898b8879f16";
    public static String USER_AUTH = "33172156";
    public static String PASS_AUTH = "T3GUXckp8K8mC5zMseGI";
    public static LatLng LokasiIbu;

    public static boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException n) {
            return false;
        }
    }

}
