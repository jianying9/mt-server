package com.xiezhutech.mt.model.local;

import com.wolf.framework.local.Local;

/**
 *
 * @author jianying9
 */
public interface ModelLocal extends Local {

    public ModelHandler getModelHandler(String type);

}
