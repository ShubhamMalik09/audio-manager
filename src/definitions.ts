export interface AudioManagerPlugin {
  // echo(options: { value: string }): Promise<{ value: string }>;
  listAudioOutputs(): Promise<{ devices: { id: number, type: number, productName: string, typeName: string }[] }>;
  switchToSpeaker(): Promise<void>;
  switchToEarpiece(): Promise<void>;
  startCall(): Promise<void>;
  endCall(): Promise<void>;
  switchCommunicationDevice(options: { deviceId: number }): Promise<void>;
  muteMicrophone(): Promise<void>;
  unmuteMicrophone(): Promise<void>;
  getMicrophoneStatus(): Promise<{ isMicMute: boolean }>;
  openNotificationSettings(): Promise<void>;
  showAppReviewPopup(): Promise<void>;
}