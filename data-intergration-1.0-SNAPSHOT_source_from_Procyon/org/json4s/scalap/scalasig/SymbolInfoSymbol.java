// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Option;
import scala.Some;
import scala.runtime.BoxedUnit;
import org.json4s.scalap.Rule;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u000113Q!\u0001\u0002\u0002\u0002-\u0011\u0001cU=nE>d\u0017J\u001c4p'fl'm\u001c7\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001A\n\u0003\u00011\u0001\"!\u0004\b\u000e\u0003\tI!a\u0004\u0002\u0003\u001dM\u001b\u0017\r\\1TS\u001e\u001c\u00160\u001c2pY\")\u0011\u0003\u0001C\u0001%\u00051A(\u001b8jiz\"\u0012a\u0005\t\u0003\u001b\u0001AQ!\u0006\u0001\u0007\u0002Y\t!b]=nE>d\u0017J\u001c4p+\u00059\u0002CA\u0007\u0019\u0013\tI\"A\u0001\u0006Ts6\u0014w\u000e\\%oM>DQa\u0007\u0001\u0005\u0002q\tQ!\u001a8uef,\u0012!\b\t\u0003=\u0005\u0002\"!D\u0010\n\u0005\u0001\u0012!\u0001C*dC2\f7+[4\n\u0005\tz\"!B#oiJL\b\"\u0002\u0013\u0001\t\u0003)\u0013\u0001\u00028b[\u0016,\u0012A\n\t\u0003O5r!\u0001K\u0016\u000e\u0003%R\u0011AK\u0001\u0006g\u000e\fG.Y\u0005\u0003Y%\na\u0001\u0015:fI\u00164\u0017B\u0001\u00180\u0005\u0019\u0019FO]5oO*\u0011A&\u000b\u0005\u0006c\u0001!\tAM\u0001\u0007a\u0006\u0014XM\u001c;\u0016\u0003M\u00022\u0001\u000b\u001b7\u0013\t)\u0014F\u0001\u0003T_6,\u0007CA\u00078\u0013\tA$A\u0001\u0004Ts6\u0014w\u000e\u001c\u0005\u0006u\u0001!\taO\u0001\bQ\u0006\u001ch\t\\1h)\tat\b\u0005\u0002){%\u0011a(\u000b\u0002\b\u0005>|G.Z1o\u0011\u0015\u0001\u0015\b1\u0001B\u0003\u00111G.Y4\u0011\u0005!\u0012\u0015BA\"*\u0005\u0011auN\\4\t\u0011\u0015\u0003\u0001R1A\u0005\u0002\u0019\u000b\u0001\"\u001b8g_RK\b/Z\u000b\u0002\u000fB\u0011Q\u0002S\u0005\u0003\u0013\n\u0011A\u0001V=qK\"A1\n\u0001E\u0001B\u0003&q)A\u0005j]\u001a|G+\u001f9fA\u0001")
public abstract class SymbolInfoSymbol extends ScalaSigSymbol
{
    private Type infoType;
    private volatile boolean bitmap$0;
    
    private Type infoType$lzycompute() {
        synchronized (this) {
            if (!this.bitmap$0) {
                this.infoType = this.applyRule((Rule<ScalaSig.Entry, ScalaSig.Entry, Type, String>)ScalaSigEntryParsers$.MODULE$.parseEntry((Rule<ScalaSig.Entry, ScalaSig.Entry, A, String>)ScalaSigEntryParsers$.MODULE$.typeEntry(), this.symbolInfo().info()));
                this.bitmap$0 = true;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.infoType;
        }
    }
    
    public abstract SymbolInfo symbolInfo();
    
    @Override
    public ScalaSig.Entry entry() {
        return this.symbolInfo().entry();
    }
    
    @Override
    public String name() {
        return this.symbolInfo().name();
    }
    
    public Some<Symbol> parent() {
        return (Some<Symbol>)new Some((Object)this.symbolInfo().owner());
    }
    
    @Override
    public boolean hasFlag(final long flag) {
        return ((long)this.symbolInfo().flags() & flag) != 0x0L;
    }
    
    public Type infoType() {
        return this.bitmap$0 ? this.infoType : this.infoType$lzycompute();
    }
}
