package com.wafflestudio.siksha.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.ParsingJson;
import com.wafflestudio.siksha.util.RestaurantCrawlingForm;
import com.wafflestudio.siksha.util.RestaurantInfo;

import java.util.ArrayList;

public class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;

    private ArrayList<String> cafeList;
    private ArrayList<RestaurantCrawlingForm> cafeMenuList;

    private int appWidgetId;
    private int hour;

    public WidgetListViewFactory(Context context, Intent intent) {
        this.context = context;

        appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;
        String input = BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId);
        Log.d("WidgetListViewFactory", input);

        RestaurantInfo.getInstance().loading(context);

        cafeList = new ArrayList<String>();
        for (int i = 0; input.length() != 0; i++) {
            if (input.substring(0, 1).equals("1"))
                cafeList.add(RestaurantInfo.restaurants[i]);
            input = input.substring(1);
        }

        cafeMenuList = new ArrayList<RestaurantCrawlingForm>();
        RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();

        if (forms != null) {
            for (int i = 0; i < cafeList.size(); i++) {
                for (int j = 0; j < forms.length; j++) {
                    if (cafeList.get(i).equals(forms[j].restaurant)) {
                        cafeMenuList.add(forms[j]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return cafeMenuList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.bab_widget_restaurant_list_row);
        RestaurantCrawlingForm item = cafeMenuList.get(position);

        Log.d("getViewAt", Integer.toString(position));

        remoteView.setTextViewText(R.id.restaurantViewWidget, item.restaurant);
        remoteView.removeAllViews(R.id.menuListWidget);
        boolean isEmpty = true;
        remoteView.setViewVisibility(R.id.widget_restaurant_empty_view, View.GONE);
        for (int i = 0; i < item.menus.length; i++) {
            if (hour >= 0 && hour <= 9) {
                if (item.menus[i].time.equals("breakfast")) {
                    RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                    child.setTextViewText(R.id.menuPriceWidget, item.menus[i].price);
                    child.setTextViewText(R.id.menuNameWidget, item.menus[i].name);
                    remoteView.addView(R.id.menuListWidget, child);
                    isEmpty = false;
                }
            }
            else if (hour >= 10 && hour <= 15) {
                if (item.menus[i].time.equals("lunch")) {
                    RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                    child.setTextViewText(R.id.menuPriceWidget, item.menus[i].price);
                    child.setTextViewText(R.id.menuNameWidget, item.menus[i].name);
                    remoteView.addView(R.id.menuListWidget, child);
                    isEmpty = false;
                }
            }
            else {
                if (item.menus[i].time.equals("dinner")) {
                    RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                    child.setTextViewText(R.id.menuPriceWidget, item.menus[i].price);
                    child.setTextViewText(R.id.menuNameWidget, item.menus[i].name);
                    remoteView.addView(R.id.menuListWidget, child);
                    isEmpty = false;
                }
            }
        }
        if (isEmpty)
            remoteView.setViewVisibility(R.id.widget_restaurant_empty_view, View.VISIBLE);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        Log.d("WidgetListViewFactory", "onDataSetChanged");
        hour = CalendarUtil.getCurrentHour();
        RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();
        if (forms != null) {
            cafeMenuList = new ArrayList<RestaurantCrawlingForm>();
            for (int i = 0; i < cafeList.size(); i++) {
                for (int j = 0; j < forms.length; j++) {
                    if (cafeList.get(i).equals(forms[j].restaurant)) {
                        cafeMenuList.add(forms[j]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() { }
}