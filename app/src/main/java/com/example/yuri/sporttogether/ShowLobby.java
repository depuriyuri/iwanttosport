package com.example.yuri.sporttogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yuri on 28-6-2015.
 */
public class ShowLobby extends AppCompatActivity {
    private String lid;
    private String np, mp;
    private int numberparticipants, maxparticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_lobby_activity);

        Button b = (Button) findViewById(R.id.meldaan);
        Intent intent = getIntent();
        final Intent intent2 = new Intent(ShowLobby.this, LobbyView.class);
        lid = intent.getStringExtra("lid");
        String sp = intent.getStringExtra("sp");
        String df = intent.getStringExtra("df");
        String tm = intent.getStringExtra("tm");
        String lc = intent.getStringExtra("lc");
        np = intent.getStringExtra("np");
        mp = intent.getStringExtra("mp");
        String dc = intent.getStringExtra("dc");
       // TextView tv1 = (TextView) findViewById(R.id.sport);
       // tv1.setText(sp);
        TextView tv2 = (TextView) findViewById(R.id.niveau);
        tv2.setText(df);
        TextView tv3 = (TextView) findViewById(R.id.tijd);
        tv3.setText(tm);
        TextView tv4 = (TextView) findViewById(R.id.adeelnemers);
        tv4.setText(np);
        TextView tv5 = (TextView) findViewById(R.id.mdeelnemers);
        tv5.setText(mp);
        numberparticipants = Integer.parseInt(np);
        String mmpp = mp.substring(1);
        maxparticipants = Integer.parseInt(mmpp);
        setTitle(sp);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberparticipants < maxparticipants) {
                    makePostRequestOnNewThread();
                    Toast.makeText(getApplicationContext(), "Aangemeld bij Lobby",

                            Toast.LENGTH_SHORT).show();
                    startActivity(intent2);

                    ShowLobby.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Lobby is vol",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void makePostRequestOnNewThread() {
        Thread t = new Thread(new Runnable() {
            public void run() {

                //    String f = opmerkingen.getText().toString();
                String p = "lobbyid=" + lid + "&numberparticipants=" + np;
                String s = "http://192.168.178.17:8080/SportTogetherBackend/UpdateLobbyServlet?" + p;
                Log.v("s", s);
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //conn.setRequestMethod("POST");
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(p);
                    wr.flush();
                    wr.close();

                    int responsecode = conn.getResponseCode();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputline;
                    StringBuffer response = new StringBuffer();

                    while ((inputline = in.readLine()) != null) {
                        response.append(inputline);
                    }
                    in.close();

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });

        t.start();
    }
}
