package farshad.mobi.spotifystreamerstage1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import farshad.mobi.spotifystreamerstage1.R;
import kaaes.spotify.webapi.android.models.Artist;

public class ArtistsAdapter extends ArrayAdapter<Artist> {
    Context mainContext;
    private static class ViewHolder {
        TextView name;
        ImageView image;
    }

    public ArtistsAdapter(Context context, List<Artist> artists) {
        super(context, 0, artists);
        mainContext = context;
    }

    @Override
    public View getView(int position, View artistRowView, ViewGroup parent) {
        // Get the data item for this position
        Artist artist = getItem(position);
        ViewHolder viewHolder;
        if (artistRowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            artistRowView = inflater.inflate(R.layout.listview_row_artist, parent, false);
            viewHolder.name = (TextView) artistRowView.findViewById(R.id.txtName);
            viewHolder.image = (ImageView) artistRowView.findViewById(R.id.imgThumb);
            artistRowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) artistRowView.getTag();
        }
        viewHolder.name.setText(artist.name);
        if (artist.images.size() == 0) {
            viewHolder.image.setImageResource(R.drawable.singer_placeholder);
        } else {
            Picasso.with(mainContext)
                    .load(artist.images.get(artist.images.size() - 1).url)
                    .placeholder(R.drawable.singer_placeholder)
                    .resize(50,50)
                    .into(viewHolder.image);
        }

        return artistRowView;
    }
}