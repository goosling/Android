package com.example.joe.alltest;

import android.os.Environment;
import android.util.Xml;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Exception;

/**
 * Created by JOE on 2016/3/2.
 */
public class SerializerXML {

    //使用拼接字符串的方式序列化成xml文件
    private void backup2Contact() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        builder.append("<contacts>");

        //遍历联系人信息
        for (Contact contact: contacts) {
            if(contact != null) {
                builder.append("<contact id='"+contact.getId()+"'>");
                builder.append("<name>");
                builder.append(contact.getName());
                builder.append("</name>");

                builder.append("<number>");
                builder.append(contact.getNumber());
                builder.append("</number>");

                builder.append("</contact>")
            }
        }
        builder.append("</contacts>");
        try{
            //在sd卡上创建一个xml文件
            File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(builder.toString().getBytes());
            fos.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    //xmlSerializer解析为xml文件
    private void backup2Contact2() {
        try{
            File file = new File(Environment.getExternalStorageDirectory(), "backup2.xml");
            FileOutputStream fos = new FileOutputStream(file);
            //获取XMLSerializer对象
            XMLSerializer serializer = Xml.newSerializer();
            //设置xml的输出流以及编码格式
            serializer.setOutput(fos, "utf-8");
            //设置文档的开头以及编码格式
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "contacts");
            for(Contact contact: contacts) {
                serializer.startTag(null, "contact");
                // 设置contact标签的id属性
                serializer.attribute(null, "id", contact.getId()+"");
                serializer.startTag(null, "name");
                serializer.text(contact.getName());
                serializer.endTag(null, "name");
                serializer.startTag(null, "number");
                serializer.text(contact.getNumber());
                serializer.endTag(null, "number");
                serializer.startTag(null, "address");
                serializer.text(contact.getAddress());
                serializer.endTag(null, "address");
                serializer.endTag(null, "contact");
            }
            serializer.endTag(null, "contacts");
            serializer.endDocument();
            fos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
