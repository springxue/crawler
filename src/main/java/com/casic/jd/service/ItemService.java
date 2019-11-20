package com.casic.jd.service;

import com.casic.jd.bean.Item;
import com.casic.jd.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class ItemService {
    @Autowired
    private ItemDao itemDao;
    @Transactional
    public void save(Item item){
        itemDao.save(item);
    }
    public void findAll(Item item){
        itemDao.findAll(item);
    }
}
