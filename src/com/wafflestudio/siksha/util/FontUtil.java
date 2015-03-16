package com.wafflestudio.siksha.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontUtil {
  private static FontUtil fontUtil;

  public static Typeface fontAPAritaDotumMedium; // 아모레퍼시픽 아리따돋움체 Medium
  public static Typeface fontAPAritaDotumSemiBold; // 아모레퍼시픽 아리따돋움체 SemiBold
  public static Typeface fontBMHanna; // 배달의민족 한나체

  private FontUtil() { }

  public static FontUtil getInstance() {
    if (fontUtil == null)
      fontUtil = new FontUtil();
    return fontUtil;
  }

  public void setFontAsset(AssetManager assetManager) {
    fontAPAritaDotumMedium = Typeface.createFromAsset(assetManager, "Arita-Dotum-Medium.otf");
    fontAPAritaDotumSemiBold = Typeface.createFromAsset(assetManager, "Arita-Dotum-SemiBold.otf");
    fontBMHanna = Typeface.createFromAsset(assetManager, "BM-Hanna.ttf");
  }
}
