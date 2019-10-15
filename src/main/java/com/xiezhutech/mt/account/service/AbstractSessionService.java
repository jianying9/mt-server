package com.xiezhutech.mt.account.service;

import com.wolf.framework.local.InjectLocalService;
import com.wolf.framework.reponse.Response;
import com.wolf.framework.request.Request;
import com.wolf.framework.service.Service;
import com.xiezhutech.mt.account.entity.SessionEntity;
import com.xiezhutech.mt.account.local.AccountLocal;

/**
 *
 * @author jianying9
 */
public abstract class AbstractSessionService implements Service {

    @InjectLocalService
    protected AccountLocal accountLocal;

    protected abstract void sessionExecute(Request request, Response response, SessionEntity sessionEntity);

    @Override
    public final void execute(Request request, Response response) {
        String sid = request.getSessionId();
        SessionEntity sessionEntity = this.accountLocal.querySession(sid);
        if (sessionEntity != null && sessionEntity.getUserId() > 0) {
            this.sessionExecute(request, response, sessionEntity);
        } else {
            response.unlogin();
        }
    }
}
