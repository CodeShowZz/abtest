package com.abtest.util;

import com.abtest.model.Group;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

/**
 * @author junlin_huang
 * @create 2021-06-09 1:13 AM
 **/

public class KryoUtil {

    public static <T> byte[] serialize(T obj) {
        Kryo kryo = new Kryo();
        kryo.register(Group.class);
        kryo.register(BigDecimal.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes) {
        Kryo kryo = new Kryo();
        kryo.register(Group.class);
        kryo.register(BigDecimal.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }


}