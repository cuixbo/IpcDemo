// IOnNewBookArrivedListener.aidl
package demo.cxb.com.ipcdemo;

// Declare any non-default types here with import statements
import demo.cxb.com.ipcdemo.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
