package cn.zandy.je.jdk.collection;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Java 实现一致性哈希算法.
 *
 * @author zandy
 * @since 1.8
 */
public class ConsistentHashExample {

    /** 所有真实的服务节点 */
    private static final String[] SERVERS = {"127.0.0.1:6377", "127.0.0.1:6378", "127.0.0.1:6379"};

    /** 为了使服务节点均匀分布，增加一些虚拟节点，此常量表示每个真实服务节点对应的虚拟节点个数 */
    private static final int VIRTUAL_NODE_NUM = 3;

    /** 存放所有节点（包含虚拟节点）的映射表：key为虚拟节点的哈希值，value为真实服务节点 */
    private static SortedMap<Long, String> virtualNodeMap = new TreeMap<>();

    public ConsistentHashExample() {
        for (String server : SERVERS) {
            addVirtualNodes(server);
        }
    }

    private void addVirtualNodes(String server) {
        for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
            long hashCode = hash(server + ":" + i);
            virtualNodeMap.put(hashCode, server);
        }
    }

    /**
     * hash 函数可以使用 md5, sha-1, sha-256 等
     * 虽然 md5, sha-1 哈希算法在签名领域已经不再安全，但运算速度比较快，在非安全领域是可以使用的.
     */
    private long hash(String key) {
        String md5key = org.apache.commons.codec.digest.DigestUtils.md5Hex(key);
        return Long.parseLong(md5key.substring(0, 15), 16) % ((long) Math.pow(2, 32));
    }

    /**
     * 按照同一个方向寻找.
     */
    private String getServer(String key) {
        SortedMap<Long, String> tailMap = virtualNodeMap.tailMap(hash(key));
        long serverKey = tailMap.isEmpty() ? virtualNodeMap.firstKey() : tailMap.firstKey();

        return virtualNodeMap.get(serverKey);
    }

    public static void main(String[] args) {
        System.out.println(new ConsistentHashExample().getServer("locking:8260:2"));
    }
}
