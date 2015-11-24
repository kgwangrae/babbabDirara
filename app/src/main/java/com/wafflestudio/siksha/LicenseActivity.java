package com.wafflestudio.siksha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wafflestudio.siksha.page.settings.LicenseRecyclerViewAdapter;
import com.wafflestudio.siksha.util.Fonts;

public class LicenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        TextView titleView = (TextView) findViewById(R.id.activity_license_title_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_license_recycler_view);

        titleView.setTypeface(Fonts.fontBMJua);

        LicenseRecyclerViewAdapter adapter = new LicenseRecyclerViewAdapter(
                getResources().getStringArray(R.array.library),
                getResources().getStringArray(R.array.link),
                getResources().getStringArray(R.array.license));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
