package com.example.job_locator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoW implements GoogleMap.InfoWindowAdapter {

private final View mW;
private Context mc;

    public CustomInfoW(Context mc) {
        this.mc = mc;
        mW = LayoutInflater.from(mc).inflate(R.layout.custom_info, null);

    }
private void rendow(Marker marker,View view){

        String title = marker.getTitle();
    TextView tv =(TextView) view.findViewById(R.id.title);
    if(!title.equals("")){
        tv.setText(title);
    }

    String snippet = marker.getSnippet();
    TextView tvs =(TextView) view.findViewById(R.id.snippet);
    if(!snippet.equals("")){
        tvs.setText(snippet);
    }
}
    @Override
    public View getInfoWindow(Marker marker) {
        rendow(marker, mW);
        return mW;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendow(marker,mW);
        return mW;
    }
}
