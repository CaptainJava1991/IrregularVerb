package com.irregularverb.example.carthy.irregularverb;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    private JSONObject verb;
    private String attributeId;
    private String present;
    private String passe;
    private String preterit;
    private String traduction;
    private String ans1;
    private String ans2;
    private String ans3;
    private String ans4;
    private int bnRep;
    private int bnTxt;
    private String bnRepTxt;
    private Handler mHandler = new Handler();
    private long currentTime;
    private long afterTime;
    private long delai;
    private ProgressBarre progressBarre;
    private Asynchrone asynchrone;
    private int nbQ = 0;
    private int nbGA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        asynchrone = new Asynchrone();
        asynchrone.execute("OK");
    }

    public class Asynchrone extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpUrlConnection = null;
            String result = "";

            try {
                URL url = new URL("http://10.0.2.2/anglais/verb.php/");
                httpUrlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = new BufferedInputStream(httpUrlConnection.getInputStream());

                int inChar;
                final StringBuilder readStr = new StringBuilder();

                while ((inChar = inStream.read()) != -1) {
                    readStr.append((char) inChar);

                }

                result = readStr.toString();
                parseJsonFile(result);
                httpUrlConnection.disconnect();
            } catch (MalformedURLException me) {
                me.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Random random = new Random();

            bnTxt = random.nextInt(4) + 1;
            bnRep = random.nextInt(3) + 1;
            initText();
            initButton();
            delai = 5000;
            doIn(0);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ans3 = null;
            ans4 = null;
        }
    }

    public class ProgressBarre extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {
            final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
            currentTime = Integer.parseInt(params[0]);
            afterTime = System.currentTimeMillis();
            // Start lengthy operation in a background thread

            while (currentTime < delai) {
                if(isCancelled()){
                    return "OK";
                }
                currentTime =  System.currentTimeMillis() - afterTime;

                mHandler.post(new Runnable() {
                    public void run() {
                        mProgress.setProgress((int)((currentTime * 100)/delai));
                    }
                });
            }

            return "finish";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("finish")){
                lock();
            }
        }
    }

    public void doIn(int n){
        if(progressBarre == null || progressBarre.isCancelled()) {
            progressBarre = new ProgressBarre();
            progressBarre.execute(n + "");
        }
    }


    private void parseJsonFile (String jString) throws Exception {

        verb = new JSONObject(jString);

        attributeId = verb.getString("id");
        Log.v("JsonParsing", attributeId);

        present = verb.getString("present");
        Log.v("JsonParsing", present);

        passe = verb.getString("passe");
        Log.v("JsonParsing", passe);

        preterit = verb.getString("preterit");
        Log.v("JsonParsing", preterit);

        traduction = verb.getString("traduction");
        Log.v("JsonParsing", traduction);

        ans1 = verb.getString("ans1");
        Log.v("JsonParsing", ans1);

        ans2 = verb.getString("ans2");
        Log.v("JsonParsing", ans2);

        ans3 = verb.getString("ans3");
        Log.v("JsonParsing", ans3);

        ans4 = verb.getString("ans4");
        Log.v("JsonParsing", ans4);

    }

    public void initText(){
        TextView txt;
        nbQ++;
        if(bnTxt == 1){
            txt = (TextView) findViewById(R.id.present);
            txt.setText("........");
            bnRepTxt = present;
            txt = (TextView) findViewById(R.id.passe);
            txt.setText(passe);
            txt = (TextView) findViewById(R.id.preterit);
            txt.setText(preterit);
            txt = (TextView) findViewById(R.id.traduction);
            txt.setText(traduction);
        }else if(bnTxt == 2){
            txt = (TextView) findViewById(R.id.present);
            txt.setText(present);
            txt = (TextView) findViewById(R.id.passe);
            txt.setText("........");
            bnRepTxt = passe;
            txt = (TextView) findViewById(R.id.preterit);
            txt.setText(preterit);
            txt = (TextView) findViewById(R.id.traduction);
            txt.setText(traduction);
        }else if(bnTxt == 3){
            txt = (TextView) findViewById(R.id.present);
            txt.setText(present);
            txt = (TextView) findViewById(R.id.passe);
            txt.setText(passe);
            txt = (TextView) findViewById(R.id.preterit);
            txt.setText("........");
            bnRepTxt = preterit;
            txt = (TextView) findViewById(R.id.traduction);
            txt.setText(traduction);
        }else if(bnTxt == 4){
            txt = (TextView) findViewById(R.id.present);
            txt.setText(present);
            txt = (TextView) findViewById(R.id.passe);
            txt.setText(passe);
            txt = (TextView) findViewById(R.id.preterit);
            txt.setText(preterit);
            txt = (TextView) findViewById(R.id.traduction);
            txt.setText("........");
            bnRepTxt = traduction;
        }

        txt = (TextView) findViewById(R.id.score);
        txt.setText(nbGA + " / " + nbQ);

    }

    public void initButton(){
        unlock();

        Button btn = (Button) findViewById(R.id.ans1);
        btn.setBackgroundResource(R.drawable.white_button);
        btn = (Button) findViewById(R.id.ans2);
        btn.setBackgroundResource(R.drawable.white_button);
        btn = (Button) findViewById(R.id.ans3);
        btn.setBackgroundResource(R.drawable.white_button);

        if(bnRep == 1){
            btn = (Button) findViewById(R.id.ans1);
            btn.setText(bnRepTxt);
            btn = (Button) findViewById(R.id.ans2);
            btn.setText(initButtonText());
            btn = (Button) findViewById(R.id.ans3);
            btn.setText(initButtonText());
        }else if(bnRep == 2){
            btn = (Button) findViewById(R.id.ans1);
            btn.setText(initButtonText());
            btn = (Button) findViewById(R.id.ans2);
            btn.setText(bnRepTxt);
            btn = (Button) findViewById(R.id.ans3);
            btn.setText(initButtonText());
        }else if(bnRep == 3){
            btn = (Button) findViewById(R.id.ans1);
            btn.setText(initButtonText());
            btn = (Button) findViewById(R.id.ans2);
            btn.setText(initButtonText());
            btn = (Button) findViewById(R.id.ans3);
            btn.setText(bnRepTxt);
        }
    }

    public String initButtonText(){
        Random random = new Random();
        int rand = random.nextInt(4) + 1;
        if(rand == 1){
            return ans1;
        }else if(rand == 2){
            return ans2;
        }else if(rand == 3){
            return (ans3.toString() == "")? initButtonText() : ans3;
        }else{
            return (ans4.toString() == "")? initButtonText() : ans4;
        }
    }

    public void lock(){
        Button button = (Button) findViewById(R.id.ans1);
        button.setEnabled(false);
        button = (Button) findViewById(R.id.ans2);
        button.setEnabled(false);
        button = (Button) findViewById(R.id.ans3);
        button.setEnabled(false);
    }

    public void unlock(){
        Button button = (Button) findViewById(R.id.ans1);
        button.setEnabled(true);
        button = (Button) findViewById(R.id.ans2);
        button.setEnabled(true);
        button = (Button) findViewById(R.id.ans3);
        button.setEnabled(true);
    }

    public void ans1(View view){
        Button b = (Button) findViewById(R.id.ans1);
        progressBarre.cancel(true);

        if (bnRep == 1){
            b.setBackgroundResource(R.drawable.green_button);
            nbGA++;

            final Button next2 = b;

            lock();
            next2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    asynchrone = new Asynchrone();
                    asynchrone.execute("OK");
                }
            }, 2000);
        }else{
            b.setBackgroundResource(R.drawable.red_button);
            modifier((Button) findViewById(R.id.ans1));
            lock();
        }
    }

    public void ans2(View view){
        Button b = (Button) findViewById(R.id.ans2);
        progressBarre.cancel(true);

        if (bnRep == 2){
            b.setBackgroundResource(R.drawable.green_button);
            nbGA++;

            final Button next2 = b;

            lock();
            next2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    asynchrone = new Asynchrone();
                    asynchrone.execute("OK");
                }
            }, 2000);
        }else{
            b.setBackgroundResource(R.drawable.red_button);
            modifier((Button) findViewById(R.id.ans2));
            lock();
        }
    }

    public void ans3(View view){
        Button b = (Button) findViewById(R.id.ans3);
        progressBarre.cancel(true);

        if (bnRep == 3){
            b.setBackgroundResource(R.drawable.green_button);
            nbGA++;

            final Button next2 = b;

            lock();
            next2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    asynchrone = new Asynchrone();
                    asynchrone.execute("OK");
                }
            }, 2000);
        }else{
            b.setBackgroundResource(R.drawable.red_button);
            modifier((Button) findViewById(R.id.ans3));
            lock();
        }
    }

    public void modifier(Button next){
        Button b;

        if(bnRep == 1){
            b = (Button) findViewById(R.id.ans1);
            b.setBackgroundResource(R.drawable.green_button);
        }else if (bnRep == 2){
            b = (Button) findViewById(R.id.ans2);
            b.setBackgroundResource(R.drawable.green_button);
        }else if(bnRep == 3){
            b = (Button) findViewById(R.id.ans3);
            b.setBackgroundResource(R.drawable.green_button);
        }
        final Button next2 = next;

        next2.postDelayed(new Runnable() {
            @Override
            public void run() {
                asynchrone = new Asynchrone();
                asynchrone.execute("OK");
            }
        }, 2000);
    }

    public void exit(View view){
        progressBarre.cancel(true);
        asynchrone.cancel(true);
        finish();
    }
}
