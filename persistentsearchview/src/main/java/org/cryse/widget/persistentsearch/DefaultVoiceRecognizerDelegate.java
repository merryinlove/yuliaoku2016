package org.cryse.widget.persistentsearch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;

import java.util.List;

public class DefaultVoiceRecognizerDelegate extends VoiceRecognitionDelegate {

    public DefaultVoiceRecognizerDelegate(Activity activity) {
        super(activity);
    }

    public DefaultVoiceRecognizerDelegate(Activity activity, int activityRequestCode) {
        super(activity, activityRequestCode);
    }

    public DefaultVoiceRecognizerDelegate(Fragment fragment) {
        super(fragment);
    }

    public DefaultVoiceRecognizerDelegate(Fragment fragment, int activityRequestCode) {
        super(fragment, activityRequestCode);
    }

    public DefaultVoiceRecognizerDelegate(android.support.v4.app.Fragment supportFragment) {
        super(supportFragment);
    }

    public DefaultVoiceRecognizerDelegate(android.support.v4.app.Fragment supportFragment, int activityRequestCode) {
        super(supportFragment, activityRequestCode);
    }

    @Override
    public Intent buildVoiceRecognitionIntent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getContext().getString(R.string.speak_now));
        return intent;
    }

    @Override
    public boolean isVoiceRecognitionAvailable() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        PackageManager mgr = getContext().getPackageManager();
        if (mgr != null) {
            List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }
}
