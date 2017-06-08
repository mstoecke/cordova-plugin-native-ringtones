package cordova.plugin.nativeRingtones;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.Ringtone;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.database.Cursor;
import android.content.Context;

/**
 * This class echoes a string called from JavaScript.
 */
public class NativeRingtones extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException {
        if (action.equals("get")){
            return this.get(args.getString(0), callbackContext);
        }
        if (action.equals("play")){
            // ringtoneUri, playOnce, volume, streamType
            return this.play(args.getString(0), args.getBoolean(1), args.getInt(2), args.getInt(3), callbackContext);
        }
        if (action.equals("stop")){
            return this.stop(args.getString(0), callbackContext);
        }
        return false;
    }

  private boolean get(String ringtoneType, final CallbackContext callbackContext) throws JSONException{
        RingtoneManager manager = new RingtoneManager(this.cordova.getActivity().getBaseContext());

        //The default value if ringtone type is "notification"
        if (ringtoneType == "alarm") {
            manager.setType(RingtoneManager.TYPE_ALARM);
        } else if (ringtoneType == "ringtone"){
            manager.setType(RingtoneManager.TYPE_RINGTONE);
        } else {
            manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        }

        Cursor cursor = manager.getCursor();
        JSONArray ringtoneList = new JSONArray();

        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);

            /****   Transfer Content URI to file URI   ******* /
            /* String filePath;

            if (notificationUri != null && "content".equals(notificationUri.getScheme())) {
                Cursor cursor1 = this.cordova.getActivity().getBaseContext().getContentResolver().query(notificationUri, new String[] {
                    android.provider.MediaStore.Images.ImageColumns.DATA
                }, null, null, null);
                cursor1.moveToFirst();
                filePath = cursor1.getString(0);
                cursor1.close();
            } else {
                filePath = notificationUri.getPath();
            }*/

            JSONObject json = new JSONObject();
            json.put("Name", notificationTitle);
            json.put("Url", notificationUri);

            ringtoneList.put(json);
        }

        if (ringtoneList.length() > 0) {
            callbackContext.success(ringtoneList);
        } else {
            callbackContext.error("Can't get system Ringtone list");
        }

        return true;
    }

    private boolean play(String ringtoneUri, boolean playOnce, int volume, int streamType, final CallbackContext callbackContext) throws JSONException{
        try {
            if (streamType == -1) {
                streamType = AudioManager.STREAM_NOTIFICATION;
            }

            Context ctx = this.cordova.getActivity().getApplicationContext();
            AssetManager am = ctx.getResources().getAssets();
            AssetFileDescriptor afd = am.openFd(ringtoneUri);

            MediaPlayer ringtoneSound = new MediaPlayer();
            ringtoneSound.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            ringtoneSound.setAudioStreamType(streamType);
            ringtoneSound.prepare();
            ringtoneSound.setLooping(!playOnce);
            ringtoneSound.setVolume(volume * 0.01f, volume * 0.01f);

            if (ringtoneSound != null) {
                ringtoneSound.start();
            } else{
                callbackContext.error("Can't play the ringtone!");
            }
        } catch (Exception e) {
            callbackContext.error("Can't play the ringtone!");
        }
        return true;
    }

    private boolean stop(String ringtoneUri, final CallbackContext callbackContext) throws JSONException{
        try {
            Context ctx = this.cordova.getActivity().getApplicationContext();
            AssetManager am = ctx.getResources().getAssets();
            AssetFileDescriptor afd = am.openFd(ringtoneUri);

            MediaPlayer ringtoneSound = new MediaPlayer();
            ringtoneSound.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            ringtoneSound.prepare();

            if (ringtoneSound != null) {
                ringtoneSound.stop();
                ringtoneSound.release();
                callbackContext.success("Stop the ringtone succennfully!");
            } else{
                callbackContext.error("Can't stop the ringtone!");
            }
        } catch (Exception e) {
            callbackContext.error("Can't play the ringtone!");
        }

        return true;
    }
}
