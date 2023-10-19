// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.dto;

public class MapDto
{
    Integer status;
    Double lat;
    Double lng;
    Integer precise;
    Integer confidence;
    Integer comprehension;
    String level;
    String analys_level;
    String address;
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(final String address) {
        this.address = address;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public Double getLat() {
        return this.lat;
    }
    
    public Double getLng() {
        return this.lng;
    }
    
    public Integer getPrecise() {
        return this.precise;
    }
    
    public Integer getConfidence() {
        return this.confidence;
    }
    
    public Integer getComprehension() {
        return this.comprehension;
    }
    
    public String getLevel() {
        return this.level;
    }
    
    public String getAnalys_level() {
        return this.analys_level;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public void setLat(final Double lat) {
        this.lat = lat;
    }
    
    public void setLng(final Double lng) {
        this.lng = lng;
    }
    
    public void setPrecise(final Integer precise) {
        this.precise = precise;
    }
    
    public void setConfidence(final Integer confidence) {
        this.confidence = confidence;
    }
    
    public void setComprehension(final Integer comprehension) {
        this.comprehension = comprehension;
    }
    
    public void setLevel(final String level) {
        this.level = level;
    }
    
    public void setAnalys_level(final String analys_level) {
        this.analys_level = analys_level;
    }
    
    @Override
    public String toString() {
        return "MapDto{status=" + this.status + ", lat=" + this.lat + ", lng=" + this.lng + ", precise=" + this.precise + ", confidence=" + this.confidence + ", comprehension=" + this.comprehension + ", level='" + this.level + '\'' + ", analys_level='" + this.analys_level + '\'' + '}';
    }
}
