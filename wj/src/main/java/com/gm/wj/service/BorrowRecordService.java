package com.gm.wj.service;

import com.gm.wj.dao.BorrowRecordDAO;
import com.gm.wj.entity.Book;
import com.gm.wj.entity.BorrowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BorrowRecordService {
    @Autowired
    BorrowRecordDAO borrowRecordDAO;
    @Autowired
    BookService bookService;

    // 1. 借书
    public String borrow(int uid, int bid) {
        Book book = bookService.getById(bid);
        if (book.getNums() <= 0) {
            return "库存不足";
        }
        book.setNums(book.getNums() - 1);
        bookService.addOrUpdate(book);

        BorrowRecord record = new BorrowRecord();
        record.setUid(uid);
        record.setBook(book);
        record.setStatus(0);
        record.setBorrowTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        borrowRecordDAO.save(record);
        return "success";
    }

    // 2. 查我的书
    public List<BorrowRecord> getMyBooks(int uid) {
        return borrowRecordDAO.findAllByUidAndStatus(uid, 0);
    }

    // 3. 还书
    public void returnBook(int id) {
        BorrowRecord record = borrowRecordDAO.findById(id).orElse(null);
        if (record != null && record.getStatus() == 0) {
            record.setStatus(1);
            record.setReturnTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            borrowRecordDAO.save(record);

            Book book = record.getBook();
            book.setNums(book.getNums() + 1);
            bookService.addOrUpdate(book);
        }
    }

    // 4. 查询所有记录 (给后台柱状图用的)
    public List<BorrowRecord> list() {
        return borrowRecordDAO.findAll();
    }

    // ↓↓↓ 5. 补上这个缺失的方法 (给后台顶部数字用的) ↓↓↓
    public long count() {
        return borrowRecordDAO.count();
    }
}