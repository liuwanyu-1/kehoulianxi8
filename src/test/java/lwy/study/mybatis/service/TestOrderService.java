package lwy.study.mybatis.service;

import lwy.study.mybatis.pojo.Order;
import lwy.study.mybatis.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/** 订单业务层分页测试：手写 limit 版 + PageHelper 插件版 */
public class TestOrderService {

    private IOrderService orderService;

    @Before
    public void init() {
        orderService = new OrderServiceImpl();
    }

    /** 手写 limit 版：第 1 页每页 2 条 */
    @Test
    public void testSelectByPageManual() {
        List<Order> orders = orderService.selectByPageManual(1, 2);
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    /** PageHelper 插件版：第 1 页每页 2 条 */
    @Test
    public void testSelectByPage() {
        List<Order> orders = orderService.selectByPage(1, 2);
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}
