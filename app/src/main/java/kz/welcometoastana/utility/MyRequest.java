package kz.welcometoastana.utility;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by nurdaulet on 6/6/17.
 */

public class MyRequest extends StringRequest {
    public MyRequest(int method, String url, Response.Listener<String> listener,
                     Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(5000,
                10, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}