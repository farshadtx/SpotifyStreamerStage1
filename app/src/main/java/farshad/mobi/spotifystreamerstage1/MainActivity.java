package farshad.mobi.spotifystreamerstage1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
public class MainActivity extends Activity {
    EditText txtSearch;
    ListView lstArtists;
    SearchSpotifyTask spotifyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstArtists = (ListView) findViewById(R.id.list_view);
        txtSearch = (EditText) findViewById(R.id.search_bar);

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
                                                     lstArtists.setAdapter(null);
                                                     Toast.makeText(getApplicationContext(), "Sorry! there is no match.", Toast.LENGTH_LONG).show();
                                                 }
                                             }
                                         }
        );
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
                ArrayList<Artist> arrayOfArtists = (ArrayList<Artist>) artistsPager.artists.items;
                ArtistsAdapter adapter = new ArtistsAdapter(getApplicationContext(), arrayOfArtists);
                lstArtists.setAdapter(adapter);
            } else {
                lstArtists.setAdapter(null);
                Toast.makeText(getApplicationContext(), "Sorry! there is no match.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
