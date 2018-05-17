package com.ambergarage.holokitsdk.holokitscreenbrightnessmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.os.Bundle;
import android.widget.Toast;

public class HolokitScreenBrightnessManagerActivity extends Activity {

    public final static int REQUEST_CODE_WRITE_SETTINGS = 333;
    private static IHolokitPermissionCallback mHolokitPermissionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holokit_screen_brightness_manager);

        if (!hasWriteSettings()) {
            Toast.makeText(getApplicationContext(), "Holokit needs to manage screen brightness", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
        } else {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            if (mHolokitPermissionCallback != null) {
                mHolokitPermissionCallback.granted();
            }
            finish();
        }
    }

    private boolean hasWriteSettings() {
        return Settings.System.canWrite(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (mHolokitPermissionCallback != null) {
                if (hasWriteSettings()) {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    mHolokitPermissionCallback.granted();
                } else {
                    mHolokitPermissionCallback.denied();
                }
            }
        }
        finish();
    }

    public static void setPermissionCallback(IHolokitPermissionCallback holokitPermissionCallback) {
        mHolokitPermissionCallback = holokitPermissionCallback;
    }
}
