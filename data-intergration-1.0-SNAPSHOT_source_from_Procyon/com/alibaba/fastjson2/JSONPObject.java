// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.ArrayList;
import java.util.List;

public class JSONPObject
{
    private String function;
    private final List<Object> parameters;
    
    public JSONPObject() {
        this.parameters = new ArrayList<Object>();
    }
    
    public JSONPObject(final String function) {
        this.parameters = new ArrayList<Object>();
        this.function = function;
    }
    
    public String getFunction() {
        return this.function;
    }
    
    public void setFunction(final String function) {
        this.function = function;
    }
    
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    public void addParameter(final Object parameter) {
        this.parameters.add(parameter);
    }
    
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
