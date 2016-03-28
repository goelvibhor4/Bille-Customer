package in.bille.app;

/**
 * Created by Vibhor Goel on 1/29/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    String CurrentString= message;
                    String[] separated = CurrentString.split(":");
                    String hello=  separated[0];

                    try
                    {
                        if (senderNum .contains("vBILLE"))
                        {
                            OtpVerification Sms = new OtpVerification();
                            Sms.recivedSms(hello);
                        }
                    }
                    catch(Exception e){}

                }
            }

        } catch (Exception e)
        {

        }
    }

}
