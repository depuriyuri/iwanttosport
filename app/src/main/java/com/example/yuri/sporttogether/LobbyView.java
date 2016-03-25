package com.example.yuri.sporttogether;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LobbyView extends AppCompatActivity {
    //ArrayList<String> listdata;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_view);
        new JsonParse().execute();
        // ListAdapter adapter = new SimpleAdapter(this, mylist, R.layout.lobby_list_items, new String[]{"sportname", "difficulty"}, new int[]{R.id.sportname, R.id.difficulty});


       /* Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {

            }
        };
*/
        //    makePostRequestOnNewThread();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.lobbies:
                finish();
                startActivity(new Intent(getApplicationContext(), LobbyView.class));
                return true;
            case R.id.lobbycreation:
                startActivity(new Intent(getApplicationContext(), MakeLobby.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class JsonParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LobbyView.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args) {

            JSONArray json =
                    JsonFromServer.getJSON("http://192.168.178.17:8080/SportTogetherBackend/GiveJSONah");
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();
            try {
                for (int i = 0; i < json.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject e = json.getJSONObject(i);
                    map.put("id", String.valueOf(i));
                    map.put("lobbyid", "" + e.getString("lobbyid"));
                    map.put("sportname", "" + e.getString("sport"));
                    map.put("difficulty", "" + e.getString("difficulty"));
                    map.put("time", "" + e.getString("time"));
                    map.put("location", "" + e.getString("location"));
                    map.put("numberparticipants", "" + e.getString("numberparticipants"));
                    map.put("maxparticipants", "/" + e.getString("maxparticipants"));
                    map.put("description", "" + e.getString("description"));
                    mylist.add(map);
                    ListAdapter adapter = new SimpleAdapter(LobbyView.this, mylist, R.layout.lobby_list_items, new String[]
                            {"sportname", "difficulty", "time", "numberparticipants", "maxparticipants"},
                            new int[]{R.id.sportname, R.id.difficulty, R.id.time, R.id.numberparticipants, R.id.maxparticipants});
                    ListView lv = (ListView) findViewById(R.id.list);
                    lv.setTextFilterEnabled(true);
                    lv.setAdapter(adapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            //   Toast.makeText(LobbyView.this, "You Clicked at " + mylist.get(+position).get("lobbyid"), Toast.LENGTH_SHORT).show();
                            String lid = mylist.get(+position).get("lobbyid");
                            String sp = mylist.get(+position).get("sportname");
                            String df = mylist.get(+position).get("difficulty");
                            String tm = mylist.get(+position).get("time");
                            String lc = mylist.get(+position).get("location");
                            String np = mylist.get(+position).get("numberparticipants");
                            String mp = mylist.get(+position).get("maxparticipants");
                            String dc = mylist.get(+position).get("description");
                            Intent intent = new Intent(LobbyView.this, ShowLobby.class);
                            intent.putExtra("lid", lid);
                            intent.putExtra("sp", sp);
                            intent.putExtra("df", df);
                            intent.putExtra("tm", tm);
                            intent.putExtra("lc", lc);
                            intent.putExtra("np", np);
                            intent.putExtra("mp", mp);
                            intent.putExtra("dc", dc);
                            startActivity(intent);
                            //   LobbyView.this.finish();
                        }
                    });
                }
            } catch (
                    JSONException e
                    )

            {
                Log.e("Json exception", e.toString());
            }

        }
    }
}
