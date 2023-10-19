// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.db;

import java.sql.Statement;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import ch.qos.logback.core.db.dialect.SQLDialectCode;
import ch.qos.logback.core.db.dialect.DBUtil;
import java.lang.reflect.Method;
import ch.qos.logback.core.db.dialect.SQLDialect;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public abstract class DBAppenderBase<E> extends UnsynchronizedAppenderBase<E>
{
    protected ConnectionSource connectionSource;
    protected boolean cnxSupportsGetGeneratedKeys;
    protected boolean cnxSupportsBatchUpdates;
    protected SQLDialect sqlDialect;
    
    public DBAppenderBase() {
        this.cnxSupportsGetGeneratedKeys = false;
        this.cnxSupportsBatchUpdates = false;
    }
    
    protected abstract Method getGeneratedKeysMethod();
    
    protected abstract String getInsertSQL();
    
    @Override
    public void start() {
        if (this.connectionSource == null) {
            throw new IllegalStateException("DBAppender cannot function without a connection source");
        }
        this.sqlDialect = DBUtil.getDialectFromCode(this.connectionSource.getSQLDialectCode());
        if (this.getGeneratedKeysMethod() != null) {
            this.cnxSupportsGetGeneratedKeys = this.connectionSource.supportsGetGeneratedKeys();
        }
        else {
            this.cnxSupportsGetGeneratedKeys = false;
        }
        this.cnxSupportsBatchUpdates = this.connectionSource.supportsBatchUpdates();
        if (!this.cnxSupportsGetGeneratedKeys && this.sqlDialect == null) {
            throw new IllegalStateException("DBAppender cannot function if the JDBC driver does not support getGeneratedKeys method *and* without a specific SQL dialect");
        }
        super.start();
    }
    
    public ConnectionSource getConnectionSource() {
        return this.connectionSource;
    }
    
    public void setConnectionSource(final ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }
    
    public void append(final E eventObject) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2        /* connection */
        //     2: aconst_null    
        //     3: astore_3        /* insertStatement */
        //     4: aload_0         /* this */
        //     5: getfield        ch/qos/logback/core/db/DBAppenderBase.connectionSource:Lch/qos/logback/core/db/ConnectionSource;
        //     8: invokeinterface ch/qos/logback/core/db/ConnectionSource.getConnection:()Ljava/sql/Connection;
        //    13: astore_2        /* connection */
        //    14: aload_2         /* connection */
        //    15: iconst_0       
        //    16: invokeinterface java/sql/Connection.setAutoCommit:(Z)V
        //    21: aload_0         /* this */
        //    22: getfield        ch/qos/logback/core/db/DBAppenderBase.cnxSupportsGetGeneratedKeys:Z
        //    25: ifeq            77
        //    28: ldc             "EVENT_ID"
        //    30: astore          EVENT_ID_COL_NAME
        //    32: aload_0         /* this */
        //    33: getfield        ch/qos/logback/core/db/DBAppenderBase.connectionSource:Lch/qos/logback/core/db/ConnectionSource;
        //    36: invokeinterface ch/qos/logback/core/db/ConnectionSource.getSQLDialectCode:()Lch/qos/logback/core/db/dialect/SQLDialectCode;
        //    41: getstatic       ch/qos/logback/core/db/dialect/SQLDialectCode.POSTGRES_DIALECT:Lch/qos/logback/core/db/dialect/SQLDialectCode;
        //    44: if_acmpne       54
        //    47: aload           EVENT_ID_COL_NAME
        //    49: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    52: astore          EVENT_ID_COL_NAME
        //    54: aload_2         /* connection */
        //    55: aload_0         /* this */
        //    56: invokevirtual   ch/qos/logback/core/db/DBAppenderBase.getInsertSQL:()Ljava/lang/String;
        //    59: iconst_1       
        //    60: anewarray       Ljava/lang/String;
        //    63: dup            
        //    64: iconst_0       
        //    65: aload           EVENT_ID_COL_NAME
        //    67: aastore        
        //    68: invokeinterface java/sql/Connection.prepareStatement:(Ljava/lang/String;[Ljava/lang/String;)Ljava/sql/PreparedStatement;
        //    73: astore_3        /* insertStatement */
        //    74: goto            88
        //    77: aload_2         /* connection */
        //    78: aload_0         /* this */
        //    79: invokevirtual   ch/qos/logback/core/db/DBAppenderBase.getInsertSQL:()Ljava/lang/String;
        //    82: invokeinterface java/sql/Connection.prepareStatement:(Ljava/lang/String;)Ljava/sql/PreparedStatement;
        //    87: astore_3        /* insertStatement */
        //    88: aload_0         /* this */
        //    89: dup            
        //    90: astore          6
        //    92: monitorenter   
        //    93: aload_0         /* this */
        //    94: aload_1         /* eventObject */
        //    95: aload_2         /* connection */
        //    96: aload_3         /* insertStatement */
        //    97: invokevirtual   ch/qos/logback/core/db/DBAppenderBase.subAppend:(Ljava/lang/Object;Ljava/sql/Connection;Ljava/sql/PreparedStatement;)V
        //   100: aload_0         /* this */
        //   101: aload_3         /* insertStatement */
        //   102: aload_2         /* connection */
        //   103: invokevirtual   ch/qos/logback/core/db/DBAppenderBase.selectEventId:(Ljava/sql/PreparedStatement;Ljava/sql/Connection;)J
        //   106: lstore          eventId
        //   108: aload           6
        //   110: monitorexit    
        //   111: goto            122
        //   114: astore          7
        //   116: aload           6
        //   118: monitorexit    
        //   119: aload           7
        //   121: athrow         
        //   122: aload_0         /* this */
        //   123: aload_1         /* eventObject */
        //   124: aload_2         /* connection */
        //   125: lload           eventId
        //   127: invokevirtual   ch/qos/logback/core/db/DBAppenderBase.secondarySubAppend:(Ljava/lang/Object;Ljava/sql/Connection;J)V
        //   130: aload_2         /* connection */
        //   131: invokeinterface java/sql/Connection.commit:()V
        //   136: aload_3         /* insertStatement */
        //   137: invokestatic    ch/qos/logback/core/db/DBHelper.closeStatement:(Ljava/sql/Statement;)V
        //   140: aload_2         /* connection */
        //   141: invokestatic    ch/qos/logback/core/db/DBHelper.closeConnection:(Ljava/sql/Connection;)V
        //   144: goto            181
        //   147: astore          sqle
        //   149: aload_0         /* this */
        //   150: ldc             "problem appending event"
        //   152: aload           sqle
        //   154: invokevirtual   ch/qos/logback/core/db/DBAppenderBase.addError:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   157: aload_3         /* insertStatement */
        //   158: invokestatic    ch/qos/logback/core/db/DBHelper.closeStatement:(Ljava/sql/Statement;)V
        //   161: aload_2         /* connection */
        //   162: invokestatic    ch/qos/logback/core/db/DBHelper.closeConnection:(Ljava/sql/Connection;)V
        //   165: goto            181
        //   168: astore          8
        //   170: aload_3         /* insertStatement */
        //   171: invokestatic    ch/qos/logback/core/db/DBHelper.closeStatement:(Ljava/sql/Statement;)V
        //   174: aload_2         /* connection */
        //   175: invokestatic    ch/qos/logback/core/db/DBHelper.closeConnection:(Ljava/sql/Connection;)V
        //   178: aload           8
        //   180: athrow         
        //   181: return         
        //    Signature:
        //  (TE;)V
        //    StackMapTable: 00 08 FE 00 36 07 00 5E 07 00 5F 07 00 60 FA 00 16 0A FF 00 19 00 07 07 00 61 07 00 62 07 00 5E 07 00 5F 00 00 07 00 62 00 01 07 00 63 FF 00 07 00 05 07 00 61 07 00 62 07 00 5E 07 00 5F 04 00 00 FF 00 18 00 04 07 00 61 07 00 62 07 00 5E 07 00 5F 00 01 07 00 63 54 07 00 63 0C
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  93     111    114    122    Any
        //  114    119    114    122    Any
        //  4      136    147    168    Ljava/lang/Throwable;
        //  4      136    168    181    Any
        //  147    157    168    181    Any
        //  168    170    168    181    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 3 out of bounds for length 3
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.assembler.Collection.get(Collection.java:43)
        //     at java.base/java.util.Collections$UnmodifiableList.get(Collections.java:1308)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1313)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:718)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:494)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected abstract void subAppend(final E p0, final Connection p1, final PreparedStatement p2) throws Throwable;
    
    protected abstract void secondarySubAppend(final E p0, final Connection p1, final long p2) throws Throwable;
    
    protected long selectEventId(final PreparedStatement insertStatement, final Connection connection) throws SQLException, InvocationTargetException {
        ResultSet rs = null;
        Statement idStatement = null;
        try {
            boolean gotGeneratedKeys = false;
            if (this.cnxSupportsGetGeneratedKeys) {
                try {
                    rs = (ResultSet)this.getGeneratedKeysMethod().invoke(insertStatement, (Object[])null);
                    gotGeneratedKeys = true;
                }
                catch (InvocationTargetException ex) {
                    final Throwable target = ex.getTargetException();
                    if (target instanceof SQLException) {
                        throw (SQLException)target;
                    }
                    throw ex;
                }
                catch (IllegalAccessException ex2) {
                    this.addWarn("IllegalAccessException invoking PreparedStatement.getGeneratedKeys", ex2);
                }
            }
            if (!gotGeneratedKeys) {
                idStatement = connection.createStatement();
                idStatement.setMaxRows(1);
                final String selectInsertIdStr = this.sqlDialect.getSelectInsertId();
                rs = idStatement.executeQuery(selectInsertIdStr);
            }
            rs.next();
            final long eventId = rs.getLong(1);
            return eventId;
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException ex3) {}
            }
            DBHelper.closeStatement(idStatement);
        }
    }
    
    @Override
    public void stop() {
        super.stop();
    }
}
