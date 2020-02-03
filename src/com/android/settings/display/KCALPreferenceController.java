/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.android.settings.display;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;

import java.util.List;

public class KCALPreferenceController extends BasePreferenceController {
    private static final String TAG = "KCALPreferenceController";

    private final String mKCALPackage;
    private final String mKCALClass;
    
    public KCALPreferenceController(Context context, String key) {
        super(context, key);
        mKCALPackage = mContext.getString(R.string.config_KCAL_package);
        mKCALClass = mContext.getString(R.string.config_KCAL_class);
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        Preference preference = screen.findPreference(getPreferenceKey());
        preference.setTitle( getTitle());
        preference.setSummary( getSummary());
    }

    public ComponentName getComponentName() {
        return new ComponentName(mKCALPackage, mKCALClass);
    }
	
	public final String getSummary(){
        return mContext.getString(R.string.kcal_settings_summary);
    }
	
	public final String getTitle(){
        return mContext.getString(R.string.kcal_settings_title);
    }
	

    public String getKeywords() {
        StringBuilder sb = new StringBuilder(mContext.getString(R.string.keywords_KCAL));
        return sb.toString();
    }

    @Override
    public int getAvailabilityStatus() {
        if (TextUtils.isEmpty(mKCALPackage) || TextUtils.isEmpty(mKCALClass)) {
            Log.e(TAG, "No KCAL specified!");
            return UNSUPPORTED_ON_DEVICE;
        }
        return canResolveKCALComponent(mKCALClass)
                ? AVAILABLE_UNSEARCHABLE : CONDITIONALLY_UNAVAILABLE;
    }

    @Override
    public void updateState(Preference preference) {
        //disablePreferenceIfManaged((RestrictedPreference) preference);
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (getPreferenceKey().equals(preference.getKey())) {
            preference.getContext().startActivity(new Intent().setComponent(getComponentName()));
            return true;
        }
        return super.handlePreferenceTreeClick(preference);
    }

    private boolean canResolveKCALComponent(String className) {
        final ComponentName componentName = new ComponentName(mKCALPackage, className);
        final PackageManager pm = mContext.getPackageManager();
        final Intent intent = new Intent().setComponent(componentName);
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0 /* flags */);
        return resolveInfos != null && !resolveInfos.isEmpty();
    }
}
