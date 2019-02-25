// IMyAidlInterface.aidl
package com.dolgikh.speedometer;
import com.dolgikh.speedometer.IMyAidlCallback;

interface IMyAidlInterface {

    oneway void subscribe(IMyAidlCallback callback);
}
