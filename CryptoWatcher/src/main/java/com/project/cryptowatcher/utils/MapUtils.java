package com.project.cryptowatcher.utils;

import java.util.Collections;
import java.util.Map;

public class MapUtils {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNestedMap(Map<String, Object> parent, String key) {
        return (Map<String, Object>) parent.getOrDefault(key, Collections.emptyMap());
    }

    public static Map<String,Object> getMarketData(Map<String, Object> coinDetail){
       return getNestedMap(coinDetail, "market_data");
    }

    public static Map<String,Object> getCurrentPrice(Map<String, Object> marketData){
        return getNestedMap(marketData, "current_price");
    }
}
