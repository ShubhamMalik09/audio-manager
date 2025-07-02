# audio-manager

to enable audio output device selection

## Install

```bash
npm install audio-manager
npx cap sync
```

## API

<docgen-index>

* [`listAudioOutputs()`](#listaudiooutputs)
* [`switchToSpeaker()`](#switchtospeaker)
* [`switchToEarpiece()`](#switchtoearpiece)
* [`startCall()`](#startcall)
* [`endCall()`](#endcall)
* [`switchCommunicationDevice(...)`](#switchcommunicationdevice)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### listAudioOutputs()

```typescript
listAudioOutputs() => Promise<{ devices: { id: number; type: number; productName: string; typeName: string; }[]; }>
```

**Returns:** <code>Promise&lt;{ devices: { id: number; type: number; productName: string; typeName: string; }[]; }&gt;</code>

--------------------


### switchToSpeaker()

```typescript
switchToSpeaker() => Promise<void>
```

--------------------


### switchToEarpiece()

```typescript
switchToEarpiece() => Promise<void>
```

--------------------


### startCall()

```typescript
startCall() => Promise<void>
```

--------------------


### endCall()

```typescript
endCall() => Promise<void>
```

--------------------


### switchCommunicationDevice(...)

```typescript
switchCommunicationDevice(options: { deviceId: number; }) => Promise<void>
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ deviceId: number; }</code> |

--------------------

</docgen-api>
