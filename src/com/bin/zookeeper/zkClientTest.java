package com.bin.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class zkClientTest {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZookeeperConnection connection = new ZookeeperConnection();
        ZooKeeper zooKeeper = connection.connect("localhost");//可以设置多个IP,用','分割


        for (int i = 0; i < 1000; i++) {
            connection.connect("localhost");
        }

        System.out.println("zookeeper 连接完成！");

        Stat stat = zooKeeper.exists("/temp", (watchedEvent) -> {
            System.out.println(watchedEvent.toString());
        });

        if (stat != null) {
            System.out.println("节点不为空,开始删除节点");
            String path = "/temp";
            deleteNodes(zooKeeper, path);
        }

        //新建节点
        String result = zooKeeper.create("/temp", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("zookeeper connect return:" + result);
        result = zooKeeper.create("/temp/test", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("return:" + result);
        result = zooKeeper.create("/temp/test/node", "http://www.baidu.com".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("re:" + result);

        //保证读取的数据最终一致，调用同步方法
        zooKeeper.sync("/temp/test/node", new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int i, String s, Object o) {
            }
        }, null);


        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("1" + watchedEvent.toString());
            }
        };
        //监听节点
        zooKeeper.getData("/temp/test/node", watcher, null, null);

        zooKeeper.getData("/temp/test/node", watcher, null, null);
        //listenNode(zooKeeper, "/temp/test/node");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println("END:" + input);
    }

    /**
     * 循环监听节点-待完善
     *
     * @param zooKeeper
     * @param path
     * @throws InterruptedException
     * @throws KeeperException
     */
    public static void listenNode(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {

        zooKeeper.getData(path, (watchedEvent) -> {
            if (Watcher.Event.EventType.NodeDataChanged.getIntValue() == watchedEvent.getType().getIntValue()) {
                try {
                    zooKeeper.getData(path, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }


    /**
     * 循环删除节点
     *
     * @param zooKeeper
     * @param path
     * @throws InterruptedException
     * @throws KeeperException
     */
    public static void deleteNodes(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren(path, false);
        for (String child : children) {
            String newPath = path + "/" + child;
            System.out.println("节点：" + newPath);
            deleteNodes(zooKeeper, newPath);
        }
        if (path != null) {
            zooKeeper.delete(path, -1);
            System.out.println("delete node :" + path);
        }
    }


}

class ZookeeperConnection {

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private ZooKeeper zoo;

    protected ZooKeeper connect(String host) throws IOException, InterruptedException {
        zoo = new ZooKeeper(host, 10000, (watchedEvent) -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        return zoo;
    }

    protected void close() throws InterruptedException {
        zoo.close();
    }
}
