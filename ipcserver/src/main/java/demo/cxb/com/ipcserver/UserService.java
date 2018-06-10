package demo.cxb.com.ipcserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import demo.cxb.com.ipcaidl.IUserCallback;
import demo.cxb.com.ipcaidl.IUserManager;
import demo.cxb.com.ipcaidl.User;

public class UserService extends Service {
    private User mUser = new User("wangdali", 28, 1, true);
    private RemoteCallbackList<IUserCallback> mRemoteCallbackList = new RemoteCallbackList<>();

    Binder mBinder = new IUserManager.Stub() {
        @Override
        public User getUser() throws RemoteException {
            return mUser;
        }

        @Override
        public void updateUser(User user) throws RemoteException {
            Log.e("server", "updateUser:user="+user);
            mUser = user;
            updateUserInfo();
        }

        @Override
        public void registerCallback(IUserCallback callback) throws RemoteException {
            mRemoteCallbackList.register(callback);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    void updateUserInfo() throws RemoteException {
//        mUser.age++;
        int num = mRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < num; i++) {
            mRemoteCallbackList.getBroadcastItem(i).onUserInfoChanged(mUser);
        }
        mRemoteCallbackList.finishBroadcast();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
