package com.miyin.zhenbaoqi.sql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "footprint")
public class FootprintEntity {

    @Id(autoincrement = true)
    private Long id;
    @Index
    @Property(nameInDb = "user_id")
    private int userId;
    @Property(nameInDb = "data")
    private String data;
    @Property(nameInDb = "browse_date")
    private String browseDate;
    @Property(nameInDb = "goods_id")
    private int goodsId;

    @Generated(hash = 838525337)
    public FootprintEntity(Long id, int userId, String data, String browseDate,
                           int goodsId) {
        this.id = id;
        this.userId = userId;
        this.data = data;
        this.browseDate = browseDate;
        this.goodsId = goodsId;
    }

    @Generated(hash = 1462549369)
    public FootprintEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBrowseDate() {
        return this.browseDate;
    }

    public void setBrowseDate(String browseDate) {
        this.browseDate = browseDate;
    }

    public int getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

}
