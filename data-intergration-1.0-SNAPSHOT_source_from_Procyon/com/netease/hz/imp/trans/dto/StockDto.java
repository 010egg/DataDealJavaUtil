// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.dto;

public class StockDto
{
    private String stock_name;
    private String stock_code;
    private Double latest_qttn;
    private String last_update_time;
    private Double ups_downs_the_frhd;
    private Double price_limit;
    private Double trdng_vlm;
    private Double trdng_amt;
    private Double crcltn_mrkt_value;
    private Double ttl_mrkt_value;
    
    public String getStock_name() {
        return this.stock_name;
    }
    
    public String getStock_code() {
        return this.stock_code;
    }
    
    public Double getLatest_qttn() {
        return this.latest_qttn;
    }
    
    public String getLast_update_time() {
        return this.last_update_time;
    }
    
    public Double getUps_downs_the_frhd() {
        return this.ups_downs_the_frhd;
    }
    
    public Double getPrice_limit() {
        return this.price_limit;
    }
    
    public Double getTrdng_vlm() {
        return this.trdng_vlm;
    }
    
    public Double getTrdng_amt() {
        return this.trdng_amt;
    }
    
    public Double getCrcltn_mrkt_value() {
        return this.crcltn_mrkt_value;
    }
    
    public Double getTtl_mrkt_value() {
        return this.ttl_mrkt_value;
    }
    
    public void setStock_name(final String stock_name) {
        this.stock_name = stock_name;
    }
    
    public void setStock_code(final String stock_code) {
        this.stock_code = stock_code;
    }
    
    public void setLatest_qttn(final Double latest_qttn) {
        this.latest_qttn = latest_qttn;
    }
    
    public void setLast_update_time(final String last_update_time) {
        this.last_update_time = last_update_time;
    }
    
    public void setUps_downs_the_frhd(final Double ups_downs_the_frhd) {
        this.ups_downs_the_frhd = ups_downs_the_frhd;
    }
    
    public void setPrice_limit(final Double price_limit) {
        this.price_limit = price_limit;
    }
    
    public void setTrdng_vlm(final Double trdng_vlm) {
        this.trdng_vlm = trdng_vlm;
    }
    
    public void setTrdng_amt(final Double trdng_amt) {
        this.trdng_amt = trdng_amt;
    }
    
    public void setCrcltn_mrkt_value(final Double crcltn_mrkt_value) {
        this.crcltn_mrkt_value = crcltn_mrkt_value;
    }
    
    public void setTtl_mrkt_value(final Double ttl_mrkt_value) {
        this.ttl_mrkt_value = ttl_mrkt_value;
    }
    
    @Override
    public String toString() {
        return "StockDto{stock_name='" + this.stock_name + '\'' + ", stock_code='" + this.stock_code + '\'' + ", latest_qttn=" + this.latest_qttn + ", last_update_time='" + this.last_update_time + '\'' + ", ups_downs_the_frhd=" + this.ups_downs_the_frhd + ", price_limit=" + this.price_limit + ", trdng_vlm=" + this.trdng_vlm + ", trdng_amt=" + this.trdng_amt + ", crcltn_mrkt_value=" + this.crcltn_mrkt_value + ", ttl_mrkt_value=" + this.ttl_mrkt_value + '}';
    }
}
