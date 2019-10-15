package com.xiezhutech.mt.thirdparty.local;

/**
 *
 * @author jianying9
 */
public class GcpUserInfo {

    private final long userId;
    //
    private final String nickName;
    //
    private final String userName;
    //
    private final String mobile;
    //
    private final String headUrl;
    //

    public GcpUserInfo(long userId, String nickName, String userName, String mobile, String headUrl) {
        this.userId = userId;
        this.nickName = nickName;
        this.userName = userName;
        this.mobile = mobile;
        this.headUrl = headUrl;
    }

    public long getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getHeadUrl() {
        return headUrl;
    }

}
