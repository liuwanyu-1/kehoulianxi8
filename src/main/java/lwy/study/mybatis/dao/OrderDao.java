package lwy.study.mybatis.dao;

import lwy.study.mybatis.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * orders 外卖订单表的 DAO 接口（注解实现）
 */
public interface OrderDao {

    /** 手写 limit 分页（注解）：SQL 直接写 limit，Service 层算 pageStart 传进来 */
    @Select("select * from orders limit #{pageStart}, #{pageSize}")
    List<Order> selectByPage(@Param("pageStart") Integer pageStart,
                             @Param("pageSize") Integer pageSize);

    /** 查询全部订单（注解版）：给 PageHelper 拦截后自动拼 limit 用 */
    @Select("select * from orders")
    List<Order> selectAllSimple();
}
