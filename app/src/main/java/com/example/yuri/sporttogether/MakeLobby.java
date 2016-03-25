package com.example.yuri.sporttogether;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Yuri on 24-6-2015.
 */
public class MakeLobby extends AppCompatActivity {

    EditText sport;
    RadioGroup moeilijkheidsgraadradio;
    RadioButton moeilijkheidsgraadbutton;
    EditText locatie;
    EditText tijd;
    EditText aantalpersonen;
    EditText opmerkingen;
    Button b1;
    String a;
    String b;
    String c;
    String d;
    String e;
    String f;
String moeilijkheidsgraad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      /*  StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_lobby);
        sport = (EditText) findViewById(R.id.sport);
        moeilijkheidsgraadradio = (RadioGroup) findViewById(R.id.moeilijkheidradio);

//        moeilijkheidsgraad = moeilijkheidsgraadbutton.getText().toString();

                locatie = (EditText) findViewById(R.id.locatie);
        tijd = (EditText) findViewById(R.id.tijd);
        aantalpersonen = (EditText) findViewById(R.id.aantal_personen);
        opmerkingen = (EditText) findViewById(R.id.opmerkingen);
        tijd.setInputType(InputType.TYPE_NULL);
        tijd.setFocusable(false);
        b1 = (Button) findViewById(R.id.createlobby);
        tijd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MakeLobby.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tijd.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
            }

        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isThisEmpty(true)) {
                    Toast.makeText(getApplicationContext(), "Enkele velden zijn nog niet ingevuld",
                            Toast.LENGTH_LONG).show();
                } else {
                    //     new Thread(MakeLobby.this).start();
                    makePostRequestOnNewThread();
                    Toast.makeText(getApplicationContext(), "Lobby aangemaakt",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LobbyView.class));
                    MakeLobby.this.finish();
                }
            }
        });
    }

    private void makePostRequestOnNewThread() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                int selectedradio = moeilijkheidsgraadradio.getCheckedRadioButtonId();

                moeilijkheidsgraadbutton = (RadioButton)findViewById(selectedradio);
                a = sport.getText().toString();
                b = moeilijkheidsgraadbutton.getText().toString();
                c = locatie.getText().toString();
                d = tijd.getText().toString();
                e = aantalpersonen.getText().toString();
                f = opmerkingen.getText().toString();
                String p = "sport=" + a + "&difficulty=" + b + "&location=" + c + "&time=" + d + "&maxparticipants=" + e + "&description=" + f;
                String pa = "sport=hi&difficulty=hard&location=loc&time=ti&maxparticipants=hi&description=oh";
                String s = "http://192.168.178.17:8080/SportTogetherBackend/CreateLobbyServlet?" + p;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.lobbies:
                startActivity(new Intent(getApplicationContext(), LobbyView.class));
                return true;
            case R.id.lobbycreation:
                startActivity(new Intent(getApplicationContext(), MakeLobby.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean isThisEmpty(boolean ba) {

        if (sport.getText().toString().isEmpty() ||
                locatie.getText().toString().isEmpty() || tijd.getText().toString().isEmpty() ||
                aantalpersonen.getText().toString().isEmpty()) {
            ba = true;
        } else {
            ba = false;
        }
        return ba;
    }
}
