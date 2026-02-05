package com.gm.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;

@Entity
@Table(name = "borrow_record")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "uid")
    private int uid; // 用户ID

    // 这里重点：直接关联 Book 表，方便前端显示书名和封面
    @ManyToOne
    @JoinColumn(name = "bid")
    private Book book;

    @Column(name = "borrow_time")
    private String borrowTime;

    @Column(name = "return_time")
    private String returnTime;

    @Column(name = "status")
    private int status; // 0-借出中，1-已归还

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUid() { return uid; }
    public void setUid(int uid) { this.uid = uid; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public String getBorrowTime() { return borrowTime; }
    public void setBorrowTime(String borrowTime) { this.borrowTime = borrowTime; }
    public String getReturnTime() { return returnTime; }
    public void setReturnTime(String returnTime) { this.returnTime = returnTime; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}