package kz.welcometoastana.Pamyatka;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import at.blogc.android.views.ExpandableTextView;
import kz.welcometoastana.R;
import kz.welcometoastana.utility.DashedUnderlineSpan;

/**
 * A simple {@link Fragment} subclass.
 */
public class Extrennaya extends Fragment {


    public Extrennaya() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.extrennaya_fragment, container, false);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading data...");
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setDimAmount(0);
        }
        progressDialog.show();
        final String Url = "http://89.219.32.107/api/v1/tourist/1013";
        final ImageView imageView = (ImageView) v.findViewById(R.id.imagePamyatka);
        final ExpandableTextView expandableTextView = (ExpandableTextView) v.findViewById(R.id.summary);
        final TextView name = (TextView) v.findViewById(R.id.name);
        final TextView OpenCollapse = (TextView) v.findViewById(R.id.openCollapse);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String img = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject o = jsonObject.getJSONObject("place");

                    name.setText(o.getString("name"));
                    expandableTextView.setText(o.getString("description"));
                    img = o.getJSONArray("images").get(0).toString();

                } catch (JSONException e) {

                    e.printStackTrace();
                }


                Glide.with(getActivity())
                        .load(img)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);


                //---------------Text Animation---------------------------

                final Shader p = expandableTextView.getPaint().getShader();

                final Shader textShader = new LinearGradient(0, 100, 0, 250, new int[]{Color.BLACK, Color.WHITE}, new float[]{0, 1}, Shader.TileMode.CLAMP);
                expandableTextView.getPaint().setShader(textShader);


                final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.read_more));

                int intSpannableStringBuilderLength = spannableStringBuilder.length();

                spannableStringBuilder.setSpan(
                        new DashedUnderlineSpan(OpenCollapse, ContextCompat.getColor(getActivity(), R.color.gray),
                                getResources().getDimension(R.dimen.dus_stroke_thickness),
                                getResources().getDimension(R.dimen.dus_dash_path),
                                getResources().getDimension(R.dimen.dus_offset_y),
                                getResources().getDimension(R.dimen.dus_spacing_extra)), 0,
                        intSpannableStringBuilderLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                OpenCollapse.setText(spannableStringBuilder);


                expandableTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (expandableTextView.isExpanded()) {
                            expandableTextView.collapse();
                            OpenCollapse.setText(spannableStringBuilder);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (expandableTextView.isExpanded()) {
                                        expandableTextView.getPaint().setShader(textShader);
                                    }
                                }
                            }, 1000);
                        } else {
                            expandableTextView.getPaint().setShader(p);
                            expandableTextView.expand();
                            OpenCollapse.setText("");
                        }
                    }

                });
                OpenCollapse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (expandableTextView.isExpanded()) {
                            expandableTextView.collapse();
                            OpenCollapse.setText(spannableStringBuilder);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (expandableTextView.isExpanded()) {
                                        expandableTextView.getPaint().setShader(textShader);
                                    }
                                }
                            }, 1000);
                        } else {
                            expandableTextView.getPaint().setShader(p);
                            expandableTextView.expand();

                            OpenCollapse.setText("");
                        }
                    }
                });

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String loc = getCurrentLocale().toString();
                if (loc.startsWith("en")) {
                    params.put("Accept-Language", "en");
                } else if (loc.startsWith("kk")) {
                    params.put("Accept-Language", "kz");
                } else {
                    params.put("Accept-Language", "ru");
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


        return v;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

}
