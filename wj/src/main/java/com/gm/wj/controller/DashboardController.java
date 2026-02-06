package com.gm.wj.controller;

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

    /**
     * 接口1：顶部卡片统计数据
     */
    @GetMapping("/api/admin/dashboard/status")
    public Result getStatus() {
        Map<String, Object> statusMap = new HashMap<>();

        // 获取数据库真实记录数
        long userCount = userService.count();
        long bookCount = bookService.count();
        long borrowCount = borrowRecordService.count();
        // 模拟一个访问量 (基础值 + 用户数 * 5)
        long visitCount = 1200 + userCount * 5;

        statusMap.put("userCount", userCount);
        statusMap.put("bookCount", bookCount);
        statusMap.put("borrowCount", borrowCount);
        statusMap.put("visitCount", visitCount);

        return ResultFactory.buildSuccessResult(statusMap);
    }

    /**
     * 接口2：饼图数据 (各分类下的图书数量)
     */
    @GetMapping("/api/admin/dashboard/pie")
    public Result getPieChartData() {
        List<Map<String, Object>> pieData = new ArrayList<>();
        List<Category> categories = categoryService.list();

        // 遍历所有分类
        for (Category category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());
            // 查询该分类下的书本数量
            item.put("value", bookService.listByCategory(category.getId()).size());
            pieData.add(item);
        }
        return ResultFactory.buildSuccessResult(pieData);
    }

    /**
     * 接口3：柱状图数据 (最近7天真实借阅量)
     */
    @GetMapping("/api/admin/dashboard/bar")
    public Result getBarChartData() {
        Map<String, Object> barData = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        // 1. 获取所有借阅记录
        List<BorrowRecord> allRecords = borrowRecordService.list();

        // 2. 循环最近 7 天
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            // 格式化日期字符串，用于跟数据库记录比对 (例如 "2026-02-06")
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 格式化显示用的日期 (例如 "02-06")
            String showDateStr = date.format(DateTimeFormatter.ofPattern("MM-dd"));

            dates.add(showDateStr);

            // 3. 统计这一天的借书数量
            int count = 0;
            for (BorrowRecord record : allRecords) {
                // 如果借阅时间不为空，且以今天的日期开头
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
}