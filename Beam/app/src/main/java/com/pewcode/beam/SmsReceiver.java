package com.pewcode.beam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
  private Boolean VALID_SENDER = false, VALID_KEYWORD = false;
  private SmsSender SENDER = new SmsSender();
  private String[] PENDING_BROADCASTS;

  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle intentExtras = intent.getExtras();

    if (intentExtras != null) {
      Object[] sms = (Object[]) intentExtras.get("pdus");

      for (int i = 0; i < sms.length; i++) {
        SmsMessage message;

        if (Build.VERSION.SDK_INT >= 19) {
          SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
          message = msgs[0];
        } else {
          Object pdus[] = (Object[]) intentExtras.get("pdus");
          message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        }

        String sender = message.getOriginatingAddress();
        String content = message.getMessageBody().toString();

        MainActivity.logEntry("Incoming SMS from " + sender);

        String keyword = content.substring(0, content.indexOf(' '));
        String broadcast = content.substring(content.indexOf(' ') + 1);

        for (String number: Globals.VALID_NUMBERS) {
          if(sender.equals(number)) {
            VALID_SENDER = true;
            break;
          }
        }
/*
        for (String number: Globals.ADMIN_NUMBERS) {
          if(sender.equals(number)) {
            for (String code:PENDING_BROADCASTS) {
              if(keyword.equals(code)) {
                //MainActivity.logEntry("Broadcast #" + + " Approved");
                // TODO: Start broadcast
                for (int i = 0; i < Globals.RECIPIENTS.length; i++) {
                  //MainActivity.logEntry("Sending Message " + (i + 1) + " of " + Globals.RECIPIENTS.length);

                  *//*
                  if() pending broadcast message is greater than 150
                    sendMultipartMessage(context, student, broadcast*);
                  else
                    sendTextMessage(context, student, broadcast*);

                  remove from pending
                  break;

                   *//*
                }
              }
            }
          }
        }*/

        if(VALID_SENDER) {
          for(String word: Globals.KEYWORDS) {
            if(keyword.equals(word)) {
              VALID_KEYWORD = true;
              break;
            }
          }

          if(VALID_KEYWORD) {
            MainActivity.logEntry("Valid SMS Received");
            SENDER.sendTextMessage(context, sender, "Your message is pending approval. We'll notify you when it has been broadcasted. Thank you for using BEAM.");

            String TARGET_ADMIN = "09430282128";
            // TODO: Send pending broadcast to correct admin/head
            MainActivity.logEntry("Requesting Approval");
            SENDER.sendTextMessage(context, TARGET_ADMIN, broadcast);

            // TODO: Server integration



          }
        }
      }
    }
  }
}
