// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.runtime.AbstractFunction2;
import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Serializable;
import scala.Product;
import java.io.Reader;
import java.io.InputStream;
import java.io.File;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\r\u001dr!B\u0001\u0003\u0011\u00039\u0011a\u00029bG.\fw-\u001a\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005!IQ\"\u0001\u0002\u0007\u000b)\u0011\u0001\u0012A\u0006\u0003\u000fA\f7m[1hKN\u0011\u0011\u0002\u0004\t\u0003\u001bAi\u0011A\u0004\u0006\u0002\u001f\u0005)1oY1mC&\u0011\u0011C\u0004\u0002\u0007\u0003:L(+\u001a4\t\u000bMIA\u0011\u0001\u000b\u0002\rqJg.\u001b;?)\u00059Q\u0001\u0002\f\n\u0001]\u0011aA\u0013,bYV,\u0007C\u0001\r\u001c\u001d\tA\u0011$\u0003\u0002\u001b\u0005\u00059!j]8o\u0003N#\u0016B\u0001\f\u001d\u0015\tQ\"\u0001C\u0004\u001f\u0013\t\u0007I\u0011A\u0010\u0002\u0011)su\u000e\u001e5j]\u001e,\u0012\u0001\t\b\u00031\u0005J!A\b\u000f\t\r\rJ\u0001\u0015!\u0003!\u0003%Qej\u001c;iS:<\u0007\u0005C\u0004&\u0013\t\u0007I\u0011\u0001\u0014\u0002\u000b)sU\u000f\u001c7\u0016\u0003\u001dr!\u0001\u0007\u0015\n\u0005\u0015b\u0002B\u0002\u0016\nA\u0003%q%\u0001\u0004K\u001dVdG\u000eI\u0003\u0005Y%\u0001QFA\u0004K'R\u0014\u0018N\\4\u0011\u0005aq\u0013B\u0001\u0017\u001d\u0011\u001d\u0001\u0014B1A\u0005\u0002E\nqAS*ue&tw-F\u00013\u001d\tA2'\u0003\u000219!1Q'\u0003Q\u0001\nI\n\u0001BS*ue&tw\rI\u0003\u0005o%\u0001\u0001HA\u0004K\t>,(\r\\3\u0011\u0005aI\u0014BA\u001c\u001d\u0011\u001dY\u0014B1A\u0005\u0002q\nqA\u0013#pk\ndW-F\u0001>\u001d\tAb(\u0003\u0002<9!1\u0001)\u0003Q\u0001\nu\n\u0001B\u0013#pk\ndW\rI\u0003\u0005\u0005&\u00011I\u0001\u0005K\t\u0016\u001c\u0017.\\1m!\tAB)\u0003\u0002C9!9a)\u0003b\u0001\n\u00039\u0015\u0001\u0003&EK\u000eLW.\u00197\u0016\u0003!s!\u0001G%\n\u0005\u0019c\u0002BB&\nA\u0003%\u0001*A\u0005K\t\u0016\u001c\u0017.\\1mA\u0015!Q*\u0003\u0001O\u0005\u0015QEj\u001c8h!\tAr*\u0003\u0002N9!9\u0011+\u0003b\u0001\n\u0003\u0011\u0016!\u0002&M_:<W#A*\u000f\u0005a!\u0016BA)\u001d\u0011\u00191\u0016\u0002)A\u0005'\u00061!\nT8oO\u0002*A\u0001W\u0005\u00013\n!!*\u00138u!\tA\",\u0003\u0002Y9!9A,\u0003b\u0001\n\u0003i\u0016\u0001\u0002&J]R,\u0012A\u0018\b\u00031}K!\u0001\u0018\u000f\t\r\u0005L\u0001\u0015!\u0003_\u0003\u0015Q\u0015J\u001c;!\u000b\u0011\u0019\u0017\u0002\u00013\u0003\u000b)\u0013un\u001c7\u0011\u0005a)\u0017BA2\u001d\u0011\u001d9\u0017B1A\u0005\u0002!\fQA\u0013\"p_2,\u0012!\u001b\b\u00031)L!a\u001a\u000f\t\r1L\u0001\u0015!\u0003j\u0003\u0019Q%i\\8mA\u0015!a.\u0003\u0001p\u0005\u0019Qe)[3mIB\u0011\u0001\u0004]\u0005\u0003]rAqA]\u0005C\u0002\u0013\u00051/\u0001\u0004K\r&,G\u000eZ\u000b\u0002i:\u0011\u0001$^\u0005\u0003erAaa^\u0005!\u0002\u0013!\u0018a\u0002&GS\u0016dG\rI\u0003\u0005s&\u0001!PA\u0004K\u001f\nTWm\u0019;\u0011\u0005aY\u0018BA=\u001d\u0011\u001di\u0018B1A\u0005\u0002y\fqAS(cU\u0016\u001cG/F\u0001\u0000\u001d\rA\u0012\u0011A\u0005\u0003{rAq!!\u0002\nA\u0003%q0\u0001\u0005K\u001f\nTWm\u0019;!\u000b\u0019\tI!\u0003\u0001\u0002\f\t1!*\u0011:sCf\u00042\u0001GA\u0007\u0013\r\tI\u0001\b\u0005\n\u0003#I!\u0019!C\u0001\u0003'\taAS!se\u0006LXCAA\u000b\u001d\rA\u0012qC\u0005\u0004\u0003#a\u0002\u0002CA\u000e\u0013\u0001\u0006I!!\u0006\u0002\u000f)\u000b%O]1zA\u00151\u0011qD\u0005\u0001\u0003C\u0011AAS*fiB\u0019\u0001$a\t\n\u0007\u0005}A\u0004C\u0005\u0002(%\u0011\r\u0011\"\u0001\u0002*\u0005!!jU3u+\t\tYCD\u0002\u0019\u0003[I1!a\n\u001d\u0011!\t\t$\u0003Q\u0001\n\u0005-\u0012!\u0002&TKR\u0004\u0003\"CA\u001b\u0013\t\u0007I\u0011AA\u001c\u0003!!\u0016\u0010]3J]\u001a|WCAA\u001d\u001d\u0011\tY$a\u0012\u000f\t\u0005u\u00121\t\b\u0004\u0011\u0005}\u0012bAA!\u0005\u00059!/\u001a4mK\u000e$\u0018bA\u0001\u0002F)\u0019\u0011\u0011\t\u0002\n\t\u0005U\u0012\u0011\n\u0006\u0004\u0003\u0005\u0015\u0003\u0002CA'\u0013\u0001\u0006I!!\u000f\u0002\u0013QK\b/Z%oM>\u0004SABA)\u0013\u0001\t\u0019F\u0001\u0005UsB,\u0017J\u001c4p!\u0011\tY$!\u0016\n\t\u0005E\u0013\u0011\n\u0004\n\u00033J\u0001\u0013aI\u0001\u00037\u00121\u0003U1sC6,G/\u001a:OC6,'+Z1eKJ\u001cR!a\u0016\r\u0003;\u0002B!a\u000f\u0002`%!\u0011\u0011LA%\u0011\u001d\t\u0019'\u0003C\u0002\u0003K\n\u0001c\u001d;sS:<'GS:p]&s\u0007/\u001e;\u0015\t\u0005\u001d\u0014Q\u000e\t\u0004\u0011\u0005%\u0014bAA6\u0005\tI!j]8o\u0013:\u0004X\u000f\u001e\u0005\t\u0003_\n\t\u00071\u0001\u0002r\u0005\t1\u000f\u0005\u0003\u0002t\u0005edbA\u0007\u0002v%\u0019\u0011q\u000f\b\u0002\rA\u0013X\rZ3g\u0013\u0011\tY(! \u0003\rM#(/\u001b8h\u0015\r\t9H\u0004\u0005\b\u0003\u0003KA1AAB\u0003A\u0011X-\u00193feJR5o\u001c8J]B,H\u000f\u0006\u0003\u0002h\u0005\u0015\u0005\u0002CAD\u0003\u007f\u0002\r!!#\u0002\u0007I$'\u000f\u0005\u0003\u0002\f\u0006UUBAAG\u0015\u0011\ty)!%\u0002\u0005%|'BAAJ\u0003\u0011Q\u0017M^1\n\t\u0005]\u0015Q\u0012\u0002\u0007%\u0016\fG-\u001a:\t\u000f\u0005m\u0015\u0002b\u0001\u0002\u001e\u0006\u00012\u000f\u001e:fC6\u0014$j]8o\u0013:\u0004X\u000f\u001e\u000b\u0005\u0003O\ny\n\u0003\u0005\u0002\"\u0006e\u0005\u0019AAR\u0003\u0019\u0019HO]3b[B!\u00111RAS\u0013\u0011\t9+!$\u0003\u0017%s\u0007/\u001e;TiJ,\u0017-\u001c\u0005\b\u0003WKA1AAW\u000391\u0017\u000e\\33\u0015N|g.\u00138qkR$B!a\u001a\u00020\"A\u0011\u0011WAU\u0001\u0004\t\u0019,\u0001\u0003gS2,\u0007\u0003BAF\u0003kKA!a.\u0002\u000e\n!a)\u001b7f\u0011\u001d\tY,\u0003C\u0002\u0003{\u000b!C\u001b<bYV,''\u001a=ue\u0006\u001cG/\u00192mKR!\u0011qXAc!\rA\u0011\u0011Y\u0005\u0004\u0003\u0007\u0014!AF#yiJ\f7\r^1cY\u0016T5o\u001c8BgRtu\u000eZ3\t\u0011\u0005\u001d\u0017\u0011\u0018a\u0001\u0003\u0013\f!A\u001b<\u0011\u0007\u0005-W#D\u0001\n\u0011\u001d\ty-\u0003C\u0002\u0003#\faB\u001b<bYV,''\\8oC\u0012L7\r\u0006\u0003\u0002T\u0006e\u0007c\u0001\u0005\u0002V&\u0019\u0011q\u001b\u0002\u0003\u001b5{g.\u00193jG*3\u0016\r\\;f\u0011!\t9-!4A\u0002\u0005%\u0007bBAo\u0013\u0011\r\u0011q\\\u0001\rUN|gn\u001e:ji\u0006\u0014G.Z\u000b\u0005\u0003C\fy\u000f\u0006\u0003\u0002d\n-A\u0003BAs\u0005\u0003\u0001R\u0001CAt\u0003WL1!!;\u0003\u00059!vNS:p]^\u0013\u0018\u000e^1cY\u0016\u0004B!!<\u0002p2\u0001A\u0001CAy\u00037\u0014\r!a=\u0003\u0003Q\u000bB!!>\u0002|B\u0019Q\"a>\n\u0007\u0005ehBA\u0004O_RD\u0017N\\4\u0011\u00075\ti0C\u0002\u0002\u0000:\u00111!\u00118z\u0011)\u0011\u0019!a7\u0002\u0002\u0003\u000f!QA\u0001\u000bKZLG-\u001a8dK\u0012\n\u0004#\u0002\u0005\u0003\b\u0005-\u0018b\u0001B\u0005\u0005\t1qK]5uKJD\u0001B!\u0004\u0002\\\u0002\u0007\u00111^\u0001\u0002C\u001a1!\u0011C\u0005A\u0005'\u0011\u0001#T1qa&tw-\u0012=dKB$\u0018n\u001c8\u0014\u0011\t=!Q\u0003B\u0016\u0005c\u0001BAa\u0006\u0003&9!!\u0011\u0004B\u0012\u001d\u0011\u0011YB!\t\u000e\u0005\tu!b\u0001B\u0010\r\u00051AH]8pizJ\u0011aD\u0005\u0003\u00039IAAa\n\u0003*\tIQ\t_2faRLwN\u001c\u0006\u0003\u00039\u00012!\u0004B\u0017\u0013\r\u0011yC\u0004\u0002\b!J|G-^2u!\ri!1G\u0005\u0004\u0005kq!\u0001D*fe&\fG.\u001b>bE2,\u0007b\u0003B\u001d\u0005\u001f\u0011)\u001a!C\u0001\u0005w\t1!\\:h+\t\t\t\bC\u0006\u0003@\t=!\u0011#Q\u0001\n\u0005E\u0014\u0001B7tO\u0002B1Ba\u0011\u0003\u0010\tU\r\u0011\"\u0001\u0003F\u0005)1-Y;tKV\u0011!Q\u0003\u0005\f\u0005\u0013\u0012yA!E!\u0002\u0013\u0011)\"\u0001\u0004dCV\u001cX\r\t\u0005\b'\t=A\u0011\u0001B')\u0019\u0011yE!\u0015\u0003TA!\u00111\u001aB\b\u0011!\u0011IDa\u0013A\u0002\u0005E\u0004\u0002\u0003B\"\u0005\u0017\u0002\rA!\u0006\t\u000fM\u0011y\u0001\"\u0001\u0003XQ!!q\nB-\u0011!\u0011ID!\u0016A\u0002\u0005E\u0004B\u0003B/\u0005\u001f\t\t\u0011\"\u0001\u0003`\u0005!1m\u001c9z)\u0019\u0011yE!\u0019\u0003d!Q!\u0011\bB.!\u0003\u0005\r!!\u001d\t\u0015\t\r#1\fI\u0001\u0002\u0004\u0011)\u0002\u0003\u0006\u0003h\t=\u0011\u0013!C\u0001\u0005S\nabY8qs\u0012\"WMZ1vYR$\u0013'\u0006\u0002\u0003l)\"\u0011\u0011\u000fB7W\t\u0011y\u0007\u0005\u0003\u0003r\tmTB\u0001B:\u0015\u0011\u0011)Ha\u001e\u0002\u0013Ut7\r[3dW\u0016$'b\u0001B=\u001d\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\t\tu$1\u000f\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007B\u0003BA\u0005\u001f\t\n\u0011\"\u0001\u0003\u0004\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u0012TC\u0001BCU\u0011\u0011)B!\u001c\t\u0015\t%%qBA\u0001\n\u0003\u0012Y)A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0003\u0005\u001b\u0003BAa$\u0003\u00166\u0011!\u0011\u0013\u0006\u0005\u0005'\u000b\t*\u0001\u0003mC:<\u0017\u0002BA>\u0005#C!B!'\u0003\u0010\u0005\u0005I\u0011\u0001BN\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\t\u0011i\nE\u0002\u000e\u0005?K1A!)\u000f\u0005\rIe\u000e\u001e\u0005\u000b\u0005K\u0013y!!A\u0005\u0002\t\u001d\u0016A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0005\u0003w\u0014I\u000b\u0003\u0006\u0003,\n\r\u0016\u0011!a\u0001\u0005;\u000b1\u0001\u001f\u00132\u0011)\u0011yKa\u0004\u0002\u0002\u0013\u0005#\u0011W\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\u0011!1\u0017\t\u0007\u0005k\u0013Y,a?\u000e\u0005\t]&b\u0001B]\u001d\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\t\tu&q\u0017\u0002\t\u0013R,'/\u0019;pe\"Q!\u0011\u0019B\b\u0003\u0003%\tAa1\u0002\u0011\r\fg.R9vC2$BA!2\u0003LB\u0019QBa2\n\u0007\t%gBA\u0004C_>dW-\u00198\t\u0015\t-&qXA\u0001\u0002\u0004\tY\u0010\u0003\u0006\u0003P\n=\u0011\u0011!C!\u0005#\f\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0003\u0005;C!B!6\u0003\u0010\u0005\u0005I\u0011\tBl\u0003\u0019)\u0017/^1mgR!!Q\u0019Bm\u0011)\u0011YKa5\u0002\u0002\u0003\u0007\u00111`\u0004\n\u0005;L\u0011\u0011!E\u0001\u0005?\f\u0001#T1qa&tw-\u0012=dKB$\u0018n\u001c8\u0011\t\u0005-'\u0011\u001d\u0004\n\u0005#I\u0011\u0011!E\u0001\u0005G\u001cbA!9\u0003f\nE\u0002C\u0003Bt\u0005[\f\tH!\u0006\u0003P5\u0011!\u0011\u001e\u0006\u0004\u0005Wt\u0011a\u0002:v]RLW.Z\u0005\u0005\u0005_\u0014IOA\tBEN$(/Y2u\rVt7\r^5p]JBqa\u0005Bq\t\u0003\u0011\u0019\u0010\u0006\u0002\u0003`\"Q!q\u001fBq\u0003\u0003%)E!?\u0002\u0011Q|7\u000b\u001e:j]\u001e$\"A!$\t\u0015\tu(\u0011]A\u0001\n\u0003\u0013y0A\u0003baBd\u0017\u0010\u0006\u0004\u0003P\r\u000511\u0001\u0005\t\u0005s\u0011Y\u00101\u0001\u0002r!A!1\tB~\u0001\u0004\u0011)\u0002\u0003\u0006\u0004\b\t\u0005\u0018\u0011!CA\u0007\u0013\tq!\u001e8baBd\u0017\u0010\u0006\u0003\u0004\f\r]\u0001#B\u0007\u0004\u000e\rE\u0011bAB\b\u001d\t1q\n\u001d;j_:\u0004r!DB\n\u0003c\u0012)\"C\u0002\u0004\u00169\u0011a\u0001V;qY\u0016\u0014\u0004BCB\r\u0007\u000b\t\t\u00111\u0001\u0003P\u0005\u0019\u0001\u0010\n\u0019\t\u0015\ru!\u0011]A\u0001\n\u0013\u0019y\"A\u0006sK\u0006$'+Z:pYZ,GCAB\u0011!\u0011\u0011yia\t\n\t\r\u0015\"\u0011\u0013\u0002\u0007\u001f\nTWm\u0019;")
public final class package
{
    public static <T> ToJsonWritable<T> jsonwritable(final T a, final Writer<T> evidence$1) {
        return package$.MODULE$.jsonwritable(a, evidence$1);
    }
    
    public static MonadicJValue jvalue2monadic(final JsonAST.JValue jv) {
        return package$.MODULE$.jvalue2monadic(jv);
    }
    
    public static ExtractableJsonAstNode jvalue2extractable(final JsonAST.JValue jv) {
        return package$.MODULE$.jvalue2extractable(jv);
    }
    
    public static JsonInput file2JsonInput(final File file) {
        return package$.MODULE$.file2JsonInput(file);
    }
    
    public static JsonInput stream2JsonInput(final InputStream stream) {
        return package$.MODULE$.stream2JsonInput(stream);
    }
    
    public static JsonInput reader2JsonInput(final Reader rdr) {
        return package$.MODULE$.reader2JsonInput(rdr);
    }
    
    public static JsonInput string2JsonInput(final String s) {
        return package$.MODULE$.string2JsonInput(s);
    }
    
    public static org.json4s.reflect.package.TypeInfo$ TypeInfo() {
        return package$.MODULE$.TypeInfo();
    }
    
    public static JsonAST.JSet$ JSet() {
        return package$.MODULE$.JSet();
    }
    
    public static JsonAST.JArray$ JArray() {
        return package$.MODULE$.JArray();
    }
    
    public static JsonAST.JObject$ JObject() {
        return package$.MODULE$.JObject();
    }
    
    public static JsonAST.JField$ JField() {
        return package$.MODULE$.JField();
    }
    
    public static JsonAST.JBool$ JBool() {
        return package$.MODULE$.JBool();
    }
    
    public static JsonAST.JInt$ JInt() {
        return package$.MODULE$.JInt();
    }
    
    public static JsonAST.JLong$ JLong() {
        return package$.MODULE$.JLong();
    }
    
    public static JsonAST.JDecimal$ JDecimal() {
        return package$.MODULE$.JDecimal();
    }
    
    public static JsonAST.JDouble$ JDouble() {
        return package$.MODULE$.JDouble();
    }
    
    public static JsonAST.JString$ JString() {
        return package$.MODULE$.JString();
    }
    
    public static JsonAST.JNull$ JNull() {
        return package$.MODULE$.JNull();
    }
    
    public static JsonAST.JNothing$ JNothing() {
        return package$.MODULE$.JNothing();
    }
    
    public static class MappingException extends Exception implements Product, scala.Serializable
    {
        private final String msg;
        private final Exception cause;
        
        public String msg() {
            return this.msg;
        }
        
        public Exception cause() {
            return this.cause;
        }
        
        public MappingException copy(final String msg, final Exception cause) {
            return new MappingException(msg, cause);
        }
        
        public String copy$default$1() {
            return this.msg();
        }
        
        public Exception copy$default$2() {
            return this.cause();
        }
        
        public String productPrefix() {
            return "MappingException";
        }
        
        public int productArity() {
            return 2;
        }
        
        public Object productElement(final int x$1) {
            Serializable s = null;
            switch (x$1) {
                default: {
                    throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
                }
                case 1: {
                    s = this.cause();
                    break;
                }
                case 0: {
                    s = this.msg();
                    break;
                }
            }
            return s;
        }
        
        public Iterator<Object> productIterator() {
            return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
        }
        
        public boolean canEqual(final Object x$1) {
            return x$1 instanceof MappingException;
        }
        
        public int hashCode() {
            return ScalaRunTime$.MODULE$._hashCode((Product)this);
        }
        
        public boolean equals(final Object x$1) {
            if (this != x$1) {
                if (x$1 instanceof MappingException) {
                    final MappingException ex = (MappingException)x$1;
                    final String msg = this.msg();
                    final String msg2 = ex.msg();
                    boolean b = false;
                    Label_0109: {
                        Label_0108: {
                            if (msg == null) {
                                if (msg2 != null) {
                                    break Label_0108;
                                }
                            }
                            else if (!msg.equals(msg2)) {
                                break Label_0108;
                            }
                            final Exception cause = this.cause();
                            final Exception cause2 = ex.cause();
                            if (cause == null) {
                                if (cause2 != null) {
                                    break Label_0108;
                                }
                            }
                            else if (!cause.equals(cause2)) {
                                break Label_0108;
                            }
                            if (ex.canEqual(this)) {
                                b = true;
                                break Label_0109;
                            }
                        }
                        b = false;
                    }
                    if (b) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public MappingException(final String msg, final Exception cause) {
            super(this.msg = msg, this.cause = cause);
            Product$class.$init$((Product)this);
        }
        
        public MappingException(final String msg) {
            this(msg, null);
        }
    }
    
    public static class MappingException$ extends AbstractFunction2<String, Exception, MappingException> implements Serializable
    {
        public static final MappingException$ MODULE$;
        
        static {
            new MappingException$();
        }
        
        public final String toString() {
            return "MappingException";
        }
        
        public MappingException apply(final String msg, final Exception cause) {
            return new MappingException(msg, cause);
        }
        
        public Option<Tuple2<String, Exception>> unapply(final MappingException x$0) {
            return (Option<Tuple2<String, Exception>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.msg(), (Object)x$0.cause())));
        }
        
        private Object readResolve() {
            return MappingException$.MODULE$;
        }
        
        public MappingException$() {
            MODULE$ = this;
        }
    }
    
    public interface ParameterNameReader extends org.json4s.reflect.package.ParameterNameReader
    {
    }
}
