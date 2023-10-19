// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleAlterTablespaceAddDataFile extends OracleSQLObjectImpl implements OracleAlterTablespaceItem
{
    private List<OracleFileSpecification> files;
    
    public OracleAlterTablespaceAddDataFile() {
        this.files = new ArrayList<OracleFileSpecification>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.files);
        }
        visitor.endVisit(this);
    }
    
    public List<OracleFileSpecification> getFiles() {
        return this.files;
    }
    
    public void setFiles(final List<OracleFileSpecification> files) {
        this.files = files;
    }
}
