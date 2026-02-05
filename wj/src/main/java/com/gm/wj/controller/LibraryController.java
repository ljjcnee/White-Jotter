package com.gm.wj.controller;

import com.gm.wj.entity.Book;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.BookService;
import com.gm.wj.service.BorrowRecordService; // 1. 新增导入
import com.gm.wj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map; // 2. 新增导入，用于接收 {uid, bid} 这种数据

/**
 * Library controller.
 *
 * @author Evan
 * @date 2019/4
 */
@RestController
public class LibraryController {
    @Autowired
    BookService bookService;

    // ↓↓↓ 3. 新增：注入借阅记录服务 ↓↓↓
    @Autowired
    BorrowRecordService borrowRecordService;

    @GetMapping("/api/books")
    public Result listBooks() {
        return ResultFactory.buildSuccessResult(bookService.list());
    }

    @PostMapping("/api/admin/content/books")
    public Result addOrUpdateBooks(@RequestBody @Valid Book book) {
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessResult("修改成功");
    }

    @PostMapping("/api/admin/content/books/delete")
    public Result deleteBook(@RequestBody @Valid Book book) {
        bookService.deleteById(book.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @GetMapping("/api/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        if ("".equals(keywords)) {
            return ResultFactory.buildSuccessResult(bookService.list());
        } else {
            return ResultFactory.buildSuccessResult(bookService.Search(keywords));
        }
    }

    @GetMapping("/api/categories/{cid}/books")
    public Result listByCategory(@PathVariable("cid") int cid) {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(bookService.listByCategory(cid));
        } else {
            return ResultFactory.buildSuccessResult(bookService.list());
        }
    }

    @PostMapping("/api/admin/content/books/covers")
    public String coversUpload(MultipartFile file) {
        String folder = "D:/workspace/img";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // ↓↓↓ 核心修改区域：替换了原来的 borrowBook，并新增了 query 和 return ↓↓↓

    /**
     * 1. 借书接口
     * 接收参数：{ "uid": 1, "bid": 15 }
     */
    @PostMapping("/api/borrow")
    public Result borrowBook(@RequestBody Map<String, Integer> request) {
        // 从请求体中获取用户ID和书籍ID
        int uid = request.get("uid");
        int bid = request.get("bid");

        // 调用 Service 进行业务处理（扣库存 + 记账）
        String res = borrowRecordService.borrow(uid, bid);

        if ("success".equals(res)) {
            return ResultFactory.buildSuccessResult("借阅成功");
        } else {
            return ResultFactory.buildFailResult(res); // 比如库存不足
        }
    }

    /**
     * 2. 查询我的书架接口
     * 接收参数：URL?uid=1
     */
    @GetMapping("/api/mybooks")
    public Result getMyBooks(@RequestParam("uid") int uid) {
        return ResultFactory.buildSuccessResult(borrowRecordService.getMyBooks(uid));
    }

    /**
     * 3. 还书接口
     * 接收参数：{ "id": 记录ID }
     */
    @PostMapping("/api/return")
    public Result returnBook(@RequestBody Map<String, Integer> request) {
        int id = request.get("id"); // 这里传的是借阅记录的 ID，不是书的 ID
        borrowRecordService.returnBook(id);
        return ResultFactory.buildSuccessResult("还书成功");
    }
}