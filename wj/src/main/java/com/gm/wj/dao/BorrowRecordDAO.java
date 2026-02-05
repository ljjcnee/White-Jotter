package com.gm.wj.dao;

import com.gm.wj.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordDAO extends JpaRepository<BorrowRecord, Integer> {
    // 查询某个用户所有未归还的书籍 (status=0)
    List<BorrowRecord> findAllByUidAndStatus(int uid, int status);
}