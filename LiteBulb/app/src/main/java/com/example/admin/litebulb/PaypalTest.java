package com.example.admin.litebulb;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class PaypalTest extends AppCompatActivity {

    private TextView mResponse;
    private PayPalConfiguration mConfiguration;
    private String mPaypalClientId = "Ad6IZpxI8Q_oFapwnrk0iAGQ914n5qHrpe5iyC5DyaaJDgb0Oyw6LlzeGG6EnLa5TEiCMWXDsc8MQBpP";
    private Intent mService;
    private int mPaypalRequestCode = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_test);

        mResponse = (TextView) findViewById(R.id.response);
        mConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(mPaypalClientId);

        mService = new Intent(this, PayPalService.class);
        mService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
        startService(mService);
    }


    void pay(View view){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(1), "USD", "Test Payment with PayPal",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, mPaypalRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == mPaypalRequestCode){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null){
                    String state = confirmation.getProofOfPayment().getState();
                    if(state.equals("approved")){
                        mResponse.setText("Approved");
                    }
                    else
                        mResponse.setText("Error in the payment");
                }
                else
                    mResponse.setText("Confirmation is null");
            }
        }
    }
}
