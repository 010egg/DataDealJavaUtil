// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r3\u0001\"\u0001\u0002\u0011\u0002\u0007\u0005!A\u0002\u0002\n\u001b\u0016\u0014x-\u001a#faNT!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sON\u0019\u0001aB\u0007\u0011\u0005!YQ\"A\u0005\u000b\u0003)\tQa]2bY\u0006L!\u0001D\u0005\u0003\r\u0005s\u0017PU3g!\tqq\"D\u0001\u0003\u0013\t\u0001\"AA\nM_^\u0004&/[8sSRLX*\u001a:hK\u0012+\u0007\u000fC\u0003\u0013\u0001\u0011\u0005A#\u0001\u0004%S:LG\u000fJ\u0002\u0001)\u0005)\u0002C\u0001\u0005\u0017\u0013\t9\u0012B\u0001\u0003V]&$x!B\r\u0001\u0011\u0007Q\u0012aA8p_B\u00111\u0004H\u0007\u0002\u0001\u0019)Q\u0004\u0001E\u0001=\t\u0019qn\\8\u0014\u0007q9q\u0004E\u0003\u000fA\t\u0012#%\u0003\u0002\"\u0005\tAQ*\u001a:hK\u0012+\u0007\u000f\u0005\u0002$M9\u0011a\u0002J\u0005\u0003K\t\tqAS:p]\u0006\u001bF+\u0003\u0002(Q\t9!j\u00142kK\u000e$(BA\u0013\u0003\u0011\u0015QC\u0004\"\u0001,\u0003\u0019a\u0014N\\5u}Q\t!\u0004C\u0003.9\u0011\u0005a&A\u0003baBd\u0017\u0010F\u0002#_EBQ\u0001\r\u0017A\u0002\t\nAA^1mc!)!\u0007\fa\u0001E\u0005!a/\u001973\u000f\u0015!\u0004\u0001c\u00016\u0003\r\t\u0017-\u0019\t\u00037Y2Qa\u000e\u0001\t\u0002a\u00121!Y1b'\r1t!\u000f\t\u0006\u001d\u0001R$H\u000f\t\u0003GmJ!\u0001\u0010\u0015\u0003\r)\u000b%O]1z\u0011\u0015Qc\u0007\"\u0001?)\u0005)\u0004\"B\u00177\t\u0003\u0001Ec\u0001\u001eB\u0005\")\u0001g\u0010a\u0001u!)!g\u0010a\u0001u\u0001")
public interface MergeDeps extends LowPriorityMergeDep
{
    ooo$ ooo();
    
    aaa$ aaa();
    
    public class ooo$ implements MergeDep<JsonAST.JObject, JsonAST.JObject, JsonAST.JObject>
    {
        @Override
        public JsonAST.JObject apply(final JsonAST.JObject val1, final JsonAST.JObject val2) {
            return new JsonAST.JObject(Merge$.MODULE$.mergeFields(val1.obj(), val2.obj()));
        }
        
        public ooo$(final MergeDeps $outer) {
        }
    }
    
    public class aaa$ implements MergeDep<JsonAST.JArray, JsonAST.JArray, JsonAST.JArray>
    {
        @Override
        public JsonAST.JArray apply(final JsonAST.JArray val1, final JsonAST.JArray val2) {
            return new JsonAST.JArray(Merge$.MODULE$.mergeVals(val1.arr(), val2.arr()));
        }
        
        public aaa$(final MergeDeps $outer) {
        }
    }
}
