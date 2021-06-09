package com.abtest.zk;

import com.abtest.model.Group;
import com.abtest.util.KryoUtil;
import com.esotericsoftware.kryo.Kryo;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author junlin_huang
 * @create 2021-06-04 3:44 PM
 **/

public class GetChildrenSync implements Watcher {

    //注意这里如果不加static的话 用的就不是同一个CountDownLatch对象 程序结束不了  具体原理还要再研究
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zooKeeper = null;

    private static Stat stat = new Stat();

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        getChildren("huangjunlin", "subject");
    }


    public static List<Group> getChildren(String projectKey, String labKey) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("localhost:2181", 5000, new GetChildrenSync());
        countDownLatch.await();
        System.out.println("zk session established");

        String path = "/" + projectKey + "/" + labKey;

        List<String> childrenList = zooKeeper.getChildren(path, true);
        List<Group> groupList = new ArrayList<>();
        for (String children : childrenList) {
            Group group = KryoUtil.deserialize(zooKeeper.getData(path + "/" + children, true, stat));
            groupList.add(group);
        }
        return groupList;
    }

    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                countDownLatch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println("Reget children:" + zooKeeper.getChildren(watchedEvent.getPath(), true));
                } catch (Exception e) {

                }
            }
        }
    }
}