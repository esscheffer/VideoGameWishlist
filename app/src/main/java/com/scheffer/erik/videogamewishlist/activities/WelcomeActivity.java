package com.scheffer.erik.videogamewishlist.activities;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.syncadapter.GenericAccountService;
import com.scheffer.erik.videogamewishlist.syncadapter.SyncUtils;

import static com.scheffer.erik.videogamewishlist.syncadapter.SyncUtils.ACCOUNT_TYPE;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.fullscreen_content)
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                               | View.SYSTEM_UI_FLAG_FULLSCREEN
                                               | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                               | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                               | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                               | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        SyncUtils.CreateSyncAccount(this);

        findViewById(R.id.fullscreen_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                ContentResolver.requestSync(
                        GenericAccountService.GetAccount(ACCOUNT_TYPE),
                        WishlistContract.AUTHORITY,
                        b);
            }
        });
    }
}
