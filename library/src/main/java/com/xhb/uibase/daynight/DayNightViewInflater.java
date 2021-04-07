package com.xhb.uibase.daynight;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xhb.uibase.daynight.styleable.AttrValueSet;
import com.xhb.uibase.daynight.styleable.StyleableSet;

import java.util.Map;
import java.util.WeakHashMap;

public class DayNightViewInflater extends DayNightBaseViewInflater {

    private static final String TAG = "DayNightViewInflater";

    public DayNightViewInflater() {
        DayNightManager.getInstance().addActiveViewInflater(this);
    }

    private final Map<View, AttrValueSet<View>> mStyledViews = new WeakHashMap<>();

    public Activity getActivity() {
        return mActivity;
    }

    public void updateViews() {
        Log.d(TAG, "updateViews");
        for (Map.Entry<View, AttrValueSet<View>> view : mStyledViews.entrySet()) {
            view.getValue().apply(view.getKey());
        }
    }

    @Override
    protected void inspectView(View view, AttributeSet attrs) {
        if (!isActivityManifestHandlingUiMode(view.getContext()))
            return;
        AttrValueSet<View> styleSet = StyleableSet.createAttrValueSet(view, attrs);
        if (!styleSet.isEmpty()) {
            mStyledViews.put(view, styleSet);
        }
    }

    private Activity mActivity = null;
    private boolean mActivityHandlesUiModeChecked = false;
    private boolean mActivityHandlesUiMode = false;

    private boolean isActivityManifestHandlingUiMode(Context context) {
        if (!mActivityHandlesUiModeChecked && context instanceof Activity) {
            mActivity = (Activity) context;
            final PackageManager pm = context.getPackageManager();
            if (pm == null) {
                // If we don't have a PackageManager, return false. Don't set
                // the checked flag though so we still check again later
                return false;
            }
            try {
                int flags = 0;
                // On newer versions of the OS we need to pass direct boot
                // flags so that getActivityInfo doesn't crash under strict
                // mode checks
                if (Build.VERSION.SDK_INT >= 29) {
                    flags = PackageManager.MATCH_DIRECT_BOOT_AUTO
                        | PackageManager.MATCH_DIRECT_BOOT_AWARE
                        | PackageManager.MATCH_DIRECT_BOOT_UNAWARE;
                } else if (Build.VERSION.SDK_INT >= 24) {
                    flags = PackageManager.MATCH_DIRECT_BOOT_AWARE
                        | PackageManager.MATCH_DIRECT_BOOT_UNAWARE;
                }
                final ActivityInfo info = pm.getActivityInfo(
                    new ComponentName(context, context.getClass()), flags);
                mActivityHandlesUiMode = info != null
                    && (info.configChanges & ActivityInfo.CONFIG_UI_MODE) != 0;
            } catch (PackageManager.NameNotFoundException e) {
                // This shouldn't happen but let's not crash because of it, we'll just log and
                // return false (since most apps won't be handling it)
                Log.d(TAG, "Exception while getting ActivityInfo", e);
                mActivityHandlesUiMode = false;
            }
        }
        // Flip the checked flag so we don't check again
        mActivityHandlesUiModeChecked = true;

        return mActivityHandlesUiMode;
    }

}
