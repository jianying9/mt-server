package com.xiezhutech.mt.user.local;

import com.wolf.framework.local.Local;
import com.xiezhutech.mt.user.entity.UserEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public interface UserLocal extends Local {

    public UserEntity queryUser(long userId);

    public UserEntity addCustomerUser(Map<String, Object> dataMap);
    
    public UserEntity addUser(Map<String, Object> dataMap);

    public void updateUser(Map<String, Object> dataMap);

    public List<UserEntity> searchUser(boolean customer, int size);

    public void updateUserMobileSid(long userId, String sid);

    public void updateUserOtherSid(long userId, String sid);
    
    public UserEntity queryUser(long userId, Map<Long, UserEntity> userEntityMap);
    
    public Map<String, Object> getUserSimpleInfoMap(UserEntity userEntity);

}
