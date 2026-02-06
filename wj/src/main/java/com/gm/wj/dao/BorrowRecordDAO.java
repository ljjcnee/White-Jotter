package com.gm.wj.dao;

import com.gm.wj.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordDAO extends JpaRepository<BorrowRecord,Integer> {
    // 查某个用户的所有未还书籍
    List<BorrowRecord> findAllByUidAndStatus(int uid, int status);

    // ↓↓↓ 新增：统计特定状态的记录总数 (用于查当前在借总数 status=0) ↓↓↓
    int countByStatus(int status);
}