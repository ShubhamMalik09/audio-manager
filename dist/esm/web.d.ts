import { WebPlugin } from '@capacitor/core';
import type { AudioManagerPlugin } from './definitions';
export declare class AudioManagerWeb extends WebPlugin implements AudioManagerPlugin {
    listAudioOutputs(): Promise<{
        devices: {
            id: number;
            type: number;
            productName: string;
            typeName: string;
        }[];
    }>;
    switchToSpeaker(): Promise<void>;
    switchToEarpiece(): Promise<void>;
    startCall(): Promise<void>;
    endCall(): Promise<void>;
    switchCommunicationDevice(_: {
        deviceId: number;
    }): Promise<void>;
    muteMicrophone(): Promise<void>;
    unmuteMicrophone(): Promise<void>;
    getMicrophoneStatus(): Promise<{
        isMicMute: boolean;
    }>;
    openNotificationSettings(): Promise<void>;
    showAppReviewPopup(): Promise<void>;
    openPermissionSettings(): Promise<void>;
}
