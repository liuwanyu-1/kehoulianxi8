package lwy.study.mybatis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lwy.study.mybatis.pojo.Order;
import lwy.study.mybatis.service.IOrderService;
import lwy.study.mybatis.service.impl.OrderServiceImpl;

import java.io.IOException;
import java.util.List;

/** 订单分页控制器：接收 pageNum/pageSize，调 Service，转发 JSP */
@WebServlet("/queryOrders")
public class QueryOrdersServlet extends HttpServlet {

    private IOrderService orderService;

    public QueryOrdersServlet() {
        orderService = new OrderServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNumStr = req.getParameter("pageNum");
        String pageSizeStr = req.getParameter("pageSize");
        int pageNum = 1;
        int pageSize = 5;
        if (pageNumStr != null && !pageNumStr.equals("")) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (pageSizeStr != null && !pageSizeStr.equals("")) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        List<Order> orders = orderService.selectByPage(pageNum, pageSize);
        req.setAttribute("orders", orders);
        req.setAttribute("pageNum", pageNum);
        req.setAttribute("pageSize", pageSize);
        req.getRequestDispatcher("/ordersList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
