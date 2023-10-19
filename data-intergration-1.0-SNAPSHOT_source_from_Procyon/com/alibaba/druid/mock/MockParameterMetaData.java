// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ParameterMetaData;

public class MockParameterMetaData implements ParameterMetaData
{
    private final List<Parameter> parameters;
    
    public MockParameterMetaData() {
        this.parameters = new ArrayList<Parameter>();
    }
    
    public List<Parameter> getParameters() {
        return this.parameters;
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return null;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }
    
    @Override
    public int getParameterCount() throws SQLException {
        return this.parameters.size();
    }
    
    @Override
    public int isNullable(final int param) throws SQLException {
        return this.parameters.get(param - 1).getNullable();
    }
    
    @Override
    public boolean isSigned(final int param) throws SQLException {
        return this.parameters.get(param - 1).isSigned();
    }
    
    @Override
    public int getPrecision(final int param) throws SQLException {
        return this.parameters.get(param - 1).getPrecision();
    }
    
    @Override
    public int getScale(final int param) throws SQLException {
        return this.parameters.get(param - 1).getScale();
    }
    
    @Override
    public int getParameterType(final int param) throws SQLException {
        return this.parameters.get(param - 1).getType();
    }
    
    @Override
    public String getParameterTypeName(final int param) throws SQLException {
        return this.parameters.get(param - 1).getTypeName();
    }
    
    @Override
    public String getParameterClassName(final int param) throws SQLException {
        return this.parameters.get(param - 1).getClassName();
    }
    
    @Override
    public int getParameterMode(final int param) throws SQLException {
        return this.parameters.get(param - 1).getMode();
    }
    
    public static class Parameter
    {
        private int nullable;
        private boolean signed;
        private int mode;
        private String className;
        private int type;
        private String typeName;
        private int scale;
        private int precision;
        
        public int getType() {
            return this.type;
        }
        
        public void setType(final int type) {
            this.type = type;
        }
        
        public int getNullable() {
            return this.nullable;
        }
        
        public void setNullable(final int nullable) {
            this.nullable = nullable;
        }
        
        public boolean isSigned() {
            return this.signed;
        }
        
        public void setSigned(final boolean signed) {
            this.signed = signed;
        }
        
        public int getMode() {
            return this.mode;
        }
        
        public void setMode(final int mode) {
            this.mode = mode;
        }
        
        public String getClassName() {
            return this.className;
        }
        
        public void setClassName(final String className) {
            this.className = className;
        }
        
        public String getTypeName() {
            return this.typeName;
        }
        
        public void setTypeName(final String typeName) {
            this.typeName = typeName;
        }
        
        public int getScale() {
            return this.scale;
        }
        
        public void setScale(final int scale) {
            this.scale = scale;
        }
        
        public int getPrecision() {
            return this.precision;
        }
        
        public void setPrecision(final int precision) {
            this.precision = precision;
        }
    }
}
