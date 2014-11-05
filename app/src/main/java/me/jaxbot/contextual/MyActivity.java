package me.jaxbot.contextual;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MyActivity extends Activity {
    @Override
    protected void onPause()
    {
       super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Activity ctx = this;

        findViewById(R.id.button_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, WiFiDetection.class);
                startActivity(intent);
            }
        });
    }
}
