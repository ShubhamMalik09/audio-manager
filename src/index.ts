import { registerPlugin } from '@capacitor/core';

import type { AudioManagerPlugin } from './definitions';
import { AudioManagerWeb } from './web';

// const AudioManager = registerPlugin<AudioManagerPlugin>('AudioManager', {
//   web: () => import('./web').then((m) => new m.AudioManagerWeb()),
// });

export const AudioOutput = registerPlugin<AudioManagerPlugin>('AudioOutput', {
    web: () => new AudioManagerWeb()
});
export * from './definitions';
// export { AudioManager };
