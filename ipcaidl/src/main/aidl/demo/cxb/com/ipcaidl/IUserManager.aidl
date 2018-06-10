// IUserManager.aidl
package demo.cxb.com.ipcaidl;

// Declare any non-default types here with import statements
import demo.cxb.com.ipcaidl.User;
import demo.cxb.com.ipcaidl.IUserCallback;

interface IUserManager {

    User getUser();

    void updateUser(in User user);

    void registerCallback(IUserCallback callback);

}
