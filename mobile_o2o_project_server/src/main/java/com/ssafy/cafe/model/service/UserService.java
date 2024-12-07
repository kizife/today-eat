package com.ssafy.cafe.model.service;

import com.ssafy.cafe.model.dto.User;


public interface UserService {
    /**
     * 사용자 정보를 DB에 저장한다.
     * 
     * @param user
     */
    public int join(User user);

    /**
     * id, pass에 해당하는 User 정보를 반환한다.
     * 
     * @param id
     * @param pass
     * @return
     * 조회된 User 정보를 반환한다.
     */
    public User login(String id, String pass);
    
    
    /**
     * 해당 아이디가 이미 사용 중인지를 반환한다.
     * @param id
     * @return
     */
    public boolean isUsedId(String id);

    /**
     * id 에 해당하는 User 정보를 반환한다.
     * 
     * @param id
     * @return
     * 조회된 User 정보를 반환한다.
     */
    public User selectUser(String id);
    
    
    /**
     * 사용자 정보를 업데이트한다.
     * 
     * @param user
     * @return
     */
    public int updateUser(User user);
    
    
}
