package com.xiezhutech.mt.model.local;

import java.util.Map;

/**
 *
 * @author jianying9
 */
public class GcRzGuangyuanGjModelHandlerImpl extends AbstractModelHandler implements ModelHandler {

    @Override
    public void fillModel(Map<String, Object> modelMap) {
        //自动填充数量
        int num = 0;
        int pay = 0;
        //规格
        int attribute = this.getAttribute(modelMap);
        //重力偏差
        int zlpcCheckNum = this.getCheckNum(modelMap, "zlpcCheckNum");
        if (zlpcCheckNum > 0) {
            num = num + zlpcCheckNum;
            pay = pay + zlpcCheckNum * 50;
        }
        //力学性能
        int lxxnCheckNum = this.getCheckNum(modelMap, "lxxnCheckNum");
        if (lxxnCheckNum > 0) {
            num = num + lxxnCheckNum;
            if (attribute > 0 && attribute <= 10) {
                pay = pay + lxxnCheckNum * 145;
            } else if (attribute >= 12 && attribute <= 20) {
                pay = pay + lxxnCheckNum * 160;
            } else if (attribute >= 22) {
                pay = pay + lxxnCheckNum * 170;
            }
        }
        //弯曲性能
        int wqCheckNum = this.getCheckNum(modelMap, "wqCheckNum");
        if (wqCheckNum > 0) {
            num = num + wqCheckNum;
            if (attribute > 0 && attribute <= 10) {
                pay = pay + wqCheckNum * 45;
            } else if (attribute >= 12 && attribute <= 20) {
                pay = pay + wqCheckNum * 60;
            } else if (attribute >= 22) {
                pay = pay + wqCheckNum * 70;
            }
        }
        //化学成分
        int hxcfCheckNum = this.getCheckNum(modelMap, "hxcfCheckNum");
        if (hxcfCheckNum > 0) {
            num = num + hxcfCheckNum;
            pay = pay + hxcfCheckNum * 500;
        }
        //尺寸
        int ccCheckNum = this.getCheckNum(modelMap, "ccCheckNum");
        if (ccCheckNum > 0) {
            num = num + ccCheckNum;
            pay = pay + ccCheckNum * 50;
        }
        //表面质量
        int bmzlCheckNum = this.getCheckNum(modelMap, "bmzlCheckNum");
        if (bmzlCheckNum > 0) {
            num = num + bmzlCheckNum;
            pay = pay + bmzlCheckNum * 50;
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

}
