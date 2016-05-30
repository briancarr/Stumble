package com.stumbleapp.stumble;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stumbleapp.me.stumble.R;

import org.json.JSONException;

import java.math.BigDecimal;

public class PlayStreamActivity extends AppCompatActivity {
    String streamId;
    TextView title;
    VideoView video;
    String streamUrl = " ";
    MediaController mediaController;
    Button launchPayPalButton;

    ProgressDialog dialog;

    private static PayPalConfiguration config ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play_stream);

        config = new PayPalConfiguration()

                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

                .clientId("AUMrhEaOjPisTvJsdiNitMaeouGHiiXivHtSTIRr0cBGj2FZ1365EXmpY7AmOOP5Rf4Xjwpk3FyxkVaP");

        dialog = new ProgressDialog(this);

        dialog.setTitle("Video Streaming");
        dialog.setMessage("Buffering...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        dialog.show();

        //Start paypal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


        Intent data = getIntent();
        streamUrl = data.getStringExtra("url");

        mediaController = new MediaController(this);

        video = (VideoView) findViewById(R.id.videoView);

        mediaController.setAnchorView(video);
        mediaController.setMediaPlayer(video);

        video.setMediaController(mediaController);

        ViewGroup.LayoutParams params = video.getLayoutParams();
        params.width = getWindowManager().getDefaultDisplay().getWidth();

        video.setLayoutParams(params);
        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });

        video.setVideoURI(Uri.parse(streamUrl));

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                dialog.dismiss();
                video.start();
            }
        });

        Button pay = (Button) findViewById(R.id.button);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBuyPressed(getCurrentFocus());
            }
        });
    }
    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.75"), "USD", "sample item",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);//must store the new intent unless getIntent() will return the old one
//
//        Intent data = getIntent();
//        streamUrl = data.getStringExtra("url");
//
//        Log.i("URL STREAM", streamUrl);
//
//    }
}
