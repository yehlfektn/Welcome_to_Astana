package kz.welcometoastana.Pamyatka;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import at.blogc.android.views.ExpandableTextView;
import kz.welcometoastana.R;
import kz.welcometoastana.utility.DashedUnderlineSpan;

public class PamyatkaDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pamyatka_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xffFFA800, 0xffFFD200});
        getSupportActionBar().setBackgroundDrawable(g);

        TextView name = (TextView)findViewById(R.id.name);
        ImageView image = (ImageView)findViewById(R.id.imagePamyatka);

        name.setText(getIntent().getStringExtra("name"));
        final String big;
        String namestr = getIntent().getStringExtra("name");
        if(namestr.equals("Разговорник")){
            big = getIntent().getStringExtra("description").substring(10,getIntent().getStringExtra("description").length()-1);
        }else if(namestr.contains("Посольства")){
            big = getIntent().getStringExtra("description").substring(19,getIntent().getStringExtra("description").length()-1);
        }
        else{
           big = getIntent().getStringExtra("description");
        }

        //---------------Text Animation---------------------------
        final ExpandableTextView expandableTextView = (ExpandableTextView) this.findViewById(R.id.summary);

        expandableTextView.setText(big);
        final Shader p = expandableTextView.getPaint().getShader();

        final Shader textShader=new LinearGradient(0, 100, 0, 250, new int[]{Color.BLACK,Color.WHITE}, new float[]{0, 1}, Shader.TileMode.CLAMP);
        expandableTextView.getPaint().setShader(textShader);
        final TextView OpenCollapse = (TextView)this.findViewById(R.id.openCollapse);

        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Читать дальше");

        int intSpannableStringBuilderLength = spannableStringBuilder.length();

        spannableStringBuilder.setSpan(
                new DashedUnderlineSpan(OpenCollapse, ContextCompat.getColor(this, R.color.gray),
                        getResources().getDimension(R.dimen.dus_stroke_thickness),
                        getResources().getDimension(R.dimen.dus_dash_path),
                        getResources().getDimension(R.dimen.dus_offset_y),
                        getResources().getDimension(R.dimen.dus_spacing_extra)), 0,
                intSpannableStringBuilderLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        OpenCollapse.setText(spannableStringBuilder);


        expandableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v){
                if (expandableTextView.isExpanded())
                {
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
                }
                else
                {
                    expandableTextView.setText(big);
                    expandableTextView.getPaint().setShader(p);
                    expandableTextView.expand();
                    OpenCollapse.setText("");
                }
            }

        });
        OpenCollapse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (expandableTextView.isExpanded())
                {
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
                }
                else
                {
                    expandableTextView.setText(big);
                    expandableTextView.getPaint().setShader(p);
                    expandableTextView.expand();

                    OpenCollapse.setText("");
                }
            }
        });

        //---------------TextAnimation End---------------------------


        Glide.with(this)
                .load(getIntent().getStringExtra("imageUrl"))
                .placeholder(R.drawable.placeholder)
                .into(image);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }


}
