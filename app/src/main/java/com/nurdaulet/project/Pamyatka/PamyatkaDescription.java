package com.nurdaulet.project.Pamyatka;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nurdaulet.project.R;

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
        TextView summary = (TextView)findViewById(R.id.summary);
        ImageView image = (ImageView)findViewById(R.id.imagePamyatka);

        name.setText(getIntent().getStringExtra("name"));
        summary.setText(getIntent().getStringExtra("description"));
        if(getIntent().getStringExtra("description").length()!=0){
            makeTextViewResizable(summary, 3, "Читать дальше", true);
        }


        Glide.with(this)
                .load(getIntent().getStringExtra("imageUrl"))
                .placeholder(R.drawable.placeholder)
                .into(image);







    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 10) + " \n\n" + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 10) + " \n\n" + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " \n\n" + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {


                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Уменьшить", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "Читать дальше", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
