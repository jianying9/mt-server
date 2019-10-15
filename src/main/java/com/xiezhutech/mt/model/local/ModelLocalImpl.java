package com.xiezhutech.mt.model.local;

import com.wolf.framework.local.LocalServiceConfig;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@LocalServiceConfig
public class ModelLocalImpl implements ModelLocal {

    private final Map<String, ModelHandler> modelHandlerMap = new HashMap();

    @Override
    public void init() {
        modelHandlerMap.put(ModelTypes.GC_RZ_GY_GJ, new GcRzGuangyuanGjModelHandlerImpl());
        //
        modelHandlerMap.put(ModelTypes.GC_RZ_DL_GJ, new GcRzDaileiGjModelHandlerImpl());
        //
        modelHandlerMap.put(ModelTypes.GC_JXLJ, new GcJiexielianjieModelHandlerImpl());
        //
        modelHandlerMap.put(ModelTypes.GC_GJHJ, new GcGangjinghanjieModelHandlerImpl());
    }

    @Override
    public ModelHandler getModelHandler(String type) {
        return this.modelHandlerMap.get(type);
    }

}
