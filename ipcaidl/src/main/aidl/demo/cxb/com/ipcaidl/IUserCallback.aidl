// IUserCallback.aidl
package demo.cxb.com.ipcaidl;

// Declare any non-default types here with import statements
import demo.cxb.com.ipcaidl.User;

interface IUserCallback {

    void onUserInfoChanged(in User user);

}
