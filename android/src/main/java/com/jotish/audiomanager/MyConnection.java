package com.jotish.audiomanager;

import android.telecom.Connection;
import android.telecom.DisconnectCause;

public class MyConnection extends Connection {

    @Override
    public void onAnswer() {
        super.onAnswer();
        setActive();
        // Start VoIP call logic here
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
        setDisconnected(new DisconnectCause(DisconnectCause.LOCAL));
        destroy();
        // Clean up VoIP call
    }

    @Override
    public void onHold() {
        super.onHold();
        setOnHold();
        // Pause media here
    }

    @Override
    public void onUnhold() {
        super.onUnhold();
        setActive();
        // Resume media here
    }
}
