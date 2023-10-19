// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import java.util.function.BiFunction;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.annotation.JSONCreator;

public interface ApacheLang3Support
{
    public interface TripleMixIn<L, M, R>
    {
        @JSONCreator
        default <L, M, R> Object of(final L left, final M middle, final R right) {
            return null;
        }
    }
    
    public static class PairReader implements ObjectReader
    {
        static final long LEFT;
        static final long RIGHT;
        final Class objectClass;
        final Type leftType;
        final Type rightType;
        final BiFunction of;
        
        public PairReader(final Class objectClass, final Type leftType, final Type rightType) {
            this.objectClass = objectClass;
            this.leftType = leftType;
            this.rightType = rightType;
            try {
                this.of = LambdaMiscCodec.createBiFunction(objectClass.getMethod("of", Object.class, Object.class));
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("Pair.of method not found", e);
            }
        }
        
        @Override
        public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.nextIfNull()) {
                return null;
            }
            if (jsonReader.nextIfMatch((byte)(-110))) {
                final long typeHash = jsonReader.readTypeHashCode();
                final long PAIR = 4645080105124911238L;
                final long MUTABLE_PAIR = 8310287657375596772L;
                final long IMMUTABLE_PAIR = -2802985644706367574L;
                if (typeHash != 4645080105124911238L && typeHash != -2802985644706367574L && typeHash != 8310287657375596772L) {
                    throw new JSONException("not support inputType : " + jsonReader.getString());
                }
            }
            Object left = null;
            Object right = null;
            if (jsonReader.nextIfObjectStart()) {
                for (int i = 0; i < 100 && !jsonReader.nextIfObjectEnd(); ++i) {
                    if (jsonReader.isString()) {
                        final long hashCode = jsonReader.readFieldNameHashCode();
                        if (hashCode == PairReader.LEFT) {
                            left = jsonReader.read(this.leftType);
                        }
                        else if (hashCode == PairReader.RIGHT) {
                            right = jsonReader.read(this.rightType);
                        }
                        else if (i == 0) {
                            left = jsonReader.getFieldName();
                            right = jsonReader.read(this.rightType);
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }
                    else {
                        if (i != 0) {
                            throw new JSONException(jsonReader.info("not support input"));
                        }
                        left = jsonReader.read(this.leftType);
                        right = jsonReader.read(this.rightType);
                    }
                }
            }
            else {
                if (!jsonReader.isArray()) {
                    throw new JSONException(jsonReader.info("not support input"));
                }
                final int len = jsonReader.startArray();
                if (len != 2) {
                    throw new JSONException(jsonReader.info("not support input"));
                }
                left = jsonReader.read(this.leftType);
                right = jsonReader.read(this.rightType);
            }
            return this.of.apply(left, right);
        }
        
        @Override
        public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
            if (jsonReader.nextIfNull()) {
                return null;
            }
            Object left = null;
            Object right = null;
            if (jsonReader.nextIfObjectStart()) {
                for (int i = 0; i < 100 && !jsonReader.nextIfObjectEnd(); ++i) {
                    if (jsonReader.isString()) {
                        final long hashCode = jsonReader.readFieldNameHashCode();
                        if (hashCode == PairReader.LEFT) {
                            left = jsonReader.read(this.leftType);
                        }
                        else if (hashCode == PairReader.RIGHT) {
                            right = jsonReader.read(this.rightType);
                        }
                        else if (i == 0) {
                            left = jsonReader.getFieldName();
                            jsonReader.nextIfMatch(':');
                            right = jsonReader.read(this.rightType);
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }
                    else {
                        if (i != 0) {
                            throw new JSONException(jsonReader.info("not support input"));
                        }
                        left = jsonReader.read(this.leftType);
                        jsonReader.nextIfMatch(':');
                        right = jsonReader.read(this.rightType);
                    }
                }
            }
            else {
                if (!jsonReader.nextIfArrayStart()) {
                    throw new JSONException(jsonReader.info("not support input"));
                }
                left = jsonReader.read(this.leftType);
                right = jsonReader.read(this.rightType);
                if (!jsonReader.nextIfArrayEnd()) {
                    throw new JSONException(jsonReader.info("not support input"));
                }
            }
            return this.of.apply(left, right);
        }
        
        static {
            LEFT = Fnv.hashCode64("left");
            RIGHT = Fnv.hashCode64("right");
        }
    }
    
    public static class PairWriter implements ObjectWriter
    {
        final Class objectClass;
        final String typeName;
        final long typeNameHash;
        Function left;
        Function right;
        byte[] typeNameJSONB;
        static final byte[] leftName;
        static final byte[] rightName;
        
        public PairWriter(final Class objectClass) {
            this.objectClass = objectClass;
            this.typeName = objectClass.getName();
            this.typeNameHash = Fnv.hashCode64(this.typeName);
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            if ((jsonWriter.getFeatures(features) & JSONWriter.Feature.WriteClassName.mask) != 0x0L) {
                if (this.typeNameJSONB == null) {
                    this.typeNameJSONB = JSONB.toBytes(this.typeName);
                }
                jsonWriter.writeTypeName(this.typeNameJSONB, this.typeNameHash);
            }
            jsonWriter.startObject();
            final Object left = this.getLeft(object);
            final Object right = this.getRight(object);
            jsonWriter.writeNameRaw(PairWriter.leftName, PairReader.LEFT);
            jsonWriter.writeAny(left);
            jsonWriter.writeNameRaw(PairWriter.rightName, PairReader.RIGHT);
            jsonWriter.writeAny(right);
            jsonWriter.endObject();
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            final Object left = this.getLeft(object);
            final Object right = this.getRight(object);
            jsonWriter.startObject();
            if ((jsonWriter.getFeatures(features) & JSONWriter.Feature.WritePairAsJavaBean.mask) != 0x0L) {
                jsonWriter.writeName("left");
                jsonWriter.writeColon();
                jsonWriter.writeAny(left);
                jsonWriter.writeName("right");
            }
            else {
                jsonWriter.writeNameAny(left);
            }
            jsonWriter.writeColon();
            jsonWriter.writeAny(right);
            jsonWriter.endObject();
        }
        
        Object getLeft(final Object object) {
            final Class<?> objectClass = object.getClass();
            if (this.left == null) {
                try {
                    this.left = LambdaMiscCodec.createFunction(objectClass.getMethod("getLeft", (Class<?>[])new Class[0]));
                }
                catch (NoSuchMethodException e) {
                    throw new JSONException("getLeft method not found", e);
                }
            }
            return this.left.apply(object);
        }
        
        Object getRight(final Object object) {
            final Class<?> objectClass = object.getClass();
            if (this.right == null) {
                try {
                    this.right = LambdaMiscCodec.createFunction(objectClass.getMethod("getRight", (Class<?>[])new Class[0]));
                }
                catch (NoSuchMethodException e) {
                    throw new JSONException("getRight method not found", e);
                }
            }
            return this.right.apply(object);
        }
        
        static {
            leftName = JSONB.toBytes("left");
            rightName = JSONB.toBytes("right");
        }
    }
}
