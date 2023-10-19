// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

public class ProfileEntryKey
{
    private final String parentName;
    private final String name;
    private final String type;
    
    public ProfileEntryKey(final String parentName, final String name, final String type) {
        this.parentName = parentName;
        this.name = name;
        this.type = type;
    }
    
    public String getParentName() {
        return this.parentName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void fillValue(final ProfileEntryStatValue value) {
        value.setParentName(this.parentName);
        value.setName(this.name);
        value.setType(this.type);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = 31 * result + ((this.parentName == null) ? 0 : this.parentName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ProfileEntryKey other = (ProfileEntryKey)obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.parentName == null) {
            if (other.parentName != null) {
                return false;
            }
        }
        else if (!this.parentName.equals(other.parentName)) {
            return false;
        }
        return true;
    }
}
