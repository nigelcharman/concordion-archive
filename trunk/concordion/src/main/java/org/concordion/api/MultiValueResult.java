package org.concordion.api;

import java.util.HashMap;

public class MultiValueResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1845462548653884738L;
    
    public MultiValueResult() {
    }
    
    public MultiValueResult with(String key, Object value) {
        this.put(key, value);
        return this;
    }
    
    public static MultiValueResult multiValueResult() {
        return new MultiValueResult();
    }
}
