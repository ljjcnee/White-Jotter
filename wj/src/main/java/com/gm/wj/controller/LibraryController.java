package com.gm.wj.controller;

import com.gm.wj.entity.Book;
import com.gm.wj.entity.User; // 1. 新增：导入User
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.BookService;
import com.gm.wj.service.BorrowRecordService;
import com.gm.wj.service.UserService; // 2. 新增：导入UserService
import com.gm.wj.util.StringUtils;
import org.apache.shiro.SecurityUtils; // 3. 新增：Shiro工具类
import org.apache.shiro.subject.Subject; // 4. 新增：Shiro主体
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;

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

    @Autowired
    BorrowRecordService borrowRecordService;

    // ↓↓↓ 5. 新增：注入 UserService 用来查用户信息 ↓↓↓
    @Autowired
    UserService userService;

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

    // ↓↓↓ 核心安全修改区域 ↓↓↓

    /**
     * 1. 借书接口 (已修复安全漏洞)
     */
    @PostMapping("/api/borrow")
    public Result borrowBook(@RequestBody Map<String, Integer> request) {
        // 1. 安全检查：获取当前登录的主体
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return ResultFactory.buildFailResult("请先登录");
        }

        // 2. 获取真实身份：从 Shiro 中拿用户名，再去数据库查 ID
        String username = subject.getPrincipal().toString();
        User user = userService.getByName(username);

        // 3. 使用真实的 uid，而不是前端传来的
        int uid = user.getId();
        int bid = request.get("bid");

        // 4. 执行借阅
        String res = borrowRecordService.borrow(uid, bid);

        if ("success".equals(res)) {
            return ResultFactory.buildSuccessResult("借阅成功");
        } else {
            return ResultFactory.buildFailResult(res);
        }
    }

    /**
     * 2. 查询我的书架接口 (建议也加上安全校验，不过目前先保持原样)
     */
    @GetMapping("/api/mybooks")
    public Result getMyBooks(@RequestParam("uid") int uid) {
        // 其实这里也应该用 SecurityUtils 获取 uid，防止偷看别人书架
        // 但为了不改动前端逻辑，暂时保留接收参数，但你可以考虑后续优化
        return ResultFactory.buildSuccessResult(borrowRecordService.getMyBooks(uid));
    }

    /**
     * 3. 还书接口
     */
    @PostMapping("/api/return")
    public Result returnBook(@RequestBody Map<String, Integer> request) {
        int id = request.get("id");
        borrowRecordService.returnBook(id);
        return ResultFactory.buildSuccessResult("还书成功");
    }
}