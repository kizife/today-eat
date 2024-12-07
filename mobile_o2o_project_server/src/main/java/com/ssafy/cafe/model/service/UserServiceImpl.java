package com.ssafy.cafe.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.User;

/**
 * @since 2021. 6. 23.
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;

    @Override
    public int join(User user) {
        return userDao.insert(user);

    }

    @Override
    public User login(String id, String pass) {
        User user = userDao.selectById(id);
        if (user != null && user.getPass().equals(pass)) {
            return user;
        } else {
            return null;
        }
    }


    @Override
    public boolean isUsedId(String id) {
        return userDao.selectById(id) != null;
    }
    
    @Override
    public User selectUser(String id) {
        User user = userDao.selectById(id);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

	@Override
	public int updateUser(User user) {
		return userDao.update(user);
	}
    
}

