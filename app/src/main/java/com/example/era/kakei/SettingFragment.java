package com.example.era.kakei;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        // 読み書きするプリファレンスのファイル名を指定
        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName("settings");
    }
}
