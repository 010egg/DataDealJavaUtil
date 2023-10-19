// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import java.util.Map;
import java.io.Serializable;

public class LoggerContextVO implements Serializable
{
    private static final long serialVersionUID = 5488023392483144387L;
    final String name;
    final Map<String, String> propertyMap;
    final long birthTime;
    
    public LoggerContextVO(final LoggerContext lc) {
        this.name = lc.getName();
        this.propertyMap = lc.getCopyOfPropertyMap();
        this.birthTime = lc.getBirthTime();
    }
    
    public LoggerContextVO(final String name, final Map<String, String> propertyMap, final long birthTime) {
        this.name = name;
        this.propertyMap = propertyMap;
        this.birthTime = birthTime;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map<String, String> getPropertyMap() {
        return this.propertyMap;
    }
    
    public long getBirthTime() {
        return this.birthTime;
    }
    
    @Override
    public String toString() {
        return "LoggerContextVO{name='" + this.name + '\'' + ", propertyMap=" + this.propertyMap + ", birthTime=" + this.birthTime + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoggerContextVO)) {
            return false;
        }
        final LoggerContextVO that = (LoggerContextVO)o;
        if (this.birthTime != that.birthTime) {
            return false;
        }
        Label_0068: {
            if (this.name != null) {
                if (this.name.equals(that.name)) {
                    break Label_0068;
                }
            }
            else if (that.name == null) {
                break Label_0068;
            }
            return false;
        }
        if (this.propertyMap != null) {
            if (this.propertyMap.equals(that.propertyMap)) {
                return true;
            }
        }
        else if (that.propertyMap == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 31 * result + ((this.propertyMap != null) ? this.propertyMap.hashCode() : 0);
        result = 31 * result + (int)(this.birthTime ^ this.birthTime >>> 32);
        return result;
    }
}
