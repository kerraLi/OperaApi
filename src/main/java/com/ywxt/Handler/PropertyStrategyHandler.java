package com.ywxt.Handler;

import net.sf.json.JSONException;
import net.sf.json.util.PropertySetStrategy;

/**
 * 处理JsonObject toBean 转换多余属性过滤
 */
public class PropertyStrategyHandler extends PropertySetStrategy {

    private PropertySetStrategy original;

    public PropertyStrategyHandler(PropertySetStrategy original) {
        this.original = original;
    }

    @Override
    public void setProperty(Object o, String string, Object o1) throws JSONException {
        try {
            original.setProperty(o, string, o1);
        } catch (Exception ex) {
            //ignore
        }
    }
}
