// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import scala.runtime.BoxedUnit;
import shadeio.univocity.parsers.csv.CsvParser;
import io.pivotal.greenplum.spark.GreenplumCSVFormat$;
import org.slf4j.Logger;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.ResultIterator;
import java.io.InputStream;
import scala.reflect.ScalaSignature;
import io.pivotal.greenplum.spark.Logging;

@ScalaSignature(bytes = "\u0006\u0001}3Q!\u0001\u0002\u0001\u00051\u0011A\u0002R1uC&#XM]1u_JT!a\u0001\u0003\u0002\u001b\u0015DH/\u001a:oC2$\u0018M\u00197f\u0015\t)a!A\u0003ta\u0006\u00148N\u0003\u0002\b\u0011\u0005IqM]3f]BdW/\u001c\u0006\u0003\u0013)\tq\u0001]5w_R\fGNC\u0001\f\u0003\tIwnE\u0002\u0001\u001by\u00012AD\b\u0012\u001b\u0005\u0011\u0011B\u0001\t\u0003\u00051qU\r\u001f;Ji\u0016\u0014\u0018\r^8s!\r\u0011RcF\u0007\u0002')\tA#A\u0003tG\u0006d\u0017-\u0003\u0002\u0017'\t)\u0011I\u001d:bsB\u0011\u0001d\u0007\b\u0003%eI!AG\n\u0002\rA\u0013X\rZ3g\u0013\taRD\u0001\u0004TiJLgn\u001a\u0006\u00035M\u0001\"a\b\u0011\u000e\u0003\u0011I!!\t\u0003\u0003\u000f1{wmZ5oO\"A1\u0005\u0001BC\u0002\u0013\u0005Q%A\u0006j]B,Ho\u0015;sK\u0006l7\u0001A\u000b\u0002MA\u0011qeK\u0007\u0002Q)\u00111\"\u000b\u0006\u0002U\u0005!!.\u0019<b\u0013\ta\u0003FA\u0006J]B,Ho\u0015;sK\u0006l\u0007\u0002\u0003\u0018\u0001\u0005\u0003\u0005\u000b\u0011\u0002\u0014\u0002\u0019%t\u0007/\u001e;TiJ,\u0017-\u001c\u0011\t\u000bA\u0002A\u0011A\u0019\u0002\rqJg.\u001b;?)\t\u00114\u0007\u0005\u0002\u000f\u0001!)1e\fa\u0001M!AQ\u0007\u0001EC\u0002\u0013\u0005a'\u0001\bqCJ\u001cXM]%uKJ\fGo\u001c:\u0016\u0003]\u0002B\u0001O!D\u00136\t\u0011H\u0003\u0002;w\u000511m\\7n_:T!\u0001P\u001f\u0002\u000fA\f'o]3sg*\u0011ahP\u0001\nk:Lgo\\2jifT\u0011\u0001Q\u0001\u0004G>l\u0017B\u0001\":\u00059\u0011Vm];mi&#XM]1u_J\u00042AE\u000bE!\t)\u0005*D\u0001G\u0015\t9\u0015&\u0001\u0003mC:<\u0017B\u0001\u000fG!\tA$*\u0003\u0002Ls\tq\u0001+\u0019:tS:<7i\u001c8uKb$\b\u0002C'\u0001\u0011\u0003\u0005\u000b\u0015B\u001c\u0002\u001fA\f'o]3s\u0013R,'/\u0019;pe\u0002BQa\u0014\u0001\u0005RA\u000bqaZ3u\u001d\u0016DH\u000fF\u0001\u0012\u0011\u0015\u0011\u0006\u0001\"\u0011Q\u0003\u0011qW\r\u001f;\t\u000bQ\u0003A\u0011I+\u0002\u000f!\f7OT3yiV\ta\u000b\u0005\u0002\u0013/&\u0011\u0001l\u0005\u0002\b\u0005>|G.Z1o\u0011\u0015Q\u0006\u0001\"\u0015\\\u0003\u0015\u0019Gn\\:f)\u0005a\u0006C\u0001\n^\u0013\tq6C\u0001\u0003V]&$\b")
public class DataIterator extends NextIterator<String[]> implements Logging
{
    private final InputStream inputStream;
    private ResultIterator<String[], ParsingContext> parserIterator;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    private volatile boolean bitmap$0;
    
    private ResultIterator parserIterator$lzycompute() {
        synchronized (this) {
            if (!this.bitmap$0) {
                this.parserIterator = new CsvParser(GreenplumCSVFormat$.MODULE$.DEFAULT()).iterate(this.inputStream(), GreenplumCSVFormat$.MODULE$.DEFAULT_ENCODING()).iterator();
                this.bitmap$0 = true;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.parserIterator;
        }
    }
    
    @Override
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @TraitSetter
    @Override
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    @Override
    public Logger log() {
        return Logging$class.log(this);
    }
    
    @Override
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    @Override
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    public InputStream inputStream() {
        return this.inputStream;
    }
    
    public ResultIterator<String[], ParsingContext> parserIterator() {
        return this.bitmap$0 ? this.parserIterator : this.parserIterator$lzycompute();
    }
    
    @Override
    public String[] getNext() {
        return this.next();
    }
    
    @Override
    public String[] next() {
        String[] array;
        if (this.parserIterator().hasNext()) {
            array = this.parserIterator().next();
        }
        else {
            this.finished_$eq(true);
            array = null;
        }
        return array;
    }
    
    @Override
    public boolean hasNext() {
        return this.parserIterator().hasNext();
    }
    
    @Override
    public void close() {
        this.inputStream().close();
    }
    
    public DataIterator(final InputStream inputStream) {
        this.inputStream = inputStream;
        Logging$class.$init$(this);
    }
}
