package demo.cxb.com.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();//支持并发

    final RemoteCallbackList<IOnNewBookArrivedListener> mCallbacks = new RemoteCallbackList<IOnNewBookArrivedListener>();

    Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mCallbacks.register(listener);
            int num = mCallbacks.beginBroadcast();
            mCallbacks.finishBroadcast();
            Log.e(TAG, "添加完成, 注册接口数: " + num);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mCallbacks.unregister(listener);
            int num = mCallbacks.beginBroadcast();
            mCallbacks.finishBroadcast();
            Log.e(TAG, "删除完成, 注册接口数:" + num);
        }
    };

    void newBookArrived() throws RemoteException {
        Book book = new Book(3, "c++");
        mBookList.add(book);
        Log.e(TAG, "发送通知的数量: " + mBookList.size());
        int num = mCallbacks.beginBroadcast();
        for (int i = 0; i < num; ++i) {
            IOnNewBookArrivedListener listener = mCallbacks.getBroadcastItem(i);
            Log.e(TAG, "发送通知: " + listener.toString());
            listener.onNewBookArrived(book);
        }
        mCallbacks.finishBroadcast();
    }

    public boolean running = true;
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (running) {
                    newBookArrived();
                    Thread.sleep(5000);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "java"));
        mBookList.add(new Book(2, "kotlin"));
        thread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        running = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
