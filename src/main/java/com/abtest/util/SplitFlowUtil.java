package com.abtest.util;

import com.abtest.model.Group;
import com.abtest.model.Lab;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author junlin_huang
 * @create 2021-06-08 11:05 PM
 **/

public class SplitFlowUtil {

    private static final BigDecimal range = BigDecimal.valueOf(1000);

    public static void assignRangeByRatio(Lab lab) {
        List<Group> groupList = lab.getGroupList();
        int current = 0;
        for (Group group : groupList) {
            BigDecimal ratio = group.getRatio();
            int count = ratio.multiply(range).setScale(0, RoundingMode.HALF_UP).intValue();
            int start = current;
            int end = current + count - 1;
            group.setStart(start);
            group.setEnd(end);
            current = end + 1;
        }
    }

    public static Group partition(String key, Lab lab) {
        int hashCode = hashCode(key, lab.getName());
        int position = hashCode % range.intValue();
        List<Group> groupList = lab.getGroupList();
        for (Group group : groupList) {
            if (group.getStart() <= position && group.getEnd() >= position) {
                return group;
            }
        }
        return null;
    }

    public static final int hashCode(String key, String value) {
        return Math.abs(Objects.hashCode(key) ^ Objects.hashCode(value));
    }

    public static void main(String[] args) {
        Lab subject = new Lab();
        Group math = new Group();
        math.setRatio(new BigDecimal(0.5));
        math.setKey("math");
        math.setName("数学");

        Group chinese = new Group();
        chinese.setRatio(new BigDecimal(0.3));
        chinese.setKey("chinese");
        chinese.setName("语文");

        Group english = new Group();
        english.setRatio(new BigDecimal(0.2));
        english.setKey("english");
        english.setName("英语");

        List<Group> groupList = Arrays.asList(math, chinese, english);
        subject.setGroupList(groupList);
        subject.setKey("subject");
        subject.setName("学科");

        SplitFlowUtil.assignRangeByRatio(subject);

        Group res = partition("the boy maybe like math", subject);
        System.out.println(res);

        res = partition("i am a programmer", subject);
        System.out.println(res);
    }
}