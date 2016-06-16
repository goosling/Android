package com.example.joe.mashangpinche.converters;

import android.util.Base64;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * Created by JOE on 2016/6/15.
 */
public class ByteArrayConverter implements Converter<ByteArrayWrapper> {

    public ByteArrayWrapper read(InputNode node) throws Exception {
        InputNode nextNode = node.getNext();
        return new ByteArrayWrapper(Base64.decode(nextNode.getValue(), Base64.DEFAULT));
    }

    public void write(OutputNode node, ByteArrayWrapper wrapper) throws Exception{
        OutputNode byteArrayNode = node.getChild("byteArray");
        byteArrayNode.setValue(Base64.encodeToString(wrapper.getByteArray(), Base64.DEFAULT));
    }
}
