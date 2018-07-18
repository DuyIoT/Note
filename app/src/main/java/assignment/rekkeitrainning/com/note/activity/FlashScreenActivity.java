package assignment.rekkeitrainning.com.note.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import assignment.rekkeitrainning.com.note.R;

public class FlashScreenActivity extends AppCompatActivity {
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        initListener();
    }

    private void initListener() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mIntent = new Intent(FlashScreenActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        }, 3000);
    }
}
