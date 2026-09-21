package lwy.study.mybatis.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Session 工具类：
 * SqlSessionFactory 只需创建一次（重量级对象），用静态代码块初始化；
 * 对外提供 getSession() 方法获取 SqlSession，避免每个测试都重复写创建流程。
 */
public class SessionUtil {

    /** 私有静态成员：全局唯一的工厂对象 */
    private static SqlSessionFactory sqlSessionFactory;

    /** 静态代码块：类加载时执行一次，读取核心配置并初始化工厂 */
    static {
        String resource = "mybatis-config.xml";
        try {
            InputStream inputStream =
                    Resources.getResourceAsStream(resource);
            sqlSessionFactory =
                    new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 对外提供获取 SqlSession 的方法 */
    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }
}
