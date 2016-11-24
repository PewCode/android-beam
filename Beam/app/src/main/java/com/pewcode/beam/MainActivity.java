package com.pewcode.beam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
  public static TextView logger;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    logger = (TextView) findViewById(R.id.tv_logs);
    Button btn_clear = (Button) findViewById(R.id.btn_logs);

    Intent i = getIntent();
    String contacts = i.getStringExtra("contacts");
    String keywords = i.getStringExtra("keywords");

    logEntry(contacts);
    logEntry(keywords);

    btn_clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        logger.setText("");
      }
    });

    logEntry("Device Ready");
    logEntry("Waiting for SMS");
  }

  public static void logEntry(String message) {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    String timestamp = sdf.format(new Date());

    logger.append(timestamp + " - " + message + "\n");
  }
}
