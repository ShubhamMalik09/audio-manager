export interface AudioManagerPlugin {
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
    switchCommunicationDevice(options: {
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
    saveFCMToken(options: {
        fcmToken: string;
    }): Promise<void>;
    getFCMToken(): Promise<{
        fcmToken: string;
    }>;
    clearFCMToken(): Promise<void>;
    getDeviceInfo(): Promise<{
        manufacturer: string;
        model: string;
        osVersion: string;
        appVersionName: string;
        appVersionCode: string;
        networkType: string;
        networkSpeed: string;
    }>;
}
