package reddit.haristimuno.com.reddit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import reddit.haristimuno.com.reddit.R;
import reddit.haristimuno.com.reddit.model.Theme;

public class ContentActivity extends AppCompatActivity {

    private Theme theme;
    private WebView webView_description;
    private ImageView postImage;
    private TextView tittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        theme = (Theme) b.get("theme");
        System.out.println(theme.getBannerImg());

        initComponents();
        addPostImage(theme);
        setWebViewContent();
        tittle.setText(theme.getTitle());
    }

    public void initComponents() {
        webView_description = (WebView) findViewById(R.id.webView_description);
        tittle = (TextView) findViewById(R.id.tittle);
    }

    public void setWebViewContent() {
        String description = fromHtml(theme.getDescriptionHtml());
        webView_description.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);
        webView_description.setInitialScale(170);
    }

    public void addPostImage(Theme theme){
        postImage = (ImageView) findViewById(R.id.postImage);
        String urlImage = theme.getIconImg();;

        if (urlImage == null ||  TextUtils.isEmpty(urlImage))
            urlImage = theme.getHeaderImg();
        else
            urlImage = theme.getIconImg();

        Picasso.with(getApplicationContext())
                .load(TextUtils.isEmpty(urlImage) ? null : urlImage)
                .placeholder( R.drawable.no_image)
                .into(postImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    @SuppressWarnings("deprecation")
    private String fromHtml(String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(text).toString();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
