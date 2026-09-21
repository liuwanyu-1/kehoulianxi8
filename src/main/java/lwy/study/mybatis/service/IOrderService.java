package lwy.study.mybatis.service;

import lwy.study.mybatis.pojo.Order;

import java.util.List;

/**
 * 订单业务层接口：按页码分页查询
 */
public interface IOrderService {

    /** PageHelper 插件版分页：startPage 后查全部，插件自动拼 limit */
    List<Order> selectByPage(int pageNum, int pageSize);

    /** 手写 limit 版分页：Service 层算 pageStart=(pageNum-1)*pageSize 再调 DAO */
    List<Order> selectByPageManual(int pageNum, int pageSize);
}
