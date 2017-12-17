package reddit.haristimuno.com.reddit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import reddit.haristimuno.com.reddit.R;
import reddit.haristimuno.com.reddit.model.Theme;

/**
 * Created by hector on 13-12-2017.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> implements View.OnClickListener {

    private static final String REDDIT_COM = "https://www.reddit.com";
    public List<Theme> themes;
    private int rowLayout;
    private Context context;
    private View.OnClickListener listener;


    public ThemeAdapter(List<Theme> movies, int rowLayout, Context context) {
        this.themes = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        view.setOnClickListener(this);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThemeViewHolder holder, int position) {
        Theme theme = themes.get(position);
        String imageIconImg = themes.get(position).getIconImg();

        if (imageIconImg == null ||  TextUtils.isEmpty(imageIconImg))
            holder.setImageView(theme.getHeaderImg(), holder.itemImage, R.drawable.no_image);
        else
            holder.setImageView(theme.getIconImg(), holder.itemImage, R.drawable.no_image);

        holder.descriptionText.setText(theme.getPublicDescription());

        String url = REDDIT_COM + theme.getUrl();
        holder.urlText.setText(url);
        holder.urlText.setPaintFlags(holder.urlText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return themes != null ? themes.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.onClick(v);
    }

    public void setList(List<Theme> list){
        this.themes = list;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public Theme get(int position) {
        return themes.get(position);
    }

    class ThemeViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView descriptionText;
        TextView urlText;

        public ThemeViewHolder(View v) {
            super(v);
            itemImage = (ImageView) v.findViewById(R.id.itemImage);
            descriptionText = (TextView) v.findViewById(R.id.descriptionText);
            urlText = (TextView) v.findViewById(R.id.text_url);
        }

        void setImageView(String urlImage, ImageView image, @DrawableRes int placeHolderId) {
            Picasso.with(context)
                    .load(TextUtils.isEmpty(urlImage) ? null : urlImage)
                    .placeholder(placeHolderId)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
    }
}
