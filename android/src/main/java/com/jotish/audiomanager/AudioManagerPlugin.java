package com.jotish.audiomanager;

import static android.content.Context.AUDIO_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@CapacitorPlugin(name = "AudioOutput")
public class AudioManagerPlugin extends Plugin {

    private int mode = -100;
    private boolean isSpeakerOn = false;
    private boolean isMicMuted = false;

    HeadsetStateProvider headsetStateProvider;

    @PluginMethod
    public void listAudioOutputs(PluginCall call) {
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

                    case AudioDeviceInfo.TYPE_BLUETOOTH_A2DP:
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
        ret.put("devices", result);
        call.resolve(ret);
    }

    @PluginMethod
    public void switchToSpeaker(PluginCall call) {
        Context context = getContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            setCommunicationDevice(getContext(), AudioDeviceInfo.TYPE_BUILTIN_SPEAKER);
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
            setCommunicationDevice(getContext(), AudioDeviceInfo.TYPE_BUILTIN_EARPIECE);
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
            for(AudioDeviceInfo deviceInfo: outputDevices) {
                if(deviceInfo.getId() == deviceId) {
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

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void setCommunicationDevice(Context context, Integer targetDeviceType) {
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        List<AudioDeviceInfo> devices = audioManager.getAvailableCommunicationDevices();
        for (AudioDeviceInfo device : devices) {
            if (device.getType() == targetDeviceType) {
                boolean result = audioManager.setCommunicationDevice(device);
                Log.d("result: ", result + "");
            }
        }
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

        // save the current settings
        mode = audioManager.getMode();
        isSpeakerOn = audioManager.isSpeakerphoneOn();
        isMicMuted = audioManager.isMicrophoneMute();

        // Moving AudioManager to the "call" state
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        // Enabling speakerphone
        audioManager.setSpeakerphoneOn(false);

        call.resolve();
    }

    @PluginMethod
    public void endCall(PluginCall call) {
        Context context = getContext();
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(isMicMuted);
        audioManager.setSpeakerphoneOn(isSpeakerOn);

        // Removing the focus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build());
        } else {
            // Let's leave it empty to keep it simple
            audioManager.abandonAudioFocus(null);
        }

        call.resolve();
    }

    public class HeadsetStateProvider {
        private final Context context;
        private final AudioManager audioManager;
        private final MutableLiveData<Boolean> isHeadsetPlugged = new MutableLiveData<>();
        private final BroadcastReceiver receiver;

        public HeadsetStateProvider(Context context, AudioManager audioManager) {
            this.context = context;
            this.audioManager = audioManager;
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction() != null && intent.getAction().equals(AudioManager.ACTION_HEADSET_PLUG)) {
                        int state = intent.getIntExtra("state", -1);
                        switch (state) {
                            case 0:
                                isHeadsetPlugged.setValue(false);
                                break;
                            case 1:
                                isHeadsetPlugged.setValue(true);
                                break;
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            context.registerReceiver(receiver, filter);

            // Initialize the state when the object is created
            isHeadsetPlugged.setValue(getHeadsetState());
        }

        // Method to get the current headset state
        public boolean getHeadsetState() {
            AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : audioDevices) {
                if (device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    return true;
                }
            }
            return false;
        }

        // Method to get the current state of the headset
        public MutableLiveData<Boolean> getIsHeadsetPlugged() {
            return isHeadsetPlugged;
        }

        // Unregister the receiver to avoid memory leaks
        public void unregisterReceiver() {
            context.unregisterReceiver(receiver);
        }
    }
}
