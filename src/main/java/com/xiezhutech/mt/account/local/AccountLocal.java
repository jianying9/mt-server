package com.xiezhutech.mt.account.local;

import com.wolf.framework.local.Local;
import com.xiezhutech.mt.account.entity.AccountEntity;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.account.entity.TokenEntity;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public interface AccountLocal extends Local {

    public void addAccount(Map<String, Object> dataMap);
    
    public AccountEntity queryAccount(String account);

    public String addMobileSession(long userId);
    
    public String addOtherSession(long userId);
    
    public SessionEntity querySession(String sid);
    
    public void updateSessionLastTime(String sid, long lastTime);
    
    public TokenEntity addToken();
    
    public TokenEntity queryToken(String token);
    
    public void updateToken(String token, long userId);

}
