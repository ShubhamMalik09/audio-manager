// import { WebPlugin } from '@capacitor/core';
// import type { AudioManagerPlugin } from './definitions';
// export class AudioManagerWeb extends WebPlugin implements AudioManagerPlugin {
//   async echo(options: { value: string }): Promise<{ value: string }> {
//     console.log('ECHO', options);
//     return options;
//   }
// }
import { WebPlugin } from '@capacitor/core';
export class AudioManagerWeb extends WebPlugin {
    async listAudioOutputs() {
        console.warn('[AudioManager] listAudioOutputs is not supported on web.');
        return {
            devices: [
                { id: 0, type: 0, productName: 'Default Web Audio Output', typeName: 'Default Web Audio Output' },
            ],
        };
    }
    async switchToSpeaker() {
        console.warn('[AudioManager] switchToSpeaker is not supported on web.');
    }
    async switchToEarpiece() {
        console.warn('[AudioManager] switchToEarpiece is not supported on web.');
    }
    async startCall() {
        console.warn('[AudioManager] startCall is not supported on web.');
    }
    async endCall() {
        console.warn('[AudioManager] endCall is not supported on web.');
    }
    async switchCommunicationDevice(_) {
        console.warn('[AudioManager] switchCommunicationDevice is not supported on web.');
    }
    async muteMicrophone() {
        console.warn('[AudioManager] muteMicrophone is not supported on web.');
    }
    async unmuteMicrophone() {
        console.warn('[AudioManager] unmuteMicrophone is not supported on web.');
    }
    async getMicrophoneStatus() {
        console.warn('[AudioManager] getMicrophoneStatus is not supported on web.');
        return {
            isMicMute: false,
        };
    }
}
//# sourceMappingURL=web.js.map