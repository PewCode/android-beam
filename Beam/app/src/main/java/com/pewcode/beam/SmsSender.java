package com.pewcode.beam;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import java.util.ArrayList;

public class SmsSender {

  public void sendTextMessage(Context context, String phone, String message) {
    SmsManager sms = SmsManager.getDefault();

    PendingIntent sentPending = PendingIntent.getBroadcast(context, 0, new Intent("SENT"), 0);
    context.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context arg0, Intent arg1) {
        switch (getResultCode()) {
          case Activity.RESULT_OK:
            Log.e("SENDER", "Sent");
            break;
          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
            Log.e("SENDER", "Not Sent: Generic failure");
            break;
          case SmsManager.RESULT_ERROR_NO_SERVICE:
            Log.e("SENDER", "Not Sent: No Service or SIM Card");
            break;
          case SmsManager.RESULT_ERROR_NULL_PDU:
            Log.e("SENDER", "Not Sent: Null");
            break;
          case SmsManager.RESULT_ERROR_RADIO_OFF:
            Log.e("SENDER", "Not Sent: Radio Off or Airplane Mode On");
            break;
        }
      }
    }, new IntentFilter("SENT"));

    PendingIntent deliveredPending = PendingIntent.getBroadcast(context,0, new Intent("DELIVERED"), 0);
    context.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context arg0,Intent arg1) {
        switch (getResultCode()) {
          case Activity.RESULT_OK:
            Log.e("DELIVERY", "Delivered");
            break;
          case Activity.RESULT_CANCELED:
            Log.e("DELIVERY", "Not Delivered");
            break;
        }
      }
    }, new IntentFilter("DELIVERED"));

    sms.sendTextMessage(phone, null, message, sentPending, deliveredPending);
  }

  public void sendMultipartMessage(Context context, String phone, String message) {
    SmsManager sms = SmsManager.getDefault();
    ArrayList<String> messageParts = sms.divideMessage(message);

    int partsCount = messageParts.size();

    ArrayList<PendingIntent> sentPendings = new ArrayList<PendingIntent>(partsCount);
    ArrayList<PendingIntent> deliveredPendings = new ArrayList<PendingIntent>(partsCount);

    for (int i = 0; i < partsCount; i++) {
      PendingIntent sentPending = PendingIntent.getBroadcast(context, 0, new Intent("SENT"), 0);
      context.registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
          switch (getResultCode()) {
            case Activity.RESULT_OK:
              break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
              break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
              break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
              break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
              break;
          }
        }
      }, new IntentFilter("SENT"));

      sentPendings.add(sentPending);

      PendingIntent deliveredPending = PendingIntent.getBroadcast(context, 0, new Intent("DELIVERED"), 0);
      context.registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
          switch (getResultCode()) {
            case Activity.RESULT_OK:
              break;
            case Activity.RESULT_CANCELED:
              break;
          }
        }
      }, new IntentFilter("DELIVERED"));

      deliveredPendings.add(deliveredPending);
    }

    sms.sendMultipartTextMessage(phone, null, messageParts, sentPendings, deliveredPendings);
  }
}
