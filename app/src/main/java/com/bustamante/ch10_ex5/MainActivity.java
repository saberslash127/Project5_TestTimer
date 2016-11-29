package com.bustamante.ch10_ex5;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener  {

    public Button startButton;
    public Button stopButton;
    public Timer timer;

    private TextView messageTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        startTimer();

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
    }

    public void startTimer() {
        final long startMillis = System.currentTimeMillis();
        timer = new Timer(true);
        TimerTask task = new TimerTask() {

            @Override
            public void run()
            {
                long elapsedMillis = System.currentTimeMillis() - startMillis;

                final String FILENAME = "news_feed.xml";{
                    try
                    {
                        URL url = new URL("http://rss.cnn.com/rss/cnn_tech.rss");
                        InputStream in = url.openStream();

                        FileOutputStream out = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                        byte[] buffer = new byte[1024];
                        int bytesRead = in.read(buffer);
                        while (bytesRead != -1){
                            out.write(buffer, 0, bytesRead);
                            bytesRead = in.read(buffer);
                        }
                        out.close();
                        in.close();
                    }
                    catch (IOException e)
                    {
                        Log.e("News reader", e.toString());
                    }

                    catch (NullPointerException n)
                    {
                        Log.e("News Reader", n.toString());
                    }
                }


                updateView(elapsedMillis);

            }
        };


        timer.schedule(task, 0, 10000);
    }

    private void updateView(final long elapsedMillis) {
        messageTextView.post(new Runnable() {
            int elapsedSeconds = (int) elapsedMillis/1000;

            @Override
            public void run() {
                messageTextView.setText("Seconds: " + elapsedSeconds);
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.startButton:
                startTimer();
                break;
            case R.id.stopButton:
                timer.cancel();
                break;
        }
    }
}