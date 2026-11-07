# 开发工具与版本说明

## 运行环境

| 工具 | 版本 | 用途 |
| --- | --- | --- |
| JDK | 17.0.8 | 后端编译与运行 |
| Spring Boot | 4.0.0 | 后端框架 |
| Maven Wrapper | 3.9.11 | 后端构建 |
| MySQL | 8.4.0 | 数据库 |
| Node.js | 25.2.1（要求 ≥ 22） | 前端构建 |
| npm | 11.6.2 | 前端依赖管理 |

## 开发工具

| 工具 | 版本 / 说明 |
| --- | --- |
| IntelliJ IDEA | 2024 |
| Visual Studio Code | 前端开发 |
| Navicat Premium | 16 / 17（本地数据库管理） |
| Postman | 接口调试 |
| Git | 2.56 |

## 主要依赖

### 后端

- MyBatis-Plus 3.5.15
- Druid 1.2.23
- Lombok 1.18.36
- Hutool 5.8.28
- MySQL Connector/J 8.4.0

### 前端

- Vue 3.5.24
- Vite 7.2.5（rolldown-vite）
- Element Plus 2.13.0
- Axios 1.13.2
- Vue Router 4.0.13
- date-fns 4.1.0

## 教学视频

本地课程视频共 47 个文件、约 4.49GB，覆盖：

- day01：JDK、IDEA、Maven、MySQL、Navicat 环境搭建
- day02：需求分析、数据字典、ER 图
- day03：MyBatis-Plus 持久层
- day04：商品模块
- day05：分页
- day06：Vue 3 + Vite 环境
- day07–day08：路由、登录、前后端联调
- day09–day11：主页面布局、文件上传、商品 CRUD

> 该套视频为本地学习资料，文件体积远超 GitHub 普通仓库单文件 100MB 的限制，且版权归原作者所有。主仓库不直接分发视频文件；如需公开分享，建议使用自有录制版本，或上传到 B 站/网盘后在仓库中放目录和链接。

## 说明

- 论文写作时部分章节按 Spring Boot 3.x 表述，代码以 `pom.xml` 中的 Spring Boot 4.0.0 为准。
- 项目为本科毕业设计，未接入生产级支付、短信、对象存储等服务。
