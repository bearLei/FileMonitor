// IMonitorAidl.aidl
package com.example.junkanalyse;

// Declare any non-default types here with import statements

interface IMonitorAidl {
   void sendData(int cmd,String path);
}