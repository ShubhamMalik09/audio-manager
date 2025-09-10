package com.jotish.audiomanager;

import static android.content.Context.AUDIO_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@CapacitorPlugin(name = "AudioOutput")
public class AudioManagerPlugin extends Plugin implements SensorEventListener {

    private int mode = -100;
//    HeadsetStateProvider headsetStateProvider;

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    //    private AudioManager audioManager;
    private PowerManager powerManager;
    private PowerManager.WakeLock proximityWakeLock;

    @PluginMethod
    public void listAudioOutputs(PluginCall call) {
        Log.d("TESTTTTTT", " In list audio devices");
        Context context = getContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        AudioDeviceInfo[] outputDevices;
        outputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

        JSONArray result = new JSONArray();
        for (AudioDeviceInfo device : outputDevices) {
            try {
                JSONObject json = new JSONObject();
                switch (device.getType()) {
                    case AudioDeviceInfo.TYPE_WIRED_HEADPHONES:
                    case AudioDeviceInfo.TYPE_WIRED_HEADSET:
                        json.put("id", device.getId());
                        json.put("type", device.getType());
                        json.put("productName", device.getProductName().toString());
                        json.put("typeName", "Wired headphone");
                        result.put(json);
                        break;

//                    case AudioDeviceInfo.TYPE_BLUETOOTH_A2DP:
                    case AudioDeviceInfo.TYPE_BLUETOOTH_SCO:
                        json.put("id", device.getId());
                        json.put("type", device.getType());
                        json.put("productName", device.getProductName().toString());
                        json.put("typeName", "Bluetooth");
                        result.put(json);
                        break;

                    case AudioDeviceInfo.TYPE_BUILTIN_SPEAKER:
                        json.put("id", device.getId());
                        json.put("type", device.getType());
                        json.put("productName", device.getProductName().toString());
                        json.put("typeName", "Speaker");
                        result.put(json);
                        break;

                    case AudioDeviceInfo.TYPE_BUILTIN_EARPIECE:
                        json.put("id", device.getId());
                        json.put("type", device.getType());
                        json.put("productName", device.getProductName().toString());
                        json.put("typeName", "Earpiece");
                        result.put(json);
                        break;

                    default:
                        Log.d("AudioDevice", "Other device type: " + device.getType());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSObject ret = new JSObject();
        Log.d("TESTTTTT", "in list audio devices resolve");
        Log.d("TESTTTTT", "in list audio devices");
        Log.d("TESTTTTT", result.toString());
        ret.put("devices", result);
        call.resolve(ret);
    }

    @PluginMethod
    public void switchToSpeaker(PluginCall call) {
        Context context = getContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//            setCommunicationDevice(getContext(), AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
        } else {
            AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            audioManager.setSpeakerphoneOn(true);
        }

        Log.d("TESTTTT", "console Switching to speaker");
        call.resolve();
    }

    @PluginMethod
    public void switchToEarpiece(PluginCall call) {
        Context context = getContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//            setCommunicationDevice(getContext(), AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
        } else {
            AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            audioManager.setSpeakerphoneOn(false);
        }
        Log.d("TESTTTT", "console Switching to earpiece");
        call.resolve();
    }

    @PluginMethod
    public void setCommunicationDevice(PluginCall call) {
        Context context = getContext();
//        MyPlugin.sendDataToNative({ message: 'Hello Android', count: 3 });
        int deviceId = call.getInt("deviceId");
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        AudioDeviceInfo[] outputDevices;
        outputDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            for (AudioDeviceInfo deviceInfo : outputDevices) {
                if (deviceInfo.getId() == deviceId) {
                    audioManager.setCommunicationDevice(deviceInfo);
                    break;
                }
            }
        } else {
            for (AudioDeviceInfo device : outputDevices) {
                try {
                    switch (device.getType()) {
                        case AudioDeviceInfo.TYPE_WIRED_HEADPHONES:
                        case AudioDeviceInfo.TYPE_WIRED_HEADSET:
                            audioManager.setSpeakerphoneOn(false);
                            break;

                        case AudioDeviceInfo.TYPE_BLUETOOTH_A2DP:
                        case AudioDeviceInfo.TYPE_BLUETOOTH_SCO:
                            audioManager.startBluetoothSco();
                            audioManager.setBluetoothScoOn(true);
                            break;

                        case AudioDeviceInfo.TYPE_BUILTIN_SPEAKER:
                            audioManager.setSpeakerphoneOn(true);
                            break;

                        case AudioDeviceInfo.TYPE_BUILTIN_EARPIECE:
                            audioManager.setSpeakerphoneOn(false);
                            break;

                        default:
                            Log.d("AudioDevice", "Other device type: " + device.getType());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        call.resolve();
    }

    @PluginMethod
    public void startCall(PluginCall call) {
        Context context = getContext();
        // get the focus for the call
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build());
        } else {
            audioManager.requestAudioFocus(focusChange -> {
                // Listener of receiving focus. Let's leave it empty for the sake of simplicity.
            }, AudioAttributes.CONTENT_TYPE_SPEECH, AudioManager.AUDIOFOCUS_GAIN);
        }

        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

//        int result = audioManager.requestAudioFocus(
//                new AudioManager.OnAudioFocusChangeListener() {
//                    @Override
//                    public void onAudioFocusChange(int focusChange) {
//
//                    }
//                },
//                AudioManager.STREAM_VOICE_CALL,  // Focus on voice call audio
//                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
//        );
//
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            // Successfully gained audio focus for the ongoing call
//        }


        // start point for enabling the proximity sensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (sensorManager != null) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            proximityWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "Jotish::ProximityLock");
        }
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        // start point for enabling the proximity sensor

        call.resolve();
    }

    @PluginMethod
    public void getMicrophoneStatus(PluginCall call) {
        Context context = getContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        Log.d("TESTTTTTT", " In get mmicrophone" + audioManager.isMicrophoneMute());

        JSObject ret = new JSObject();
        ret.put("isMicMute", audioManager.isMicrophoneMute());
        call.resolve(ret);
    }

    @PluginMethod
    public void muteMicrophone(PluginCall call) {
        Log.d("TESTTTTTT", " In mute mmicrophone");
        Context context = getContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

//        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(true);

        call.resolve();
    }

    @PluginMethod
    public void unmuteMicrophone(PluginCall call) {
        Context context = getContext();
        Log.d("TESTTTTTT", " In unmute mmicrophone");
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

//        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);

        call.resolve();
    }

    @PluginMethod
    public void openNotificationSettings(PluginCall call) {
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + getContext().getPackageName()));
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);

        call.resolve();
    }

    @PluginMethod
    public void showAppReviewPopup(PluginCall call) {
        ReviewManager manager = ReviewManagerFactory.create(getContext());

        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(flowTask -> {
                    call.resolve();
                });
            } else {
                @ReviewErrorCode int reviewErrorCode = ((ReviewException) task.getException()).getErrorCode();
                call.reject("Review call failed " + reviewErrorCode);
            }
        });

        call.resolve();
    }

    @PluginMethod
    public void endCall(PluginCall call) {
        Context context = getContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        audioManager.setMode(AudioManager.MODE_NORMAL);
//        audioManager.setMicrophoneMute(true);
//        audioManager.setSpeakerphoneOn(isSpeakerOn);

        // Removing the focus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build());
        } else {
            // Let's leave it empty to keep it simple
            audioManager.abandonAudioFocus(null);
        }

        // Unregister the proximity sensor listener and release wake lock to stop using the proximity sensor
        if (sensorManager != null) {
            sensorManager.unregisterListener(this, proximitySensor);
        }
        if (proximityWakeLock != null && proximityWakeLock.isHeld()) {
            proximityWakeLock.release();
        }

        call.resolve();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < proximitySensor.getMaximumRange()) {
                acquireProximityLock();
            } else {
                releaseProximityLock();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for proximity sensor
    }

    private void acquireProximityLock() {
        if (proximityWakeLock != null && !proximityWakeLock.isHeld()) {
            proximityWakeLock.acquire();
        }
    }

    private void releaseProximityLock() {
        if (proximityWakeLock != null && proximityWakeLock.isHeld()) {
            proximityWakeLock.release();
        }
    }

//    public class HeadsetStateProvider {
//        private final Context context;
//        private final AudioManager audioManager;
//        private final MutableLiveData<Boolean> isHeadsetPlugged = new MutableLiveData<>();
//        private final BroadcastReceiver receiver;
//
//        public HeadsetStateProvider(Context context, AudioManager audioManager) {
//            this.context = context;
//            this.audioManager = audioManager;
//            this.receiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    if (intent.getAction() != null && intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG)) {
//                        int state = intent.getIntExtra("state", -1);
//                        switch (state) {
//                            case 0:
//                                isHeadsetPlugged.setValue(false);
//                                break;
//                            case 1:
//                                isHeadsetPlugged.setValue(true);
//                                break;
//                        }
//                    }
//                }
//            };
//
//            IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//            context.registerReceiver(receiver, filter);
//
//            // Initialize the state when the object is created
//            isHeadsetPlugged.setValue(getHeadsetState());
//        }
//
//        // Method to get the current headset state
//        public boolean getHeadsetState() {
//            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
//            for (AudioDeviceInfo device : audioDevices) {
//                if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        // Method to get the current state of the headset
//        public MutableLiveData<Boolean> getIsHeadsetPlugged() {
//            return isHeadsetPlugged;
//        }
//
//        // Unregister the receiver to avoid memory leaks
//        public void unregisterReceiver() {
//            context.unregisterReceiver(receiver);
//        }
//    }
}
