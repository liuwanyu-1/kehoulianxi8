# 课后练习8 —— 自选表（orders 外卖订单表）分页与 PageHelper

> 期末复习向整理：题目要求 → 分页原理 → 两条实现链路 → 怎么跑 → 踩过的坑。
> 自选表：`design` 库的 `orders` 外卖订单表（oid 订单编号 / ono 订单号 / ogoods 商品 / oprice 金额 / mid 店铺编号）。

---

## 1. 题目要求

1. 用**基础的分页查询语句**（limit）对自选表分页，DAO 用**注解**实现查询
2. 编写**业务层**及相应方法实现分页业务（**按页码查询**）
3. 用 **PageHelper** 实现相同功能
4. 用 **Servlet + JSP** 实现页面分页显示

---

## 2. 分页原理（必考）

### 2.1 MySQL limit 语法

```sql
select * from 表名 limit 起始下标, 每页条数
```

- **下标从 0 开始**（类比 JDBC 的 ResultSet，指针默认在第一条之前，`rs.next()` 才到第一条）
- 4 条记录每页 2 条：第 1 页 `limit 0,2`，第 2 页 `limit 2,2`——写成 `limit 1,2` 会丢第一条数据

### 2.2 两个公式（写在 Service 层，不写前端不写 Controller）

```
pageStart = (pageNumber - 1) * pageSize      -- limit 的起始下标
totalPage = (totalRecord + pageSize - 1) / pageSize   -- 总页数（整除不加，不整除进一）
```

前端传 `pageNumber`（从 1 开始）和 `pageSize`；`totalRecord` 靠 DAO 的 `select count(*)` 查。

### 2.3 两种实现对比

| | 手写 limit | PageHelper 插件 |
|---|---|---|
| SQL | 自己写 `limit #{pageStart},#{pageSize}` | 写普通 `select * from 表`，插件自动改写加 limit |
| 下标换算 | Service 层自己算 | 插件内部算 |
| count 语句 | 手写 | 插件自动发 `SELECT count(0) ...` |
| 底层 | MySQL limit | **还是 MySQL limit**，靠 MyBatis 拦截器改 SQL |
| 考试 | **必考原理** | 工作常用 |

---

## 3. 项目结构（请求链路）

```
浏览器 → Servlet(queryOrders) → Service(OrderServiceImpl) → DAO(OrderDao 注解) → MySQL
       ← JSP(ordersList.jsp) 渲染表格 + 上一页/下一页链接
```

```
src/main/java/lwy/study/mybatis/
├── dao/OrderDao.java              注解：selectByPage + selectAllSimple
├── pojo/Order.java                oid/ono/ogoods/oprice/mid
├── service/IOrderService.java     接口：两个分页方法
├── service/impl/OrderServiceImpl  实现：PageHelper 版 + 手写版
├── servlet/QueryOrdersServlet     /queryOrders，pageNum 默认 1、pageSize 默认 5
└── util/SessionUtil.java          SqlSession 工具
src/main/resources/mybatis-config.xml   PageInterceptor + 包扫描注册
src/main/webapp/ordersList.jsp          脚本片段渲染表格 + 翻页链接
```

---

## 4. 两条链路的代码

### 4.1 DAO（注解实现）

```java
/** 手写 limit 分页（注解）：SQL 直接写 limit，Service 层算 pageStart 传进来 */
@Select("select * from orders limit #{pageStart}, #{pageSize}")
List<Order> selectByPage(@Param("pageStart") Integer pageStart,
                         @Param("pageSize") Integer pageSize);

/** 查询全部订单（注解版）：给 PageHelper 拦截后自动拼 limit 用 */
@Select("select * from orders")
List<Order> selectAllSimple();
```

> 多个 Integer 参数**必须加 @Param**，否则 `#{pageStart}` 找不到参数（MyBatis 默认只认 param1/arg0）。

### 4.2 Service（业务层）

```java
@Override
public List<Order> selectByPage(int pageNum, int pageSize) {
    PageHelper.startPage(pageNum, pageSize);   // 必须写在查询的上一行，中间不能插别的 SQL
    List<Order> orders = orderDao.selectAllSimple();
    return orders;
}

@Override
public List<Order> selectByPageManual(int pageNum, int pageSize) {
    Integer pageStart = (pageNum - 1) * pageSize;   // 公式在 Service 层算
    return orderDao.selectByPage(pageStart, pageSize);
}
```

### 4.3 Servlet（控制层）

- `req.getParameter("pageNum"/"pageSize")`，为空给默认值（第 1 页、每页 5 条）
- 查询结果存 **request 域**（分页数据属于单次请求，不能用 session）
- **请求转发**跳 JSP（重定向 request 域数据会丢）

### 4.4 JSP（页面）

- 脚本片段 `<% %>` 循环渲染表格
- 翻页链接用 `request.getContextPath()` 拼项目路径，不带会 404：

```jsp
<a href="<%= request.getContextPath() %>/queryOrders?pageNum=<%= pageNum - 1 %>&pageSize=<%= pageSize %>">上一页</a>
```

---

## 5. 怎么跑

1. MySQL（phpStudy）启动，`design` 库有 orders 数据
2. IDEA 打开项目（Open 选 pom.xml），等 Maven 同步完
3. Run → Edit Configurations → Tomcat Server → Local → Deployment 选 `kehoulianxi8:war exploded`，Application context 填 `/kehoulianxi8`
4. 访问：`http://localhost:8080/kehoulianxi8/queryOrders?pageNum=1&pageSize=2`
5. 单元测试：`TestOrderService` 两个方法分别对应手写版和插件版（运行配置 VM options 加 `-Dfile.encoding=UTF-8` 防中文乱码）

---

## 6. 踩坑记录（都是真踩过的）

| # | 坑 | 解法 |
|---|---|---|
| 1 | 多 Integer 参数不加 @Param，`#{pageStart}` 报 Parameter not found | 每个参数加 `@Param("xxx")` |
| 2 | 控制台/测试中文乱码 | JVM 参数 `-Dfile.encoding=UTF-8`；pom 加 `project.build.sourceEncoding=UTF-8` |
| 3 | XML Mapper 删掉换成注解后绑定失败 | mybatis-config 的 `<mapper resource>` 换成 `<package name="...dao"/>` 包扫描 |
| 4 | plugins 插错位置启动报 must match | `<plugins>` 必须在 typeAliases 之后、environments 之前（DTD 顺序） |
| 5 | web.xml 还是 2.3 老头导致 EL 不求值 | 换 `web-app_6_0.xsd`（本项目用脚本片段不受影响，但配置保持 6.0） |
| 6 | IDEA 部署后根路径 404 | 根路径没有欢迎页，访问 `/queryOrders`，或 index.jsp 里 `sendRedirect` 跳转 |
| 7 | PageHelper.startPage 后插入其他查询 | startPage 和目标查询之间不能有任何别的 SQL，否则 limit 拼错地方 |

---

## 7. 期末复习自问自答

- **limit 两个参数什么含义？下标从几开始？** → 起始下标 + 每页条数；从 0 开始
- **pageStart 怎么算？写在哪一层？** → `(pageNumber-1)*pageSize`，Service 层
- **PageHelper 底层是什么？** → 还是 limit，MyBatis 拦截器拦截 SQL 自动改写（先 count 再拼 limit）
- **startPage 写在哪？** → 紧挨着查询方法的上一行
- **分页数据存 request 还是 session？为什么？** → request，单次请求数据
- **转发和重定向区别？** → 转发一次请求域数据不丢；重定向两次请求域数据丢
- **为什么手写版还要会？** → 考试考原理；PageHelper 只是简化，底层一模一样
