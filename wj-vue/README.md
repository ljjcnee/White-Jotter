# 基于智慧学习的图书馆管理系统 (White-Jotter)

## 📖 项目简介

本项目是**山东师范大学**毕业设计项目，题目为**《基于智慧学习的图书馆管理系统的设计与实现》**。

该系统是一个前后端分离的 Web 应用，旨在提供一个现代化的图书管理平台，并融合了“智慧学习”的理念（通过笔记/文章模块实现知识沉淀）。项目采用行业主流的 **Spring Boot + Vue** 架构，实现了图书管理、用户权限控制、数据可视化展示以及个人知识库（Jotter）等功能。

## 🛠️ 技术栈

### 后端 (Backend)
基于 **Spring Boot 2.1.1** 构建，提供 RESTful API 接口。
* **核心框架**: Spring Boot 2.1.1.RELEASE
* **安全框架**: Apache Shiro 1.4.1 (实现用户认证与细粒度权限控制)
* **持久层**: Spring Data JPA (ORM 框架)
* **数据库**: MySQL 8.0.16
* **缓存**: Redis (提升系统性能)
* **工具库**: Lombok, Hutool, Fastjson
* **日志**: Log4j2

### 前端 (Frontend)
基于 **Vue.js 2.x** 构建的单页面应用 (SPA)。
* **核心框架**: Vue.js 2.6.10, Vue Router, Vuex
* **UI 组件库**: Element-UI 2.12.0
* **HTTP 客户端**: Axios 0.18.0
* **图表可视化**: ECharts 4.2.1 (用于后台数据大屏展示)
* **Markdown 编辑器**: mavon-editor 2.6.17 (用于撰写学习笔记/文章)

## 📂 项目结构

```text
WhiteJotter-Project/
├── wj/                 # 后端项目 (Spring Boot)
│   ├── src/main/java   # Java 源代码
│   ├── src/main/resources
│   │   ├── application.properties # 配置文件
│   │   └── wj.sql      # 数据库初始化脚本
│   └── pom.xml         # Maven 依赖管理
├── wj-vue/             # 前端项目 (Vue)
│   ├── src/            # 前端源代码 (组件、路由、状态管理)
│   ├── static/         # 静态资源
│   └── package.json    # NPM 依赖管理
└── README.md           # 项目说明文档
```
🚀 快速开始
1. 环境准备
JDK: 1.8+

Node.js: 6.0+ (建议使用 LTS 版本)

MySQL: 5.7 或 8.0+

Redis: 3.0+

2. 数据库配置
在 MySQL 中创建一个名为 wj 的数据库。

导入后端项目中的 wj/src/main/resources/wj.sql 脚本以初始化表结构和数据。

确保您的数据库账号密码与配置文件一致（默认 root/123456）。

3. 后端启动 (wj)
进入 wj 目录。

修改 src/main/resources/application.properties (如果您的数据库或 Redis 密码不同):

Properties
spring.datasource.username=root
spring.datasource.password=123456
spring.redis.host=localhost
spring.redis.port=6379
运行 WjApplication.java 启动服务。

后端服务默认运行在端口 8443。

4. 前端启动 (wj-vue)
进入 wj-vue 目录。

安装依赖：

Bash
npm install
启动开发服务器：

Bash
npm run dev
浏览器访问 http://localhost:8080 (默认端口)。

✨ 主要功能模块
图书管理模块

图书的增删改查 (CRUD)。

按分类筛选图书。

图书搜索功能。

借阅/图书馆模块

展示馆藏书籍信息。

智慧学习/笔记模块 (Jotter)

集成 Markdown 编辑器，支持富文本写作。

文章管理与发布。

后台管理系统

数据看板: 使用 ECharts 展示借阅统计、用户活跃度等关键指标。

用户管理: 查看与管理注册用户信息。

内容管理: 管理图书信息、广告 Banner、文章内容等。

权限控制

基于 Shiro 实现的登录认证。

基于角色的访问控制 (RBAC)，区分管理员与普通用户权限。

📸 系统截图
(在此处添加你的项目截图，例如首页、后台管理页等，这对于毕设演示非常重要)

📄 许可证
MIT


***

### 📝 补充建议（针对毕业设计）：

1.  **关于“智慧学习”**：我在功能模块里特别提到了 **Jotter (笔记模块)** 和 **Mavon Editor**，这是你项目中体现“学习/知识沉淀”的关键点，答辩时可以重点介绍这个模块是“智慧学习”的基础。
2.  **截图占位符**：文档里留了 `📸 系统截图` 的位置，建议你截几张图（比如登录页、图书展示页、ECharts 后台大屏）放进项目里（可以新建一个 `screenshots` 文件夹），然后在 Markdown 里引用，这样导师看 GitHub 时第一印象会非常好。
3.  **数据库文件**：我在“快速开始”里提到了 `wj.sql`，请确保你上传代码时，`wj/src/main/resources/` 目录下确实
