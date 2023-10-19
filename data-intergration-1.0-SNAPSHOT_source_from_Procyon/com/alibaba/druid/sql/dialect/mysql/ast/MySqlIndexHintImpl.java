// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast;

import com.alibaba.druid.sql.ast.SQLHint;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;

public abstract class MySqlIndexHintImpl extends MySqlObjectImpl implements MySqlIndexHint
{
    private Option option;
    private List<SQLName> indexList;
    
    public MySqlIndexHintImpl() {
        this.indexList = new ArrayList<SQLName>();
    }
    
    @Override
    public abstract void accept0(final MySqlASTVisitor p0);
    
    public Option getOption() {
        return this.option;
    }
    
    public void setOption(final Option option) {
        this.option = option;
    }
    
    public List<SQLName> getIndexList() {
        return this.indexList;
    }
    
    public void setIndexList(final List<SQLName> indexList) {
        this.indexList = indexList;
    }
    
    @Override
    public abstract MySqlIndexHintImpl clone();
    
    public void cloneTo(final MySqlIndexHintImpl x) {
        x.option = this.option;
        for (final SQLName name : this.indexList) {
            final SQLName name2 = name.clone();
            name2.setParent(x);
            x.indexList.add(name2);
        }
    }
}
