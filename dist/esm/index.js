import { registerPlugin } from '@capacitor/core';
import { AudioManagerWeb } from './web';
// const AudioManager = registerPlugin<AudioManagerPlugin>('AudioManager', {
//   web: () => import('./web').then((m) => new m.AudioManagerWeb()),
// });
export const AudioOutput = registerPlugin('AudioOutput', {
    web: () => new AudioManagerWeb()
});
export * from './definitions';
// export { AudioManager };
//# sourceMappingURL=index.js.map