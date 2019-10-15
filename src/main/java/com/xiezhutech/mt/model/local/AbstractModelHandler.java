package com.xiezhutech.mt.model.local;

import com.wolf.framework.utils.MapUtils;
import com.xiezhutech.mt.org.entity.OrgEntity;
import com.xiezhutech.mt.util.MoneyUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author jianying9
 */
public abstract class AbstractModelHandler implements ModelHandler {

    protected final int getCheckNum(Map<String, Object> modelMap, String fieldName) {
        int num = 0;
        Map<String, Object> fieldMap = this.searchFiledMap(modelMap, fieldName);
        if (fieldMap != null) {
            num = MapUtils.getIntValue(fieldMap, "value");
        }
        return num;
    }

    protected final int getAttribute(Map<String, Object> modelMap) {
        int num = 0;
        Map<String, Object> fieldMap = this.searchFiledMap(modelMap, "attribute");
        int index = MapUtils.getIntValue(fieldMap, "value");
        if (index >= 0) {
            List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(fieldMap, "itemArray");
            Map<String, Object> itemMap = itemMapList.get(index);
            String label = MapUtils.getStringValue(itemMap, "label");
            num = Integer.parseInt(label);
        }
        return num;
    }

    protected final void updateNum(Map<String, Object> modelMap, int num) {
        Map<String, Object> fieldMap = this.searchFiledMap(modelMap, "num");
        if (fieldMap != null) {
            String numValue = Integer.toString(num) + "根";
            fieldMap.put("value", numValue);
        }
    }

    protected final void updatePay(Map<String, Object> modelMap, int pay) {
        Map<String, Object> fieldMap = this.searchFiledMap(modelMap, "pay");
        if (fieldMap != null) {
            fieldMap.put("value", pay);
        }
    }

    private Map<String, Object> searchCheckboxFiledMap(Map<String, Object> checkboxMap, String filedName) {
        Map<String, Object> result = null;
        String type;
        String name;
        boolean selected;
        List<Map<String, Object>> fieldMapList;
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(checkboxMap, "itemArray");
        for (Map<String, Object> itemMap : itemMapList) {
            selected = MapUtils.getBooleanValue(itemMap, "selected");
            if (selected) {
                //分析子节点
                fieldMapList = MapUtils.getObjectListValue(itemMap, "fieldArray");
                if (fieldMapList != null) {
                    for (Map<String, Object> fieldMap : fieldMapList) {
                        name = MapUtils.getStringValue(fieldMap, "name");
                        if (name != null && name.equals(filedName)) {
                            result = fieldMap;
                            break;
                        } else {
                            //如果有子节点,就搜寻子节点集合
                            type = MapUtils.getStringValue(fieldMap, "type");
                            switch (type) {
                                case ModelFieldTypes.RADIO:
                                    result = this.searchRadioFiledMap(fieldMap, filedName);
                                    break;
                                case ModelFieldTypes.CHECKBOX:
                                    result = this.searchCheckboxFiledMap(fieldMap, filedName);
                                    break;
                            }
                            //
                            if (result != null) {
                                break;
                            }
                        }
                    }
                }
                //
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    private Map<String, Object> searchRadioFiledMap(Map<String, Object> radioMap, String filedName) {
        Map<String, Object> result = null;
        Integer index = MapUtils.getIntValue(radioMap, "value");
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(radioMap, "itemArray");
        if (index > -1 && index < itemMapList.size()) {
            Map<String, Object> itemMap = itemMapList.get(index);
            //判断扩展字段
            List<Map<String, Object>> fieldMapList = MapUtils.getObjectListValue(itemMap, "fieldArray");
            if (fieldMapList != null) {
                String type;
                String name;
                for (Map<String, Object> fieldMap : fieldMapList) {
                    name = MapUtils.getStringValue(fieldMap, "name");
                    if (name != null && name.equals(filedName)) {
                        result = fieldMap;
                        break;
                    } else {
                        //如果有子节点,就搜寻子节点集合
                        type = MapUtils.getStringValue(fieldMap, "type");
                        switch (type) {
                            case ModelFieldTypes.RADIO:
                                result = this.searchRadioFiledMap(fieldMap, filedName);
                                break;
                            case ModelFieldTypes.CHECKBOX:
                                result = this.searchCheckboxFiledMap(fieldMap, filedName);
                                break;
                        }
                        //
                        if (result != null) {
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    private Map<String, Object> searchGroupFiledMap(Map<String, Object> groupMap, String filedName) {
        Map<String, Object> result = null;
        String type;
        String name;
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(groupMap, "itemArray");
        for (Map<String, Object> fieldMap : itemMapList) {
            name = MapUtils.getStringValue(fieldMap, "name");
            if (name != null && name.equals(filedName)) {
                result = fieldMap;
                break;
            } else {
                //如果有子节点,就搜寻子节点集合
                type = MapUtils.getStringValue(fieldMap, "type");
                switch (type) {
                    case ModelFieldTypes.RADIO:
                        result = this.searchRadioFiledMap(fieldMap, filedName);
                        break;
                    case ModelFieldTypes.CHECKBOX:
                        result = this.searchCheckboxFiledMap(fieldMap, filedName);
                        break;
                }
                //
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    protected final Map<String, Object> searchFiledMap(Map<String, Object> modelMap, String filedName) {
        Map<String, Object> result = null;
        String type;
        String name;
        List<Map<String, Object>> filedMapList = MapUtils.getObjectListValue(modelMap, "fieldArray");
        for (Map<String, Object> fieldMap : filedMapList) {
            name = MapUtils.getStringValue(fieldMap, "name");
            if (name != null && name.equals(filedName)) {
                result = fieldMap;
                break;
            } else {
                //如果有子节点,就搜寻子节点集合
                type = MapUtils.getStringValue(fieldMap, "type");
                switch (type) {
                    case ModelFieldTypes.GROUP:
                        result = this.searchGroupFiledMap(fieldMap, filedName);
                        break;
                    case ModelFieldTypes.RADIO:
                        result = this.searchRadioFiledMap(fieldMap, filedName);
                        break;
                    case ModelFieldTypes.CHECKBOX:
                        result = this.searchCheckboxFiledMap(fieldMap, filedName);
                        break;
                }
                //
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public final void fillModel(OrgEntity orgEntity, Map<String, Object> modelMap) {
        modelMap.put("orgId", orgEntity.getOrgId());
        //
        Map<String, Object> orgMap = new HashMap();
        orgMap.put("projectName", orgEntity.getProjectName());
        orgMap.put("constructionName", orgEntity.getConstructionName());
        orgMap.put("witnessName", orgEntity.getWitnessName());
        orgMap.put("companyName", orgEntity.getCompanyName());
        //
        Set<Map.Entry<String, Object>> entrySet = orgMap.entrySet();
        Map<String, Object> filedMap;
        for (Map.Entry<String, Object> entry : entrySet) {
            filedMap = this.searchFiledMap(modelMap, entry.getKey());
            if (filedMap != null) {
                filedMap.put("value", entry.getValue());
            }
        }
    }

    private void addFieldMapList(List<Map<String, Object>> resultMapList, Map<String, Object> fieldMap) {
        //添加自己
        resultMapList.add(fieldMap);
        //判断是否要添加子节点
        String type = MapUtils.getStringValue(fieldMap, "type");
        switch (type) {
            case ModelFieldTypes.GROUP:
                this.addGroupFieldMapList(resultMapList, fieldMap);
                break;
            case ModelFieldTypes.RADIO:
                this.addRadioFieldMapList(resultMapList, fieldMap);
                break;
            case ModelFieldTypes.CHECKBOX:
                this.addCheckboxFieldMapList(resultMapList, fieldMap);
                break;
        }
    }

    private void addCheckboxFieldMapList(List<Map<String, Object>> resultMapList, Map<String, Object> fieldMap) {
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(fieldMap, "itemArray");
        if (itemMapList != null) {
            List<Map<String, Object>> fieldMapList;
            for (Map<String, Object> itemMap : itemMapList) {
                fieldMapList = MapUtils.getObjectListValue(itemMap, "fieldArray");
                if (fieldMapList != null) {
                    resultMapList.addAll(fieldMapList);
                }
            }
        }
    }

    private void addRadioFieldMapList(List<Map<String, Object>> resultMapList, Map<String, Object> fieldMap) {
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(fieldMap, "itemArray");
        if (itemMapList != null) {
            List<Map<String, Object>> fieldMapList;
            for (Map<String, Object> itemMap : itemMapList) {
                fieldMapList = MapUtils.getObjectListValue(itemMap, "fieldArray");
                if (fieldMapList != null) {
                    resultMapList.addAll(fieldMapList);
                }
            }
        }
    }

    private void addGroupFieldMapList(List<Map<String, Object>> resultMapList, Map<String, Object> fieldMap) {
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(fieldMap, "itemArray");
        if (itemMapList != null) {
            for (Map<String, Object> itemMap : itemMapList) {
                this.addFieldMapList(resultMapList, itemMap);
            }
        }
    }

    private List<Map<String, Object>> getAllFieldMapList(Map<String, Object> modelMap) {
        List<Map<String, Object>> resultMapList = new ArrayList();
        List<Map<String, Object>> fieldMapList = MapUtils.getObjectListValue(modelMap, "fieldArray");
        for (Map<String, Object> fieldMap : fieldMapList) {
            this.addFieldMapList(resultMapList, fieldMap);
        }
        return resultMapList;
    }

    @Override
    public void filterModel(String role, Map<String, Object> modelMap) {
        //获取所有field
        String fieldRole;
        List<Map<String, Object>> fieldMapList = this.getAllFieldMapList(modelMap);
        for (Map<String, Object> fieldMap : fieldMapList) {
            fieldRole = MapUtils.getStringValue(fieldMap, "role");
            if (fieldRole != null && fieldRole.equals(role) == false) {
                fieldMap.put("hide", true);
            } else {
                fieldMap.remove("hide");
            }
        }
    }

    private void addLabelResult(List<Map<String, Object>> resultMapList, Map<String, Object> labelMap) {
        String name = MapUtils.getStringValue(labelMap, "name");
        if (name != null) {
            String label = MapUtils.getStringValue(labelMap, "label");
            String value = MapUtils.getStringValue(labelMap, "value");
            if (value == null) {
                value = "";
            }
            Map<String, Object> resultMap = new HashMap();
            resultMap.put("name", name);
            resultMap.put("type", "text");
            resultMap.put("label", label);
            resultMap.put("value", value);
            resultMapList.add(resultMap);
        }
    }

    private void addTextResult(List<Map<String, Object>> resultMapList, Map<String, Object> textMap) {
        String name = MapUtils.getStringValue(textMap, "name");
        String label = MapUtils.getStringValue(textMap, "label");
        String value = MapUtils.getStringValue(textMap, "value");
        if (value == null) {
            value = "";
        }
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("name", name);
        resultMap.put("type", "text");
        resultMap.put("label", label);
        resultMap.put("value", value);
        resultMapList.add(resultMap);
    }

    private void addDateResult(List<Map<String, Object>> resultMapList, Map<String, Object> dateMap) {
        String name = MapUtils.getStringValue(dateMap, "name");
        String label = MapUtils.getStringValue(dateMap, "label");
        String value = MapUtils.getStringValue(dateMap, "value");
        if (value == null) {
            value = "";
        }
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("name", name);
        resultMap.put("type", "text");
        resultMap.put("label", label);
        resultMap.put("value", value);
        resultMapList.add(resultMap);
    }

    private void addNumberResult(List<Map<String, Object>> resultMapList, Map<String, Object> numberMap) {
        String name = MapUtils.getStringValue(numberMap, "name");
        String label = MapUtils.getStringValue(numberMap, "label");
        String value = "";
        Long numValue = MapUtils.getLongValue(numberMap, "value");
        if (numValue != null) {
            value = Long.toString(numValue);
            String unit = MapUtils.getStringValue(numberMap, "unit");
            if (unit != null) {
                value = value + unit;
            }
        }
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("name", name);
        resultMap.put("type", "text");
        resultMap.put("label", label);
        resultMap.put("value", value);
        resultMapList.add(resultMap);
    }

    private void addRadioResult(List<Map<String, Object>> resultMapList, Map<String, Object> radioMap) {
        Long radioValue = MapUtils.getLongValue(radioMap, "value");
        int index = radioValue.intValue();
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(radioMap, "itemArray");
        if (index > -1 && index < itemMapList.size()) {
            Map<String, Object> itemMap = itemMapList.get(index);
            //
            String name = MapUtils.getStringValue(radioMap, "name");
            String label = MapUtils.getStringValue(radioMap, "label");
            String value = MapUtils.getStringValue(itemMap, "label");
            Map<String, Object> resultMap = new HashMap();
            resultMap.put("name", name);
            resultMap.put("type", "text");
            resultMap.put("label", label);
            resultMap.put("value", value);
            resultMapList.add(resultMap);
            //判断扩展字段
            List<Map<String, Object>> fieldMapList = MapUtils.getObjectListValue(itemMap, "fieldArray");
            if (fieldMapList != null) {
                String type;
                for (Map<String, Object> fieldMap : fieldMapList) {
                    type = MapUtils.getStringValue(fieldMap, "type");
                    switch (type) {
                        case ModelFieldTypes.LABEL:
                            this.addLabelResult(resultMapList, fieldMap);
                            break;
                        case ModelFieldTypes.TEXT:
                            this.addTextResult(resultMapList, fieldMap);
                            break;
                        case ModelFieldTypes.DATE:
                            this.addDateResult(resultMapList, fieldMap);
                            break;
                        case ModelFieldTypes.NUMBER:
                            this.addNumberResult(resultMapList, fieldMap);
                            break;
                        case ModelFieldTypes.RADIO:
                            this.addRadioResult(resultMapList, fieldMap);
                            break;
                        case ModelFieldTypes.CHECKBOX:
                            this.addCheckboxResult(resultMapList, fieldMap);
                            break;
                    }
                }
            }
        }
    }

    private void addCheckboxResult(List<Map<String, Object>> resultMapList, Map<String, Object> checkboxMap) {
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(checkboxMap, "itemArray");
        Boolean selected;
        String name = MapUtils.getStringValue(checkboxMap, "name");
        String label = MapUtils.getStringValue(checkboxMap, "label");
        String value = "";
        String itemLabel;
        for (Map<String, Object> itemMap : itemMapList) {
            selected = MapUtils.getBooleanValue(itemMap, "selected");
            if (selected != null && selected) {
                itemLabel = MapUtils.getStringValue(itemMap, "label");
                value = value + " " + itemLabel;
            }
        }
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("name", name);
        resultMap.put("type", "text");
        resultMap.put("label", label);
        resultMap.put("value", value);
        resultMapList.add(resultMap);
        //分析子节点
        String type;
        List<Map<String, Object>> fieldMapList;
        for (Map<String, Object> itemMap : itemMapList) {
            selected = MapUtils.getBooleanValue(itemMap, "selected");
            if (selected != null && selected) {
                //分析子节点
                fieldMapList = MapUtils.getObjectListValue(itemMap, "fieldArray");
                if (fieldMapList != null) {
                    for (Map<String, Object> fieldMap : fieldMapList) {
                        type = MapUtils.getStringValue(fieldMap, "type");
                        switch (type) {
                            case ModelFieldTypes.LABEL:
                                this.addLabelResult(resultMapList, fieldMap);
                                break;
                            case ModelFieldTypes.TEXT:
                                this.addTextResult(resultMapList, fieldMap);
                                break;
                            case ModelFieldTypes.DATE:
                                this.addDateResult(resultMapList, fieldMap);
                                break;
                            case ModelFieldTypes.NUMBER:
                                this.addNumberResult(resultMapList, fieldMap);
                                break;
                            case ModelFieldTypes.RADIO:
                                this.addRadioResult(resultMapList, fieldMap);
                                break;
                            case ModelFieldTypes.CHECKBOX:
                                this.addCheckboxResult(resultMapList, fieldMap);
                                break;
                        }
                    }
                }
            }
        }
    }

    private void getGroupValue(List<Map<String, Object>> resultMapList, Map<String, Object> groupMap) {
        List<Map<String, Object>> itemMapList = MapUtils.getObjectListValue(groupMap, "itemArray");
        if (itemMapList != null) {
            String type;
            for (Map<String, Object> fieldMap : itemMapList) {
                type = MapUtils.getStringValue(fieldMap, "type");
                switch (type) {
                    case ModelFieldTypes.LABEL:
                        this.addLabelResult(resultMapList, fieldMap);
                        break;
                    case ModelFieldTypes.TEXT:
                        this.addTextResult(resultMapList, fieldMap);
                        break;
                    case ModelFieldTypes.DATE:
                        this.addDateResult(resultMapList, fieldMap);
                        break;
                    case ModelFieldTypes.NUMBER:
                        this.addNumberResult(resultMapList, fieldMap);
                        break;
                    case ModelFieldTypes.RADIO:
                        this.addRadioResult(resultMapList, fieldMap);
                        break;
                    case ModelFieldTypes.CHECKBOX:
                        this.addCheckboxResult(resultMapList, fieldMap);
                        break;
                }
            }
        }
    }

    @Override
    public Map<String, Object> getModelResult(Map<String, Object> modelMap) {
        Map<String, Object> resultModelMap = new HashMap();
        resultModelMap.putAll(modelMap);
        resultModelMap.remove("fieldArray");
        List<Map<String, Object>> resultMapList = new ArrayList();
        //
        List<Map<String, Object>> filedMapList = MapUtils.getObjectListValue(modelMap, "fieldArray");
        for (Map<String, Object> fieldMap : filedMapList) {
            String type = MapUtils.getStringValue(fieldMap, "type");
            switch (type) {
                case ModelFieldTypes.LABEL:
                    this.addLabelResult(resultMapList, fieldMap);
                    break;
                case ModelFieldTypes.TEXT:
                    this.addTextResult(resultMapList, fieldMap);
                    break;
                case ModelFieldTypes.DATE:
                    this.addDateResult(resultMapList, fieldMap);
                    break;
                case ModelFieldTypes.NUMBER:
                    this.addNumberResult(resultMapList, fieldMap);
                    break;
                case ModelFieldTypes.RADIO:
                    this.addRadioResult(resultMapList, fieldMap);
                    break;
                case ModelFieldTypes.CHECKBOX:
                    this.addCheckboxResult(resultMapList, fieldMap);
                    break;
                case ModelFieldTypes.GROUP:
                    this.getGroupValue(resultMapList, fieldMap);
                    break;
            }
        }
        //根据业务模型整合数据结果
        resultModelMap.put("resultArray", resultMapList);
        return resultModelMap;
    }

    private Map<String, Object> createPrintLogo() {
        Map<String, Object> logoMap = new HashMap();
        logoMap.put("type", "logo");
        logoMap.put("value", "mt/mt_1x.png");
        logoMap.put("align", "center");
        return logoMap;
    }

    private Map<String, Object> createPrintCode(long reportId) {
        String code = Long.toString(reportId);
        if (code.length() > 8) {
            code = code.substring(0, 8);
        }
        int zeroNum = 8 - code.length();
        //生成14位字符串编码
        StringBuilder sb = new StringBuilder(14);
        sb.append("mt");
        for (int i = 0; i < zeroNum; i++) {
            sb.append("0");
        }
        sb.append(code);
        code = sb.toString();
        //
        Map<String, Object> logoMap = new HashMap();
        logoMap.put("type", "code");
        logoMap.put("value", code);
        logoMap.put("align", "center");
        return logoMap;
    }

    private Map<String, Object> createPrintText(String text, boolean title, String align) {
        Map<String, Object> textMap;
        if (title) {
            if (text.length() > 8) {
                text = text.substring(0, 8);
            }
            textMap = new HashMap();
            textMap.put("type", "text");
            textMap.put("value", text);
            textMap.put("align", align);
            textMap.put("blod", true);
            textMap.put("size", 40);
        } else {
            if (text.length() > 16) {
                text = text.substring(0, 16);
            }
            textMap = new HashMap();
            textMap.put("type", "text");
            textMap.put("value", text);
            textMap.put("align", align);
            textMap.put("blod", false);
            textMap.put("size", 20);
        }
        return textMap;
    }

    private List<Map<String, Object>> createPrintTextListWrap(String text) {
        List<Map<String, Object>> resultList = new ArrayList();
        List<String> textList = new ArrayList();
        if (text.length() > 32) {
            text = text.substring(0, 32);
        }
        if (text.length() > 16) {
            String first = text.substring(0, 16);
            String second = text.substring(16);
            textList.add(first);
            textList.add(second);
        } else {
            textList.add(text);
        }
        //
        Map<String, Object> textMap;
        for (String value : textList) {
            textMap = new HashMap();
            textMap.put("type", "text");
            textMap.put("value", value);
            textMap.put("align", "left");
            textMap.put("blod", false);
            textMap.put("size", 20);
            resultList.add(textMap);
        }
        return resultList;
    }

    private List<Map<String, Object>> filterPrint(List<Map<String, Object>> sourceMapList) {
        List<Map<String, Object>> printMapList = new ArrayList();
        String name;
        Map<String, Map<String, Object>> nameSourceMap = new HashMap();
        String value;
        List<String> nameList = new ArrayList();
        nameList.add("projectName");
        nameList.add("attribute");
        nameList.add("num");
        nameList.add("paihao");
        for (Map<String, Object> sourceMap : sourceMapList) {
            name = MapUtils.getStringValue(sourceMap, "name");
            if (nameList.contains(name)) {
                nameSourceMap.put(name, sourceMap);
            }
        }
        //
        Map<String, Object> textMap;
        Map<String, Object> sourceMap;
        List<Map<String, Object>> textMapList;
        String label;
        for (String printName : nameList) {
            sourceMap = nameSourceMap.get(printName);
            if (sourceMap != null) {
                value = MapUtils.getStringValue(sourceMap, "value");
                if (printName.equals("projectName")) {
                    textMapList = this.createPrintTextListWrap(value);
                    printMapList.addAll(textMapList);
                } else {
                    label = MapUtils.getStringValue(sourceMap, "label");
                    value = label + ":" + value;
                    textMap = this.createPrintText(value, false, "left");
                    printMapList.add(textMap);
                }
            }
        }
        return printMapList;
    }

    @Override
    public final Map<String, Object> getModelResultPrint(long reportId, String checkName, Map<String, Object> modelResultMap) {
        Map<String, Object> printModelMap = new HashMap();
        printModelMap.putAll(modelResultMap);
        printModelMap.put("width", 384);
        printModelMap.put("height", 537);
        printModelMap.put("paddingLeft", 30);
        printModelMap.put("lineHeight", 6);
        printModelMap.remove("resultArray");
        //
        List<Map<String, Object>> printMapList = new ArrayList();
        Map<String, Object> logoMap = this.createPrintLogo();
        printMapList.add(logoMap);
        //
        Map<String, Object> codeMap = this.createPrintCode(reportId);
        printMapList.add(codeMap);
        //
        String type = MapUtils.getStringValue(modelResultMap, "type");
        Map<String, Object> textMap = this.createPrintText(type, true, "center");
        printMapList.add(textMap);
        //
        checkName = checkName + "(受理)";
        textMap = this.createPrintText(checkName, false, "center");
        printMapList.add(textMap);
        //
        List<Map<String, Object>> resultMapList = MapUtils.getObjectListValue(modelResultMap, "resultArray");
        List<Map<String, Object>> textMapList = this.filterPrint(resultMapList);
        printMapList.addAll(textMapList);
        printModelMap.put("printArray", printMapList);
        return printModelMap;
    }

    @Override
    public final Map<String, String> getModelResultExcel(Map<String, Object> modelMap) {
        Map<String, String> excelResultMap = new HashMap();
        Map<String, Object> modelresultMap = this.getModelResult(modelMap);
        List<Map<String, Object>> resultMapList = MapUtils.getObjectListValue(modelresultMap, "resultArray");
        String value;
        String name;
        for (Map<String, Object> resultMap : resultMapList) {
            name = MapUtils.getStringValue(resultMap, "name");
            if (name != null) {
                value = MapUtils.getStringValue(resultMap, "value");
                excelResultMap.put(name, value);
            }
        }
        //
        String pay = excelResultMap.get("pay");
        pay = pay.replace("元", "");
        if (pay.isEmpty()) {
            pay = "0";
        }
        double money = Double.parseDouble(pay);
        BigDecimal numberOfMoney = new BigDecimal(money);
        String bigPay = MoneyUtils.number2CNMontrayUnit(numberOfMoney);
        excelResultMap.put("pay", pay);
        excelResultMap.put("bigPay", bigPay);
        //
        String yuancailiaoCheck = excelResultMap.get("yuancailiaoCheck");
        if (yuancailiaoCheck == null) {
            yuancailiaoCheck = "";
        }
        StringBuilder yuancailiaoCheckBuilder = new StringBuilder();
        String[] yuancailiaoCheckArray = {"力学性能", "弯曲性能", "最大力总伸长率", "重量偏差", "规定塑性延伸强度Rp0.2",
            "弹性模量", "化学成分"};
        for (String item : yuancailiaoCheckArray) {
            if (yuancailiaoCheck.contains(item)) {
                //选中
                yuancailiaoCheckBuilder.append("    (√)").append(item).append("、");
            } else {
                //未选中
                yuancailiaoCheckBuilder.append("    (  )").append(item).append("、");
            }
        }
        yuancailiaoCheckBuilder.setLength(yuancailiaoCheckBuilder.length() - 1);
        yuancailiaoCheckBuilder.append(";");
        excelResultMap.put("yuancailiaoCheck", yuancailiaoCheckBuilder.toString());
        //连接件
        String lianjiejianCheck = "    (  )接头力学性能/焊接性能;     (  )其他：";
        excelResultMap.put("lianjiejianCheck", lianjiejianCheck);
        //标准
        String yuancailiaoStandard = "    (  )《钢筋混凝土用钢》GB 1499    (  )《低合金高强度结构钢》GB/T 1591   (  )《碳素结构钢》GB 700"
                + "    (  )《预应力砼用钢绞线》GB/T 5224    (  )《预应力混凝土用螺纹钢筋》GB/T 20065";
        String lianjiejianStandard = "    (  )《钢筋焊接及验收规程》JGJ 18   (  )《钢筋机械连接技术规程》JGJ 107";
        String gangguanStandard = "    (  )《结构用无缝钢管》GB/T 8162     (  )《输送流体用无缝钢管》GB/T 8163    (  )《直缝电焊钢管》GB/T 13793"
                + "    (  )《低压流体输送用焊接钢管》GB/T 3091    (  )其他：";
        //
        excelResultMap.put("yuancailiaoStandard", yuancailiaoStandard);
        excelResultMap.put("lianjiejianStandard", lianjiejianStandard);
        excelResultMap.put("gangguanStandard", gangguanStandard);
        //样品处置
        String yangpingchuzhi = excelResultMap.get("yangpingchuzhi");
        if (yangpingchuzhi == null) {
            yangpingchuzhi = "";
        }
        String[] yangpingchuzhiArray = {"委托本单位处理", "试毕取回", "其它:"};
        StringBuilder yangpingchuzhiBuilder = new StringBuilder();
        for (String item : yangpingchuzhiArray) {
            if (yangpingchuzhi.contains(item)) {
                //选中
                yangpingchuzhiBuilder.append("    (√)").append(item);
            } else {
                //未选中
                yangpingchuzhiBuilder.append("    (  )").append(item);
            }
        }
        //reportStyle报告形式
        String reportStyle = excelResultMap.get("reportStyle");
        if (reportStyle == null) {
            reportStyle = "";
        }
        String[] reportStyleArray = {"简装", "精装"};
        StringBuilder reportStyleBuilder = new StringBuilder();
        for (String item : reportStyleArray) {
            if (reportStyle.contains(item)) {
                //选中
                reportStyleBuilder.append("    (√)").append(item);
            } else {
                //未选中
                reportStyleBuilder.append("    (  )").append(item);
            }
        }
        excelResultMap.put("reportStyle", reportStyleBuilder.toString());
        //报告发放
        String reportSend = excelResultMap.get("reportSend");
        if (reportSend == null) {
            reportSend = "";
        }
        String[] reportSendArray = {"自取", "邮寄", "电话告知结果", "其它"};
        StringBuilder reportSendBuilder = new StringBuilder();
        for (String item : reportSendArray) {
            if (reportSend.contains(item)) {
                //选中
                reportSendBuilder.append("    (√)").append(item);
            } else {
                //未选中
                reportSendBuilder.append("    (  )").append(item);
            }
        }
        excelResultMap.put("reportSend", reportSendBuilder.toString());
        //缴费方式
        String payType = excelResultMap.get("payType");
        if (payType == null) {
            payType = "";
        }
        String[] payTypeArray = {"冲帐", "现金", "转账"};
        StringBuilder payTypeBuilder = new StringBuilder();
        for (String item : payTypeArray) {
            if (payType.contains(item)) {
                //选中
                payTypeBuilder.append("    (√)").append(item);
            } else {
                //未选中
                payTypeBuilder.append("    (  )").append(item);
            }
        }
        excelResultMap.put("payType", payTypeBuilder.toString());
        //
        String payCompany = excelResultMap.get("payCompany");
        if (payCompany == null) {
            payCompany = "";
        }
        payCompany = "汇款单位:" + payCompany;
        excelResultMap.put("payCompany", payCompany);
        //样品检验是否合格
        String sampleCheck = excelResultMap.get("sampleCheck");
        if (sampleCheck == null) {
            sampleCheck = "";
        }
        String[] sampleCheckArray = {"符合", "不符合"};
        StringBuilder sampleCheckBuilder = new StringBuilder();
        for (String item : sampleCheckArray) {
            if (sampleCheck.contains(item)) {
                //选中
                sampleCheckBuilder.append("    (√)").append(item);
            } else {
                //未选中
                sampleCheckBuilder.append("    (  )").append(item);
            }
        }
        excelResultMap.put("sampleCheck", sampleCheckBuilder.toString());
        //检测类别
        String checkType = excelResultMap.get("checkType");
        if (checkType == null) {
            checkType = "";
        }
        String[] checkTypeArray = {"委托检测", "抽样检测", "见证送检", "见证检测", "其他"};
        StringBuilder checkTypeBuilder = new StringBuilder();
        for (String item : checkTypeArray) {
            if (checkType.contains(item)) {
                //选中
                checkTypeBuilder.append("    (√)").append(item);
            } else {
                //未选中
                checkTypeBuilder.append("    (  )").append(item);
            }
        }
        excelResultMap.put("checkType", checkTypeBuilder.toString());
        //
        String checkTypeOther = excelResultMap.get("checkTypeOther");
        if (checkTypeOther == null) {
            checkTypeOther = "";
        }
        excelResultMap.put("checkTypeOther", checkTypeOther);
        return excelResultMap;
    }

}
