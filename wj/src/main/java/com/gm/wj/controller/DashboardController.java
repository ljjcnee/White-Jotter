package com.gm.wj.controller;

import com.gm.wj.entity.Book;
import com.gm.wj.entity.BorrowRecord;
import com.gm.wj.entity.Category;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.BookService;
import com.gm.wj.service.BorrowRecordService;
import com.gm.wj.service.CategoryService;
import com.gm.wj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DashboardController {

    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    BorrowRecordService borrowRecordService;
    @Autowired
    CategoryService categoryService;

    // 1. 顶部卡片
    @GetMapping("/api/admin/dashboard/status")
    public Result getStatus() {
        Map<String, Object> statusMap = new HashMap<>();
        long userCount = userService.count();
        long bookCount = bookService.count();
        long borrowCount = borrowRecordService.count(); // 历史总借阅

        // ↓↓↓ 修改：这里不再是 visitCount，而是真实的“当前在借”数量 ↓↓↓
        int currentBorrowCount = borrowRecordService.countCurrentBorrows();

        statusMap.put("userCount", userCount);
        statusMap.put("bookCount", bookCount);
        statusMap.put("borrowCount", borrowCount);
        // 我们继续沿用 visitCount 这个 key 发给前端，但值已经是“当前在借”了
        statusMap.put("visitCount", currentBorrowCount);

        return ResultFactory.buildSuccessResult(statusMap);
    }

    // 2. 饼图
    @GetMapping("/api/admin/dashboard/pie")
    public Result getPieChartData() {
        List<Map<String, Object>> pieData = new ArrayList<>();
        List<Category> categories = categoryService.list();
        for (Category category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            item.put("value", bookService.listByCategory(category.getId()).size());
            pieData.add(item);
        }
        return ResultFactory.buildSuccessResult(pieData);
    }

    // 3. 柱状图
    @GetMapping("/api/admin/dashboard/bar")
    public Result getBarChartData() {
        Map<String, Object> barData = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        List<BorrowRecord> allRecords = borrowRecordService.list();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String showDateStr = date.format(DateTimeFormatter.ofPattern("MM-dd"));
            dates.add(showDateStr);

            int count = 0;
            for (BorrowRecord record : allRecords) {
                if (record.getBorrowTime() != null && record.getBorrowTime().startsWith(dateStr)) {
                    count++;
                }
            }
            values.add(count);
        }
        barData.put("dates", dates);
        barData.put("values", values);
        return ResultFactory.buildSuccessResult(barData);
    }

    // 4. 折线图
    @GetMapping("/api/admin/dashboard/line")
    public Result getLineChartData() {
        Map<String, Object> lineData = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> borrowCounts = new ArrayList<>();
        List<Integer> returnCounts = new ArrayList<>();

        List<BorrowRecord> allRecords = borrowRecordService.list();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));

            int bCount = 0;
            int rCount = 0;

            for (BorrowRecord record : allRecords) {
                if (record.getBorrowTime() != null && record.getBorrowTime().startsWith(dateStr)) {
                    bCount++;
                }
                if (record.getReturnTime() != null && record.getReturnTime().startsWith(dateStr)) {
                    rCount++;
                }
            }
            borrowCounts.add(bCount);
            returnCounts.add(rCount);
        }

        lineData.put("dates", dates);
        lineData.put("actualData", borrowCounts);
        lineData.put("expectedData", returnCounts);
        return ResultFactory.buildSuccessResult(lineData);
    }

    // 5. 雷达图
    @GetMapping("/api/admin/dashboard/radar")
    public Result getRadarChartData() {
        List<Category> categories = categoryService.list();
        if (categories.size() > 6) {
            categories = categories.subList(0, 6);
        }

        List<Map<String, Object>> indicators = new ArrayList<>();
        List<Integer> stockList = new ArrayList<>();
        List<Integer> borrowList = new ArrayList<>();

        List<Book> allBooks = bookService.list();
        List<BorrowRecord> allRecords = borrowRecordService.list();

        for (Category category : categories) {
            Map<String, Object> indicator = new HashMap<>();
            indicator.put("name", category.getName());
            indicator.put("max", 20);
            indicators.add(indicator);

            int stock = 0;
            for (Book book : allBooks) {
                if (book.getCategory() != null && book.getCategory().getId() == category.getId()) {
                    stock++;
                }
            }
            stockList.add(stock);

            int borrows = 0;
            for (BorrowRecord record : allRecords) {
                if (record.getBook() != null &&
                        record.getBook().getCategory() != null &&
                        record.getBook().getCategory().getId() == category.getId()) {
                    borrows++;
                }
            }
            borrowList.add(borrows);
        }

        Map<String, Object> radarData = new HashMap<>();
        radarData.put("indicators", indicators);
        radarData.put("stock", stockList);
        radarData.put("borrows", borrowList);

        return ResultFactory.buildSuccessResult(radarData);
    }
}