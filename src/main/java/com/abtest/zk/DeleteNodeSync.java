package com.abtest.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// ZooKeeper API 删除节点，使用同步(sync)接口。
public class DeleteNodeSync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;


    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        deleteNodeSync("huangjunlin","subject");
    }

    public static void deleteNodeSync(String projectKey, String labKey) throws IOException, KeeperException, InterruptedException {
        String path = "/" + projectKey + "/" + labKey;
        zk = new ZooKeeper("localhost:2181",
                5000, //
                new DeleteNodeSync());
        connectedSemaphore.await();
        List<String> childrenList = zk.getChildren(path, true);
        for (String children : childrenList) {
            zk.delete(path + "/" + children, -1);
        }
        zk.delete(path,-1);
        zk.delete("/" + projectKey,-1);
    }


    public void process(WatchedEvent event) {
        if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}