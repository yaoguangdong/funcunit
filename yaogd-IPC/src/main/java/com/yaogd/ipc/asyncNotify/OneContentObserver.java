package com.yaogd.ipc.asyncNotify;

import android.database.ContentObserver;
import android.os.Handler;

import com.yaogd.lib.A;

public class OneContentObserver extends ContentObserver {
		
    public OneContentObserver() {
        super(new Handler());
    }

    @Override
    public void onChange(final boolean selfChange) {
        A.i("db changed");
    }
    
}