package com.xiezhutech.mt.model.local;

import com.wolf.framework.utils.MapUtils;
import java.util.Map;

/**
 *
 * @author jianying9
 */
public class GcGangjinghanjieModelHandlerImpl extends AbstractModelHandler implements ModelHandler {

    @Override
    public void fillModel(Map<String, Object> modelMap) {
        //自动填充数量
        int num = 0;
        int pay = 0;
        //规格
        int attribute = this.getAttribute(modelMap);
        //抗拉强度-初检
        int klqdCheckNum = this.getCheckNum(modelMap, "klqdCheckNum");
        if (klqdCheckNum > 0) {
            num = num + klqdCheckNum;
            if (attribute > 0 && attribute <= 10) {
                pay = pay + klqdCheckNum * 50;
            } else if (attribute >= 12 && attribute <= 20) {
                pay = pay + klqdCheckNum * 80;
            } else if (attribute >= 22) {
                pay = pay + klqdCheckNum * 90;
            }
        }
        //抗拉强度-复检
        int klqdFujianCheckNum = this.getCheckNum(modelMap, "klqdFujianCheckNum");
        if (klqdFujianCheckNum > 0) {
            num = num + klqdFujianCheckNum;
            if (attribute > 0 && attribute <= 10) {
                pay = pay + klqdFujianCheckNum * 50;
            } else if (attribute >= 12 && attribute <= 20) {
                pay = pay + klqdFujianCheckNum * 80;
            } else if (attribute >= 22) {
                pay = pay + klqdFujianCheckNum * 90;
            }
        }
        //弯曲性能
        int wqCheckNum = this.getCheckNum(modelMap, "wqCheckNum");
        if (wqCheckNum > 0) {
            num = num + wqCheckNum;
            if (attribute > 0 && attribute <= 10) {
                pay = pay + wqCheckNum * 30;
            } else if (attribute >= 12 && attribute <= 20) {
                pay = pay + wqCheckNum * 40;
            } else if (attribute >= 22) {
                pay = pay + wqCheckNum * 50;
            }
        }
        //更新数量
        this.updateNum(modelMap, num);
        //更新金额
        this.updatePay(modelMap, pay);
    }

    @Override
    public String getExcelModelPath() {
        return "mt/excel_temp/gangcai.xls";
    }

    private String getExcelResult(Map<String, Object> sourceMap, String fieldName) {
        String value = MapUtils.getStringValue(sourceMap, fieldName);
        if (value == null) {
            value = "";
        }
        return value;
    }

}
