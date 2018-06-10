// IBookManager.aidl
package demo.cxb.com.ipcdemo;

// Declare any non-default types here with import statements
import demo.cxb.com.ipcdemo.Book;
import demo.cxb.com.ipcdemo.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList(); // 返回书籍列表
    void addBook(in Book book); // 添加书籍
    void registerListener(IOnNewBookArrivedListener listener); // 注册接口
    void unregisterListener(IOnNewBookArrivedListener listener); // 注册接口
}
