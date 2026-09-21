package lwy.study.mybatis.pojo;

/**
 * 实体类：对应 design 库里的 orders 外卖订单表
 * oid（订单编号）、ono（订单号）、ogoods（所点商品）、
 * oprice（订单金额）、mid（店铺编号）
 */
public class Order {
    private Integer oid;
    private String ono;
    private String ogoods;
    private Integer oprice;
    private Integer mid;

    public Order() {
    }

    public Order(Integer oid, String ono, String ogoods, Integer oprice, Integer mid) {
        this.oid = oid;
        this.ono = ono;
        this.ogoods = ogoods;
        this.oprice = oprice;
        this.mid = mid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getOno() {
        return ono;
    }

    public void setOno(String ono) {
        this.ono = ono;
    }

    public String getOgoods() {
        return ogoods;
    }

    public void setOgoods(String ogoods) {
        this.ogoods = ogoods;
    }

    public Integer getOprice() {
        return oprice;
    }

    public void setOprice(Integer oprice) {
        this.oprice = oprice;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid=" + oid +
                ", ono='" + ono + '\'' +
                ", ogoods='" + ogoods + '\'' +
                ", oprice=" + oprice +
                ", mid=" + mid +
                '}';
    }
}
