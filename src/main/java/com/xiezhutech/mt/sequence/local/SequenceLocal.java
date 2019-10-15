package com.xiezhutech.mt.sequence.local;

import com.wolf.framework.local.Local;

/**
 *
 * @author jianying9
 */
public interface SequenceLocal extends Local {
    
    public long nextValue(String name);

}
