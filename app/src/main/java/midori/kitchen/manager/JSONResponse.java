package midori.kitchen.manager;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;
import java.security.KeyStore;
import java.util.List;
import java.util.zip.GZIPInputStream;

import midori.kitchen.manager.ConfigManager;
import midori.kitchen.manager.MySSLSocketFactoryManager;
import midori.kitchen.manager.TlsSniFactory;


public class JSONResponse {
    static InputStream _inputStream = null;
    static JSONArray jsonArray = null;
    static JSONObject _jObj = null;
    static String _json = "";
    public static final int TARGET_HTTPS_PORT   = 443;
    private Socket socket;

    private HttpClient createDevelopmentHttpClientInstance() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            org.apache.http.conn.ssl.SSLSocketFactory sf = new MySSLSocketFactoryManager(trustStore);
            sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            registry.register(new Scheme("https", new TlsSniFactory(),443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public JSONObject GETResponse(String url) throws ConnectException {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
        }
        catch(ConnectException e){
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return _jObj;
    }

    public JSONObject GETResponseToken(String url,String midorikey) throws ConnectException {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", midorikey);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();


        }
        catch(ConnectException e){
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return _jObj;
    }

    public JSONObject GETResponse(String url,String apptoken, String token) throws ConnectException {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept-Encoding", "gzip");
            httpGet.setHeader("X-App-Token", apptoken);
            httpGet.setHeader("Accept-Version", ConfigManager.version);
            httpGet.setHeader("X-Access-Token", token);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }

        }
        catch(ConnectException e){
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();
            Log.d("token",_json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return _jObj;
    }

    public JSONArray GETResponseArray(String url,String apptoken, String token) throws ConnectException {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept-Encoding", "gzip");
            httpGet.setHeader("X-App-Token", apptoken);
            httpGet.setHeader("Accept-Version", ConfigManager.version);
            httpGet.setHeader("X-Access-Token", token);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }

        }
        catch(ConnectException e){
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            jsonArray = new JSONArray(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    public JSONObject GETResponseObject(String url,String apptoken) throws ConnectException {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept-Encoding", "gzip");
            httpGet.setHeader("X-App-Token", apptoken);
            httpGet.setHeader("Accept-Version", ConfigManager.version);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }

        }
        catch(ConnectException e){
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            jsonArray = new JSONArray();
        }
        return _jObj;
    }

    public JSONObject POSTResponse(String url, List<NameValuePair> params) {

        try {
            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return _jObj;

    }

    public JSONObject POSTResponse(String url, String midorikey, List<NameValuePair> params) {

        try {
            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setHeader("Authorization", midorikey);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            //Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
//            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
//                _inputStream = new GZIPInputStream(_inputStream);
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return _jObj;

    }

    public JSONObject POSTResponseToken(String url, String apptoken, String accessToken, List<NameValuePair> params) {

        try {
            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.addHeader("Accept-Encoding", "gzip");
            httpPost.setHeader("X-App-Token", apptoken);
            httpPost.setHeader("X-Access-Token", accessToken);
            httpPost.setHeader("Accept-Version", ConfigManager.version);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            _jObj = new JSONObject(_json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return _jObj;

    }

    public String POSTResponseString(String url, String token, List<NameValuePair> params) {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.addHeader("Accept-Encoding", "gzip");
            httpPost.setHeader("X-App-Token", token);
            httpPost.setHeader("Accept-Version", ConfigManager.version);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return _json;

    }

    public String POSTResponseTokenString(String url, String xToken,String token, List<NameValuePair> params) {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.addHeader("Accept-Encoding", "gzip");
            httpPost.setHeader("X-Access-Token", xToken);
            httpPost.setHeader("X-App-Token", token);
            httpPost.setHeader("Accept-Version", ConfigManager.version);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return _json;

    }

    public String PutResponseTokenString(String url, String xToken,String token, List<NameValuePair> params) {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPut httpPost = new HttpPut(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.addHeader("Accept-Encoding", "gzip");
            httpPost.setHeader("X-Access-Token", xToken);
            httpPost.setHeader("X-App-Token", token);
            httpPost.setHeader("Accept-Version", ConfigManager.version);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();
            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return _json;

    }

    public String POSTLogoutAll(String url, String token, String apptoken, List<NameValuePair> params) {
        try {

            DefaultHttpClient  httpClient = (DefaultHttpClient)createDevelopmentHttpClientInstance();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Accept-Encoding", "gzip");
            httpPost.setHeader("X-App-Token", token);
            httpPost.setHeader("Accept-Version", ConfigManager.version);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            _inputStream = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    _inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            _inputStream.close();
            _json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return _json;

    }





}
