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
}
