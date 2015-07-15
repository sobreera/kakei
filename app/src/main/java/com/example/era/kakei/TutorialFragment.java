package com.example.era.kakei;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFragment extends Fragment {

    ImageView imageView;
    TextView textView;
    SharedPreferences data;
    SharedPreferences.Editor editor;

    public TutorialFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final int position = bundle.getInt("position");

        data = getActivity().getSharedPreferences("settings", getActivity().MODE_PRIVATE);
        editor =data.edit();

        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        imageView = (ImageView)view.findViewById(R.id.imageView);
        textView = (TextView)view.findViewById(R.id.textView);
        Log.d(null,"position:"+position);
        switch (position){
            case 0:
                imageView.setImageResource(R.drawable.main_kai);
                textView.setText(R.string.mainTutorial);
                break;
            case 1:
                imageView.setImageResource(R.drawable.main2);
                textView.setText(R.string.mainTutorial2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.list);
                textView.setText(R.string.listTutorial);
                break;
            case 3:
                imageView.setImageResource(R.drawable.data);
                textView.setText(R.string.dataTutorial);
                break;
            case 4:
                break;
            case 5:
                getDialog();
                break;
        }
        return view;
    }

    public void getDialog(){
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("今月の予算");
        builder.setMessage("今月の予算を入力してください\n入力がない場合10000に設定されます");
        builder.setCancelable(false);
        builder.setView(editText);
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        // フィルターを作成
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9]+$")) {
                    return source;
                } else {
                    return "";
                }
            }
        };
        // フィルターの配列を作成
        InputFilter[] filters = new InputFilter[]{inputFilter};
        // フィルターの配列をセット
        editText.setFilters(filters);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int edit = Integer.parseInt(editText.getText().toString());
                if (edit < 1) {
                    onDestroy();
                } else {
                    editor.putString("old_yosan", editText.getText().toString());
                    editor.putString("old_yosan",editText.getText().toString());
                    editor.putBoolean("Tutorial", true);
                    editor.apply();
                    onDestroy();
                }
            }
        });
        builder.create().show();
    }


}
