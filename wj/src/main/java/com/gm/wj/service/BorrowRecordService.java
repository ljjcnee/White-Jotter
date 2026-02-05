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

    // 1. 借书业务
    public String borrow(int uid, int bid) {
        Book book = bookService.getById(bid);
        if (book.getNums() <= 0) {
            return "库存不足";
        }

        // 扣减库存
        book.setNums(book.getNums() - 1);
        bookService.addOrUpdate(book);

        // 生成记录
        BorrowRecord record = new BorrowRecord();
        record.setUid(uid);
        record.setBook(book);
        record.setStatus(0); // 0表示未还
        record.setBorrowTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        borrowRecordDAO.save(record);

        return "success";
    }

    // 2. 查询我的书架（只查未还的）
    public List<BorrowRecord> getMyBooks(int uid) {
        return borrowRecordDAO.findAllByUidAndStatus(uid, 0);
    }

    // 3. 还书业务
    public void returnBook(int id) { // 这里的 id 是借阅记录的 ID
        BorrowRecord record = borrowRecordDAO.findById(id).orElse(null);
        if (record != null && record.getStatus() == 0) {
            // 状态改为已还
            record.setStatus(1);
            record.setReturnTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            borrowRecordDAO.save(record);

            // 库存加回 1
            Book book = record.getBook();
            book.setNums(book.getNums() + 1);
            bookService.addOrUpdate(book);
        }
    }
}