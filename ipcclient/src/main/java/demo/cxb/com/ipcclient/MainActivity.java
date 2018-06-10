package demo.cxb.com.ipcclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import demo.cxb.com.ipcaidl.IUserCallback;
import demo.cxb.com.ipcaidl.IUserManager;
import demo.cxb.com.ipcaidl.User;


public class MainActivity extends AppCompatActivity {

    private Button mBtnBindService;
    private Button mBtnUnbindService;
    private Button mBtnGetUserInfo;
    private Button mBtnUpdateUserInfo;

    private IUserManager mUserManager;
    private IUserCallback mUserCallback = new IUserCallback.Stub() {
        @Override
        public void onUserInfoChanged(User user) throws RemoteException {
            Log.e("client", "onUserInfoChanged:user=" + user);
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("client", "服务已绑定");
            mUserManager = IUserManager.Stub.asInterface(service);
            if (mUserManager != null) {
                try {
                    mUserManager.registerCallback(mUserCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("client", "服务绑定已解除");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        mBtnBindService = findViewById(R.id.btn_bind_service);
        mBtnUnbindService = findViewById(R.id.btn_unbind_service);
        mBtnGetUserInfo = findViewById(R.id.btn_get_user_info);
        mBtnUpdateUserInfo = findViewById(R.id.btn_update_user_info);
    }


    private void initListener() {

        mBtnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindUserService();
            }
        });

        mBtnUnbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindUserService();
            }
        });

        mBtnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserManager != null) {
                    try {
                        User user = mUserManager.getUser();
                        Log.e("client", user.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBtnUpdateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserManager != null) {
                    User mUser = new User("changhong", 37, 0, false);
                    try {
                        mUserManager.updateUser(mUser);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void bindUserService() {
        Intent intent = new Intent("demo.cxb.com.ipcserver.UserService");
        intent.setPackage("demo.cxb.com.ipcserver");
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void unbindUserService() {
        unbindService(mServiceConnection);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindUserService();
    }
}
