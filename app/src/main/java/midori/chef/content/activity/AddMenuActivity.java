package midori.chef.content.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midori.kitchen.R;
import midori.chef.content.model.MenuModel;
import midori.chef.manager.AppController;
import midori.chef.manager.AppData;
import midori.chef.manager.AppListMenu;
import midori.chef.manager.AppPrefManager;
import midori.chef.manager.ConfigManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by BimoV on 3/11/2017.
 */

public class AddMenuActivity extends AppCompatActivity {
    @BindView(R.id.backButton)
    ImageView backButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image_layout)
    LinearLayout imageLayout;
    @BindView(R.id.etMenu)
    MaterialEditText etMenu;
    @BindView(R.id.etDesc)
    MaterialEditText etDesc;
    @BindView(R.id.etPrice)
    MaterialEditText etPrice;
    @BindView(R.id.etDate)
    MaterialEditText etDate;
    @BindView(R.id.tambahfoto)
    TextView tambahfoto;
    @BindView(R.id.etStock)
    MaterialEditText etStock;
    @BindView(R.id.rootLayout)
    ScrollView rootLayout;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener datePicker;
    private String photo;
    private AppListMenu appListMenu;
    private String id_image;
    private String menu;
    private String deskripsi;
    private String harga;
    private String stock;
    private String date;
    private AppPrefManager appPrefManager;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        ButterKnife.bind(this);
        appListMenu = new AppListMenu();
        appPrefManager = new AppPrefManager(getApplicationContext());
        initDatePicker();
        initProgressDialog();

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private void initDatePicker() {
        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDeliveryDate();
            }

        };
    }

    private void updateDeliveryDate() {
        String dateFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        etDate.setText(sdf.format(calendar.getTime()));
    }

    @OnClick({R.id.backButton, R.id.image, R.id.btnAccept, R.id.btnDecline, R.id.etDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.image:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
                break;
            case R.id.btnAccept:
                insertData();
                break;
            case R.id.btnDecline:
                onBackPressed();
                break;
            case R.id.etDate:
                openDatePicker();
                break;
        }
    }

    private void insertData() {

        menu = etMenu.getText().toString();
        deskripsi = etDesc.getText().toString();
        harga = etPrice.getText().toString();
        stock = etStock.getText().toString();
        //date = etDate.getText().toString();
        date = "now";

        if (validateData(menu, deskripsi, harga, stock)) {
            MenuModel item = new MenuModel();
            /*item.setMenu(menu);
            item.setDescription(deskripsi);
            item.setPrice(Double.parseDouble(harga));
            item.setDeliveryDate(date);
            item.setImage(" ");*/
            postProduct();


        }

    }

    private void postProduct() {
        if (!AppController.isConnected(getApplicationContext())) {
            AppController.showErrorConnection(rootLayout);
        } else {
            requestPostProduct(ConfigManager.CREATE_PRODUCT);
        }
    }

    private void requestPostProduct(String url) {
        JsonObject jsonObjectName = new JsonObject();
        JsonObject jsonObjectValue = new JsonObject();
        jsonObjectValue.addProperty("category_id", "464");
        jsonObjectValue.addProperty("name", appPrefManager.getUser().get("name") + "-" + menu);
        jsonObjectValue.addProperty("new", "true");
        jsonObjectValue.addProperty("price", harga);
        //jsonObjectValue.addProperty("negotiable", "");//optional
        jsonObjectValue.addProperty("weight", "100");
        jsonObjectValue.addProperty("stock", stock);
        jsonObjectValue.addProperty("description_bb", deskripsi + "-" + date);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(0);
        jsonObjectValue.add("free_shipping", jsonArray);//optional
        //jsonObjectValue.addProperty("product_detail_attributes", "");//optional

        jsonObjectName.add("product", jsonObjectValue);
        jsonObjectName.addProperty("images", id_image);
        jsonObjectName.addProperty("force_insurance", "");

        Ion.with(getApplicationContext())
                .load(url)
                .setLogging("Insert Product", Log.DEBUG)
                .basicAuthentication(AppData.USER_AUTH, AppData.PASS_AUTH)
                .setJsonObjectBody(jsonObjectName)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        try {
                            Log.d("Headercode", String.valueOf(result.getHeaders().code()));
                            if (result.getHeaders().code() == 200) {
                                JsonObject object = result.getResult();
                                String status = object.get("status").getAsString();
                                String message = object.get("message").getAsString();

                                if (status.equals("OK")) {
                                    onBackPressed();
                                    Toast.makeText(AddMenuActivity.this, "" + message, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(AddMenuActivity.this, "" + message, Toast.LENGTH_LONG).show();
                                }
                                Log.d("Insert Message", message);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private boolean validateData(String menu, String deskripsi, String harga, String stock) {
        boolean valid = true;
        if (menu.isEmpty()) {
            etMenu.setError("Masukkan Menu Makanan Anda");
            valid = false;
        } else {
            etMenu.setError(null);
        }
        if (deskripsi.isEmpty()) {
            etDesc.setError("Masukkan Deskripsi Makanan Anda");
            valid = false;
        } else {
            etDesc.setError(null);
        }
        if (harga.isEmpty()) {
            etPrice.setError("Masukkan Harga Makanan Anda");
            valid = false;
        } else {
            etPrice.setError(null);
        }
        if (stock.isEmpty()) {
            etStock.setError("Masukkan Stock Makanan Anda");
            valid = false;
        } else {
            etStock.setError(null);
        }
//        if (date.isEmpty()) {
//            etDate.setError("Masukkan Tanggal Pengiriman Masakan Anda");
//            valid = false;
//        } else {
//            etMenu.setError(null);
//        }
        return valid;
    }

    private void openDatePicker() {
        new DatePickerDialog(this, datePicker, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                Log.d("imageUri", imageUri.toString());
                try {
                    // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                            alertBuilder.setCancelable(true);
                            alertBuilder.setTitle("Read External Storage ");
                            alertBuilder.setMessage("Midorichef need read external storage permission.");
                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(AddMenuActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            2903);
                                }
                            });

                            AlertDialog alert = alertBuilder.create();
                            alert.show();
                        }
                    }else {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        photo = encodeImage(bitmap);
                        Log.d("encodeImage", encodeImage(bitmap));
                        Glide.with(AddMenuActivity.this)
                                .loadFromMediaStore(imageUri)
                                .asBitmap()
                                .into(image);
                        tambahfoto.setVisibility(View.GONE);
                        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        image.setLayoutParams(parms);
                        image.setScaleType(ImageView.ScaleType.FIT_XY);

                        uploadImage(imageUri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("AddMenuActivity", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }
    private void uploadImage(Uri imageUri) {
        if (AppController.isConnected(getApplicationContext())) {
            postUploadImage(ConfigManager.CREATE_PRODUCT_IMAGE, imageUri);
        } else {

        }
    }

    private void postUploadImage(String url, Uri imageUri) {
        Ion.with(getApplicationContext())
                .load(url)
                .setLogging("Insert Image", Log.DEBUG)
                .basicAuthentication(AppData.USER_AUTH, AppData.PASS_AUTH)
                .setMultipartFile("file", new File(getPath(getApplicationContext(),imageUri)))
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        try {
                            Log.d("HeaderCode", String.valueOf(result.getHeaders().code()));
                            if (result.getHeaders().code() == 200) {
                                showProgressDialog("Loading...");

                                JsonObject object = result.getResult();
                                String status = object.get("status").getAsString();
                                String message;
                                if (object.get("id").isJsonNull()) {
                                    id_image = "";
                                } else {
                                    id_image = object.get("id").getAsString();
                                }
                                if (object.get("message").isJsonNull()) {
                                    message = "";
                                } else {
                                    message = object.get("message").getAsString();
                                }


                                if (status.equals("OK")) {
                                    Toast.makeText(AddMenuActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddMenuActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        hideProgressDialog();
                    }

                });
    }

    /*private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
*/
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return encodedImage;
    }
    private void initProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
    }

    public void showProgressDialog(String content) {
        if (!pDialog.isShowing())
            pDialog.setMessage(content);
        pDialog.show();
    }

    public void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppData.refreshDataMenu = true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
