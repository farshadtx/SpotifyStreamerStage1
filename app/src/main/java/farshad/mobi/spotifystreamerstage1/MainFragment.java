package farshad.mobi.spotifystreamerstage1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import farshad.mobi.spotifystreamerstage1.adapter.ArtistsAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class MainFragment extends Fragment {

    ListView lstArtists;
    SearchSpotifyTask spotifyTask;
    ArrayList<Artist> arrayOfArtists;
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        View view =  inflater.inflate(R.layout.fragment_main,
                container, false);
        lstArtists = (ListView) view.findViewById(R.id.lst_artists);
        lstArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_fragment, ArtistFragment.newInstance(arrayOfArtists.get(position).id,arrayOfArtists.get(position).name,arrayOfArtists.get(position).images.get(arrayOfArtists.get(position).images.size() -1 ).url))
                        .addToBackStack(null)
                        .commit();
            }
        });

        EditText txtSearch = (EditText) view.findViewById(R.id.search_bar);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (spotifyTask != null) {
                    spotifyTask.cancel(false);
                }
                if (s.length() > 0) {
                    spotifyTask = new SearchSpotifyTask();
                    spotifyTask.execute(s.toString());
                } else {
                    arrayOfArtists.clear();
                    lstArtists.setAdapter(null);
                    Toast.makeText(getActivity().getApplicationContext(), R.string.search_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public class SearchSpotifyTask extends AsyncTask<String, Void, ArtistsPager> {
        @Override
        protected ArtistsPager doInBackground(String... strings) {

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            HashMap queryMap = new HashMap();
            queryMap.put("limit", "10");

            return spotify.searchArtists(strings[0], queryMap);
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {

            if (artistsPager.artists.items.size()>0) {
                arrayOfArtists = (ArrayList<Artist>) artistsPager.artists.items;
                ArtistsAdapter adapter = new ArtistsAdapter(getActivity().getApplicationContext(), arrayOfArtists);
                lstArtists.setAdapter(adapter);
            } else {
                arrayOfArtists.clear();
                lstArtists.setAdapter(null);
                Toast.makeText(getActivity().getApplicationContext(), R.string.search_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
