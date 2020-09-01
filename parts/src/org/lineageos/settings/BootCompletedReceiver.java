/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.ActivityManager;
import android.os.SELinux;
import android.util.Log;
import android.widget.Toast;
import java.util.List;

import org.lineageos.settings.dirac.DiracUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String PREF_SELINUX_MODE = "selinux_mode";
    private static final String TAG = "SettingsOnBoot";
    private boolean mSetupRunning = false;
    private Context settingsContext = null;
    private Context mContext;
    private static final String TAG = "XiaomiParts";
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (DEBUG) Log.d(TAG, "Received boot completed intent");
        new DiracUtils(context).onBootCompleted();
        }
            if (DEBUG) Log.d(TAG, "sharedpreferences.contains(" + PREF_SELINUX_MODE + "): " + (sharedpreferences.contains(PREF_SELINUX_MODE) ? "True":"False"));

            if (sharedpreferences.contains(PREF_SELINUX_MODE)) {
                boolean currentIsSelinuxEnforcing = SELinux.isSELinuxEnforced();
                boolean isSelinuxEnforcing = sharedpreferences.getBoolean(PREF_SELINUX_MODE, currentIsSelinuxEnforcing);
                if (DEBUG) Log.d(TAG, String.format("currentIsSelinuxEnforcing: %s, isSelinuxEnforcing: %s", (currentIsSelinuxEnforcing ? "True" : "False"), (isSelinuxEnforcing ? "True" : "False")));
                try {
                    if (isSelinuxEnforcing) {
                        if (!currentIsSelinuxEnforcing) {
                            SuShell.runWithSuCheck("setenforce 1");
                            showToast(context.getString(R.string.selinux_enforcing_toast_title),
                                    context);
                        }
                    } else {
                        if (currentIsSelinuxEnforcing) {
                            SuShell.runWithSuCheck("setenforce 0");
                            showToast(context.getString(R.string.selinux_permissive_toast_title),
                                    context);
                        }
                    }
                } catch (SuShell.SuDeniedException e) {
                    showToast(context.getString(R.string.cannot_get_su), context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        private void showToast(String toastString, Context context) {
        Toast.makeText(context, toastString, Toast.LENGTH_SHORT)
                .show();
    }
}
