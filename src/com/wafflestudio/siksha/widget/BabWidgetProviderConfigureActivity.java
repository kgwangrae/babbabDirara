package com.wafflestudio.siksha.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.HashSet;
import java.util.Set;

public class BabWidgetProviderConfigureActivity extends Activity {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    String[] restaurants;

    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bab_widget_provider_configure);
        setResult(RESULT_CANCELED);
        FontUtil.getInstance().setFontAsset(this);
        TextView title = (TextView) findViewById(R.id.config_activity_main_title);
        title.setTypeface(FontUtil.fontAPAritaDotumMedium);
        TextView appName = (TextView) findViewById(R.id.config_activity_main_app_name);
        appName.setTypeface(FontUtil.fontAPAritaDotumMedium);

        restaurants = this.getResources().getStringArray(R.array.restaurants);

        listView = (ListView) findViewById(R.id.widget_configure_listview);
        ImageButton addButton = (ImageButton) findViewById(R.id.widget_config_accept_button);
        listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listAdapter.setChecked(position);
                    listAdapter.notifyDataSetChanged();
                  }
                }
        );

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addWidgetId(BabWidgetProviderConfigureActivity.this, appWidgetId);
                        saveTitlePref(BabWidgetProviderConfigureActivity.this, appWidgetId, listAdapter.getChecked());
                        startWidget();
                    }
                }
        );

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    class ListAdapter extends BaseAdapter {
        private ViewHolder viewHolder;
        private LayoutInflater inflater;

        private boolean[] isChecked;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            this.isChecked = new boolean[restaurants.length];
        }

        public Set<String> getChecked() {
            Set<String> restaurantSet = new HashSet<String>();
            for (int i = 0; i < isChecked.length; i++) {
                if (isChecked[i])
                    restaurantSet.add(restaurants[i]);
            }
            return restaurantSet;
        }

        @Override
        public int getCount() {
            return isChecked.length;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            return restaurants[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.bab_widget_configure_list_row, null);

                viewHolder = new ViewHolder();
                viewHolder.box = (CheckBox) convertView.findViewById(R.id.widget_configure_checkbox);
                convertView.setTag(viewHolder);

            }
            else
                viewHolder = (ViewHolder) convertView.getTag();

            final String restaurantName = restaurants[position];
            TextView textView = (TextView) convertView.findViewById(R.id.widget_configure_row_restaurant);
            textView.setText(restaurantName);
            textView.setTypeface(FontUtil.fontAPAritaDotumMedium);

            viewHolder.box.setClickable(false);
            viewHolder.box.setFocusable(false);
            viewHolder.box.setChecked(isChecked[position]);

            return convertView;
        }

        public void setChecked(int position) {
            isChecked[position] = !isChecked[position];
        }

        private class ViewHolder {
            private CheckBox box;
        }
    }

    private void startWidget() {
        new WidgetBreakfastCheckDialog(this, appWidgetId).show();
    }

    static void addWidgetId(Context context, int appWidgetId) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        idSet.add(Integer.toString(appWidgetId));
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID, idSet);
    }

    static boolean isValidId(Context context, int appWidgetId) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        return idSet.contains(Integer.toString(appWidgetId));
    }

    static Set<String> getAllWidgetIds(Context context) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        return idSet;
    }

    static void removeWidgetId(Context context, int appWidgetId) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            return;
        }

        idSet.remove(Integer.toString(appWidgetId));
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID, idSet);
    }

    static void saveTitlePref(Context context, int appWidgetId, Set<String> text) {
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId, text);
    }

    static Set<String> loadTitlePref(Context context, int appWidgetId) {
        Set<String> titleValue = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId);
        if (titleValue == null) {
            titleValue = new HashSet<String>();
        }
        return titleValue;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferenceUtil.removeValue(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId);
    }
}