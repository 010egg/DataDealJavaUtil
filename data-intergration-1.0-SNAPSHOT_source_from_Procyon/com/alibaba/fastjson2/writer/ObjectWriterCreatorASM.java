// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.LinkedHashMap;
import com.alibaba.fastjson2.codec.FieldInfo;
import java.util.Map;
import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import com.alibaba.fastjson2.util.JDKUtils;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.UUID;
import java.math.BigInteger;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.math.BigDecimal;
import com.alibaba.fastjson2.internal.asm.MethodWriter;
import java.io.Serializable;
import com.alibaba.fastjson2.internal.asm.Label;
import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import java.util.function.Function;
import com.alibaba.fastjson2.internal.asm.ClassWriter;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.util.List;
import com.alibaba.fastjson2.util.IOUtils;
import com.alibaba.fastjson2.util.DynamicClassLoader;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectWriterCreatorASM extends ObjectWriterCreator
{
    static final boolean DISABLE_STRING_UNSAFE_GET;
    public static final ObjectWriterCreatorASM INSTANCE;
    protected static final AtomicLong seed;
    protected final DynamicClassLoader classLoader;
    static final String[] INTERFACES;
    static final String METHOD_DESC_WRITE_VALUE;
    static final String METHOD_DESC_WRITE;
    static final String METHOD_DESC_WRITE_FIELD_NAME;
    static final String METHOD_DESC_WRITE_OBJECT;
    static final String METHOD_DESC_WRITE_J;
    static final String METHOD_DESC_WRITE_D;
    static final String METHOD_DESC_WRITE_F;
    static final String METHOD_DESC_WRITE_DATE_WITH_FIELD_NAME;
    static final String METHOD_DESC_WRITE_Z;
    static final String METHOD_DESC_WRITE_ZARRAY;
    static final String METHOD_DESC_WRITE_FARRAY;
    static final String METHOD_DESC_WRITE_DARRAY;
    static final String METHOD_DESC_WRITE_I;
    static final String METHOD_DESC_WRITE_SArray;
    static final String METHOD_DESC_WRITE_BArray;
    static final String METHOD_DESC_WRITE_CArray;
    static final String METHOD_DESC_WRITE_ENUM;
    static final String METHOD_DESC_WRITE_LIST;
    static final String METHOD_DESC_FIELD_WRITE_OBJECT;
    static final String METHOD_DESC_GET_OBJECT_WRITER;
    static final String METHOD_DESC_GET_ITEM_WRITER;
    static final String METHOD_DESC_WRITE_TYPE_INFO;
    static final String METHOD_DESC_HAS_FILTER;
    static final String METHOD_DESC_SET_PATH2;
    static final String METHOD_DESC_WRITE_REFERENCE = "(Ljava/lang/String;)V";
    static final String METHOD_DESC_WRITE_CLASS_INFO;
    static final int THIS = 0;
    static final int JSON_WRITER = 1;
    static final String NOT_WRITE_DEFAULT_VALUE = "WRITE_DEFAULT_VALUE";
    static final String WRITE_NULLS = "WRITE_NULLS";
    static final String CONTEXT_FEATURES = "CONTEXT_FEATURES";
    static final String UTF8_DIRECT = "UTF8_DIRECT";
    
    static String fieldWriter(final int i) {
        switch (i) {
            case 0: {
                return "fieldWriter0";
            }
            case 1: {
                return "fieldWriter1";
            }
            case 2: {
                return "fieldWriter2";
            }
            case 3: {
                return "fieldWriter3";
            }
            case 4: {
                return "fieldWriter4";
            }
            case 5: {
                return "fieldWriter5";
            }
            case 6: {
                return "fieldWriter6";
            }
            case 7: {
                return "fieldWriter7";
            }
            case 8: {
                return "fieldWriter8";
            }
            case 9: {
                return "fieldWriter9";
            }
            case 10: {
                return "fieldWriter10";
            }
            case 11: {
                return "fieldWriter11";
            }
            case 12: {
                return "fieldWriter12";
            }
            case 13: {
                return "fieldWriter13";
            }
            case 14: {
                return "fieldWriter14";
            }
            case 15: {
                return "fieldWriter15";
            }
            default: {
                final String base = "fieldWriter";
                final int size = IOUtils.stringSize(i);
                final char[] chars = new char[base.length() + size];
                base.getChars(0, base.length(), chars, 0);
                IOUtils.getChars(i, chars.length, chars);
                return new String(chars);
            }
        }
    }
    
    public ObjectWriterCreatorASM() {
        this.classLoader = new DynamicClassLoader();
    }
    
    public ObjectWriterCreatorASM(final ClassLoader classLoader) {
        this.classLoader = (DynamicClassLoader)((classLoader instanceof DynamicClassLoader) ? classLoader : new DynamicClassLoader(classLoader));
    }
    
    @Override
    public ObjectWriter createObjectWriter(final List<FieldWriter> fieldWriters) {
        boolean allFunction = true;
        for (int i = 0; i < fieldWriters.size(); ++i) {
            if (fieldWriters.get(i).getFunction() == null) {
                allFunction = false;
                break;
            }
        }
        if (!allFunction) {
            return super.createObjectWriter(fieldWriters);
        }
        final BeanInfo beanInfo = new BeanInfo();
        return this.jitWriter(null, JSONFactory.getDefaultObjectWriterProvider(), beanInfo, fieldWriters, 0L);
    }
    
    @Override
    public ObjectWriter createObjectWriter(final Class objectClass, final long features, final ObjectWriterProvider provider) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Class.getModifiers:()I
        //     4: istore          modifiers
        //     6: aload_0         /* this */
        //     7: getfield        com/alibaba/fastjson2/writer/ObjectWriterCreatorASM.classLoader:Lcom/alibaba/fastjson2/util/DynamicClassLoader;
        //    10: aload_1         /* objectClass */
        //    11: invokevirtual   com/alibaba/fastjson2/util/DynamicClassLoader.isExternalClass:(Ljava/lang/Class;)Z
        //    14: istore          externalClass
        //    16: iload           modifiers
        //    18: invokestatic    java/lang/reflect/Modifier.isPublic:(I)Z
        //    21: istore          publicClass
        //    23: new             Lcom/alibaba/fastjson2/codec/BeanInfo;
        //    26: dup            
        //    27: invokespecial   com/alibaba/fastjson2/codec/BeanInfo.<init>:()V
        //    30: astore          beanInfo
        //    32: aload           provider
        //    34: aload           beanInfo
        //    36: aload_1         /* objectClass */
        //    37: invokevirtual   com/alibaba/fastjson2/writer/ObjectWriterProvider.getBeanInfo:(Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/lang/Class;)V
        //    40: aload           beanInfo
        //    42: getfield        com/alibaba/fastjson2/codec/BeanInfo.serializer:Ljava/lang/Class;
        //    45: ifnull          108
        //    48: ldc             Lcom/alibaba/fastjson2/writer/ObjectWriter;.class
        //    50: aload           beanInfo
        //    52: getfield        com/alibaba/fastjson2/codec/BeanInfo.serializer:Ljava/lang/Class;
        //    55: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
        //    58: ifeq            108
        //    61: aload           beanInfo
        //    63: getfield        com/alibaba/fastjson2/codec/BeanInfo.serializer:Ljava/lang/Class;
        //    66: iconst_0       
        //    67: anewarray       Ljava/lang/Class;
        //    70: invokevirtual   java/lang/Class.getDeclaredConstructor:([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //    73: astore          constructor
        //    75: aload           constructor
        //    77: iconst_1       
        //    78: invokevirtual   java/lang/reflect/Constructor.setAccessible:(Z)V
        //    81: aload           constructor
        //    83: iconst_0       
        //    84: anewarray       Ljava/lang/Object;
        //    87: invokevirtual   java/lang/reflect/Constructor.newInstance:([Ljava/lang/Object;)Ljava/lang/Object;
        //    90: checkcast       Lcom/alibaba/fastjson2/writer/ObjectWriter;
        //    93: areturn        
        //    94: astore          e
        //    96: new             Lcom/alibaba/fastjson2/JSONException;
        //    99: dup            
        //   100: ldc             "create serializer error"
        //   102: aload           e
        //   104: invokespecial   com/alibaba/fastjson2/JSONException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   107: athrow         
        //   108: aload           beanInfo
        //   110: getfield        com/alibaba/fastjson2/codec/BeanInfo.writerFeatures:J
        //   113: lstore          beanFeatures
        //   115: aload           beanInfo
        //   117: getfield        com/alibaba/fastjson2/codec/BeanInfo.seeAlso:[Ljava/lang/Class;
        //   120: ifnull          138
        //   123: lload           beanFeatures
        //   125: getstatic       com/alibaba/fastjson2/JSONWriter$Feature.WriteClassName:Lcom/alibaba/fastjson2/JSONWriter$Feature;
        //   128: getfield        com/alibaba/fastjson2/JSONWriter$Feature.mask:J
        //   131: ldc2_w          -1
        //   134: lxor           
        //   135: land           
        //   136: lstore          beanFeatures
        //   138: lload_2         /* features */
        //   139: lload           beanFeatures
        //   141: lor            
        //   142: lstore          writerFieldFeatures
        //   144: lload           writerFieldFeatures
        //   146: getstatic       com/alibaba/fastjson2/JSONWriter$Feature.FieldBased:Lcom/alibaba/fastjson2/JSONWriter$Feature;
        //   149: getfield        com/alibaba/fastjson2/JSONWriter$Feature.mask:J
        //   152: land           
        //   153: lconst_0       
        //   154: lcmp           
        //   155: ifeq            169
        //   158: aload_1         /* objectClass */
        //   159: invokevirtual   java/lang/Class.isInterface:()Z
        //   162: ifne            169
        //   165: iconst_1       
        //   166: goto            170
        //   169: iconst_0       
        //   170: istore          fieldBased
        //   172: ldc             Ljava/lang/Throwable;.class
        //   174: aload_1         /* objectClass */
        //   175: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
        //   178: ifne            188
        //   181: aload_1         /* objectClass */
        //   182: invokestatic    com/alibaba/fastjson2/util/BeanUtils.isExtendedMap:(Ljava/lang/Class;)Z
        //   185: ifeq            197
        //   188: aload_0         /* this */
        //   189: aload_1         /* objectClass */
        //   190: lload_2         /* features */
        //   191: aload           provider
        //   193: invokespecial   com/alibaba/fastjson2/writer/ObjectWriterCreator.createObjectWriter:(Ljava/lang/Class;JLcom/alibaba/fastjson2/writer/ObjectWriterProvider;)Lcom/alibaba/fastjson2/writer/ObjectWriter;
        //   196: areturn        
        //   197: aload_1         /* objectClass */
        //   198: invokestatic    com/alibaba/fastjson2/util/BeanUtils.isRecord:(Ljava/lang/Class;)Z
        //   201: istore          record
        //   203: new             Ljava/util/LinkedHashMap;
        //   206: dup            
        //   207: invokespecial   java/util/LinkedHashMap.<init>:()V
        //   210: astore          fieldWriterMap
        //   212: iload           fieldBased
        //   214: ifeq            222
        //   217: iload           record
        //   219: ifeq            441
        //   222: new             Ljava/util/ArrayList;
        //   225: dup            
        //   226: invokespecial   java/util/ArrayList.<init>:()V
        //   229: astore          fieldWriterList
        //   231: iconst_0       
        //   232: istore          fieldWritersCreated
        //   234: aload           provider
        //   236: getfield        com/alibaba/fastjson2/writer/ObjectWriterProvider.modules:Ljava/util/List;
        //   239: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   244: astore          19
        //   246: aload           19
        //   248: invokeinterface java/util/Iterator.hasNext:()Z
        //   253: ifeq            291
        //   256: aload           19
        //   258: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   263: checkcast       Lcom/alibaba/fastjson2/modules/ObjectWriterModule;
        //   266: astore          module
        //   268: aload           module
        //   270: aload_0         /* this */
        //   271: aload_1         /* objectClass */
        //   272: aload           fieldWriterList
        //   274: invokeinterface com/alibaba/fastjson2/modules/ObjectWriterModule.createFieldWriters:(Lcom/alibaba/fastjson2/writer/ObjectWriterCreator;Ljava/lang/Class;Ljava/util/List;)Z
        //   279: ifeq            288
        //   282: iconst_1       
        //   283: istore          fieldWritersCreated
        //   285: goto            291
        //   288: goto            246
        //   291: iload           fieldWritersCreated
        //   293: ifeq            370
        //   296: aload           fieldWriterList
        //   298: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   303: astore          19
        //   305: aload           19
        //   307: invokeinterface java/util/Iterator.hasNext:()Z
        //   312: ifeq            367
        //   315: aload           19
        //   317: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   322: checkcast       Lcom/alibaba/fastjson2/writer/FieldWriter;
        //   325: astore          fieldWriter
        //   327: aload           fieldWriter
        //   329: getfield        com/alibaba/fastjson2/writer/FieldWriter.method:Ljava/lang/reflect/Method;
        //   332: astore          method
        //   334: aload           method
        //   336: ifnonnull       349
        //   339: aload_0         /* this */
        //   340: aload_1         /* objectClass */
        //   341: lload           writerFieldFeatures
        //   343: aload           provider
        //   345: invokespecial   com/alibaba/fastjson2/writer/ObjectWriterCreator.createObjectWriter:(Ljava/lang/Class;JLcom/alibaba/fastjson2/writer/ObjectWriterProvider;)Lcom/alibaba/fastjson2/writer/ObjectWriter;
        //   348: areturn        
        //   349: aload           fieldWriterMap
        //   351: aload           fieldWriter
        //   353: getfield        com/alibaba/fastjson2/writer/FieldWriter.fieldName:Ljava/lang/String;
        //   356: aload           fieldWriter
        //   358: invokeinterface java/util/Map.putIfAbsent:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   363: pop            
        //   364: goto            305
        //   367: goto            438
        //   370: new             Lcom/alibaba/fastjson2/codec/FieldInfo;
        //   373: dup            
        //   374: invokespecial   com/alibaba/fastjson2/codec/FieldInfo.<init>:()V
        //   377: astore          fieldInfo
        //   379: iload           record
        //   381: ifne            405
        //   384: aload_1         /* objectClass */
        //   385: aload_0         /* this */
        //   386: aload           fieldInfo
        //   388: aload_1         /* objectClass */
        //   389: lload           writerFieldFeatures
        //   391: aload           provider
        //   393: aload           beanInfo
        //   395: aload           fieldWriterMap
        //   397: invokedynamic   BootstrapMethod #0, accept:(Lcom/alibaba/fastjson2/writer/ObjectWriterCreatorASM;Lcom/alibaba/fastjson2/codec/FieldInfo;Ljava/lang/Class;JLcom/alibaba/fastjson2/writer/ObjectWriterProvider;Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/util/Map;)Ljava/util/function/Consumer;
        //   402: invokestatic    com/alibaba/fastjson2/util/BeanUtils.declaredFields:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //   405: aload           provider
        //   407: aload_1         /* objectClass */
        //   408: invokevirtual   com/alibaba/fastjson2/writer/ObjectWriterProvider.getMixIn:(Ljava/lang/Class;)Ljava/lang/Class;
        //   411: astore          mixIn
        //   413: aload_1         /* objectClass */
        //   414: aload           mixIn
        //   416: aload_0         /* this */
        //   417: aload           fieldInfo
        //   419: lload           writerFieldFeatures
        //   421: aload           beanInfo
        //   423: aload           provider
        //   425: aload_1         /* objectClass */
        //   426: iload           record
        //   428: aload           fieldWriterMap
        //   430: invokedynamic   BootstrapMethod #1, accept:(Lcom/alibaba/fastjson2/writer/ObjectWriterCreatorASM;Lcom/alibaba/fastjson2/codec/FieldInfo;JLcom/alibaba/fastjson2/codec/BeanInfo;Lcom/alibaba/fastjson2/writer/ObjectWriterProvider;Ljava/lang/Class;ZLjava/util/Map;)Ljava/util/function/Consumer;
        //   435: invokestatic    com/alibaba/fastjson2/util/BeanUtils.getters:(Ljava/lang/Class;Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //   438: goto            471
        //   441: new             Lcom/alibaba/fastjson2/codec/FieldInfo;
        //   444: dup            
        //   445: invokespecial   com/alibaba/fastjson2/codec/FieldInfo.<init>:()V
        //   448: astore          fieldInfo
        //   450: aload_1         /* objectClass */
        //   451: aload_0         /* this */
        //   452: aload           fieldInfo
        //   454: aload_1         /* objectClass */
        //   455: lload           writerFieldFeatures
        //   457: aload           provider
        //   459: aload           beanInfo
        //   461: aload           fieldWriterMap
        //   463: invokedynamic   BootstrapMethod #2, accept:(Lcom/alibaba/fastjson2/writer/ObjectWriterCreatorASM;Lcom/alibaba/fastjson2/codec/FieldInfo;Ljava/lang/Class;JLcom/alibaba/fastjson2/writer/ObjectWriterProvider;Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/util/Map;)Ljava/util/function/Consumer;
        //   468: invokestatic    com/alibaba/fastjson2/util/BeanUtils.declaredFields:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //   471: new             Ljava/util/ArrayList;
        //   474: dup            
        //   475: aload           fieldWriterMap
        //   477: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //   482: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //   485: astore          fieldWriters
        //   487: aload_0         /* this */
        //   488: aload           beanInfo
        //   490: aload           fieldWriters
        //   492: invokevirtual   com/alibaba/fastjson2/writer/ObjectWriterCreatorASM.handleIgnores:(Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/util/List;)V
        //   495: aload           beanInfo
        //   497: getfield        com/alibaba/fastjson2/codec/BeanInfo.alphabetic:Z
        //   500: ifeq            656
        //   503: aload           fieldWriters
        //   505: invokestatic    java/util/Collections.sort:(Ljava/util/List;)V
        //   508: goto            656
        //   511: astore          e
        //   513: new             Ljava/lang/StringBuilder;
        //   516: dup            
        //   517: ldc_w           "fieldWriters sort error, objectClass "
        //   520: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   523: aload_1         /* objectClass */
        //   524: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   527: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   530: ldc_w           ", fields "
        //   533: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   536: astore          msg
        //   538: new             Lcom/alibaba/fastjson2/JSONArray;
        //   541: dup            
        //   542: invokespecial   com/alibaba/fastjson2/JSONArray.<init>:()V
        //   545: astore          array
        //   547: aload           fieldWriters
        //   549: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   554: astore          20
        //   556: aload           20
        //   558: invokeinterface java/util/Iterator.hasNext:()Z
        //   563: ifeq            633
        //   566: aload           20
        //   568: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   573: checkcast       Lcom/alibaba/fastjson2/writer/FieldWriter;
        //   576: astore          fieldWriter
        //   578: aload           array
        //   580: ldc_w           "name"
        //   583: aload           fieldWriter
        //   585: getfield        com/alibaba/fastjson2/writer/FieldWriter.fieldName:Ljava/lang/String;
        //   588: ldc_w           "type"
        //   591: aload           fieldWriter
        //   593: getfield        com/alibaba/fastjson2/writer/FieldWriter.fieldClass:Ljava/lang/Class;
        //   596: ldc_w           "ordinal"
        //   599: aload           fieldWriter
        //   601: getfield        com/alibaba/fastjson2/writer/FieldWriter.ordinal:I
        //   604: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   607: ldc_w           "field"
        //   610: aload           fieldWriter
        //   612: getfield        com/alibaba/fastjson2/writer/FieldWriter.field:Ljava/lang/reflect/Field;
        //   615: ldc_w           "method"
        //   618: aload           fieldWriter
        //   620: getfield        com/alibaba/fastjson2/writer/FieldWriter.method:Ljava/lang/reflect/Method;
        //   623: invokestatic    com/alibaba/fastjson2/JSONObject.of:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Lcom/alibaba/fastjson2/JSONObject;
        //   626: invokevirtual   com/alibaba/fastjson2/JSONArray.add:(Ljava/lang/Object;)Z
        //   629: pop            
        //   630: goto            556
        //   633: aload           msg
        //   635: aload           array
        //   637: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   640: pop            
        //   641: new             Lcom/alibaba/fastjson2/JSONException;
        //   644: dup            
        //   645: aload           msg
        //   647: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   650: aload           e
        //   652: invokespecial   com/alibaba/fastjson2/JSONException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   655: athrow         
        //   656: aload           fieldWriters
        //   658: invokeinterface java/util/List.size:()I
        //   663: bipush          100
        //   665: if_icmpge       681
        //   668: ldc             Ljava/lang/Throwable;.class
        //   670: aload_1         /* objectClass */
        //   671: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
        //   674: ifne            681
        //   677: iconst_1       
        //   678: goto            682
        //   681: iconst_0       
        //   682: istore          match
        //   684: iload           publicClass
        //   686: ifeq            694
        //   689: iload           externalClass
        //   691: ifeq            742
        //   694: aload           fieldWriters
        //   696: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   701: astore          18
        //   703: aload           18
        //   705: invokeinterface java/util/Iterator.hasNext:()Z
        //   710: ifeq            742
        //   713: aload           18
        //   715: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   720: checkcast       Lcom/alibaba/fastjson2/writer/FieldWriter;
        //   723: astore          fieldWriter
        //   725: aload           fieldWriter
        //   727: getfield        com/alibaba/fastjson2/writer/FieldWriter.method:Ljava/lang/reflect/Method;
        //   730: ifnull          739
        //   733: iconst_0       
        //   734: istore          match
        //   736: goto            742
        //   739: goto            703
        //   742: aload           fieldWriters
        //   744: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   749: astore          18
        //   751: aload           18
        //   753: invokeinterface java/util/Iterator.hasNext:()Z
        //   758: ifeq            818
        //   761: aload           18
        //   763: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   768: checkcast       Lcom/alibaba/fastjson2/writer/FieldWriter;
        //   771: astore          fieldWriter
        //   773: aload           fieldWriter
        //   775: invokevirtual   com/alibaba/fastjson2/writer/FieldWriter.getInitWriter:()Lcom/alibaba/fastjson2/writer/ObjectWriter;
        //   778: ifnonnull       809
        //   781: aload           fieldWriter
        //   783: getfield        com/alibaba/fastjson2/writer/FieldWriter.features:J
        //   786: ldc2_w          281474976710656
        //   789: land           
        //   790: lconst_0       
        //   791: lcmp           
        //   792: ifne            809
        //   795: aload           fieldWriter
        //   797: getfield        com/alibaba/fastjson2/writer/FieldWriter.features:J
        //   800: ldc2_w          1125899906842624
        //   803: land           
        //   804: lconst_0       
        //   805: lcmp           
        //   806: ifeq            815
        //   809: iconst_0       
        //   810: istore          match
        //   812: goto            818
        //   815: goto            751
        //   818: aload_1         /* objectClass */
        //   819: invokevirtual   java/lang/Class.getSuperclass:()Ljava/lang/Class;
        //   822: ldc             Ljava/lang/Object;.class
        //   824: if_acmpne       858
        //   827: aload_1         /* objectClass */
        //   828: invokevirtual   java/lang/Class.getSimpleName:()Ljava/lang/String;
        //   831: astore          simpleName
        //   833: aload           simpleName
        //   835: bipush          36
        //   837: invokevirtual   java/lang/String.indexOf:(I)I
        //   840: iconst_m1      
        //   841: if_icmpeq       858
        //   844: aload           simpleName
        //   846: ldc_w           "$$"
        //   849: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   852: ifeq            858
        //   855: iconst_0       
        //   856: istore          match
        //   858: lload_2         /* features */
        //   859: aload           beanInfo
        //   861: getfield        com/alibaba/fastjson2/codec/BeanInfo.writerFeatures:J
        //   864: lor            
        //   865: lstore          writerFeatures
        //   867: iload           match
        //   869: ifne            881
        //   872: aload_0         /* this */
        //   873: aload_1         /* objectClass */
        //   874: lload_2         /* features */
        //   875: aload           provider
        //   877: invokespecial   com/alibaba/fastjson2/writer/ObjectWriterCreator.createObjectWriter:(Ljava/lang/Class;JLcom/alibaba/fastjson2/writer/ObjectWriterProvider;)Lcom/alibaba/fastjson2/writer/ObjectWriter;
        //   880: areturn        
        //   881: aload_0         /* this */
        //   882: aload           fieldWriters
        //   884: aload_1         /* objectClass */
        //   885: invokevirtual   com/alibaba/fastjson2/writer/ObjectWriterCreatorASM.setDefaultValue:(Ljava/util/List;Ljava/lang/Class;)V
        //   888: aload_0         /* this */
        //   889: aload_1         /* objectClass */
        //   890: aload           provider
        //   892: aload           beanInfo
        //   894: aload           fieldWriters
        //   896: lload           writerFeatures
        //   898: invokespecial   com/alibaba/fastjson2/writer/ObjectWriterCreatorASM.jitWriter:(Ljava/lang/Class;Lcom/alibaba/fastjson2/writer/ObjectWriterProvider;Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/util/List;J)Lcom/alibaba/fastjson2/writer/ObjectWriterAdapter;
        //   901: areturn        
        //    StackMapTable: 00 23 FF 00 5E 00 08 07 00 45 07 00 6E 04 07 00 01 01 01 01 07 00 60 00 01 07 07 17 0D FC 00 1D 04 FC 00 1E 04 40 01 FC 00 11 01 08 FE 00 18 01 00 07 00 F5 FE 00 17 07 00 4E 01 07 00 DE 29 FA 00 02 FC 00 0D 07 00 DE FD 00 2B 07 00 56 07 06 15 F8 00 11 02 FC 00 22 07 00 FA F8 00 20 02 1D FF 00 27 00 0E 07 00 45 07 00 6E 04 07 00 01 01 01 01 07 00 60 04 04 01 01 07 00 4E 07 00 F5 00 01 07 01 26 FF 00 2C 00 12 07 00 45 07 00 6E 04 07 00 01 01 01 01 07 00 60 04 04 01 01 07 00 4E 07 00 F5 07 01 26 07 01 28 07 01 39 07 00 DE 00 00 FA 00 4C F8 00 16 18 40 01 FC 00 0B 01 FC 00 08 07 00 DE 23 FA 00 02 FC 00 08 07 00 DE FC 00 39 07 00 56 FA 00 05 FA 00 02 27 FC 00 16 04
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  61     93     94     108    Ljava/lang/InstantiationException;
        //  61     93     94     108    Ljava/lang/IllegalAccessException;
        //  61     93     94     108    Ljava/lang/NoSuchMethodException;
        //  61     93     94     108    Ljava/lang/reflect/InvocationTargetException;
        //  503    508    511    656    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    private ObjectWriterAdapter jitWriter(final Class objectClass, final ObjectWriterProvider provider, final BeanInfo beanInfo, final List<FieldWriter> fieldWriters, final long writerFeatures) {
        final ClassWriter cw = new ClassWriter(null);
        final String className = "OWG_" + ObjectWriterCreatorASM.seed.incrementAndGet() + "_" + fieldWriters.size() + ((objectClass == null) ? "" : ("_" + objectClass.getSimpleName()));
        final Package pkg = ObjectWriterCreatorASM.class.getPackage();
        String classNameFull;
        String classNameType;
        if (pkg != null) {
            final String packageName = pkg.getName();
            final int packageNameLength = packageName.length();
            final int charsLength = packageNameLength + 1 + className.length();
            final char[] chars = new char[charsLength];
            packageName.getChars(0, packageName.length(), chars, 0);
            chars[packageNameLength] = '.';
            className.getChars(0, className.length(), chars, packageNameLength + 1);
            classNameFull = new String(chars);
            chars[packageNameLength] = '/';
            for (int i = 0; i < packageNameLength; ++i) {
                if (chars[i] == '.') {
                    chars[i] = '/';
                }
            }
            classNameType = new String(chars);
        }
        else {
            classNameType = className;
            classNameFull = className;
        }
        String objectWriterSupper = null;
        switch (fieldWriters.size()) {
            case 1: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_1;
                break;
            }
            case 2: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_2;
                break;
            }
            case 3: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_3;
                break;
            }
            case 4: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_4;
                break;
            }
            case 5: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_5;
                break;
            }
            case 6: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_6;
                break;
            }
            case 7: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_7;
                break;
            }
            case 8: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_8;
                break;
            }
            case 9: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_9;
                break;
            }
            case 10: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_10;
                break;
            }
            case 11: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_11;
                break;
            }
            case 12: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_12;
                break;
            }
            default: {
                objectWriterSupper = ASMUtils.TYPE_OBJECT_WRITER_ADAPTER;
                break;
            }
        }
        cw.visit(52, 49, classNameType, objectWriterSupper, ObjectWriterCreatorASM.INTERFACES);
        this.genFields(fieldWriters, cw, objectWriterSupper);
        this.genMethodInit(fieldWriters, cw, classNameType, objectWriterSupper);
        this.genMethodWriteJSONB(provider, objectClass, fieldWriters, cw, classNameType, writerFeatures);
        if ((writerFeatures & JSONWriter.Feature.BeanToArray.mask) != 0x0L) {
            this.genMethodWriteArrayMapping(provider, "write", objectClass, writerFeatures, fieldWriters, cw, classNameType);
        }
        else {
            this.genMethodWrite(provider, objectClass, fieldWriters, cw, classNameType, writerFeatures);
        }
        this.genMethodWriteArrayMappingJSONB(provider, objectClass, writerFeatures, fieldWriters, cw, classNameType, writerFeatures);
        this.genMethodWriteArrayMapping(provider, "writeArrayMapping", objectClass, writerFeatures, fieldWriters, cw, classNameType);
        final byte[] code = cw.toByteArray();
        final Class<?> deserClass = this.classLoader.defineClassPublic(classNameFull, code, 0, code.length);
        try {
            final Constructor<?> constructor = deserClass.getConstructor(Class.class, String.class, String.class, Long.TYPE, List.class);
            final ObjectWriterAdapter objectWriter = (ObjectWriterAdapter)constructor.newInstance(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
            if (beanInfo.serializeFilters != null) {
                ObjectWriterCreator.configSerializeFilters(beanInfo, objectWriter);
            }
            return objectWriter;
        }
        catch (Throwable e) {
            throw new JSONException("create objectWriter error, objectType " + objectClass, e);
        }
    }
    
    private void genMethodWrite(final ObjectWriterProvider provider, final Class objectType, final List<FieldWriter> fieldWriters, final ClassWriter cw, final String classNameType, final long objectFeatures) {
        final MethodWriter mw = cw.visitMethod(1, "write", ObjectWriterCreatorASM.METHOD_DESC_WRITE, (fieldWriters.size() < 6) ? 512 : 1024);
        final int OBJECT = 2;
        final int FIELD_NAME = 3;
        final int FIELD_TYPE = 4;
        final int FIELD_FEATURES = 5;
        final int COMMA = 7;
        final Label json_ = new Label();
        final Label jsonb_ = new Label();
        final Label notSuper_ = new Label();
        final MethodWriterContext mwc = new MethodWriterContext(provider, objectType, objectFeatures, classNameType, mw, 8, false);
        mwc.genVariantsMethodBefore();
        mwc.genIsEnabled(JSONWriter.Feature.IgnoreErrorGetter.mask, notSuper_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(183, ASMUtils.TYPE_OBJECT_WRITER_ADAPTER, "write", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, false);
        mw.visitInsn(177);
        mw.visitLabel(notSuper_);
        mw.visitVarInsn(25, 1);
        mw.visitFieldInsn(180, ASMUtils.TYPE_JSON_WRITER, "jsonb", "Z");
        mw.visitJumpInsn(153, json_);
        mwc.genIsEnabled(JSONWriter.Feature.BeanToArray.mask, jsonb_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(182, classNameType, "writeArrayMappingJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, false);
        mw.visitInsn(177);
        mw.visitLabel(jsonb_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(182, classNameType, "writeJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, false);
        mw.visitInsn(177);
        mw.visitLabel(json_);
        final Label checkFilter_ = new Label();
        mwc.genIsEnabled(JSONWriter.Feature.BeanToArray.mask, checkFilter_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(182, classNameType, "writeArrayMapping", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, false);
        mw.visitInsn(177);
        mw.visitLabel(checkFilter_);
        final Label object_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, "hasFilter", ObjectWriterCreatorASM.METHOD_DESC_HAS_FILTER, true);
        mw.visitJumpInsn(153, object_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(182, classNameType, "writeWithFilter", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, false);
        mw.visitInsn(177);
        mw.visitLabel(object_);
        final Label return_ = new Label();
        if (objectType == null || !Serializable.class.isAssignableFrom(objectType)) {
            final Label endIgnoreNoneSerializable_ = new Label();
            mwc.genIsEnabled(JSONWriter.Feature.IgnoreNoneSerializable.mask, endIgnoreNoneSerializable_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
            mw.visitJumpInsn(167, return_);
            mw.visitLabel(endIgnoreNoneSerializable_);
            final Label endErrorOnNoneSerializable_ = new Label();
            mwc.genIsEnabled(JSONWriter.Feature.ErrorOnNoneSerializable.mask, endErrorOnNoneSerializable_);
            mw.visitVarInsn(25, 0);
            mw.visitMethodInsn(182, mwc.classNameType, "errorOnNoneSerializable", "()V", false);
            mw.visitJumpInsn(167, return_);
            mw.visitLabel(endErrorOnNoneSerializable_);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "startObject", "()V", false);
        mw.visitInsn(4);
        mw.visitVarInsn(54, 7);
        final Label writeFields_ = new Label();
        isWriteTypeInfo(objectFeatures, mw, 2, 4, writeFields_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, "writeTypeInfo", ObjectWriterCreatorASM.METHOD_DESC_WRITE_TYPE_INFO, true);
        mw.visitInsn(4);
        mw.visitInsn(130);
        mw.visitVarInsn(54, 7);
        mw.visitLabel(writeFields_);
        for (int i = 0; i < fieldWriters.size(); ++i) {
            final FieldWriter fieldWriter = fieldWriters.get(i);
            this.gwFieldValue(mwc, fieldWriter, 2, i);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "endObject", "()V", false);
        mw.visitLabel(return_);
        mw.visitInsn(177);
        mw.visitMaxs(mwc.maxVariant + 1, mwc.maxVariant + 1);
    }
    
    private static void isWriteTypeInfo(final long objectFeatures, final MethodWriter mw, final int OBJECT, final int FIELD_TYPE, final Label notWriteType) {
        if ((objectFeatures & JSONWriter.Feature.WriteClassName.mask) == 0x0L) {
            mw.visitVarInsn(25, OBJECT);
            mw.visitJumpInsn(198, notWriteType);
            mw.visitVarInsn(25, OBJECT);
            mw.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            mw.visitVarInsn(25, FIELD_TYPE);
            mw.visitJumpInsn(165, notWriteType);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, OBJECT);
            mw.visitVarInsn(25, FIELD_TYPE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isWriteTypeInfo", "(Ljava/lang/Object;Ljava/lang/reflect/Type;)Z", false);
            mw.visitJumpInsn(153, notWriteType);
        }
    }
    
    private void genMethodWriteJSONB(final ObjectWriterProvider provider, final Class objectType, final List<FieldWriter> fieldWriters, final ClassWriter cw, final String classNameType, final long objectFeatures) {
        final MethodWriter mw = cw.visitMethod(1, "writeJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE, (fieldWriters.size() < 6) ? 512 : 1024);
        final int OBJECT = 2;
        final int FIELD_NAME = 3;
        final int FIELD_TYPE = 4;
        final int FIELD_FEATURES = 5;
        final MethodWriterContext mwc = new MethodWriterContext(provider, objectType, objectFeatures, classNameType, mw, 7, true);
        mwc.genVariantsMethodBefore();
        final Label return_ = new Label();
        if (objectType == null || !Serializable.class.isAssignableFrom(objectType)) {
            final Label endIgnoreNoneSerializable_ = new Label();
            mwc.genIsEnabled(JSONWriter.Feature.IgnoreNoneSerializable.mask, endIgnoreNoneSerializable_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
            mw.visitJumpInsn(167, return_);
            mw.visitLabel(endIgnoreNoneSerializable_);
            final Label endErrorOnNoneSerializable_ = new Label();
            mwc.genIsEnabled(JSONWriter.Feature.ErrorOnNoneSerializable.mask, endErrorOnNoneSerializable_);
            mw.visitVarInsn(25, 0);
            mw.visitMethodInsn(182, mwc.classNameType, "errorOnNoneSerializable", "()V", false);
            mw.visitJumpInsn(167, return_);
            mw.visitLabel(endErrorOnNoneSerializable_);
        }
        final Label notWriteType = new Label();
        isWriteTypeInfo(objectFeatures, mw, 2, 4, notWriteType);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, classNameType, "writeClassInfo", ObjectWriterCreatorASM.METHOD_DESC_WRITE_CLASS_INFO, false);
        mw.visitLabel(notWriteType);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "startObject", "()V", false);
        for (int i = 0; i < fieldWriters.size(); ++i) {
            final FieldWriter fieldWriter = fieldWriters.get(i);
            this.gwFieldValueJSONB(mwc, fieldWriter, 2, i);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "endObject", "()V", false);
        mw.visitLabel(return_);
        mw.visitInsn(177);
        mw.visitMaxs(mwc.maxVariant + 1, mwc.maxVariant + 1);
    }
    
    private void genMethodWriteArrayMappingJSONB(final ObjectWriterProvider provider, final Class objectType, final long objectFeatures, final List<FieldWriter> fieldWriters, final ClassWriter cw, final String classNameType, final long features) {
        final MethodWriter mw = cw.visitMethod(1, "writeArrayMappingJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE, 512);
        final int OBJECT = 2;
        final int FIELD_NAME = 3;
        final int FIELD_TYPE = 4;
        final int FIELD_FEATURES = 5;
        final Label notWriteType = new Label();
        isWriteTypeInfo(objectFeatures, mw, 2, 4, notWriteType);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, classNameType, "writeClassInfo", ObjectWriterCreatorASM.METHOD_DESC_WRITE_CLASS_INFO, false);
        mw.visitLabel(notWriteType);
        final int size = fieldWriters.size();
        mw.visitVarInsn(25, 1);
        if (size >= 128) {
            mw.visitIntInsn(17, size);
        }
        else {
            mw.visitIntInsn(16, size);
        }
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "startArray", "(I)V", false);
        final MethodWriterContext mwc = new MethodWriterContext(provider, objectType, objectFeatures, classNameType, mw, 7, true);
        mwc.genVariantsMethodBefore();
        for (int i = 0; i < size; ++i) {
            this.gwValueJSONB(mwc, fieldWriters.get(i), 2, i, false);
        }
        mw.visitInsn(177);
        mw.visitMaxs(mwc.maxVariant + 1, mwc.maxVariant + 1);
    }
    
    private void gwValueJSONB(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i, final boolean table) {
        final long features = fieldWriter.features | mwc.objectFeatures;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final boolean beanToArray = (features & JSONWriter.Feature.BeanToArray.mask) != 0x0L || table;
        boolean userDefineWriter = false;
        if ((fieldClass == Long.TYPE || fieldClass == Long.class || fieldClass == long[].class) && (mwc.provider.userDefineMask & 0x4L) != 0x0L) {
            userDefineWriter = (mwc.provider.getObjectWriter(Long.class) != ObjectWriterImplInt64.INSTANCE);
        }
        if (fieldClass == Boolean.TYPE || fieldClass == boolean[].class || fieldClass == Character.TYPE || fieldClass == char[].class || fieldClass == Byte.TYPE || fieldClass == byte[].class || fieldClass == Short.TYPE || fieldClass == short[].class || fieldClass == Integer.TYPE || fieldClass == int[].class || fieldClass == Long.TYPE || (fieldClass == long[].class && !userDefineWriter) || fieldClass == Float.TYPE || fieldClass == float[].class || fieldClass == Double.TYPE || fieldClass == double[].class || fieldClass == String.class || fieldClass == Integer.class || fieldClass == Long.class || fieldClass == BigDecimal.class || fieldClass.isEnum()) {
            this.gwValue(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Date.class) {
            this.gwDate(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldWriter instanceof FieldWriterList) {
            this.gwListJSONB(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass.isArray()) {
            this.gwObjectA(mwc, fieldWriter, OBJECT, i);
        }
        else {
            this.gwObjectJSONB(fieldWriter, OBJECT, mwc, i, beanToArray);
        }
    }
    
    private void gwObjectJSONB(final FieldWriter fieldWriter, final int OBJECT, final MethodWriterContext mwc, final int i, final boolean beanToArray) {
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String fieldName = fieldWriter.fieldName;
        final String classNameType = mwc.classNameType;
        final MethodWriter mw = mwc.mw;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final int REF_PATH = mwc.var("REF_PATH");
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        final boolean refDetection = !ObjectWriterProvider.isNotReferenceDetect(fieldClass);
        if (refDetection) {
            final Label endDetect_ = new Label();
            final Label refSetPath_ = new Label();
            mwc.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, endDetect_);
            mw.visitVarInsn(25, OBJECT);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitJumpInsn(166, refSetPath_);
            mw.visitVarInsn(25, 1);
            mw.visitLdcInsn("..");
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
            mw.visitJumpInsn(167, endIfNull_);
            mw.visitLabel(refSetPath_);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "setPath", ObjectWriterCreatorASM.METHOD_DESC_SET_PATH2, false);
            mw.visitInsn(89);
            mw.visitVarInsn(58, REF_PATH);
            mw.visitJumpInsn(198, endDetect_);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, REF_PATH);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
            mw.visitJumpInsn(167, endIfNull_);
            mw.visitLabel(endDetect_);
        }
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "getObjectWriter", ObjectWriterCreatorASM.METHOD_DESC_GET_OBJECT_WRITER, false);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitLdcInsn(fieldName);
        mwc.loadFieldType(i, fieldWriter.fieldType);
        mw.visitLdcInsn(fieldWriter.features);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, beanToArray ? "writeJSONB" : "writeArrayMappingJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, true);
        if (refDetection) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
        }
        mw.visitLabel(endIfNull_);
    }
    
    private void gwListJSONB(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final Type fieldType = fieldWriter.fieldType;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String classNameType = mwc.classNameType;
        final MethodWriter mw = mwc.mw;
        final int LIST = mwc.var(fieldClass);
        final int REF_PATH = mwc.var("REF_PATH");
        boolean listSimple = false;
        Type itemType = null;
        Class itemClass = null;
        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                itemType = actualTypeArguments[0];
                itemClass = TypeUtils.getClass(itemType);
                listSimple = (itemType == String.class || itemType == Integer.class || itemType == Long.class);
            }
        }
        final Label endIfListNull_ = new Label();
        final Label listNotNull_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, LIST);
        mw.visitJumpInsn(199, listNotNull_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
        mw.visitJumpInsn(167, endIfListNull_);
        mw.visitLabel(listNotNull_);
        final Label endDetect_ = new Label();
        final Label refSetPath_ = new Label();
        mwc.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, endDetect_);
        mw.visitVarInsn(25, OBJECT);
        mw.visitVarInsn(25, LIST);
        mw.visitJumpInsn(166, refSetPath_);
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn("..");
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
        mw.visitJumpInsn(167, endIfListNull_);
        mw.visitLabel(refSetPath_);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, LIST);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "setPath", ObjectWriterCreatorASM.METHOD_DESC_SET_PATH2, false);
        mw.visitInsn(89);
        mw.visitVarInsn(58, REF_PATH);
        mw.visitJumpInsn(198, endDetect_);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, REF_PATH);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, LIST);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
        mw.visitJumpInsn(167, endIfListNull_);
        mw.visitLabel(endDetect_);
        if (listSimple) {
            gwListSimpleType(mwc, i, mw, fieldClass, itemClass, LIST);
        }
        else {
            final int PREVIOUS_CLASS = mwc.var("ITEM_CLASS");
            final int ITEM_OBJECT_WRITER = mwc.var("ITEM_OBJECT_WRITER");
            mw.visitInsn(1);
            mw.visitInsn(89);
            mw.visitVarInsn(58, PREVIOUS_CLASS);
            mw.visitVarInsn(58, ITEM_OBJECT_WRITER);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, LIST);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeListValueJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE_LIST, false);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, LIST);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
        }
        mw.visitLabel(endIfListNull_);
    }
    
    private void gwDate(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        mw.visitInsn(3);
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeDate", ObjectWriterCreatorASM.METHOD_DESC_WRITE_DATE_WITH_FIELD_NAME, false);
    }
    
    private void gwValue(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        final Class fieldClass = fieldWriter.fieldClass;
        if (fieldClass == String.class) {
            this.genGetObject(mwc, fieldWriter, i, OBJECT);
            mw.visitTypeInsn(192, "java/lang/String");
            final int FIELD_VALUE = mwc.var("FIELD_VALUE_" + fieldWriter.fieldClass.getName());
            mw.visitVarInsn(58, FIELD_VALUE);
            gwString(mwc, false, true, FIELD_VALUE);
            return;
        }
        mw.visitVarInsn(25, 1);
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        if (fieldWriter.decimalFormat != null) {
            if (fieldClass == Double.TYPE) {
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(DLjava/text/DecimalFormat;)V", false);
            }
            else if (fieldClass == Float.TYPE) {
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(FLjava/text/DecimalFormat;)V", false);
            }
            else {
                if (fieldClass != BigDecimal.class) {
                    throw new UnsupportedOperationException();
                }
                mw.visitLdcInsn(fieldWriter.features);
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V", false);
            }
            return;
        }
        String methodName;
        String methodDesc;
        if (fieldClass == Boolean.TYPE) {
            methodName = "writeBool";
            methodDesc = "(Z)V";
        }
        else if (fieldClass == Character.TYPE) {
            methodName = "writeChar";
            methodDesc = "(C)V";
        }
        else if (fieldClass == Byte.TYPE) {
            methodName = "writeInt8";
            methodDesc = "(B)V";
        }
        else if (fieldClass == Short.TYPE) {
            methodName = "writeInt16";
            methodDesc = "(S)V";
        }
        else if (fieldClass == Integer.TYPE) {
            methodName = "writeInt32";
            methodDesc = "(I)V";
        }
        else if (fieldClass == Integer.class) {
            methodName = "writeInt32";
            methodDesc = "(Ljava/lang/Integer;)V";
        }
        else if (fieldClass == Long.TYPE) {
            methodName = "writeInt64";
            methodDesc = "(J)V";
        }
        else if (fieldClass == Long.class) {
            methodName = "writeInt64";
            methodDesc = "(Ljava/lang/Long;)V";
        }
        else if (fieldClass == Float.TYPE) {
            methodName = "writeFloat";
            methodDesc = "(F)V";
        }
        else if (fieldClass == Double.TYPE) {
            methodName = "writeDouble";
            methodDesc = "(D)V";
        }
        else if (fieldClass == boolean[].class) {
            methodName = "writeBool";
            methodDesc = "([Z)V";
        }
        else if (fieldClass == char[].class) {
            methodName = "writeString";
            methodDesc = "([C)V";
        }
        else if (fieldClass == byte[].class) {
            methodName = "writeBinary";
            methodDesc = "([B)V";
        }
        else if (fieldClass == short[].class) {
            methodName = "writeInt16";
            methodDesc = "([S)V";
        }
        else if (fieldClass == int[].class) {
            methodName = "writeInt32";
            methodDesc = "([I)V";
        }
        else if (fieldClass == long[].class && mwc.provider.getObjectWriter(Long.class) == ObjectWriterImplInt64.INSTANCE) {
            methodName = "writeInt64";
            methodDesc = "([J)V";
        }
        else if (fieldClass == float[].class) {
            methodName = "writeFloat";
            methodDesc = "([F)V";
        }
        else if (fieldClass == double[].class) {
            methodName = "writeDouble";
            methodDesc = "([D)V";
        }
        else if (fieldClass == BigDecimal.class) {
            methodName = "writeDecimal";
            methodDesc = "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V";
            mw.visitLdcInsn(fieldWriter.features);
            mw.visitInsn(1);
        }
        else {
            if (!Enum.class.isAssignableFrom(fieldClass)) {
                throw new UnsupportedOperationException();
            }
            methodName = "writeEnum";
            methodDesc = "(Ljava/lang/Enum;)V";
        }
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, methodName, methodDesc, false);
    }
    
    private void gwObjectA(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        if (fieldWriter.fieldClass == String[].class) {
            mw.visitVarInsn(25, 1);
            this.genGetObject(mwc, fieldWriter, i, OBJECT);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "([Ljava/lang/String;)V", false);
        }
        else {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, OBJECT);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeValue", ObjectWriterCreatorASM.METHOD_DESC_WRITE_VALUE, false);
        }
    }
    
    private void genMethodWriteArrayMapping(final ObjectWriterProvider provider, final String methodName, final Class objectType, final long objectFeatures, final List<FieldWriter> fieldWriters, final ClassWriter cw, final String classNameType) {
        final MethodWriter mw = cw.visitMethod(1, methodName, ObjectWriterCreatorASM.METHOD_DESC_WRITE, 512);
        final int OBJECT = 2;
        final int FIELD_NAME = 3;
        final int FIELD_TYPE = 4;
        final int FIELD_FEATURES = 5;
        final Label jsonb_ = new Label();
        mw.visitVarInsn(25, 1);
        mw.visitFieldInsn(180, ASMUtils.TYPE_JSON_WRITER, "jsonb", "Z");
        mw.visitJumpInsn(153, jsonb_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(182, classNameType, "writeArrayMappingJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, false);
        mw.visitInsn(177);
        mw.visitLabel(jsonb_);
        final Label object_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, "hasFilter", ObjectWriterCreatorASM.METHOD_DESC_HAS_FILTER, true);
        mw.visitJumpInsn(153, object_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(25, 4);
        mw.visitVarInsn(22, 5);
        mw.visitMethodInsn(183, ASMUtils.TYPE_OBJECT_WRITER_ADAPTER, methodName, ObjectWriterCreatorASM.METHOD_DESC_WRITE, false);
        mw.visitInsn(177);
        mw.visitLabel(object_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "startArray", "()V", false);
        final MethodWriterContext mwc = new MethodWriterContext(provider, objectType, objectFeatures, classNameType, mw, 7, false);
        for (int i = 0; i < fieldWriters.size(); ++i) {
            if (i != 0) {
                mw.visitVarInsn(25, 1);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeComma", "()V", false);
            }
            this.gwFieldValueArrayMapping(fieldWriters.get(i), mwc, 2, i);
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "endArray", "()V", false);
        mw.visitInsn(177);
        mw.visitMaxs(mwc.maxVariant + 1, mwc.maxVariant + 1);
    }
    
    private void gwFieldValueArrayMapping(final FieldWriter fieldWriter, final MethodWriterContext mwc, final int OBJECT, final int i) {
        final Class objectType = mwc.objectClass;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String TYPE_OBJECT = (objectType == null) ? "java/lang/Object" : ASMUtils.type(objectType);
        boolean userDefineWriter = false;
        if ((fieldClass == Long.TYPE || fieldClass == Long.class || fieldClass == long[].class) && (mwc.provider.userDefineMask & 0x4L) != 0x0L) {
            userDefineWriter = (mwc.provider.getObjectWriter(Long.class) != ObjectWriterImplInt64.INSTANCE);
        }
        if (fieldClass == Boolean.TYPE || fieldClass == boolean[].class || fieldClass == Character.TYPE || fieldClass == char[].class || fieldClass == Byte.TYPE || fieldClass == byte[].class || fieldClass == Short.TYPE || fieldClass == short[].class || fieldClass == Integer.TYPE || fieldClass == int[].class || fieldClass == Long.TYPE || (fieldClass == long[].class && !userDefineWriter) || fieldClass == Float.TYPE || fieldClass == float[].class || fieldClass == Double.TYPE || fieldClass == double[].class || fieldClass == String.class || fieldClass == Integer.class || fieldClass == Long.class || fieldClass == BigDecimal.class || fieldClass.isEnum()) {
            this.gwValue(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Date.class) {
            this.gwDate(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldWriter instanceof FieldWriterList) {
            this.gwList(mwc, OBJECT, i, fieldWriter);
        }
        else {
            this.gwObject(mwc, OBJECT, i, fieldWriter, TYPE_OBJECT);
        }
    }
    
    private void gwObject(final MethodWriterContext mwc, final int OBJECT, final int i, final FieldWriter fieldWriter, final String TYPE_OBJECT) {
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String fieldName = fieldWriter.fieldName;
        final MethodWriter mw = mwc.mw;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final int REF_PATH = mwc.var("REF_PATH");
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        if (fieldClass == Double.class || fieldClass == Float.class || fieldClass == BigDecimal.class) {
            mw.visitVarInsn(25, 1);
            if (fieldWriter.decimalFormat != null) {
                mw.visitVarInsn(25, FIELD_VALUE);
                if (fieldClass == Double.class) {
                    mw.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                    mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(DLjava/text/DecimalFormat;)V", false);
                }
                else if (fieldClass == Float.class) {
                    mw.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                    mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(FLjava/text/DecimalFormat;)V", false);
                }
                else {
                    final long features = fieldWriter.features;
                    mw.visitLdcInsn(features);
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                    mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V", false);
                }
            }
            else {
                mw.visitVarInsn(25, FIELD_VALUE);
                if (fieldClass == Double.class) {
                    mw.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(D)V", false);
                }
                else if (fieldClass == Float.class) {
                    mw.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(F)V", false);
                }
                else {
                    final long features = fieldWriter.features;
                    mw.visitLdcInsn(features);
                    mw.visitInsn(1);
                    mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V", false);
                }
            }
        }
        else {
            final boolean refDetection = !ObjectWriterProvider.isNotReferenceDetect(fieldClass);
            if (refDetection) {
                final Label endDetect_ = new Label();
                final Label refSetPath_ = new Label();
                mw.visitVarInsn(25, 1);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isRefDetect", "()Z", false);
                mw.visitJumpInsn(153, endDetect_);
                mw.visitVarInsn(25, OBJECT);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitJumpInsn(166, refSetPath_);
                mw.visitVarInsn(25, 1);
                mw.visitLdcInsn("..");
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
                mw.visitJumpInsn(167, endIfNull_);
                mw.visitLabel(refSetPath_);
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "setPath", ObjectWriterCreatorASM.METHOD_DESC_SET_PATH2, false);
                mw.visitInsn(89);
                mw.visitVarInsn(58, REF_PATH);
                mw.visitJumpInsn(198, endDetect_);
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, REF_PATH);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
                mw.visitJumpInsn(167, endIfNull_);
                mw.visitLabel(endDetect_);
            }
            if (fieldClass == String[].class) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "([Ljava/lang/String;)V", false);
            }
            else {
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
                mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "getObjectWriter", ObjectWriterCreatorASM.METHOD_DESC_GET_OBJECT_WRITER, false);
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitLdcInsn(fieldWriter.fieldName);
                mwc.loadFieldType(i, fieldWriter.fieldType);
                mw.visitLdcInsn(fieldWriter.features);
                mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, "write", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, true);
            }
            if (refDetection) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
            }
        }
        mw.visitLabel(endIfNull_);
    }
    
    private void gwList(final MethodWriterContext mwc, final int OBJECT, final int i, final FieldWriter fieldWriter) {
        final Type fieldType = fieldWriter.fieldType;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final int LIST = mwc.var(fieldClass);
        final MethodWriter mw = mwc.mw;
        boolean listSimple = false;
        Class itemClass = null;
        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                final Type itemType = actualTypeArguments[0];
                itemClass = TypeUtils.getMapping(itemType);
                listSimple = (itemType == String.class || itemType == Integer.class || itemType == Long.class);
            }
        }
        final Label endIfListNull_ = new Label();
        final Label listNotNull_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, LIST);
        mw.visitJumpInsn(199, listNotNull_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
        mw.visitJumpInsn(167, endIfListNull_);
        mw.visitLabel(listNotNull_);
        if (listSimple) {
            this.genGetObject(mwc, fieldWriter, i, OBJECT);
            mw.visitVarInsn(58, LIST);
            gwListSimpleType(mwc, i, mw, fieldClass, itemClass, LIST);
        }
        else {
            final int LIST_SIZE = mwc.var("LIST_SIZE");
            final int J = mwc.var("J");
            final int ITEM_CLASS = mwc.var(Class.class);
            final int PREVIOUS_CLASS = mwc.var("PREVIOUS_CLASS");
            final int ITEM_OBJECT_WRITER = mwc.var("ITEM_OBJECT_WRITER");
            mw.visitInsn(1);
            mw.visitInsn(89);
            mw.visitVarInsn(58, PREVIOUS_CLASS);
            mw.visitVarInsn(58, ITEM_OBJECT_WRITER);
            mw.visitVarInsn(25, LIST);
            mw.visitMethodInsn(185, "java/util/List", "size", "()I", true);
            mw.visitVarInsn(54, LIST_SIZE);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "startArray", "()V", false);
            final Label for_start_j_ = new Label();
            final Label for_end_j_ = new Label();
            final Label for_inc_j_ = new Label();
            final Label notFirst_ = new Label();
            mw.visitInsn(3);
            mw.visitVarInsn(54, J);
            mw.visitLabel(for_start_j_);
            mw.visitVarInsn(21, J);
            mw.visitVarInsn(21, LIST_SIZE);
            mw.visitJumpInsn(162, for_end_j_);
            mw.visitVarInsn(21, J);
            mw.visitJumpInsn(153, notFirst_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeComma", "()V", false);
            mw.visitLabel(notFirst_);
            final int ITEM = mwc.var(itemClass);
            final Label notNull_ = new Label();
            final Label classEQ_ = new Label();
            mw.visitVarInsn(25, LIST);
            mw.visitVarInsn(21, J);
            mw.visitMethodInsn(185, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
            mw.visitInsn(89);
            mw.visitVarInsn(58, ITEM);
            mw.visitJumpInsn(199, notNull_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
            mw.visitJumpInsn(167, for_inc_j_);
            mw.visitLabel(notNull_);
            mw.visitVarInsn(25, ITEM);
            mw.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            mw.visitInsn(89);
            mw.visitVarInsn(58, ITEM_CLASS);
            mw.visitVarInsn(25, PREVIOUS_CLASS);
            mw.visitJumpInsn(165, classEQ_);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, ITEM_CLASS);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "getItemWriter", ObjectWriterCreatorASM.METHOD_DESC_GET_ITEM_WRITER, false);
            mw.visitVarInsn(58, ITEM_OBJECT_WRITER);
            mw.visitVarInsn(25, ITEM_CLASS);
            mw.visitVarInsn(58, PREVIOUS_CLASS);
            mw.visitLabel(classEQ_);
            mw.visitVarInsn(25, ITEM_OBJECT_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, ITEM);
            mw.visitVarInsn(21, J);
            mw.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            mwc.loadFieldType(i, fieldType);
            mw.visitLdcInsn(fieldWriter.features);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, "write", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, true);
            mw.visitLabel(for_inc_j_);
            mw.visitIincInsn(J, 1);
            mw.visitJumpInsn(167, for_start_j_);
            mw.visitLabel(for_end_j_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "endArray", "()V", false);
        }
        mw.visitLabel(endIfListNull_);
    }
    
    private void gwFieldValue(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        if (fieldClass == Boolean.TYPE) {
            this.gwFieldValueBooleanV(mwc, fieldWriter, OBJECT, i, false);
        }
        else if (fieldClass == boolean[].class || fieldClass == byte[].class || fieldClass == char[].class || fieldClass == short[].class || fieldClass == float[].class || fieldClass == double[].class) {
            this.gwFieldValueArray(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Character.TYPE || fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Float.TYPE || fieldClass == Double.TYPE) {
            this.gwFieldName(mwc, fieldWriter, i);
            this.gwValue(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Integer.TYPE) {
            this.gwFieldValueInt32V(mwc, fieldWriter, OBJECT, i, false);
        }
        else if (fieldClass == int[].class) {
            this.gwFieldValueIntVA(mwc, fieldWriter, OBJECT, i, false);
        }
        else if (fieldClass == Long.TYPE) {
            this.gwFieldValueInt64V(mwc, fieldWriter, OBJECT, i, false);
        }
        else if (fieldClass == long[].class && mwc.provider.getObjectWriter(Long.class) == ObjectWriterImplInt64.INSTANCE) {
            this.gwFieldValueInt64VA(mwc, fieldWriter, OBJECT, i, false);
        }
        else if (fieldClass == Integer.class) {
            this.gwInt32(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Long.class) {
            this.gwInt64(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Float.class) {
            this.gwFloat(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Double.class) {
            this.gwDouble(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == String.class) {
            this.gwFieldValueString(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass.isEnum() && BeanUtils.getEnumValueField(fieldClass, mwc.provider) == null && !(fieldWriter instanceof FieldWriterObject)) {
            this.gwFieldValueEnum(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Date.class) {
            this.gwFieldValueDate(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == List.class) {
            this.gwFieldValueList(mwc, fieldWriter, OBJECT, i);
        }
        else {
            this.gwFieldValueObject(mwc, fieldWriter, OBJECT, i);
        }
    }
    
    private void gwFieldValueEnum(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final MethodWriter mw = mwc.mw;
        final int FIELD_VALUE = mwc.var(fieldClass);
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        final Label null_ = new Label();
        final Label notNull_ = new Label();
        mw.visitJumpInsn(198, null_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeEnum", ObjectWriterCreatorASM.METHOD_DESC_WRITE_ENUM, false);
        mw.visitJumpInsn(167, notNull_);
        mw.visitLabel(null_);
        mw.visitVarInsn(21, mwc.var("WRITE_NULLS"));
        mw.visitJumpInsn(153, notNull_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
        mw.visitLabel(notNull_);
    }
    
    private void gwFieldValueObject(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final Type fieldType = fieldWriter.fieldType;
        final String fieldName = fieldWriter.fieldName;
        final boolean refDetection = !ObjectWriterProvider.isNotReferenceDetect(fieldClass);
        final int FIELD_VALUE = mwc.var(fieldClass);
        Integer REF_PATH = null;
        if (refDetection) {
            REF_PATH = mwc.var("REF_PATH");
        }
        final long features = fieldWriter.features | mwc.objectFeatures;
        final MethodWriter mw = mwc.mw;
        final Label null_ = new Label();
        final Label notNull_ = new Label();
        if (fieldWriter.unwrapped()) {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, OBJECT);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "write", ObjectWriterCreatorASM.METHOD_DESC_FIELD_WRITE_OBJECT, false);
            mw.visitInsn(87);
            mw.visitJumpInsn(167, notNull_);
        }
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(198, null_);
        if (!Serializable.class.isAssignableFrom(fieldClass) && fieldClass != List.class) {
            mw.visitVarInsn(25, 1);
            if (!fieldWriter.isFieldClassSerializable()) {
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isIgnoreNoneSerializable", "()Z", false);
            }
            else {
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isIgnoreNoneSerializable", "(Ljava/lang/Object;)Z", false);
            }
            mw.visitJumpInsn(154, notNull_);
        }
        if (refDetection) {
            final Label endDetect_ = new Label();
            final Label refSetPath_ = new Label();
            final int REF_DETECT = mwc.var("REF_DETECT");
            if (fieldClass == Object.class) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isRefDetect", "(Ljava/lang/Object;)Z", false);
            }
            else {
                mwc.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, null);
            }
            mw.visitInsn(89);
            mw.visitVarInsn(54, REF_DETECT);
            mw.visitJumpInsn(153, endDetect_);
            mw.visitVarInsn(25, OBJECT);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitJumpInsn(166, refSetPath_);
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitLdcInsn("..");
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
            mw.visitJumpInsn(167, notNull_);
            mw.visitLabel(refSetPath_);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "setPath", ObjectWriterCreatorASM.METHOD_DESC_SET_PATH2, false);
            mw.visitInsn(89);
            mw.visitVarInsn(58, REF_PATH);
            mw.visitJumpInsn(198, endDetect_);
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, REF_PATH);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
            mw.visitJumpInsn(167, notNull_);
            mw.visitLabel(endDetect_);
            if ("this$0".equals(fieldName) || "this$1".equals(fieldName) || "this$2".equals(fieldName)) {
                mw.visitVarInsn(21, REF_DETECT);
                mw.visitJumpInsn(153, notNull_);
            }
        }
        if (Object[].class.isAssignableFrom(fieldClass)) {
            final Label notWriteEmptyArrayEnd_ = new Label();
            mwc.genIsEnabled(JSONWriter.Feature.NotWriteEmptyArray.mask, notWriteEmptyArrayEnd_);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitTypeInsn(192, "[Ljava/lang/Object;");
            mw.visitInsn(190);
            mw.visitJumpInsn(154, notWriteEmptyArrayEnd_);
            mw.visitJumpInsn(167, notNull_);
            mw.visitLabel(notWriteEmptyArrayEnd_);
        }
        this.gwFieldName(mwc, fieldWriter, i);
        if (fieldClass == BigDecimal.class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitLdcInsn(features);
            if (fieldWriter.decimalFormat != null) {
                mw.visitVarInsn(25, 0);
                mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
                mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
            }
            else {
                mw.visitInsn(1);
            }
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDecimal", "(Ljava/math/BigDecimal;JLjava/text/DecimalFormat;)V", false);
        }
        else if (fieldClass == BigInteger.class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            if (features == 0L) {
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeBigInt", "(Ljava/math/BigInteger;)V", false);
            }
            else {
                mw.visitLdcInsn(features);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeBigInt", "(Ljava/math/BigInteger;J)V", false);
            }
        }
        else if (fieldClass == UUID.class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeUUID", "(Ljava/util/UUID;)V", false);
        }
        else if (fieldClass == LocalDate.class && fieldWriter.format == null && mwc.provider.getObjectWriter(LocalDate.class) == ObjectWriterImplLocalDate.INSTANCE) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeLocalDate", "(Ljava/time/LocalDate;)V", false);
        }
        else if (fieldClass == OffsetDateTime.class && fieldWriter.format == null && mwc.provider.getObjectWriter(OffsetDateTime.class) == ObjectWriterImplOffsetDateTime.INSTANCE) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeOffsetDateTime", "(Ljava/time/OffsetDateTime;)V", false);
        }
        else if (fieldClass == String[].class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "([Ljava/lang/String;)V", false);
        }
        else {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "getObjectWriter", ObjectWriterCreatorASM.METHOD_DESC_GET_OBJECT_WRITER, false);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitLdcInsn(fieldName);
            mwc.loadFieldType(i, fieldType);
            mw.visitLdcInsn(features);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, ((features & JSONWriter.Feature.BeanToArray.mask) != 0x0L) ? "writeArrayMapping" : "write", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, true);
        }
        mw.visitJumpInsn(167, notNull_);
        if (refDetection) {
            final int REF_DETECT2 = mwc.var("REF_DETECT");
            final Label endDetect_2 = new Label();
            mw.visitVarInsn(21, REF_DETECT2);
            mw.visitJumpInsn(153, endDetect_2);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
            mw.visitLabel(endDetect_2);
        }
        mw.visitLabel(null_);
        if ((features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
            long nullFeatures = JSONWriter.Feature.WriteNulls.mask;
            if (fieldClass == AtomicLongArray.class || fieldClass == AtomicIntegerArray.class || Collection.class.isAssignableFrom(fieldClass) || fieldClass.isArray()) {
                nullFeatures |= JSONWriter.Feature.WriteNullListAsEmpty.mask;
                nullFeatures |= JSONWriter.Feature.NullAsDefaultValue.mask;
            }
            else if (Number.class.isAssignableFrom(fieldClass)) {
                nullFeatures |= JSONWriter.Feature.WriteNullNumberAsZero.mask;
                nullFeatures |= JSONWriter.Feature.NullAsDefaultValue.mask;
            }
            else if (fieldClass == Boolean.class) {
                nullFeatures |= JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
                nullFeatures |= JSONWriter.Feature.NullAsDefaultValue.mask;
            }
            else if (fieldClass == String.class) {
                nullFeatures |= JSONWriter.Feature.WriteNullStringAsEmpty.mask;
                nullFeatures |= JSONWriter.Feature.NullAsDefaultValue.mask;
            }
            mwc.genIsEnabled(nullFeatures, notNull_);
        }
        this.gwFieldName(mwc, fieldWriter, i);
        String WRITE_NULL_METHOD;
        if (fieldClass == AtomicLongArray.class || fieldClass == AtomicIntegerArray.class || Collection.class.isAssignableFrom(fieldClass) || fieldClass.isArray()) {
            WRITE_NULL_METHOD = "writeArrayNull";
        }
        else if (Number.class.isAssignableFrom(fieldClass)) {
            WRITE_NULL_METHOD = "writeNumberNull";
        }
        else if (fieldClass == Boolean.class) {
            WRITE_NULL_METHOD = "writeBooleanNull";
        }
        else if (fieldClass == String.class || fieldClass == Appendable.class || fieldClass == StringBuffer.class || fieldClass == StringBuilder.class) {
            WRITE_NULL_METHOD = "writeStringNull";
        }
        else {
            WRITE_NULL_METHOD = "writeNull";
        }
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, WRITE_NULL_METHOD, "()V", false);
        mw.visitLabel(notNull_);
    }
    
    private void gwFieldValueList(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final Type fieldType = fieldWriter.fieldType;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final MethodWriter mw = mwc.mw;
        final int LIST = mwc.var(fieldClass);
        final int REF_PATH = mwc.var("REF_PATH");
        Class itemClass = null;
        boolean listSimple = false;
        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                final Type arg0 = actualTypeArguments[0];
                itemClass = TypeUtils.getClass(arg0);
                listSimple = (arg0 == String.class || arg0 == Integer.class || arg0 == Long.class);
            }
        }
        final int FIELD_VALUE = mwc.var(fieldClass);
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        final Label null_ = new Label();
        final Label notNull_ = new Label();
        mw.visitJumpInsn(198, null_);
        final Label endDetect_ = new Label();
        final Label refSetPath_ = new Label();
        mwc.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, endDetect_);
        mw.visitVarInsn(25, OBJECT);
        mw.visitVarInsn(25, LIST);
        mw.visitJumpInsn(166, refSetPath_);
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn("..");
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
        mw.visitJumpInsn(167, notNull_);
        mw.visitLabel(refSetPath_);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, LIST);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "setPath", ObjectWriterCreatorASM.METHOD_DESC_SET_PATH2, false);
        mw.visitInsn(89);
        mw.visitVarInsn(58, REF_PATH);
        mw.visitJumpInsn(198, endDetect_);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, REF_PATH);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, LIST);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
        mw.visitJumpInsn(167, notNull_);
        mw.visitLabel(endDetect_);
        final Label notWriteEmptyArrayEnd_ = new Label();
        mwc.genIsEnabled(JSONWriter.Feature.NotWriteEmptyArray.mask, notWriteEmptyArrayEnd_);
        mw.visitVarInsn(25, LIST);
        mw.visitMethodInsn(185, "java/util/Collection", "isEmpty", "()Z", true);
        mw.visitJumpInsn(153, notWriteEmptyArrayEnd_);
        mw.visitJumpInsn(167, notNull_);
        mw.visitLabel(notWriteEmptyArrayEnd_);
        if (listSimple) {
            this.gwFieldName(mwc, fieldWriter, i);
            gwListSimpleType(mwc, i, mw, fieldClass, itemClass, FIELD_VALUE);
        }
        else {
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, mwc.jsonb ? "writeListValueJSONB" : "writeListValue", ObjectWriterCreatorASM.METHOD_DESC_WRITE_LIST, false);
        }
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, LIST);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
        mw.visitJumpInsn(167, notNull_);
        mw.visitLabel(null_);
        mwc.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask, notNull_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeArrayNull", "()V", false);
        mw.visitLabel(notNull_);
    }
    
    private void gwFieldValueJSONB(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        final Class objectType = mwc.objectClass;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        if (fieldClass == Boolean.TYPE) {
            this.gwFieldValueBooleanV(mwc, fieldWriter, OBJECT, i, true);
        }
        else if (fieldClass == boolean[].class || fieldClass == byte[].class || fieldClass == char[].class || fieldClass == short[].class || fieldClass == float[].class || fieldClass == double[].class) {
            this.gwFieldValueArray(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Character.TYPE || fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Float.TYPE || fieldClass == Double.TYPE) {
            this.gwFieldName(mwc, fieldWriter, i);
            this.gwValue(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Integer.TYPE) {
            this.gwFieldValueInt32V(mwc, fieldWriter, OBJECT, i, true);
        }
        else if (fieldClass == int[].class) {
            this.gwFieldValueIntVA(mwc, fieldWriter, OBJECT, i, true);
        }
        else if (fieldClass == Long.TYPE) {
            this.gwFieldValueInt64V(mwc, fieldWriter, OBJECT, i, true);
        }
        else if (fieldClass == long[].class && mwc.provider.getObjectWriter(Long.class) == ObjectWriterImplInt64.INSTANCE) {
            this.gwFieldValueInt64VA(mwc, fieldWriter, OBJECT, i, true);
        }
        else if (fieldClass == Integer.class) {
            this.gwInt32(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Long.class) {
            this.gwInt64(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == String.class) {
            this.gwFieldValueString(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass.isEnum()) {
            this.gwFieldValueArray(mwc, fieldWriter, OBJECT, i);
        }
        else if (fieldClass == Date.class) {
            this.gwFieldValueDate(mwc, fieldWriter, OBJECT, i);
        }
        else {
            this.gwFieldValueObjectJSONB(mwc, fieldWriter, OBJECT, i);
        }
    }
    
    private void gwInt32(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final boolean jsonb = mwc.jsonb;
        final String classNameType = mwc.classNameType;
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        final Label writeNullValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mwc.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, writeNullValue_, endIfNull_);
        mw.visitLabel(writeNullValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNumberNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(I)V", false);
        mw.visitLabel(endIfNull_);
    }
    
    private void gwInt64(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final boolean jsonb = mwc.jsonb;
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String classNameType = mwc.classNameType;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        final Label writeNullValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mwc.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, writeNullValue_, endIfNull_);
        mw.visitLabel(writeNullValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNumberNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        if (jsonb || (fieldWriter.features & (JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.WriteLongAsString.mask | JSONWriter.Feature.BrowserCompatible.mask)) == 0x0L) {
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt64", "(J)V", false);
        }
        else {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeInt64", ObjectWriterCreatorASM.METHOD_DESC_WRITE_J, false);
        }
        mw.visitLabel(endIfNull_);
    }
    
    private void gwDouble(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final boolean jsonb = mwc.jsonb;
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String classNameType = mwc.classNameType;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        final Label writeNullValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mwc.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, writeNullValue_, endIfNull_);
        mw.visitLabel(writeNullValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNumberNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        if (jsonb) {
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeDouble", "(D)V", false);
        }
        else {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeDouble", ObjectWriterCreatorASM.METHOD_DESC_WRITE_D, false);
        }
        mw.visitLabel(endIfNull_);
    }
    
    private void gwFloat(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final boolean jsonb = mwc.jsonb;
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String classNameType = mwc.classNameType;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        final Label writeNullValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mwc.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask, writeNullValue_, endIfNull_);
        mw.visitLabel(writeNullValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNumberNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        if (jsonb) {
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeFloat", "(D)V", false);
        }
        else {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeFloat", ObjectWriterCreatorASM.METHOD_DESC_WRITE_F, false);
        }
        mw.visitLabel(endIfNull_);
    }
    
    private void gwFieldValueObjectJSONB(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String fieldName = fieldWriter.fieldName;
        final boolean refDetection = !ObjectWriterProvider.isNotReferenceDetect(fieldClass);
        final int FIELD_VALUE = mwc.var(fieldClass);
        Integer REF_PATH = null;
        if (refDetection) {
            REF_PATH = mwc.var("REF_PATH");
        }
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(198, endIfNull_);
        if (!Serializable.class.isAssignableFrom(fieldClass) && fieldClass != List.class) {
            mw.visitVarInsn(25, 1);
            if (!fieldWriter.isFieldClassSerializable()) {
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isIgnoreNoneSerializable", "()Z", false);
            }
            else {
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isIgnoreNoneSerializable", "(Ljava/lang/Object;)Z", false);
            }
            mw.visitJumpInsn(154, endIfNull_);
        }
        if (refDetection) {
            final Label endDetect_ = new Label();
            final Label refSetPath_ = new Label();
            final int REF_DETECT = mwc.var("REF_DETECT");
            if (fieldClass == Object.class) {
                mw.visitVarInsn(25, 1);
                mw.visitVarInsn(25, FIELD_VALUE);
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isRefDetect", "(Ljava/lang/Object;)Z", false);
            }
            else {
                mwc.genIsEnabled(JSONWriter.Feature.ReferenceDetection.mask, null);
            }
            mw.visitInsn(89);
            mw.visitVarInsn(54, REF_DETECT);
            mw.visitJumpInsn(153, endDetect_);
            mw.visitVarInsn(25, OBJECT);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitJumpInsn(166, refSetPath_);
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitLdcInsn("..");
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
            mw.visitJumpInsn(167, endIfNull_);
            mw.visitLabel(refSetPath_);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "setPath", ObjectWriterCreatorASM.METHOD_DESC_SET_PATH2, false);
            mw.visitInsn(89);
            mw.visitVarInsn(58, REF_PATH);
            mw.visitJumpInsn(198, endDetect_);
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, REF_PATH);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeReference", "(Ljava/lang/String;)V", false);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
            mw.visitJumpInsn(167, endIfNull_);
            mw.visitLabel(endDetect_);
            if ("this$0".equals(fieldName) || "this$1".equals(fieldName) || "this$2".equals(fieldName)) {
                mw.visitVarInsn(21, REF_DETECT);
                mw.visitJumpInsn(153, endIfNull_);
            }
        }
        this.gwFieldName(mwc, fieldWriter, i);
        final Class itemClass = fieldWriter.getItemClass();
        if (fieldClass == List.class && (itemClass == String.class || itemClass == Integer.class || itemClass == Long.class)) {
            gwListSimpleType(mwc, i, mw, fieldClass, itemClass, FIELD_VALUE);
        }
        else {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "getObjectWriter", ObjectWriterCreatorASM.METHOD_DESC_GET_OBJECT_WRITER, false);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitLdcInsn(fieldName);
            mwc.loadFieldType(i, fieldWriter.fieldType);
            mw.visitLdcInsn(fieldWriter.features);
            mw.visitMethodInsn(185, ASMUtils.TYPE_OBJECT_WRITER, "writeJSONB", ObjectWriterCreatorASM.METHOD_DESC_WRITE_OBJECT, true);
        }
        if (refDetection) {
            final int REF_DETECT2 = mwc.var("REF_DETECT");
            final Label endDetect_2 = new Label();
            mw.visitVarInsn(21, REF_DETECT2);
            mw.visitJumpInsn(153, endDetect_2);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "popPath", "(Ljava/lang/Object;)V", false);
            mw.visitLabel(endDetect_2);
        }
        mw.visitLabel(endIfNull_);
    }
    
    private static void gwListSimpleType(final MethodWriterContext mwc, final int i, final MethodWriter mw, final Class<?> fieldClass, final Class itemClass, final int FIELD_VALUE) {
        if (mwc.jsonb) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mwc.loadFieldClass(i, fieldClass);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "checkAndWriteTypeName", "(Ljava/lang/Object;Ljava/lang/Class;)V", false);
        }
        if (itemClass == Integer.class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeListInt32", "(Ljava/util/List;)V", false);
            return;
        }
        if (itemClass == Long.class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeListInt64", "(Ljava/util/List;)V", false);
            return;
        }
        if (itemClass == String.class) {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "(Ljava/util/List;)V", false);
            return;
        }
        throw new JSONException("TOOD " + itemClass.getName());
    }
    
    static void gwString(final MethodWriterContext mwc, final boolean symbol, final boolean checkNull, final int STR) {
        final MethodWriter mw = mwc.mw;
        final Label notNull_ = new Label();
        final Label endNull_ = new Label();
        if (checkNull) {
            mw.visitVarInsn(25, STR);
            mw.visitJumpInsn(199, notNull_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeStringNull", "()V", false);
            mw.visitJumpInsn(167, endNull_);
            mw.visitLabel(notNull_);
        }
        if (!ObjectWriterCreatorASM.DISABLE_STRING_UNSAFE_GET && JDKUtils.JVM_VERSION == 8 && !JDKUtils.OPENJ9 && !JDKUtils.FIELD_STRING_VALUE_ERROR && !symbol) {
            mw.visitVarInsn(25, 1);
            mw.visitFieldInsn(178, ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            mw.visitVarInsn(25, STR);
            mw.visitLdcInsn(JDKUtils.FIELD_STRING_VALUE_OFFSET);
            mw.visitMethodInsn(182, "sun/misc/Unsafe", "getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;", false);
            mw.visitTypeInsn(192, "[C");
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "([C)V", false);
        }
        else if (!ObjectWriterCreatorASM.DISABLE_STRING_UNSAFE_GET && JDKUtils.JVM_VERSION > 8 && !JDKUtils.OPENJ9 && JDKUtils.FIELD_STRING_CODER_OFFSET != -1L && JDKUtils.FIELD_STRING_VALUE_OFFSET != -1L && !symbol) {
            final Label utf16_ = new Label();
            final Label end_ = new Label();
            mw.visitVarInsn(25, 1);
            mw.visitFieldInsn(178, ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            mw.visitVarInsn(25, STR);
            mw.visitLdcInsn(JDKUtils.FIELD_STRING_VALUE_OFFSET);
            mw.visitMethodInsn(182, "sun/misc/Unsafe", "getObject", "(Ljava/lang/Object;J)Ljava/lang/Object;", false);
            mw.visitTypeInsn(192, "[B");
            mw.visitFieldInsn(178, ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
            mw.visitVarInsn(25, STR);
            mw.visitLdcInsn(JDKUtils.FIELD_STRING_CODER_OFFSET);
            mw.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(Ljava/lang/Object;J)B", false);
            mw.visitJumpInsn(154, utf16_);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeStringLatin1", "([B)V", false);
            mw.visitJumpInsn(167, end_);
            mw.visitLabel(utf16_);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeStringUTF16", "([B)V", false);
            mw.visitLabel(end_);
        }
        else {
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, STR);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, symbol ? "writeSymbol" : "writeString", "(Ljava/lang/String;)V", false);
        }
        if (checkNull) {
            mw.visitLabel(endNull_);
        }
    }
    
    private void gwFieldValueDate(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final Label null_ = new Label();
        final Label writeNull_ = new Label();
        final Label endIfNull_ = new Label();
        final int FIELD_VALUE = mwc.var(fieldClass);
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(198, null_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitMethodInsn(182, "java/util/Date", "getTime", "()J", false);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeDate", ObjectWriterCreatorASM.METHOD_DESC_WRITE_J, false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(null_);
        if ((fieldWriter.features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
            mw.visitVarInsn(21, mwc.var("WRITE_NULLS"));
            mw.visitJumpInsn(154, writeNull_);
            mw.visitJumpInsn(167, endIfNull_);
        }
        mw.visitLabel(writeNull_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeNull", "()V", false);
        mw.visitLabel(endIfNull_);
    }
    
    private void gwFieldValueArray(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final MethodWriter mw = mwc.mw;
        final Class fieldClass = fieldWriter.fieldClass;
        String methodName;
        String methodDesc;
        if (fieldClass == char[].class) {
            methodName = "writeString";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_CArray;
        }
        else if (fieldClass == boolean[].class) {
            methodName = "writeBool";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_ZARRAY;
        }
        else if (fieldClass == byte[].class) {
            methodName = "writeBinary";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_BArray;
        }
        else if (fieldClass == short[].class) {
            methodName = "writeInt16";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_SArray;
        }
        else if (fieldClass == float[].class) {
            methodName = "writeFloat";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_FARRAY;
        }
        else if (fieldClass == double[].class) {
            methodName = "writeDouble";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_DARRAY;
        }
        else {
            if (!fieldClass.isEnum()) {
                throw new UnsupportedOperationException();
            }
            methodName = "writeEnumJSONB";
            methodDesc = ObjectWriterCreatorASM.METHOD_DESC_WRITE_ENUM;
        }
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, methodName, methodDesc, false);
    }
    
    private void gwFieldName(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int i) {
        final MethodWriter mw = mwc.mw;
        final String classNameType = mwc.classNameType;
        final Label labelElse = new Label();
        final Label labelEnd = new Label();
        boolean writeDirect = false;
        if (!mwc.jsonb) {
            final byte[] fieldNameUTF8 = fieldWriter.fieldName.getBytes(StandardCharsets.UTF_8);
            final int length = fieldNameUTF8.length;
            if (length >= 3 && length <= 9) {
                writeDirect = true;
                int name1 = 0;
                String methodDesc = "(J)V";
                byte[] bytes = new byte[8];
                String methodName = null;
                switch (length) {
                    case 3: {
                        bytes[0] = 34;
                        System.arraycopy(fieldNameUTF8, 0, bytes, 1, 3);
                        bytes[4] = 34;
                        bytes[5] = 58;
                        methodName = "writeName3Raw";
                        break;
                    }
                    case 4: {
                        bytes[0] = 34;
                        System.arraycopy(fieldNameUTF8, 0, bytes, 1, 4);
                        bytes[5] = 34;
                        bytes[6] = 58;
                        methodName = "writeName4Raw";
                        break;
                    }
                    case 5: {
                        bytes[0] = 34;
                        System.arraycopy(fieldNameUTF8, 0, bytes, 1, 5);
                        bytes[6] = 34;
                        bytes[7] = 58;
                        methodName = "writeName5Raw";
                        break;
                    }
                    case 6: {
                        bytes[0] = 34;
                        System.arraycopy(fieldNameUTF8, 0, bytes, 1, 6);
                        bytes[7] = 34;
                        methodName = "writeName6Raw";
                        break;
                    }
                    case 7: {
                        bytes[0] = 34;
                        System.arraycopy(fieldNameUTF8, 0, bytes, 1, 7);
                        methodName = "writeName7Raw";
                        break;
                    }
                    case 8: {
                        bytes = fieldNameUTF8;
                        methodName = "writeName8Raw";
                        break;
                    }
                    case 9: {
                        bytes[0] = 34;
                        System.arraycopy(fieldNameUTF8, 0, bytes, 1, 7);
                        methodDesc = "(JI)V";
                        final byte[] name1Bytes = { fieldNameUTF8[7], fieldNameUTF8[8], 34, 58 };
                        name1 = JDKUtils.UNSAFE.getInt(name1Bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                        methodName = "writeName9Raw";
                        break;
                    }
                    default: {
                        throw new IllegalStateException("length : " + length);
                    }
                }
                final long nameIn64 = JDKUtils.UNSAFE.getLong(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET);
                mw.visitVarInsn(21, mwc.var("UTF8_DIRECT"));
                mw.visitJumpInsn(153, labelElse);
                mw.visitVarInsn(25, 1);
                mw.visitLdcInsn(nameIn64);
                if (methodDesc.equals("(JI)V")) {
                    mw.visitLdcInsn(name1);
                }
                mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, methodName, methodDesc, false);
                mw.visitJumpInsn(167, labelEnd);
            }
        }
        if (writeDirect) {
            mw.visitLabel(labelElse);
        }
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, mwc.jsonb ? "writeFieldNameJSONB" : "writeFieldName", ObjectWriterCreatorASM.METHOD_DESC_WRITE_FIELD_NAME, false);
        if (writeDirect) {
            mw.visitLabel(labelEnd);
        }
    }
    
    private void gwFieldValueInt64VA(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i, final boolean jsonb) {
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        final Label writeNullValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(21, mwc.var("WRITE_NULLS"));
        mw.visitJumpInsn(154, writeNullValue_);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(writeNullValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeArrayNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt64", "([J)V", false);
        mw.visitLabel(endIfNull_);
    }
    
    private void gwFieldValueInt64V(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i, final boolean jsonb) {
        final MethodWriter mw = mwc.mw;
        final String format = fieldWriter.format;
        final String classNameType = mwc.classNameType;
        final int FIELD_VALUE = mwc.var(Long.TYPE);
        final int WRITE_DEFAULT_VALUE = mwc.var("WRITE_DEFAULT_VALUE");
        final Label notDefaultValue_ = new Label();
        final Label endWriteValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(92);
        mw.visitVarInsn(55, FIELD_VALUE);
        mw.visitInsn(9);
        mw.visitInsn(148);
        mw.visitJumpInsn(154, notDefaultValue_);
        if (fieldWriter.defaultValue == null) {
            mw.visitVarInsn(21, WRITE_DEFAULT_VALUE);
            mw.visitJumpInsn(153, notDefaultValue_);
            mw.visitJumpInsn(167, endWriteValue_);
        }
        mw.visitLabel(notDefaultValue_);
        final boolean iso8601 = "iso8601".equals(format);
        if (iso8601 || (fieldWriter.features & (JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.WriteLongAsString.mask | JSONWriter.Feature.BrowserCompatible.mask)) != 0x0L) {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(22, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, iso8601 ? "writeDate" : "writeInt64", ObjectWriterCreatorASM.METHOD_DESC_WRITE_J, false);
        }
        else {
            this.gwFieldName(mwc, fieldWriter, i);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(22, FIELD_VALUE);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt64", "(J)V", false);
        }
        mw.visitLabel(endWriteValue_);
    }
    
    void gwFieldValueIntVA(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i, final boolean jsonb) {
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label endIfNull_ = new Label();
        final Label notNull_ = new Label();
        final Label writeNullValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(21, mwc.var("WRITE_NULLS"));
        mw.visitJumpInsn(154, writeNullValue_);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(writeNullValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeArrayNull", "()V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(notNull_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, FIELD_VALUE);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt32", "([I)V", false);
        mw.visitLabel(endIfNull_);
    }
    
    private void gwFieldValueInt32V(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i, final boolean jsonb) {
        final MethodWriter mw = mwc.mw;
        final String format = fieldWriter.format;
        final String classNameType = mwc.classNameType;
        final int FIELD_VALUE = mwc.var(Integer.TYPE);
        final int WRITE_DEFAULT_VALUE = mwc.var("WRITE_DEFAULT_VALUE");
        final Label notDefaultValue_ = new Label();
        final Label endWriteValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(54, FIELD_VALUE);
        mw.visitJumpInsn(154, notDefaultValue_);
        if (fieldWriter.defaultValue == null) {
            mw.visitVarInsn(21, WRITE_DEFAULT_VALUE);
            mw.visitJumpInsn(153, notDefaultValue_);
            mw.visitJumpInsn(167, endWriteValue_);
        }
        mw.visitLabel(notDefaultValue_);
        this.gwFieldName(mwc, fieldWriter, i);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(21, FIELD_VALUE);
        if ("string".equals(format)) {
            mw.visitMethodInsn(184, "java/lang/Integer", "toString", "(I)Ljava/lang/String;", false);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "(Ljava/lang/String;)V", false);
        }
        else if (format != null) {
            mw.visitLdcInsn(format);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(ILjava/lang/String;)V", false);
        }
        else if (format != null) {
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "decimalFormat", "Ljava/text/DecimalFormat;");
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(ILjava/text/DecimalFormat;)V", false);
        }
        else {
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeInt32", "(I)V", false);
        }
        mw.visitLabel(endWriteValue_);
    }
    
    private void gwFieldValueBooleanV(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i, final boolean jsonb) {
        final MethodWriter mw = mwc.mw;
        final String classNameType = mwc.classNameType;
        final int FIELD_VALUE = mwc.var(Boolean.TYPE);
        final int WRITE_DEFAULT_VALUE = mwc.var("WRITE_DEFAULT_VALUE");
        final Label notDefaultValue_ = new Label();
        final Label endWriteValue_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(54, FIELD_VALUE);
        mw.visitJumpInsn(154, notDefaultValue_);
        if (fieldWriter.defaultValue == null) {
            mw.visitVarInsn(21, WRITE_DEFAULT_VALUE);
            mw.visitJumpInsn(153, notDefaultValue_);
            mw.visitJumpInsn(167, endWriteValue_);
        }
        mw.visitLabel(notDefaultValue_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(21, FIELD_VALUE);
        mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "writeBool", ObjectWriterCreatorASM.METHOD_DESC_WRITE_Z, false);
        mw.visitLabel(endWriteValue_);
    }
    
    private void gwFieldValueString(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int OBJECT, final int i) {
        final boolean jsonb = mwc.jsonb;
        final long features = fieldWriter.features | mwc.objectFeatures;
        final MethodWriter mw = mwc.mw;
        final Class<?> fieldClass = (Class<?>)fieldWriter.fieldClass;
        final String format = fieldWriter.format;
        final int FIELD_VALUE = mwc.var(fieldClass);
        final Label null_ = new Label();
        final Label endIfNull_ = new Label();
        this.genGetObject(mwc, fieldWriter, i, OBJECT);
        mw.visitInsn(89);
        mw.visitVarInsn(58, FIELD_VALUE);
        mw.visitJumpInsn(198, null_);
        this.gwFieldName(mwc, fieldWriter, i);
        if ("trim".equals(format)) {
            mw.visitVarInsn(25, FIELD_VALUE);
            mw.visitMethodInsn(182, "java/lang/String", "trim", "()Ljava/lang/String;", false);
            mw.visitVarInsn(58, FIELD_VALUE);
        }
        final boolean symbol = jsonb && "symbol".equals(format);
        gwString(mwc, symbol, false, FIELD_VALUE);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(null_);
        final Label writeNullValue_ = new Label();
        final Label writeNull_ = new Label();
        final long defaultValueMask = JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullNumberAsZero.mask | JSONWriter.Feature.WriteNullBooleanAsFalse.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask;
        if ((features & (JSONWriter.Feature.WriteNulls.mask | defaultValueMask)) == 0x0L) {
            mwc.genIsEnabled(JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask, writeNull_, endIfNull_);
        }
        mw.visitLabel(writeNull_);
        if (fieldWriter.defaultValue == null) {
            mwc.genIsDisabled(JSONWriter.Feature.NotWriteDefaultValue.mask, endIfNull_);
        }
        this.gwFieldName(mwc, fieldWriter, i);
        if ((features & defaultValueMask) == 0x0L) {
            long mask = JSONWriter.Feature.NullAsDefaultValue.mask;
            if (fieldClass == String.class) {
                mask |= JSONWriter.Feature.WriteNullStringAsEmpty.mask;
            }
            else if (fieldClass == Boolean.class) {
                mask |= JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
            }
            else if (Number.class.isAssignableFrom(fieldClass)) {
                mask |= JSONWriter.Feature.WriteNullNumberAsZero.mask;
            }
            else if (Collection.class.isAssignableFrom(fieldClass)) {
                mask |= JSONWriter.Feature.WriteNullListAsEmpty.mask;
            }
            mw.visitVarInsn(25, 1);
            mw.visitLdcInsn(mask);
            mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "isEnabled", "(J)Z", false);
            mw.visitJumpInsn(153, writeNullValue_);
        }
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn("");
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeString", "(Ljava/lang/String;)V", false);
        mw.visitJumpInsn(167, endIfNull_);
        mw.visitLabel(writeNullValue_);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "writeStringNull", "()V", false);
        mw.visitLabel(endIfNull_);
    }
    
    private void genMethodInit(final List<FieldWriter> fieldWriters, final ClassWriter cw, final String classNameType, final String objectWriterSupper) {
        final MethodWriter mw = cw.visitMethod(1, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V", 64);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitVarInsn(22, 4);
        mw.visitVarInsn(25, 6);
        mw.visitMethodInsn(183, objectWriterSupper, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;JLjava/util/List;)V", false);
        if (objectWriterSupper == ASMUtils.TYPE_OBJECT_WRITER_ADAPTER) {
            for (int i = 0; i < fieldWriters.size(); ++i) {
                mw.visitVarInsn(25, 0);
                mw.visitInsn(89);
                mw.visitFieldInsn(180, ASMUtils.TYPE_OBJECT_WRITER_ADAPTER, "fieldWriterArray", ASMUtils.DESC_FIELD_WRITER_ARRAY);
                switch (i) {
                    case 0: {
                        mw.visitInsn(3);
                        break;
                    }
                    case 1: {
                        mw.visitInsn(4);
                        break;
                    }
                    case 2: {
                        mw.visitInsn(5);
                        break;
                    }
                    case 3: {
                        mw.visitInsn(6);
                        break;
                    }
                    case 4: {
                        mw.visitInsn(7);
                        break;
                    }
                    case 5: {
                        mw.visitInsn(8);
                        break;
                    }
                    default: {
                        if (i >= 128) {
                            mw.visitIntInsn(17, i);
                            break;
                        }
                        mw.visitIntInsn(16, i);
                        break;
                    }
                }
                mw.visitInsn(50);
                mw.visitTypeInsn(192, ASMUtils.TYPE_FIELD_WRITER);
                mw.visitFieldInsn(181, classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            }
        }
        mw.visitInsn(177);
        mw.visitMaxs(7, 7);
    }
    
    private void genFields(final List<FieldWriter> fieldWriters, final ClassWriter cw, final String objectWriterSupper) {
        if (objectWriterSupper != ASMUtils.TYPE_OBJECT_WRITER_ADAPTER) {
            return;
        }
        for (int i = 0; i < fieldWriters.size(); ++i) {
            cw.visitField(1, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
        }
    }
    
    @Override
    public <T> FieldWriter<T> createFieldWriter(final ObjectWriterProvider provider, final String fieldName, final int ordinal, final long features, String format, final String label, final Field field, final ObjectWriter initObjectWriter) {
        final Class<?> declaringClass = field.getDeclaringClass();
        if (Throwable.class.isAssignableFrom(declaringClass) || declaringClass.getName().startsWith("java.lang")) {
            return super.createFieldWriter(provider, fieldName, ordinal, features, format, label, field, initObjectWriter);
        }
        Class<?> fieldClass = field.getType();
        Type fieldType = field.getGenericType();
        if (initObjectWriter != null) {
            if (fieldClass == Byte.TYPE) {
                fieldClass = (Class<?>)(fieldType = Byte.class);
            }
            else if (fieldClass == Short.TYPE) {
                fieldClass = (Class<?>)(fieldType = Short.class);
            }
            else if (fieldClass == Float.TYPE) {
                fieldClass = (Class<?>)(fieldType = Float.class);
            }
            else if (fieldClass == Double.TYPE) {
                fieldClass = (Class<?>)(fieldType = Double.class);
            }
            else if (fieldClass == Boolean.TYPE) {
                fieldClass = (Class<?>)(fieldType = Boolean.class);
            }
            final FieldWriterObject objImp = new FieldWriterObject(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, null);
            objImp.initValueClass = fieldClass;
            if (initObjectWriter != ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
                objImp.initObjectWriter = initObjectWriter;
            }
            return (FieldWriter<T>)objImp;
        }
        if (fieldClass == Boolean.TYPE) {
            return (FieldWriter<T>)new FieldWriterBoolValField(fieldName, ordinal, features, format, label, field, fieldClass);
        }
        if (fieldClass == Byte.TYPE) {
            return new FieldWriterInt8ValField<T>(fieldName, ordinal, features, format, label, field);
        }
        if (fieldClass == Short.TYPE) {
            return new FieldWriterInt16ValField<T>(fieldName, ordinal, features, format, label, field);
        }
        if (fieldClass == Integer.TYPE) {
            return new FieldWriterInt32Val<T>(fieldName, ordinal, features, format, label, field);
        }
        if (fieldClass == Long.TYPE) {
            if (format == null || format.isEmpty()) {
                return new FieldWriterInt64ValField<T>(fieldName, ordinal, features, format, label, field);
            }
            return new FieldWriterMillisField<T>(fieldName, ordinal, features, format, label, field);
        }
        else {
            if (fieldClass == Float.TYPE) {
                return new FieldWriterFloatValField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Float.class) {
                return new FieldWriterFloatField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Double.TYPE) {
                return new FieldWriterDoubleValField<T>(fieldName, ordinal, format, label, field);
            }
            if (fieldClass == Double.class) {
                return new FieldWriterDoubleField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Character.TYPE) {
                return new FieldWriterCharValField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == BigInteger.class) {
                return new FieldWriterBigIntField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == BigDecimal.class) {
                return new FieldWriterBigDecimalField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Date.class) {
                if (format != null) {
                    format = format.trim();
                    if (format.isEmpty()) {
                        format = null;
                    }
                }
                return new FieldWriterDateField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == String.class) {
                return new FieldWriterStringField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass.isEnum()) {
                final BeanInfo beanInfo = new BeanInfo();
                provider.getBeanInfo(beanInfo, fieldClass);
                boolean writeEnumAsJavaBean = beanInfo.writeEnumAsJavaBean;
                if (!writeEnumAsJavaBean) {
                    final ObjectWriter objectWriter = provider.cache.get(fieldClass);
                    if (objectWriter != null && !(objectWriter instanceof ObjectWriterImplEnum)) {
                        writeEnumAsJavaBean = true;
                    }
                }
                final Member enumValueField = BeanUtils.getEnumValueField(fieldClass, provider);
                if (enumValueField == null && !writeEnumAsJavaBean) {
                    final String[] enumAnnotationNames = BeanUtils.getEnumAnnotationNames(fieldClass);
                    if (enumAnnotationNames == null) {
                        return (FieldWriter<T>)new FieldWriterEnum(fieldName, ordinal, features, format, label, fieldType, (Class<? extends Enum>)fieldClass, field, null);
                    }
                }
            }
            if (fieldClass == List.class || fieldClass == ArrayList.class) {
                Type itemType = null;
                if (fieldType instanceof ParameterizedType) {
                    itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
                }
                return new FieldWriterListField<T>(fieldName, itemType, ordinal, features, format, label, fieldType, fieldClass, field);
            }
            if (fieldClass.isArray()) {
                final Class<?> itemClass = fieldClass.getComponentType();
                if (declaringClass == Throwable.class && "stackTrace".equals(fieldName)) {
                    try {
                        final Method method = Throwable.class.getMethod("getStackTrace", (Class<?>[])new Class[0]);
                        return new FieldWriterObjectArrayMethod<T>(fieldName, itemClass, ordinal, features, format, label, fieldType, fieldClass, field, method);
                    }
                    catch (NoSuchMethodException ex) {}
                }
            }
            if (fieldClass == BigDecimal[].class) {
                return new FieldWriterObjectArrayField<T>(fieldName, BigDecimal.class, ordinal, features, format, label, BigDecimal[].class, BigDecimal[].class, field);
            }
            if (fieldClass == Float[].class) {
                return new FieldWriterObjectArrayField<T>(fieldName, Float.class, ordinal, features, format, label, Float[].class, Float[].class, field);
            }
            if (fieldClass == Double[].class) {
                return new FieldWriterObjectArrayField<T>(fieldName, Float.class, ordinal, features, format, label, Double[].class, Double[].class, field);
            }
            if (TypeUtils.isFunction(fieldClass)) {
                return null;
            }
            return new FieldWriterObject<T>(fieldName, ordinal, features, format, label, field.getGenericType(), fieldClass, field, null);
        }
    }
    
    void genGetObject(final MethodWriterContext mwc, final FieldWriter fieldWriter, final int i, final int OBJECT) {
        final MethodWriter mw = mwc.mw;
        final Class objectClass = mwc.objectClass;
        final String TYPE_OBJECT = (objectClass == null) ? "java/lang/Object" : ASMUtils.type(objectClass);
        final Class fieldClass = fieldWriter.fieldClass;
        final Member member = (fieldWriter.method != null) ? fieldWriter.method : fieldWriter.field;
        final Function function = fieldWriter.getFunction();
        if (member == null && function != null) {
            mw.visitVarInsn(25, 0);
            mw.visitFieldInsn(180, mwc.classNameType, fieldWriter(i), ASMUtils.DESC_FIELD_WRITER);
            mw.visitMethodInsn(182, ASMUtils.TYPE_FIELD_WRITER, "getFunction", "()Ljava/util/function/Function;", false);
            mw.visitVarInsn(25, OBJECT);
            mw.visitMethodInsn(185, ASMUtils.type(Function.class), "apply", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            mw.visitTypeInsn(192, ASMUtils.type(fieldClass));
            return;
        }
        if (member instanceof Method) {
            mw.visitVarInsn(25, OBJECT);
            mw.visitTypeInsn(192, TYPE_OBJECT);
            mw.visitMethodInsn(182, TYPE_OBJECT, member.getName(), "()" + ASMUtils.desc(fieldClass), false);
            return;
        }
        if (Modifier.isPublic(objectClass.getModifiers()) && Modifier.isPublic(member.getModifiers()) && !this.classLoader.isExternalClass(objectClass)) {
            mw.visitVarInsn(25, OBJECT);
            mw.visitTypeInsn(192, TYPE_OBJECT);
            mw.visitFieldInsn(180, TYPE_OBJECT, member.getName(), ASMUtils.desc(fieldClass));
            return;
        }
        final Field field = (Field)member;
        String castToType = null;
        String methodName;
        String methodDes;
        if (fieldClass == Integer.TYPE) {
            methodName = "getInt";
            methodDes = "(Ljava/lang/Object;J)I";
        }
        else if (fieldClass == Long.TYPE) {
            methodName = "getLong";
            methodDes = "(Ljava/lang/Object;J)J";
        }
        else if (fieldClass == Float.TYPE) {
            methodName = "getFloat";
            methodDes = "(Ljava/lang/Object;J)F";
        }
        else if (fieldClass == Double.TYPE) {
            methodName = "getDouble";
            methodDes = "(Ljava/lang/Object;J)D";
        }
        else if (fieldClass == Character.TYPE) {
            methodName = "getChar";
            methodDes = "(Ljava/lang/Object;J)C";
        }
        else if (fieldClass == Byte.TYPE) {
            methodName = "getByte";
            methodDes = "(Ljava/lang/Object;J)B";
        }
        else if (fieldClass == Short.TYPE) {
            methodName = "getShort";
            methodDes = "(Ljava/lang/Object;J)S";
        }
        else if (fieldClass == Boolean.TYPE) {
            methodName = "getBoolean";
            methodDes = "(Ljava/lang/Object;J)Z";
        }
        else {
            methodName = "getObject";
            methodDes = "(Ljava/lang/Object;J)Ljava/lang/Object;";
            if (fieldClass.isEnum()) {
                castToType = "java/lang/Enum";
            }
            else if (ObjectWriterProvider.isPrimitiveOrEnum(fieldClass)) {
                castToType = ASMUtils.type(fieldClass);
            }
            else if (fieldClass.isArray() && ObjectWriterProvider.isPrimitiveOrEnum(fieldClass.getComponentType())) {
                castToType = ASMUtils.type(fieldClass);
            }
            else if (Map.class.isAssignableFrom(fieldClass)) {
                castToType = "java/util/Map";
            }
            else if (List.class.isAssignableFrom(fieldClass)) {
                castToType = "java/util/List";
            }
            else if (Collection.class.isAssignableFrom(fieldClass)) {
                castToType = "java/util/Collection";
            }
        }
        mw.visitFieldInsn(178, ObjectWriterCreatorASMUtils.TYPE_UNSAFE_UTILS, "UNSAFE", "Lsun/misc/Unsafe;");
        mw.visitVarInsn(25, OBJECT);
        mw.visitLdcInsn(JDKUtils.UNSAFE.objectFieldOffset(field));
        mw.visitMethodInsn(182, "sun/misc/Unsafe", methodName, methodDes, false);
        if (castToType != null) {
            mw.visitTypeInsn(192, castToType);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterCreatorASM(DynamicClassLoader.getInstance());
        final String key = "fastjson2.disableStringUnsafeGet";
        String property = System.getProperty(key);
        if (property == null || property.isEmpty()) {
            final String str = JSONFactory.getProperty(key);
            if (str != null && !property.isEmpty()) {
                property = str;
            }
        }
        boolean value = JDKUtils.ANDROID || JDKUtils.GRAAL;
        if ("".equals(property) || "true".equals(property)) {
            value = true;
        }
        else if ("false".equals(property)) {
            value = false;
        }
        DISABLE_STRING_UNSAFE_GET = false;
        seed = new AtomicLong();
        INTERFACES = new String[] { ASMUtils.TYPE_OBJECT_WRITER };
        METHOD_DESC_WRITE_VALUE = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;)V";
        METHOD_DESC_WRITE = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V";
        METHOD_DESC_WRITE_FIELD_NAME = "(" + ASMUtils.DESC_JSON_WRITER + ")V";
        METHOD_DESC_WRITE_OBJECT = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;J)V";
        METHOD_DESC_WRITE_J = "(" + ASMUtils.DESC_JSON_WRITER + "J)V";
        METHOD_DESC_WRITE_D = "(" + ASMUtils.DESC_JSON_WRITER + "D)V";
        METHOD_DESC_WRITE_F = "(" + ASMUtils.DESC_JSON_WRITER + "F)V";
        METHOD_DESC_WRITE_DATE_WITH_FIELD_NAME = "(" + ASMUtils.DESC_JSON_WRITER + "ZLjava/util/Date;)V";
        METHOD_DESC_WRITE_Z = "(" + ASMUtils.DESC_JSON_WRITER + "Z)V";
        METHOD_DESC_WRITE_ZARRAY = "(" + ASMUtils.DESC_JSON_WRITER + "[Z)V";
        METHOD_DESC_WRITE_FARRAY = "(" + ASMUtils.DESC_JSON_WRITER + "[F)V";
        METHOD_DESC_WRITE_DARRAY = "(" + ASMUtils.DESC_JSON_WRITER + "[D)V";
        METHOD_DESC_WRITE_I = "(" + ASMUtils.DESC_JSON_WRITER + "I)V";
        METHOD_DESC_WRITE_SArray = "(" + ASMUtils.DESC_JSON_WRITER + "[S)V";
        METHOD_DESC_WRITE_BArray = "(" + ASMUtils.DESC_JSON_WRITER + "[B)V";
        METHOD_DESC_WRITE_CArray = "(" + ASMUtils.DESC_JSON_WRITER + "[C)V";
        METHOD_DESC_WRITE_ENUM = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Enum;)V";
        METHOD_DESC_WRITE_LIST = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/util/List;)V";
        METHOD_DESC_FIELD_WRITE_OBJECT = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Object;)Z";
        METHOD_DESC_GET_OBJECT_WRITER = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/Class;)" + ASMUtils.DESC_OBJECT_WRITER;
        METHOD_DESC_GET_ITEM_WRITER = "(" + ASMUtils.DESC_JSON_WRITER + "Ljava/lang/reflect/Type;)" + ASMUtils.DESC_OBJECT_WRITER;
        METHOD_DESC_WRITE_TYPE_INFO = "(" + ASMUtils.DESC_JSON_WRITER + ")Z";
        METHOD_DESC_HAS_FILTER = "(" + ASMUtils.DESC_JSON_WRITER + ")Z";
        METHOD_DESC_SET_PATH2 = "(" + ASMUtils.DESC_FIELD_WRITER + "Ljava/lang/Object;)Ljava/lang/String;";
        METHOD_DESC_WRITE_CLASS_INFO = "(" + ASMUtils.DESC_JSON_WRITER + ")V";
    }
    
    static class MethodWriterContext
    {
        final ObjectWriterProvider provider;
        final Class objectClass;
        final long objectFeatures;
        final String classNameType;
        final MethodWriter mw;
        final Map<Object, Integer> variants;
        final boolean jsonb;
        int maxVariant;
        
        public MethodWriterContext(final ObjectWriterProvider provider, final Class objectClass, final long objectFeatures, final String classNameType, final MethodWriter mw, final int maxVariant, final boolean jsonb) {
            this.variants = new LinkedHashMap<Object, Integer>();
            this.provider = provider;
            this.objectClass = objectClass;
            this.objectFeatures = objectFeatures;
            this.classNameType = classNameType;
            this.mw = mw;
            this.jsonb = jsonb;
            this.maxVariant = maxVariant;
        }
        
        int var(final Object key) {
            Integer var = this.variants.get(key);
            if (var == null) {
                var = this.maxVariant;
                this.variants.put(key, var);
                if (key == Long.TYPE || key == Double.TYPE) {
                    this.maxVariant += 2;
                }
                else {
                    ++this.maxVariant;
                }
            }
            return var;
        }
        
        int var2(final Object key) {
            Integer var = this.variants.get(key);
            if (var == null) {
                var = this.maxVariant;
                this.variants.put(key, var);
                this.maxVariant += 2;
            }
            return var;
        }
        
        void genVariantsMethodBefore() {
            final Label notDefault_ = new Label();
            final Label end_ = new Label();
            this.mw.visitVarInsn(25, 1);
            this.mw.visitMethodInsn(182, ASMUtils.TYPE_JSON_WRITER, "getFeatures", "()J", false);
            this.mw.visitVarInsn(55, this.var2("CONTEXT_FEATURES"));
            final Label l1 = new Label();
            final Label l2 = new Label();
            this.mw.visitVarInsn(25, 1);
            this.mw.visitFieldInsn(180, ASMUtils.TYPE_JSON_WRITER, "utf8", "Z");
            this.mw.visitJumpInsn(153, l1);
            this.mw.visitVarInsn(22, this.var2("CONTEXT_FEATURES"));
            this.mw.visitLdcInsn(JSONWriter.Feature.UnquoteFieldName.mask | JSONWriter.Feature.UseSingleQuotes.mask);
            this.mw.visitInsn(127);
            this.mw.visitInsn(9);
            this.mw.visitInsn(148);
            this.mw.visitJumpInsn(154, l1);
            this.mw.visitInsn(4);
            this.mw.visitJumpInsn(167, l2);
            this.mw.visitLabel(l1);
            this.mw.visitInsn(3);
            this.mw.visitLabel(l2);
            this.mw.visitVarInsn(54, this.var2("UTF8_DIRECT"));
            this.genIsEnabledAndAssign(JSONWriter.Feature.NotWriteDefaultValue.mask, this.var("WRITE_DEFAULT_VALUE"));
            this.mw.visitVarInsn(21, this.var("WRITE_DEFAULT_VALUE"));
            this.mw.visitJumpInsn(153, notDefault_);
            this.mw.visitInsn(3);
            this.mw.visitVarInsn(54, this.var("WRITE_NULLS"));
            this.mw.visitJumpInsn(167, end_);
            this.mw.visitLabel(notDefault_);
            final long features = JSONWriter.Feature.WriteNulls.mask | JSONWriter.Feature.NullAsDefaultValue.mask;
            this.genIsEnabledAndAssign(features, this.var("WRITE_NULLS"));
            this.mw.visitLabel(end_);
        }
        
        void genIsEnabled(final long features, final Label elseLabel) {
            this.mw.visitVarInsn(22, this.var2("CONTEXT_FEATURES"));
            this.mw.visitLdcInsn(features);
            this.mw.visitInsn(127);
            this.mw.visitInsn(9);
            this.mw.visitInsn(148);
            if (elseLabel != null) {
                this.mw.visitJumpInsn(153, elseLabel);
            }
        }
        
        void genIsDisabled(final long features, final Label elseLabel) {
            this.mw.visitVarInsn(22, this.var2("CONTEXT_FEATURES"));
            this.mw.visitLdcInsn(features);
            this.mw.visitInsn(127);
            this.mw.visitInsn(9);
            this.mw.visitInsn(148);
            this.mw.visitJumpInsn(154, elseLabel);
        }
        
        void genIsEnabled(final long features, final Label trueLabel, final Label falseLabel) {
            this.mw.visitVarInsn(22, this.var2("CONTEXT_FEATURES"));
            this.mw.visitLdcInsn(features);
            this.mw.visitInsn(127);
            this.mw.visitInsn(9);
            this.mw.visitInsn(148);
            this.mw.visitJumpInsn(153, falseLabel);
            this.mw.visitJumpInsn(167, trueLabel);
        }
        
        void genIsEnabledAndAssign(final long features, final int var) {
            this.mw.visitVarInsn(22, this.var2("CONTEXT_FEATURES"));
            this.mw.visitLdcInsn(features);
            this.mw.visitInsn(127);
            this.mw.visitInsn(9);
            this.mw.visitInsn(148);
            this.mw.visitVarInsn(54, var);
        }
        
        private void loadFieldType(final int fieldIndex, final Type fieldType) {
            if (fieldType instanceof Class && fieldType.getTypeName().startsWith("java")) {
                this.mw.visitLdcInsn((Class)fieldType);
                return;
            }
            this.mw.visitVarInsn(25, 0);
            this.mw.visitFieldInsn(180, this.classNameType, ObjectWriterCreatorASM.fieldWriter(fieldIndex), ASMUtils.DESC_FIELD_WRITER);
            this.mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "fieldType", "Ljava/lang/reflect/Type;");
        }
        
        private void loadFieldClass(final int fieldIndex, final Class fieldClass) {
            if (fieldClass.getName().startsWith("java")) {
                this.mw.visitLdcInsn(fieldClass);
                return;
            }
            this.mw.visitVarInsn(25, 0);
            this.mw.visitFieldInsn(180, this.classNameType, ObjectWriterCreatorASM.fieldWriter(fieldIndex), ASMUtils.DESC_FIELD_WRITER);
            this.mw.visitFieldInsn(180, ASMUtils.TYPE_FIELD_WRITER, "fieldClass", "Ljava/lang/Class;");
        }
    }
}
