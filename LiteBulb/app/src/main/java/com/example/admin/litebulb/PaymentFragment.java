package com.example.admin.litebulb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;


public class PaymentFragment extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    private Button buyNowPaypal;
    private PayPalConfiguration mConfiguration;
    private String mPaypalClientId = "Ad6IZpxI8Q_oFapwnrk0iAGQ914n5qHrpe5iyC5DyaaJDgb0Oyw6LlzeGG6EnLa5TEiCMWXDsc8MQBpP";
    private Intent mService;
    private int mPaypalRequestCode = 999;
    private SharedPreferences preferences;
    private TextView mResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_payment, container,
                false);

        buyNowPaypal = (Button) parentHolder.findViewById(R.id.buy_now_payment);
        preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        mResponse = (TextView) parentHolder.findViewById(R.id.paypal_response);
        buyNowPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(Integer.parseInt(preferences.getString("itemPrice",""))), "USD", "Test Payment with PayPal",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(getContext(), PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, mPaypalRequestCode);
            }
        });
        mConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(mPaypalClientId);

        mService = new Intent(getContext(), PayPalService.class);
        mService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mConfiguration);
        getActivity().startService(mService);
        return parentHolder;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
