// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleXmlColumnProperties extends OracleSQLObjectImpl
{
    private SQLName column;
    private OracleXMLTypeStorage storage;
    private Boolean allowNonSchema;
    private Boolean allowAnySchema;
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.storage);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.column = x;
    }
    
    public OracleXMLTypeStorage getStorage() {
        return this.storage;
    }
    
    public void setStorage(final OracleXMLTypeStorage x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storage = x;
    }
    
    public Boolean getAllowNonSchema() {
        return this.allowNonSchema;
    }
    
    public void setAllowNonSchema(final Boolean allowNonSchema) {
        this.allowNonSchema = allowNonSchema;
    }
    
    public Boolean getAllowAnySchema() {
        return this.allowAnySchema;
    }
    
    public void setAllowAnySchema(final Boolean allowAnySchema) {
        this.allowAnySchema = allowAnySchema;
    }
    
    public static class OracleXMLTypeStorage extends OracleSQLObjectImpl
    {
        private boolean secureFile;
        private boolean basicFile;
        private boolean clob;
        private boolean binaryXml;
        private OracleLobParameters lobParameters;
        
        @Override
        public void accept0(final OracleASTVisitor visitor) {
        }
        
        public boolean isSecureFile() {
            return this.secureFile;
        }
        
        public void setSecureFile(final boolean secureFile) {
            this.secureFile = secureFile;
        }
        
        public boolean isBasicFile() {
            return this.basicFile;
        }
        
        public void setBasicFile(final boolean basicFile) {
            this.basicFile = basicFile;
        }
        
        public boolean isClob() {
            return this.clob;
        }
        
        public void setClob(final boolean clob) {
            this.clob = clob;
        }
        
        public boolean isBinaryXml() {
            return this.binaryXml;
        }
        
        public void setBinaryXml(final boolean binaryXml) {
            this.binaryXml = binaryXml;
        }
        
        public OracleLobParameters getLobParameters() {
            return this.lobParameters;
        }
        
        public void setLobParameters(final OracleLobParameters x) {
            if (x != null) {
                x.setParent(this);
            }
            this.lobParameters = x;
        }
    }
}
