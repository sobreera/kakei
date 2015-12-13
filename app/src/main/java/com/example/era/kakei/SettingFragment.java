package com.example.era.kakei;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
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

        CharSequence cs = getText(R.string.newYosan);
        EditTextPreference edit = (EditTextPreference)findPreference(cs);
        edit.setOnPreferenceChangeListener(editChange);
    }

    private Preference.OnPreferenceChangeListener editChange = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return editChangeBool(preference,newValue);
        }
    };

    private boolean editChangeBool(Preference preference, Object newValue){
        String input = newValue.toString();
        if (!input.equals("") && Integer.parseInt(input) > 1){ //返ってくるのは""　空で判定した場合!=null判定式はいらない　というかいらない
            //nullでなく1以上であれば要約を変更する
            preference.setSummary(input);
            return true;
        } else {
            //nullまたは1以下はエラー
            return false;
        }
    }
}
