// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import org.apache.spark.scheduler.SparkListenerApplicationEnd;
import io.pivotal.greenplum.spark.conf.GreenplumOptions;
import scala.reflect.ScalaSignature;
import org.apache.spark.scheduler.SparkListener;

@ScalaSignature(bytes = "\u0006\u0001}2A!\u0001\u0002\u0001\u001b\tyBI]8q\u000bb$XM\u001d8bYR\u000b'\r\\3t'B\f'o\u001b'jgR,g.\u001a:\u000b\u0005\r!\u0011!D3yi\u0016\u0014h.\u00197uC\ndWM\u0003\u0002\u0006\r\u0005)1\u000f]1sW*\u0011q\u0001C\u0001\nOJ,WM\u001c9mk6T!!\u0003\u0006\u0002\u000fALgo\u001c;bY*\t1\"\u0001\u0002j_\u000e\u00011C\u0001\u0001\u000f!\tyq#D\u0001\u0011\u0015\t\t\"#A\u0005tG\",G-\u001e7fe*\u0011Qa\u0005\u0006\u0003)U\ta!\u00199bG\",'\"\u0001\f\u0002\u0007=\u0014x-\u0003\u0002\u0019!\ti1\u000b]1sW2K7\u000f^3oKJD\u0001B\u0007\u0001\u0003\u0002\u0003\u0006IaG\u0001\fi\u0006\u0014G.\u001a)sK\u001aL\u0007\u0010\u0005\u0002\u001dE9\u0011Q\u0004I\u0007\u0002=)\tq$A\u0003tG\u0006d\u0017-\u0003\u0002\"=\u00051\u0001K]3eK\u001aL!a\t\u0013\u0003\rM#(/\u001b8h\u0015\t\tc\u0004\u0003\u0005'\u0001\t\u0005\t\u0015!\u0003(\u0003A9'/Z3oa2,Xn\u00149uS>t7\u000f\u0005\u0002)W5\t\u0011F\u0003\u0002+\t\u0005!1m\u001c8g\u0013\ta\u0013F\u0001\tHe\u0016,g\u000e\u001d7v[>\u0003H/[8og\")a\u0006\u0001C\u0001_\u00051A(\u001b8jiz\"2\u0001\r\u001a4!\t\t\u0004!D\u0001\u0003\u0011\u0015QR\u00061\u0001\u001c\u0011\u00151S\u00061\u0001(\u0011\u0015)\u0004\u0001\"\u00117\u0003Ayg.\u00119qY&\u001c\u0017\r^5p]\u0016sG\r\u0006\u00028uA\u0011Q\u0004O\u0005\u0003sy\u0011A!\u00168ji\")1\b\u000ea\u0001y\u0005q\u0011\r\u001d9mS\u000e\fG/[8o\u000b:$\u0007CA\b>\u0013\tq\u0004CA\u000eTa\u0006\u00148\u000eT5ti\u0016tWM]!qa2L7-\u0019;j_:,e\u000e\u001a")
public class DropExternalTablesSparkListener extends SparkListener
{
    private final String tablePrefix;
    private final GreenplumOptions greenplumOptions;
    
    public void onApplicationEnd(final SparkListenerApplicationEnd applicationEnd) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: aload_0         /* this */
        //     4: getfield        io/pivotal/greenplum/spark/externaltable/DropExternalTablesSparkListener.greenplumOptions:Lio/pivotal/greenplum/spark/conf/GreenplumOptions;
        //     7: getstatic       io/pivotal/greenplum/spark/jdbc/ConnectionManager$.MODULE$:Lio/pivotal/greenplum/spark/jdbc/ConnectionManager$;
        //    10: invokevirtual   io/pivotal/greenplum/spark/jdbc/ConnectionManager$.getConnection$default$2:()Z
        //    13: invokevirtual   io/pivotal/greenplum/spark/jdbc/ConnectionManager$.getConnection:(Lio/pivotal/greenplum/spark/conf/GreenplumOptions;Z)Ljava/sql/Connection;
        //    16: astore_2        /* conn */
        //    17: getstatic       io/pivotal/greenplum/spark/jdbc/Jdbc$.MODULE$:Lio/pivotal/greenplum/spark/jdbc/Jdbc$;
        //    20: aload_2         /* conn */
        //    21: aload_0         /* this */
        //    22: getfield        io/pivotal/greenplum/spark/externaltable/DropExternalTablesSparkListener.greenplumOptions:Lio/pivotal/greenplum/spark/conf/GreenplumOptions;
        //    25: invokevirtual   io/pivotal/greenplum/spark/conf/GreenplumOptions.dbSchema:()Ljava/lang/String;
        //    28: aload_0         /* this */
        //    29: getfield        io/pivotal/greenplum/spark/externaltable/DropExternalTablesSparkListener.tablePrefix:Ljava/lang/String;
        //    32: invokevirtual   io/pivotal/greenplum/spark/jdbc/Jdbc$.dropExternalTables:(Ljava/sql/Connection;Ljava/lang/String;Ljava/lang/String;)V
        //    35: aload_2         /* conn */
        //    36: invokeinterface java/sql/Connection.close:()V
        //    41: return         
        //    42: astore_3       
        //    43: aload_2        
        //    44: invokeinterface java/sql/Connection.close:()V
        //    49: aload_3        
        //    50: athrow         
        //    StackMapTable: 00 01 FF 00 2A 00 03 07 00 02 07 00 37 07 00 31 00 01 07 00 39
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  17     35     42     51     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public DropExternalTablesSparkListener(final String tablePrefix, final GreenplumOptions greenplumOptions) {
        this.tablePrefix = tablePrefix;
        this.greenplumOptions = greenplumOptions;
    }
}
