package com.xiezhutech.mt.sequence.local;

import com.wolf.framework.dao.elasticsearch.EsEntityDao;
import com.wolf.framework.dao.elasticsearch.annotation.InjectEsEntityDao;
import com.wolf.framework.local.LocalServiceConfig;
import com.xiezhutech.mt.sequence.entity.SequenceEntity;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jianying9
 */
@LocalServiceConfig
public class SequenceLocalImpl implements SequenceLocal {

    @InjectEsEntityDao
    private EsEntityDao<SequenceEntity> sequenceEntityDao;

    @Override
    public void init() {
    }

    @Override
    public synchronized long nextValue(String name) {
        long value;
        SequenceEntity sequenceEntity = this.sequenceEntityDao.inquireByKey(name);
        if (sequenceEntity == null) {
            value = 10001;
            Map<String, Object> dataMap = new HashMap();
            dataMap.put("name", name);
            dataMap.put("value", value);
            this.sequenceEntityDao.insert(dataMap);
        } else {
            value = sequenceEntity.getValue() + 1;
            Map<String, Object> dataMap = new HashMap();
            dataMap.put("name", name);
            dataMap.put("value", value);
            this.sequenceEntityDao.update(dataMap);
        }
        return value;
    }

}
