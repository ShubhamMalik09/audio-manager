// import { WebPlugin } from '@capacitor/core';

// import type { AudioManagerPlugin } from './definitions';

// export class AudioManagerWeb extends WebPlugin implements AudioManagerPlugin {
//   async echo(options: { value: string }): Promise<{ value: string }> {
//     console.log('ECHO', options);
//     return options;
//   }
// }

import { WebPlugin } from '@capacitor/core';
import type { AudioManagerPlugin } from './definitions';

export class AudioManagerWeb extends WebPlugin implements AudioManagerPlugin {
  async listAudioOutputs(): Promise<{ devices: { id: number; type: number; productName: string, typeName: string }[] }> {
    console.warn('[AudioManager] listAudioOutputs is not supported on web.');
    return {
      devices: [
        { id: 0, type: 0, productName: 'Default Web Audio Output', typeName: 'Default Web Audio Output' },
      ],
    };
  }

  async switchToSpeaker(): Promise<void> {
    console.warn('[AudioManager] switchToSpeaker is not supported on web.');
  }

  async switchToEarpiece(): Promise<void> {
    console.warn('[AudioManager] switchToEarpiece is not supported on web.');
  }

  async startCall(): Promise<void> {
    console.warn('[AudioManager] startCall is not supported on web.');
  }

  async endCall(): Promise<void> {
    console.warn('[AudioManager] endCall is not supported on web.');
  }

  async switchCommunicationDevice(_: { deviceId: number }): Promise<void> {
    console.warn('[AudioManager] switchCommunicationDevice is not supported on web.');
  }

  async muteMicrophone(): Promise<void> {
    console.warn('[AudioManager] muteMicrophone is not supported on web.');
  }

  async unmuteMicrophone(): Promise<void> {
    console.warn('[AudioManager] unmuteMicrophone is not supported on web.');
  }

  async getMicrophoneStatus(): Promise<{ isMicMute: boolean }> {
    console.warn('[AudioManager] getMicrophoneStatus is not supported on web.');
    return {
      isMicMute: false,
    };
  }

  async openNotificationSettings(): Promise<void> {
    console.warn('[AudioManager] openNotificationSettings is not supported on web.');
  }

  async showAppReviewPopup(): Promise<void> {
    console.warn('[AudioManager] showAppReviewPopup is not supported on web.');
  }

  async openPermissionSettings(): Promise<void> {
    console.warn('[AudioManager] openPermissionSettings is not supported on web.');
  }
}

