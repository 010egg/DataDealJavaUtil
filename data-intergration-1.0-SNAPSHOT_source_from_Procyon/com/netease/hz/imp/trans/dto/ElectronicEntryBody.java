// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.dto;

public class ElectronicEntryBody
{
    private String entryId;
    private String itemNo;
    private String goodsName;
    private String goodsModel;
    private String codeTs;
    private String qty;
    private String unitDesc;
    private String decPrice;
    private String decTotal;
    private String decPriceUsd;
    private String currDesc;
    private String countryCodeDesc;
    private String qty1;
    private String unit1Desc;
    private String qty2;
    private String unit2Desc;
    private String exchangeRate;
    private String dutyModeName;
    
    public String getEntryId() {
        return this.entryId;
    }
    
    public String getItemNo() {
        return this.itemNo;
    }
    
    public String getGoodsName() {
        return this.goodsName;
    }
    
    public String getGoodsModel() {
        return this.goodsModel;
    }
    
    public String getCodeTs() {
        return this.codeTs;
    }
    
    public String getQty() {
        return this.qty;
    }
    
    public String getUnitDesc() {
        return this.unitDesc;
    }
    
    public String getDecPrice() {
        return this.decPrice;
    }
    
    public String getDecTotal() {
        return this.decTotal;
    }
    
    public String getDecPriceUsd() {
        return this.decPriceUsd;
    }
    
    public String getCurrDesc() {
        return this.currDesc;
    }
    
    public String getCountryCodeDesc() {
        return this.countryCodeDesc;
    }
    
    public String getQty1() {
        return this.qty1;
    }
    
    public String getUnit1Desc() {
        return this.unit1Desc;
    }
    
    public String getQty2() {
        return this.qty2;
    }
    
    public String getUnit2Desc() {
        return this.unit2Desc;
    }
    
    public String getExchangeRate() {
        return this.exchangeRate;
    }
    
    public String getDutyModeName() {
        return this.dutyModeName;
    }
    
    public void setEntryId(final String entryId) {
        this.entryId = entryId;
    }
    
    public void setItemNo(final String itemNo) {
        this.itemNo = itemNo;
    }
    
    public void setGoodsName(final String goodsName) {
        this.goodsName = goodsName;
    }
    
    public void setGoodsModel(final String goodsModel) {
        this.goodsModel = goodsModel;
    }
    
    public void setCodeTs(final String codeTs) {
        this.codeTs = codeTs;
    }
    
    public void setQty(final String qty) {
        this.qty = qty;
    }
    
    public void setUnitDesc(final String unitDesc) {
        this.unitDesc = unitDesc;
    }
    
    public void setDecPrice(final String decPrice) {
        this.decPrice = decPrice;
    }
    
    public void setDecTotal(final String decTotal) {
        this.decTotal = decTotal;
    }
    
    public void setDecPriceUsd(final String decPriceUsd) {
        this.decPriceUsd = decPriceUsd;
    }
    
    public void setCurrDesc(final String currDesc) {
        this.currDesc = currDesc;
    }
    
    public void setCountryCodeDesc(final String countryCodeDesc) {
        this.countryCodeDesc = countryCodeDesc;
    }
    
    public void setQty1(final String qty1) {
        this.qty1 = qty1;
    }
    
    public void setUnit1Desc(final String unit1Desc) {
        this.unit1Desc = unit1Desc;
    }
    
    public void setQty2(final String qty2) {
        this.qty2 = qty2;
    }
    
    public void setUnit2Desc(final String unit2Desc) {
        this.unit2Desc = unit2Desc;
    }
    
    public void setExchangeRate(final String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    
    public void setDutyModeName(final String dutyModeName) {
        this.dutyModeName = dutyModeName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ElectronicEntryBody)) {
            return false;
        }
        final ElectronicEntryBody other = (ElectronicEntryBody)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$entryId = this.getEntryId();
        final Object other$entryId = other.getEntryId();
        Label_0065: {
            if (this$entryId == null) {
                if (other$entryId == null) {
                    break Label_0065;
                }
            }
            else if (this$entryId.equals(other$entryId)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$itemNo = this.getItemNo();
        final Object other$itemNo = other.getItemNo();
        Label_0102: {
            if (this$itemNo == null) {
                if (other$itemNo == null) {
                    break Label_0102;
                }
            }
            else if (this$itemNo.equals(other$itemNo)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$goodsName = this.getGoodsName();
        final Object other$goodsName = other.getGoodsName();
        Label_0139: {
            if (this$goodsName == null) {
                if (other$goodsName == null) {
                    break Label_0139;
                }
            }
            else if (this$goodsName.equals(other$goodsName)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$goodsModel = this.getGoodsModel();
        final Object other$goodsModel = other.getGoodsModel();
        Label_0176: {
            if (this$goodsModel == null) {
                if (other$goodsModel == null) {
                    break Label_0176;
                }
            }
            else if (this$goodsModel.equals(other$goodsModel)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$codeTs = this.getCodeTs();
        final Object other$codeTs = other.getCodeTs();
        Label_0213: {
            if (this$codeTs == null) {
                if (other$codeTs == null) {
                    break Label_0213;
                }
            }
            else if (this$codeTs.equals(other$codeTs)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$qty = this.getQty();
        final Object other$qty = other.getQty();
        Label_0250: {
            if (this$qty == null) {
                if (other$qty == null) {
                    break Label_0250;
                }
            }
            else if (this$qty.equals(other$qty)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$unitDesc = this.getUnitDesc();
        final Object other$unitDesc = other.getUnitDesc();
        Label_0287: {
            if (this$unitDesc == null) {
                if (other$unitDesc == null) {
                    break Label_0287;
                }
            }
            else if (this$unitDesc.equals(other$unitDesc)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$decPrice = this.getDecPrice();
        final Object other$decPrice = other.getDecPrice();
        Label_0324: {
            if (this$decPrice == null) {
                if (other$decPrice == null) {
                    break Label_0324;
                }
            }
            else if (this$decPrice.equals(other$decPrice)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$decTotal = this.getDecTotal();
        final Object other$decTotal = other.getDecTotal();
        Label_0361: {
            if (this$decTotal == null) {
                if (other$decTotal == null) {
                    break Label_0361;
                }
            }
            else if (this$decTotal.equals(other$decTotal)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$decPriceUsd = this.getDecPriceUsd();
        final Object other$decPriceUsd = other.getDecPriceUsd();
        Label_0398: {
            if (this$decPriceUsd == null) {
                if (other$decPriceUsd == null) {
                    break Label_0398;
                }
            }
            else if (this$decPriceUsd.equals(other$decPriceUsd)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$currDesc = this.getCurrDesc();
        final Object other$currDesc = other.getCurrDesc();
        Label_0435: {
            if (this$currDesc == null) {
                if (other$currDesc == null) {
                    break Label_0435;
                }
            }
            else if (this$currDesc.equals(other$currDesc)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$countryCodeDesc = this.getCountryCodeDesc();
        final Object other$countryCodeDesc = other.getCountryCodeDesc();
        Label_0472: {
            if (this$countryCodeDesc == null) {
                if (other$countryCodeDesc == null) {
                    break Label_0472;
                }
            }
            else if (this$countryCodeDesc.equals(other$countryCodeDesc)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$qty2 = this.getQty1();
        final Object other$qty2 = other.getQty1();
        Label_0509: {
            if (this$qty2 == null) {
                if (other$qty2 == null) {
                    break Label_0509;
                }
            }
            else if (this$qty2.equals(other$qty2)) {
                break Label_0509;
            }
            return false;
        }
        final Object this$unit1Desc = this.getUnit1Desc();
        final Object other$unit1Desc = other.getUnit1Desc();
        Label_0546: {
            if (this$unit1Desc == null) {
                if (other$unit1Desc == null) {
                    break Label_0546;
                }
            }
            else if (this$unit1Desc.equals(other$unit1Desc)) {
                break Label_0546;
            }
            return false;
        }
        final Object this$qty3 = this.getQty2();
        final Object other$qty3 = other.getQty2();
        Label_0583: {
            if (this$qty3 == null) {
                if (other$qty3 == null) {
                    break Label_0583;
                }
            }
            else if (this$qty3.equals(other$qty3)) {
                break Label_0583;
            }
            return false;
        }
        final Object this$unit2Desc = this.getUnit2Desc();
        final Object other$unit2Desc = other.getUnit2Desc();
        Label_0620: {
            if (this$unit2Desc == null) {
                if (other$unit2Desc == null) {
                    break Label_0620;
                }
            }
            else if (this$unit2Desc.equals(other$unit2Desc)) {
                break Label_0620;
            }
            return false;
        }
        final Object this$exchangeRate = this.getExchangeRate();
        final Object other$exchangeRate = other.getExchangeRate();
        Label_0657: {
            if (this$exchangeRate == null) {
                if (other$exchangeRate == null) {
                    break Label_0657;
                }
            }
            else if (this$exchangeRate.equals(other$exchangeRate)) {
                break Label_0657;
            }
            return false;
        }
        final Object this$dutyModeName = this.getDutyModeName();
        final Object other$dutyModeName = other.getDutyModeName();
        if (this$dutyModeName == null) {
            if (other$dutyModeName == null) {
                return true;
            }
        }
        else if (this$dutyModeName.equals(other$dutyModeName)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ElectronicEntryBody;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $entryId = this.getEntryId();
        result = result * 59 + (($entryId == null) ? 43 : $entryId.hashCode());
        final Object $itemNo = this.getItemNo();
        result = result * 59 + (($itemNo == null) ? 43 : $itemNo.hashCode());
        final Object $goodsName = this.getGoodsName();
        result = result * 59 + (($goodsName == null) ? 43 : $goodsName.hashCode());
        final Object $goodsModel = this.getGoodsModel();
        result = result * 59 + (($goodsModel == null) ? 43 : $goodsModel.hashCode());
        final Object $codeTs = this.getCodeTs();
        result = result * 59 + (($codeTs == null) ? 43 : $codeTs.hashCode());
        final Object $qty = this.getQty();
        result = result * 59 + (($qty == null) ? 43 : $qty.hashCode());
        final Object $unitDesc = this.getUnitDesc();
        result = result * 59 + (($unitDesc == null) ? 43 : $unitDesc.hashCode());
        final Object $decPrice = this.getDecPrice();
        result = result * 59 + (($decPrice == null) ? 43 : $decPrice.hashCode());
        final Object $decTotal = this.getDecTotal();
        result = result * 59 + (($decTotal == null) ? 43 : $decTotal.hashCode());
        final Object $decPriceUsd = this.getDecPriceUsd();
        result = result * 59 + (($decPriceUsd == null) ? 43 : $decPriceUsd.hashCode());
        final Object $currDesc = this.getCurrDesc();
        result = result * 59 + (($currDesc == null) ? 43 : $currDesc.hashCode());
        final Object $countryCodeDesc = this.getCountryCodeDesc();
        result = result * 59 + (($countryCodeDesc == null) ? 43 : $countryCodeDesc.hashCode());
        final Object $qty2 = this.getQty1();
        result = result * 59 + (($qty2 == null) ? 43 : $qty2.hashCode());
        final Object $unit1Desc = this.getUnit1Desc();
        result = result * 59 + (($unit1Desc == null) ? 43 : $unit1Desc.hashCode());
        final Object $qty3 = this.getQty2();
        result = result * 59 + (($qty3 == null) ? 43 : $qty3.hashCode());
        final Object $unit2Desc = this.getUnit2Desc();
        result = result * 59 + (($unit2Desc == null) ? 43 : $unit2Desc.hashCode());
        final Object $exchangeRate = this.getExchangeRate();
        result = result * 59 + (($exchangeRate == null) ? 43 : $exchangeRate.hashCode());
        final Object $dutyModeName = this.getDutyModeName();
        result = result * 59 + (($dutyModeName == null) ? 43 : $dutyModeName.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ElectronicEntryBody(entryId=" + this.getEntryId() + ", itemNo=" + this.getItemNo() + ", goodsName=" + this.getGoodsName() + ", goodsModel=" + this.getGoodsModel() + ", codeTs=" + this.getCodeTs() + ", qty=" + this.getQty() + ", unitDesc=" + this.getUnitDesc() + ", decPrice=" + this.getDecPrice() + ", decTotal=" + this.getDecTotal() + ", decPriceUsd=" + this.getDecPriceUsd() + ", currDesc=" + this.getCurrDesc() + ", countryCodeDesc=" + this.getCountryCodeDesc() + ", qty1=" + this.getQty1() + ", unit1Desc=" + this.getUnit1Desc() + ", qty2=" + this.getQty2() + ", unit2Desc=" + this.getUnit2Desc() + ", exchangeRate=" + this.getExchangeRate() + ", dutyModeName=" + this.getDutyModeName() + ")";
    }
}
