package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.DownloadingRetryDialog;
import com.wafflestudio.siksha.dialog.NotifyWidgetDialog;
import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.service.DownloadingJson;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;

import java.util.ArrayList;
import java.util.List;

public class InitialLoadingMenu {
  private Context context;
  private DownloadingJsonReceiver downloadingJsonReceiver;

  private ViewPager viewPager;
  private ProgressDialog progressDialog;

  public RestaurantCrawlingForm[] forms;

  public InitialLoadingMenu(Context context, DownloadingJsonReceiver downloadingJsonReceiver, ViewPager viewPager) {
    this.context = context;
    this.downloadingJsonReceiver = downloadingJsonReceiver;
    this.viewPager = viewPager;
  }

  public void initSetting() {
    int option = DownloadingJson.getDownloadOption();
    String downloadingDate = DownloadingJson.getDownloadingDate(option);

    if (DownloadingJson.isJsonUpdated(context, downloadingDate)) {
      Log.d("is_json_updated", "true");

      forms = new ParsingJson(context).getParsedForms();
      RestaurantInfoUtil.getInstance().setMenuMap(forms);
      RestaurantSequencer.getInstance().setMenuListOnSequence();

      setAdapters();
      setInitialPage();

      notifyWidget();
    }
    else {
      Log.d("is_json_updated", "false");

      if (!NetworkUtil.getInstance().isOnline())
        new DownloadingRetryDialog(context, this, option, downloadingDate).show();
      else
        startDownloadingService(context, option, downloadingDate);
    }
  }

  private void setAdapters() {
    RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();

    List<AdapterUtil.ExpandableListAdapter> adapters = new ArrayList<AdapterUtil.ExpandableListAdapter>();
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, restaurantSequencer.breakfastMenuList, 0));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, restaurantSequencer.lunchMenuList, 1));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, restaurantSequencer.dinnerMenuList, 2));

    viewPager.setAdapter(new AdapterUtil.ViewPagerAdapter(context, adapters));
  }

  private void setReceiverCallBack() {
    downloadingJsonReceiver.setOnCompleteDownloadListener(new DownloadingJsonReceiver.OnCompleteDownloadListener() {
      @Override
      public void onComplete() {
        forms = new ParsingJson(context).getParsedForms();
        RestaurantInfoUtil.getInstance().setMenuMap(forms);
        RestaurantSequencer.getInstance().setMenuListOnSequence();

        setAdapters();
        setInitialPage();

        notifyWidget();

        if (progressDialog != null && progressDialog.isShowing())
          progressDialog.quitShowing();
      }

      @Override
      public void onFail(int option, String downloadingDate) {
        if (progressDialog != null && progressDialog.isShowing())
          progressDialog.quitShowing();

        new DownloadingRetryDialog(context, InitialLoadingMenu.this, option, downloadingDate).show();
      }
    });
  }

  public void startDownloadingService(final Context context, int option, String downloadingDate) {
    setReceiverCallBack();

    progressDialog = new ProgressDialog(context, context.getString(R.string.downloading_message));
    progressDialog.setCancelable(false);
    progressDialog.startShowing();

    Intent intent = new Intent(context, DownloadingJson.class);
    intent.setAction(DownloadingJsonReceiver.ACTION_CURRENT_DOWNLOAD);
    intent.putExtra(DownloadingJson.KEY_OPTION, option);
    intent.putExtra(DownloadingJson.KEY_DATE, downloadingDate);
    context.startService(intent);
  }

  private void setInitialPage() {
    int hour = CalendarUtil.getCurrentHour();

    if (hour <= 9 || hour >= 21)
      viewPager.setCurrentItem(0);
    else if (hour >= 10 && hour <= 14)
      viewPager.setCurrentItem(1);
    else
      viewPager.setCurrentItem(2);
  }

  private void notifyWidget() {
    boolean isNoticed = SharedPreferenceUtil.loadValueOfBoolean(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_NOTIFY_WIDGET);

    if (!isNoticed) {
      new NotifyWidgetDialog(context).show();
      SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_NOTIFY_WIDGET, true);
    }
  }
}