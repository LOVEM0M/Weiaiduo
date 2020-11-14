package com.miyin.zhenbaoqi.sql;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "auction_goods_draft")
public class AuctionGoodsDraftEntity {

    @Id(autoincrement = true)
    private Long id;
    @Index
    @Property(nameInDb = "user_id")
    private int userId;
    @Property(nameInDb = "data")
    private String data;

    @Generated(hash = 636626173)
    public AuctionGoodsDraftEntity(Long id, int userId, String data) {
        this.id = id;
        this.userId = userId;
        this.data = data;
    }

    @Generated(hash = 1037100036)
    public AuctionGoodsDraftEntity() {
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

}
