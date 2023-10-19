// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc3;

import org.postgresql.core.Utils;
import java.sql.SQLException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.Savepoint;

public class PSQLSavepoint implements Savepoint
{
    private boolean _isValid;
    private boolean _isNamed;
    private int _id;
    private String _name;
    
    public PSQLSavepoint(final int id) {
        this._isValid = true;
        this._isNamed = false;
        this._id = id;
    }
    
    public PSQLSavepoint(final String name) {
        this._isValid = true;
        this._isNamed = true;
        this._name = name;
    }
    
    @Override
    public int getSavepointId() throws SQLException {
        if (!this._isValid) {
            throw new PSQLException(GT.tr("Cannot reference a savepoint after it has been released."), PSQLState.INVALID_SAVEPOINT_SPECIFICATION);
        }
        if (this._isNamed) {
            throw new PSQLException(GT.tr("Cannot retrieve the id of a named savepoint."), PSQLState.WRONG_OBJECT_TYPE);
        }
        return this._id;
    }
    
    @Override
    public String getSavepointName() throws SQLException {
        if (!this._isValid) {
            throw new PSQLException(GT.tr("Cannot reference a savepoint after it has been released."), PSQLState.INVALID_SAVEPOINT_SPECIFICATION);
        }
        if (!this._isNamed) {
            throw new PSQLException(GT.tr("Cannot retrieve the name of an unnamed savepoint."), PSQLState.WRONG_OBJECT_TYPE);
        }
        return this._name;
    }
    
    public void invalidate() {
        this._isValid = false;
    }
    
    public String getPGName() throws SQLException {
        if (!this._isValid) {
            throw new PSQLException(GT.tr("Cannot reference a savepoint after it has been released."), PSQLState.INVALID_SAVEPOINT_SPECIFICATION);
        }
        if (this._isNamed) {
            return Utils.escapeIdentifier(null, this._name).toString();
        }
        return "JDBC_SAVEPOINT_" + this._id;
    }
}
