package com.ecommerce.dao;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.bean.UserBean;
import com.ecommerce.bean.UserData;
import com.ecommerce.doc.UserDocument;
import com.mongodb.client.result.DeleteResult;



@Repository
public class UserDAO implements DAOInterface {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public String addUser(UserBean userBean) {
        Query query = new Query(Criteria.where("userName").is(userBean.getUserName()));
        UserDocument existing = mongoTemplate.findOne(query, UserDocument.class);

        if (existing != null) {
            throw new RuntimeException("User already exists with username: " + userBean.getUserName());
        }

        UserDocument userDocument = new UserDocument();
        BeanUtils.copyProperties(userBean, userDocument);
        UserDocument addedDocument = mongoTemplate.save(userDocument);
        return addedDocument.getUserId();
    }
    
    @Override
    public UserData editUser(UserData userData) {
    	Query query=new Query(Criteria.where("userName").is(userData.getUserName()));
    	UserDocument existing=mongoTemplate.findOne(query, UserDocument.class);
    	modelMapper.map(userData,existing);
    	UserDocument doc=mongoTemplate.save(existing);
    	UserData edited=modelMapper.map(doc, UserData.class);
    	return edited;
    }

    @Override
    public Map<String,Object> findUser(String userName) {
        Query query = new Query(Criteria.where("userName").is(userName));
        UserDocument user = mongoTemplate.findOne(query, UserDocument.class);
        if(user==null) {
        	return null;
        }
        UserBean userBean=modelMapper.map(user, UserBean.class);
        UserData userData = modelMapper.map(user, UserData.class);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userBean",userBean);
        map.put("userData", userData);
        return map;
    }

    @Override
    public boolean deleteUser(String userName) {
        Query query = new Query(Criteria.where("userName").is(userName));
        DeleteResult result = mongoTemplate.remove(query, UserDocument.class);
        return result.getDeletedCount() > 0;
    }
}
