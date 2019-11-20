package com.casic.jd.dao;

import com.casic.jd.bean.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemDao {
    /**
     * 保存商品
     * @param item
     */

    public void save(Item item);

    /**
     * 根据查询条件查询商品
     * @param item
     * @return
     */
    public List<Item> findAll(Item item);
}
