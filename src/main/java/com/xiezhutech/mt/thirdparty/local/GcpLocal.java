package com.xiezhutech.mt.thirdparty.local;

import com.wolf.framework.local.Local;

/**
 * 工程派api服务
 *
 * @author jianying9
 */
public interface GcpLocal extends Local {
    
    public GcpUserInfo getGcpUserInfo(String authCode);

}
