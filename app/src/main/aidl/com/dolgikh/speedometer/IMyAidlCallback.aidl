// IMyAidlCallback.aidl
package com.dolgikh.speedometer;

interface IMyAidlCallback {

    oneway void handleSpeed(float speed);
}
