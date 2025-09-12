package com.jotish.audiomanager;

import android.app.Service;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;

public class MyConnectionService extends ConnectionService {

    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount,
                                                 ConnectionRequest request) {
        MyConnection connection = new MyConnection();
        connection.setInitializing();
        connection.setActive(); // Or setRinging if appropriate
        return connection;
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount,
                                                 ConnectionRequest request) {
        MyConnection connection = new MyConnection();
        connection.setInitializing();
        connection.setDialing();
        return connection;
    }
}
