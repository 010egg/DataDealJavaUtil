// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.spi;

import java.util.ArrayList;
import com.alibaba.druid.wall.WallUpdateCheckItem;
import com.alibaba.druid.wall.Violation;
import java.util.List;
import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallVisitor;

public abstract class WallVisitorBase implements WallVisitor
{
    protected final WallConfig config;
    protected final WallProvider provider;
    protected final List<Violation> violations;
    protected boolean sqlModified;
    protected boolean sqlEndOfComment;
    protected List<WallUpdateCheckItem> updateCheckItems;
    
    public WallVisitorBase(final WallProvider provider) {
        this.violations = new ArrayList<Violation>();
        this.sqlModified = false;
        this.sqlEndOfComment = false;
        this.config = provider.getConfig();
        this.provider = provider;
    }
    
    @Override
    public boolean isSqlModified() {
        return this.sqlModified;
    }
    
    @Override
    public void setSqlModified(final boolean sqlModified) {
        this.sqlModified = sqlModified;
    }
    
    @Override
    public WallProvider getProvider() {
        return this.provider;
    }
    
    @Override
    public WallConfig getConfig() {
        return this.config;
    }
    
    @Override
    public void addViolation(final Violation violation) {
        this.violations.add(violation);
    }
    
    @Override
    public List<Violation> getViolations() {
        return this.violations;
    }
    
    @Override
    public boolean isSqlEndOfComment() {
        return this.sqlEndOfComment;
    }
    
    @Override
    public void setSqlEndOfComment(final boolean sqlEndOfComment) {
        this.sqlEndOfComment = sqlEndOfComment;
    }
    
    @Override
    public void addWallUpdateCheckItem(final WallUpdateCheckItem item) {
        if (this.updateCheckItems == null) {
            this.updateCheckItems = new ArrayList<WallUpdateCheckItem>();
        }
        this.updateCheckItems.add(item);
    }
    
    @Override
    public List<WallUpdateCheckItem> getUpdateCheckItems() {
        return this.updateCheckItems;
    }
    
    @Override
    public boolean isDenyTable(final String name) {
        return this.config.isTableCheck() && !this.provider.checkDenyTable(name);
    }
}
