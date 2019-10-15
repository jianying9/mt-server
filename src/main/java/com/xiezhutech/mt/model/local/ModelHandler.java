package com.xiezhutech.mt.model.local;

import com.xiezhutech.mt.org.entity.OrgEntity;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public interface ModelHandler {

    /**
     * 自动填充字段
     *
     * @param modelMap
     */
    public void fillModel(Map<String, Object> modelMap);

    /**
     * 自动填充字段
     *
     * @param orgEntity
     * @param modelMap
     */
    public void fillModel(OrgEntity orgEntity, Map<String, Object> modelMap);

    /**
     * 根据角色过滤字段
     *
     * @param role
     * @param modelMap
     */
    public void filterModel(String role, Map<String, Object> modelMap);

    /**
     * 获取结果模型
     *
     * @param modelMap
     * @return
     */
    public Map<String, Object> getModelResult(Map<String, Object> modelMap);

    /**
     * 获取打印模型
     *
     * @param reportId
     * @param checkName
     * @param modelResultMap
     * @return
     */
    public Map<String, Object> getModelResultPrint(long reportId, String checkName, Map<String, Object> modelResultMap);

    /**
     * 获取excel模板
     *
     * @return
     */
    public String getExcelModelPath();

    /**
     * 获取excel结果模型
     *
     * @param modelMap
     * @return
     */
    public Map<String, String> getModelResultExcel(Map<String, Object> modelMap);
}
