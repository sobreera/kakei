package com.example.era.kakei;

import android.content.ClipData;
import android.view.View;

/**
 * Created by era on 15/06/24.
 */
public class MyLongClickListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View v){
        ClipData clipData = ClipData.newPlainText("","");
        //ドラッグ中に表示するイメージのビルダー
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
        //ドラッグを開始
        v.startDrag(clipData,shadow,v,0);

        return true;
    }
}
