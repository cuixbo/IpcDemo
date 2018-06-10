package demo.cxb.com.ipcdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mBtnBind;
    private Button mBtnUnbind;
    private IBookManager mRemoteBookManager;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "服务已绑定");
            mRemoteBookManager=IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager.registerListener(mOnNewBookArrivedListener);

                List<Book>  bookList=mRemoteBookManager.getBookList();
                Log.e("xbc","bookList:"+ Arrays.toString(bookList.toArray()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "服务已解绑");
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener=new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.e("xbc","onNewBookArrived:"+newBook.toString());
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
        mBtnBind = findViewById(R.id.btn_bind);
        mBtnUnbind = findViewById(R.id.btn_unbind);
    }


    private void initListener() {
        mBtnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BookManagerService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

        mBtnUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService();
            }
        });
    }

    void unbindService() {
        try {
            unbindService(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }
}
