package com.wafflestudio.siksha.form.response;

import com.google.gson.annotations.SerializedName;

public class AppVersion {
    @SerializedName("latest")
    public String latest; // latest app version

    public String current; // current app version
}
