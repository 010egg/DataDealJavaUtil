// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import java.text.SimpleDateFormat;
import java.lang.reflect.Type;
import scala.Tuple2;
import org.json4s.prefs.EmptyValueStrategy;
import scala.collection.immutable.Set;
import scala.collection.immutable.List;
import org.json4s.reflect.package;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\t\u001dr!B\u0001\u0003\u0011\u00039\u0011A\u0004#fM\u0006,H\u000e\u001e$pe6\fGo\u001d\u0006\u0003\u0007\u0011\taA[:p]R\u001a(\"A\u0003\u0002\u0007=\u0014xm\u0001\u0001\u0011\u0005!IQ\"\u0001\u0002\u0007\u000b)\u0011\u0001\u0012A\u0006\u0003\u001d\u0011+g-Y;mi\u001a{'/\\1ugN\u0019\u0011\u0002\u0004\n\u0011\u00055\u0001R\"\u0001\b\u000b\u0003=\tQa]2bY\u0006L!!\u0005\b\u0003\r\u0005s\u0017PU3g!\tA1CB\u0004\u000b\u0005A\u0005\u0019\u0011\u0001\u000b\u0014\u0007MaQ\u0003\u0005\u0002\t-%\u0011qC\u0001\u0002\b\r>\u0014X.\u0019;t\u0011\u0015I2\u0003\"\u0001\u001b\u0003\u0019!\u0013N\\5uIQ\t1\u0004\u0005\u0002\u000e9%\u0011QD\u0004\u0002\u0005+:LG\u000f\u0003\u0004 '\u0001\u0006I\u0001I\u0001\u0003I\u001a\u00042\u0001C\u0011$\u0013\t\u0011#AA\u0006UQJ,\u0017\r\u001a'pG\u0006d\u0007C\u0001\u0013*\u001b\u0005)#B\u0001\u0014(\u0003\u0011!X\r\u001f;\u000b\u0003!\nAA[1wC&\u0011!&\n\u0002\u0011'&l\u0007\u000f\\3ECR,gi\u001c:nCRDq\u0001L\nC\u0002\u0013\u0005S&A\tusB,\u0007*\u001b8u\r&,G\u000e\u001a(b[\u0016,\u0012A\f\t\u0003_Ir!!\u0004\u0019\n\u0005Er\u0011A\u0002)sK\u0012,g-\u0003\u00024i\t11\u000b\u001e:j]\u001eT!!\r\b\t\rY\u001a\u0002\u0015!\u0003/\u0003I!\u0018\u0010]3IS:$h)[3mI:\u000bW.\u001a\u0011\t\u000fa\u001a\"\u0019!C!s\u0005\u0019\u0002/\u0019:b[\u0016$XM\u001d(b[\u0016\u0014V-\u00193feV\t!\b\u0005\u0002<\u0005:\u0011Ah\u0010\b\u0003\u0011uJ!A\u0010\u0002\u0002\u000fI,g\r\\3di&\u0011\u0001)Q\u0001\ba\u0006\u001c7.Y4f\u0015\tq$!\u0003\u0002D\t\n\u0019\u0002+\u0019:b[\u0016$XM\u001d(b[\u0016\u0014V-\u00193fe*\u0011\u0001)\u0011\u0005\u0007\rN\u0001\u000b\u0011\u0002\u001e\u0002)A\f'/Y7fi\u0016\u0014h*Y7f%\u0016\fG-\u001a:!\u0011\u001dA5C1A\u0005B%\u000b\u0011\u0002^=qK\"Kg\u000e^:\u0016\u0003)\u0003\"\u0001C&\n\u00051\u0013!!\u0003+za\u0016D\u0015N\u001c;t\u0011\u0019q5\u0003)A\u0005\u0015\u0006QA/\u001f9f\u0011&tGo\u001d\u0011\t\u000fA\u001b\"\u0019!C!#\u0006\t2-^:u_6\u001cVM]5bY&TXM]:\u0016\u0003I\u00032a\u0015.^\u001d\t!\u0016L\u0004\u0002V16\taK\u0003\u0002X\r\u00051AH]8pizJ\u0011aD\u0005\u0003\u0001:I!a\u0017/\u0003\t1K7\u000f\u001e\u0006\u0003\u0001:\u0001$AX2\u0011\u0007!y\u0016-\u0003\u0002a\u0005\tQ1+\u001a:jC2L'0\u001a:\u0011\u0005\t\u001cG\u0002\u0001\u0003\nI\u0016\f\t\u0011!A\u0003\u00021\u0014Aa\u0018\u00135m!1am\u0005Q\u0001\n\u001d\f!cY;ti>l7+\u001a:jC2L'0\u001a:tAA\u00191K\u001751\u0005%\\\u0007c\u0001\u0005`UB\u0011!m\u001b\u0003\nI\u0016\f\t\u0011!A\u0003\u00021\f\"!\u001c9\u0011\u00055q\u0017BA8\u000f\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"!D9\n\u0005It!aA!os\"9Ao\u0005b\u0001\n\u0003*\u0018\u0001F2vgR|WnS3z'\u0016\u0014\u0018.\u00197ju\u0016\u00148/F\u0001w!\r\u0019&l\u001e\u0019\u0003qr\u00042\u0001C=|\u0013\tQ(AA\u0007LKf\u001cVM]5bY&TXM\u001d\t\u0003Er$\u0011\" @\u0002\u0002\u0003\u0005)\u0011\u00017\u0003\t}#Cg\u000e\u0005\b\u007fN\u0001\u000b\u0011BA\u0001\u0003U\u0019Wo\u001d;p[.+\u0017pU3sS\u0006d\u0017N_3sg\u0002\u0002Ba\u0015.\u0002\u0004A\"\u0011QAA\u0005!\u0011A\u00110a\u0002\u0011\u0007\t\fI\u0001B\u0005~}\u0006\u0005\t\u0011!B\u0001Y\"I\u0011QB\nC\u0002\u0013\u0005\u0013qB\u0001\u0011M&,G\u000eZ*fe&\fG.\u001b>feN,\"!!\u0005\u0011\tMS\u00161\u0003\t\b\u001b\u0005U\u0011\u0011DA#\u0013\r\t9B\u0004\u0002\u0007)V\u0004H.\u001a\u001a1\t\u0005m\u00111\u0005\t\u0006_\u0005u\u0011\u0011E\u0005\u0004\u0003?!$!B\"mCN\u001c\bc\u00012\u0002$\u0011Y\u0011QEA\u0014\u0003\u0003\u0005\tQ!\u0001m\u0005\u0011yF\u0005\u000e\u001d\t\u0011\u0005%2\u0003)A\u0005\u0003W\t\u0011CZ5fY\u0012\u001cVM]5bY&TXM]:!!\u0011\u0019&,!\f\u0011\u000f5\t)\"a\f\u00028A\"\u0011\u0011GA\u001b!\u0015y\u0013QDA\u001a!\r\u0011\u0017Q\u0007\u0003\f\u0003K\t9#!A\u0001\u0002\u000b\u0005A\u000e\r\u0003\u0002:\u0005\u0005\u0003#\u0002\u0005\u0002<\u0005}\u0012bAA\u001f\u0005\tya)[3mIN+'/[1mSj,'\u000fE\u0002c\u0003\u0003\"1\"a\u0011\u0002(\u0005\u0005\t\u0011!B\u0001Y\n!q\f\n\u001b:a\u0011\t9%a\u0013\u0011\u000b!\tY$!\u0013\u0011\u0007\t\fY\u0005B\u0006\u0002D\u0005\u001d\u0012\u0011!A\u0001\u0006\u0003a\u0007\"CA('\t\u0007I\u0011IA)\u0003-9\u0018M\u001c;t\u0005&<\u0017J\u001c;\u0016\u0005\u0005M\u0003cA\u0007\u0002V%\u0019\u0011q\u000b\b\u0003\u000f\t{w\u000e\\3b]\"A\u00111L\n!\u0002\u0013\t\u0019&\u0001\u0007xC:$8OQ5h\u0013:$\b\u0005C\u0005\u0002`M\u0011\r\u0011\"\u0011\u0002R\u0005yq/\u00198ug\nKw\rR3dS6\fG\u000e\u0003\u0005\u0002dM\u0001\u000b\u0011BA*\u0003A9\u0018M\u001c;t\u0005&<G)Z2j[\u0006d\u0007\u0005C\u0005\u0002hM\u0011\r\u0011\"\u0011\u0002j\u0005Q\u0001O]5nSRLg/Z:\u0016\u0005\u0005-\u0004#B\u0018\u0002n\u0005E\u0014bAA8i\t\u00191+\u001a;\u0011\t\u0005M\u00141P\u0007\u0003\u0003kR1APA<\u0015\r\tIhJ\u0001\u0005Y\u0006tw-\u0003\u0003\u0002~\u0005U$\u0001\u0002+za\u0016D\u0001\"!!\u0014A\u0003%\u00111N\u0001\faJLW.\u001b;jm\u0016\u001c\b\u0005C\u0005\u0002\u0006N\u0011\r\u0011\"\u0011\u0002\b\u0006Q1m\\7qC:LwN\\:\u0016\u0005\u0005%\u0005\u0003B*[\u0003\u0017\u0003b!DA\u000b\u0003\u001bc\u0001\u0007BAH\u0003'\u0003RaLA\u000f\u0003#\u00032AYAJ\t-\t)*a&\u0002\u0002\u0003\u0005)\u0011\u00017\u0003\t}#S\u0007\r\u0005\t\u00033\u001b\u0002\u0015!\u0003\u0002\u001c\u0006Y1m\\7qC:LwN\\:!!\u0011\u0019&,!(\u0011\r5\t)\"a(\ra\u0011\t\t+!*\u0011\u000b=\ni\"a)\u0011\u0007\t\f)\u000bB\u0006\u0002\u0016\u0006]\u0015\u0011!A\u0001\u0006\u0003a\u0007\"CAU'\t\u0007I\u0011IA)\u0003M\u0019HO]5di>\u0003H/[8o!\u0006\u00148/\u001b8h\u0011!\tik\u0005Q\u0001\n\u0005M\u0013\u0001F:ue&\u001cGo\u00149uS>t\u0007+\u0019:tS:<\u0007\u0005C\u0005\u00022N\u0011\r\u0011\"\u0011\u00024\u0006\u0011R-\u001c9usZ\u000bG.^3TiJ\fG/Z4z+\t\t)\f\u0005\u0003\u00028\u0006uVBAA]\u0015\r\tYLA\u0001\u0006aJ,gm]\u0005\u0005\u0003\u007f\u000bIL\u0001\nF[B$\u0018PV1mk\u0016\u001cFO]1uK\u001eL\b\u0002CAb'\u0001\u0006I!!.\u0002'\u0015l\u0007\u000f^=WC2,Xm\u0015;sCR,w-\u001f\u0011\t\u0013\u0005\u001d7C1A\u0005B\u0005E\u0013!C1mY><h*\u001e7m\u0011!\tYm\u0005Q\u0001\n\u0005M\u0013AC1mY><h*\u001e7mA!I\u0011qZ\nC\u0002\u0013\u0005\u0011\u0011[\u0001\u000bI\u0006$XMR8s[\u0006$XCAAj!\rA\u0011Q[\u0005\u0004\u0003/\u0014!A\u0003#bi\u00164uN]7bi\"A\u00111\\\n!\u0002\u0013\t\u0019.A\u0006eCR,gi\u001c:nCR\u0004\u0003bBAp'\u0011E\u0011\u0011]\u0001\u000eI\u0006$XMR8s[\u0006$H/\u001a:\u0016\u0003\rBq!!:\u0014\t\u0003\t9/\u0001\u0005m_N\u001cH.Z:t+\u0005)\u0002bBAv'\u0011\u0005\u0011Q^\u0001\no&$\b\u000eS5oiN$2!FAx\u0011\u001d\t\t0!;A\u0002)\u000bQ\u0001[5oiNDq!!>\n\t\u0003\t90\u0001\u0004=S:LGO\u0010\u000b\u0002\u000f!I\u00111`\u0005C\u0002\u0013\u0005\u0011Q`\u0001\u0004+R\u001bUCAA\u0000!\u0011\u0011\tAa\u0002\u000e\u0005\t\r!b\u0001B\u0003O\u0005!Q\u000f^5m\u0013\u0011\u0011IAa\u0001\u0003\u0011QKW.\u001a.p]\u0016D\u0001B!\u0004\nA\u0003%\u0011q`\u0001\u0005+R\u001b\u0005\u0005C\u0005\u0003\u0012%\u0011\r\u0011\"\u0001\u0003\u0014\u0005aAn\\:tY\u0016\u001c8\u000fR1uKV\t\u0001\u0005C\u0004\u0003\u0018%\u0001\u000b\u0011\u0002\u0011\u0002\u001b1|7o\u001d7fgN$\u0015\r^3!\u0011%\u0011Y\"CA\u0001\n\u0013\u0011i\"A\u0006sK\u0006$'+Z:pYZ,GC\u0001B\u0010!\u0011\u0011\tCa\t\u000e\u0005\u0005]\u0014\u0002\u0002B\u0013\u0003o\u0012aa\u00142kK\u000e$\b")
public interface DefaultFormats extends Formats
{
    ThreadLocal org$json4s$DefaultFormats$$df();
    
    void org$json4s$DefaultFormats$_setter_$org$json4s$DefaultFormats$$df_$eq(final ThreadLocal p0);
    
    void org$json4s$DefaultFormats$_setter_$typeHintFieldName_$eq(final String p0);
    
    void org$json4s$DefaultFormats$_setter_$parameterNameReader_$eq(final package.ParameterNameReader p0);
    
    void org$json4s$DefaultFormats$_setter_$typeHints_$eq(final TypeHints p0);
    
    void org$json4s$DefaultFormats$_setter_$customSerializers_$eq(final List p0);
    
    void org$json4s$DefaultFormats$_setter_$customKeySerializers_$eq(final List p0);
    
    void org$json4s$DefaultFormats$_setter_$fieldSerializers_$eq(final List p0);
    
    void org$json4s$DefaultFormats$_setter_$wantsBigInt_$eq(final boolean p0);
    
    void org$json4s$DefaultFormats$_setter_$wantsBigDecimal_$eq(final boolean p0);
    
    void org$json4s$DefaultFormats$_setter_$primitives_$eq(final Set p0);
    
    void org$json4s$DefaultFormats$_setter_$companions_$eq(final List p0);
    
    void org$json4s$DefaultFormats$_setter_$strictOptionParsing_$eq(final boolean p0);
    
    void org$json4s$DefaultFormats$_setter_$emptyValueStrategy_$eq(final EmptyValueStrategy p0);
    
    void org$json4s$DefaultFormats$_setter_$allowNull_$eq(final boolean p0);
    
    void org$json4s$DefaultFormats$_setter_$dateFormat_$eq(final DateFormat p0);
    
    String typeHintFieldName();
    
    package.ParameterNameReader parameterNameReader();
    
    TypeHints typeHints();
    
    List<Serializer<?>> customSerializers();
    
    List<KeySerializer<?>> customKeySerializers();
    
    List<Tuple2<Class<?>, FieldSerializer<?>>> fieldSerializers();
    
    boolean wantsBigInt();
    
    boolean wantsBigDecimal();
    
    Set<Type> primitives();
    
    List<Tuple2<Class<?>, Object>> companions();
    
    boolean strictOptionParsing();
    
    EmptyValueStrategy emptyValueStrategy();
    
    boolean allowNull();
    
    DateFormat dateFormat();
    
    SimpleDateFormat dateFormatter();
    
    Formats lossless();
    
    Formats withHints(final TypeHints p0);
}
