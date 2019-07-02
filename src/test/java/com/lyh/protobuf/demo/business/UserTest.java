package com.lyh.protobuf.demo.business;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wtcar.mqtt.json.common.enums.ObjectMessageType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class UserTest {

    @Test
    public void user() throws InvalidProtocolBufferException {
        long now = System.currentTimeMillis();
        Simple.User user = Simple.User.newBuilder()
                .setAge(100)
                .setName("lyh")
                .setTs(now)
                .build();
        byte[] bytes = user.toByteArray();
        System.out.println(bytes);
        System.out.println(user+":"+bytes.length);
        Simple.User user2 = Simple.User.newBuilder()
                .mergeFrom(user)
                .build();

        System.out.println(user2+":"+bytes.length);
        /*--------------------------------------------------------------*/
        User normalUser = new User();
        normalUser.setAge(100);
        normalUser.setName("lyh");
        normalUser.setTs(now);
        Gson gson = new Gson();
        String str = gson.toJson(normalUser);
        System.out.println(normalUser+":"+str.getBytes().length);
    }
    @Test
    public void testAddress() throws InvalidProtocolBufferException {
        Address.Person person = Address.Person.newBuilder()
                .setId(11)
                .setName("lyh")
                .addPhones(Address.Person.PhoneNumber.newBuilder()
                        .setType(Address.Person.PhoneType.HOME)
                        .setNumber("1326017xxxx")
                        .build())
                .addPhones(Address.Person.PhoneNumber.newBuilder()
                        .setType(Address.Person.PhoneType.MOBILE)
                        .setNumber("477150")
                        .build())
                .setEmail("aaa@163.com")
                .build();
        System.out.println("序列化前：");
        System.out.println(person);
        //序列化
        byte[] personBytes = person.toByteArray();
        //反序列化
        Address.Person personAfterSer = Address.Person.parseFrom(personBytes);
        System.out.println("反序列化：");
        System.out.println(personAfterSer);

    }
    @Test
    public void testBrakingInfo() throws InvalidProtocolBufferException {
        Common.ObjectMessageType objectMessageType = Common.ObjectMessageType.REPORT_BRAKING_INFO;
        List<BrakingInfoReportProto.BrakingInfoReport.BrakingInfo> brakingInfoList = new ArrayList<>();
        brakingInfoList.add(BrakingInfoReportProto.BrakingInfoReport.BrakingInfo.newBuilder()
                .setBol(0.77F)
                .setRts(System.currentTimeMillis())
                .build());
        brakingInfoList.add(BrakingInfoReportProto.BrakingInfoReport.BrakingInfo.newBuilder()
                .setBol(0.88F)
                .setRts(System.currentTimeMillis())
                .build());
        BrakingInfoReportProto.BrakingInfoReport report = BrakingInfoReportProto.BrakingInfoReport
                .newBuilder()
                .setJsonMessageType(Common.JsonMessageType.REPORT)
                .setObjectMessageType(objectMessageType)
                .setEt(ObjectMessageType.getObjectMessageTypeByIndex(objectMessageType.getNumber())==null?""
                        :ObjectMessageType.getObjectMessageTypeByIndex(objectMessageType.getNumber()).getValue())
                .addData(BrakingInfoReportProto.BrakingInfoReport.BrakingInfo.newBuilder()
                        .setBol(0.55F)
                        .setRts(System.currentTimeMillis())
                        .build())
                .addData(BrakingInfoReportProto.BrakingInfoReport.BrakingInfo.newBuilder()
                        .setBol(0.555F)
                        .setRts(System.currentTimeMillis())
                        .build())
                .addData(BrakingInfoReportProto.BrakingInfoReport.BrakingInfo.newBuilder()
                        .setBol(0.66F)
                        .setRts(System.currentTimeMillis())
                        .build())
                //还可以用这种方式加list
                .addAllData(brakingInfoList)
                .build();
        Gson gson = new Gson();
        System.out.println("序列化前："+gson.toJson(report)+"-》gson长度："+gson.toJson(report).length());
        //序列化对象，序列化之后的数组就是交互的数据报文
        byte[] reportBytes = report.toByteArray();
        System.out.println("protobuf序列化：:protobuf长度"+reportBytes.length);
        //反序列化
        BrakingInfoReportProto.BrakingInfoReport brakingInfoReport = BrakingInfoReportProto.BrakingInfoReport.parseFrom(reportBytes);
        System.out.println("反序列化："+brakingInfoReport);
    }
}
