package net.publisher;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;

import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

/**
 * Utility methods class.
 * Created by Mayboroda on 6/8/15.
 */
public final class Utils {

    /** Fonts names. */
    public static final String ROBOTO_LIGHT     = "Roboto-Light.ttf";
    public static final String ROBOTO_MEDIUM    = "Roboto-Medium.ttf";
    public static final String ROBOTO_REGULAR   = "Roboto-Regular.ttf";
    public static final String ROBOTO_THIN      = "Roboto-Thin.ttf";
    public static final String ROBOTO_BLACK     = "Roboto-Black.ttf";
    public static final String ROBOTO_COND      = "RobotoCondensed-Light.ttf";

    private Utils(){
        throw new AssertionError("Unable to instantiate");
    }

    /** Check internet connection method. */
    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /** Generating typeface. */
    public static Typeface typeface(Context context, String name) {
        return Typeface.createFromAsset(context.getAssets(), name);
    }

    /** Dialog error reporting. */
    public static void errorReport(VKError error, Context context) {
        if (error.errorCode == 14) {
            VKCaptchaDialog captchaDialog = new VKCaptchaDialog(error);
            captchaDialog.show(context, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        } else {
            AppCompatDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(context.getString(Utils.getErrorStringId(error.errorCode)))
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    /** Errors string ids. */
    public static int getErrorStringId(int responseId) {
        switch (responseId) {
            case 1: return R.string.error_unknown;
            case 2: return R.string.error_settings;
            case 3: return R.string.error_method;
            case 4: return R.string.error_sign;
            case 5: return R.string.error_auth;
            case 6: return R.string.error_count;
            case 7: return R.string.error_permission;
            case 8: return R.string.error_mono;
            case 9: return R.string.error_request;
            case 10: return R.string.error_server;
            case 11: return R.string.error_disable_app;
            case 15: return R.string.error_unable;
            case 16: return R.string.error_https;
            case 17: return R.string.error_validation;
            case 20: return R.string.error_not_standalone;
            case 21: return R.string.error_only_standalone;
            case 23: return R.string.error_method_disabled;
            case 24: return R.string.error_user_agreement;
            case 100: return R.string.error_params;
            case 101: return R.string.error_app_id;
            case 113: return R.string.error_user_id;
            case 150: return R.string.error_timestamp;
            case 200: return R.string.error_album;
            case 201: return R.string.error_audio;
            case 203: return R.string.error_group;
            case 300: return R.string.error_album_overloaded;
            case 500: return R.string.error_translation;
            case 600: return R.string.error_ads_permission;
            case 603: return R.string.error_ads_manager;
            default: return R.string.error_unknown;
        }
    }

    /** Genres strings. */
    public static int getGenres(int genreId) {
        switch (genreId) {
            case 1: return R.string.genre_rock;
            case 2: return R.string.genre_pop;
            case 3: return R.string.genre_rap_hh;
            case 4: return R.string.genre_easy;
            case 5: return R.string.genre_dance_house;
            case 6: return R.string.genre_instrumental;
            case 7: return R.string.genre_metal;
            case 21: return R.string.genre_alternative;
            case 8: return R.string.genre_dubstep;
            case 9: return R.string.genre_jazz_blues;
            case 10: return R.string.genre_drum_bass;
            case 11: return R.string.genre_trance;
            case 12: return R.string.genre_chanson;
            case 13: return R.string.genre_ethnic;
            case 14: return R.string.genre_acoustic_vocal;
            case 15: return R.string.genre_reggae;
            case 16: return R.string.genre_classic;
            case 17: return R.string.genre_indie_pop;
            case 19: return R.string.genre_speech;
            case 22: return R.string.genre_electropop_disco;
            case 18: return R.string.genre_other;
            default: return R.string.genre_other;
        }
    }
}
