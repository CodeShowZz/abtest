package com.abtest.zk;

import com.abtest.model.Group;
import com.abtest.model.Lab;
import com.abtest.util.KryoUtil;
import com.abtest.util.SplitFlowUtil;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author junlin_huang
 * @create 2021-06-04 3:44 PM
 **/

public class CreateNodeSync implements Watcher {

    //注意这里如果不加static的话 用的就不是同一个CountDownLatch对象 程序结束不了  具体原理还要再研究
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void createNode(Lab lab) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new CreateNodeSync());
        countDownLatch.await();
        System.out.println("zk session established");
        String projectKey = lab.getProjectKey();
        zooKeeper.create("/" + projectKey,
                "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.create("/" + projectKey + "/" + lab.getKey(),
                "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        List<Group> groupList = lab.getGroupList();
        for (Group group : groupList) {
            byte[] bytes = KryoUtil.serialize(group);
            String path = zooKeeper.create("/" + projectKey + "/" + lab.getKey() + "/" + group.getKey(),
                    bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("Success create znode:" + path);
        }
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
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

        Group res = SplitFlowUtil.partition("the boy maybe like math", subject);
        System.out.println(res);

        res = SplitFlowUtil.partition("i am a programmer", subject);
        System.out.println(res);

        CreateNodeSync createNodeSync = new CreateNodeSync();
        createNodeSync.createNode(subject);
    }
}