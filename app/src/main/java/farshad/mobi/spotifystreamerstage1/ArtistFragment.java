package farshad.mobi.spotifystreamerstage1;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import farshad.mobi.spotifystreamerstage1.adapter.ArtistsAdapter;
import farshad.mobi.spotifystreamerstage1.adapter.TracksAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


public class ArtistFragment extends Fragment {
    private static final String ARTIST_ID = "artist.id";
    private static final String ARTIST_NAME = "artist.name";
    private static final String ARTIST_IMAGE = "artist.image";

    private String aId;
    private String aName;
    private String aImage;

    ListView lstTracks;
    SearchSpotifyTask spotifyTask;
    ArrayList<Track> arrayOfTracks;

    public static ArtistFragment newInstance(String _id, String _name, String _imageURL) {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_ID, _id);
        args.putString(ARTIST_NAME, _name);
        args.putString(ARTIST_IMAGE, _imageURL);
        fragment.setArguments(args);
        return fragment;
    }

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            aId = getArguments().getString(ARTIST_ID);
            aName = getArguments().getString(ARTIST_NAME);
            aImage = getArguments().getString(ARTIST_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        View view = inflater.inflate(R.layout.fragment_artist,
                container, false);
        lstTracks = (ListView) view.findViewById(R.id.lst_tracks);
        lstTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.content_fragment, ArtistFragment.newInstance(arrayOfArtists.get(position).id, arrayOfArtists.get(position).name, arrayOfArtists.get(position).images.get(arrayOfArtists.get(position).images.size() - 1).url))
//                        .addToBackStack("artist")
//                        .commit();
            }
        });

        spotifyTask = new SearchSpotifyTask();
        spotifyTask.execute(aId);

        return view;
    }

    public class SearchSpotifyTask extends AsyncTask<String, Void, Tracks> {
        @Override
        protected Tracks doInBackground(String... strings) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            HashMap queryMap = new HashMap();
            queryMap.put("limit", "10");
            queryMap.put("country", "US");
            return spotify.getArtistTopTrack(strings[0], queryMap);
        }

        @Override
        protected void onPostExecute(Tracks topTracks) {

            if (topTracks.tracks.size() > 0) {
                arrayOfTracks = (ArrayList<Track>) topTracks.tracks;
                TracksAdapter adapter = new TracksAdapter(getActivity().getApplicationContext(), arrayOfTracks);
                lstTracks.setAdapter(adapter);
            } else {
                arrayOfTracks.clear();
                lstTracks.setAdapter(null);
                Toast.makeText(getActivity().getApplicationContext(), R.string.search_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
