package lwy.study.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import lwy.study.mybatis.dao.OrderDao;
import lwy.study.mybatis.pojo.Order;
import lwy.study.mybatis.service.IOrderService;
import lwy.study.mybatis.util.SessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 订单业务层实现：Controller 调 Service，Service 调 DAO
 */
public class OrderServiceImpl implements IOrderService {

    SqlSession session;
    OrderDao orderDao;

    public OrderServiceImpl() {
        this.session = SessionUtil.getSession();
        this.orderDao = session.getMapper(OrderDao.class);
    }

    @Override
    public List<Order> selectByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderDao.selectAllSimple();
        return orders;
    }

    /** 手写 limit 版：pageStart=(pageNum-1)*pageSize，公式必须在 Service 层算 */
    @Override
    public List<Order> selectByPageManual(int pageNum, int pageSize) {
        Integer pageStart = (pageNum - 1) * pageSize;
        return orderDao.selectByPage(pageStart, pageSize);
    }
}
