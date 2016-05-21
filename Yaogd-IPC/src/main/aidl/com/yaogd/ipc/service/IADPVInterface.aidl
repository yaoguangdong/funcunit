// IADPVInterface.aidl
package com.yaogd.ipc.service;

// Declare any non-default types here with import statements
import com.yaogd.ipc.service.ADPVEntity;

interface IADPVInterface {

    void save(in ADPVEntity[] pvEntity);
    void push();
    void thirdpush(in String[] urls);

}
