package com.nanrailgun.springbootstartersnowflake.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicStampedReference;

public class SnowFlower {
    // 最大序列，12位
    private static final int MAX_SEQUENCE = (1 << 12) - 1;
    // 起始时间, 2016-01-01
    private static final long START_TIME = 1451577600000L;
    //最大时间戳， 41位
    private static final long MAX_TIME = (1L << 41) - 1;


    // 带时间的序列，原子操作
    private AtomicStampedReference<StampedSequence> current;
    // 数据中心id
    private long datacenterId;
    // 工作id
    private long workerId;


    //提前计算好的机器id
    private long machineId;

    public SnowFlower(int datacenterId, int workerId) {
        StampedSequence dt = new StampedSequence(START_TIME, 0);
        current = new AtomicStampedReference<>(dt, 0);
        int maxId = (1 << 5) - 1;
        if (datacenterId < 0 || datacenterId > maxId) {
            throw new IllegalArgumentException(
                    String.format("datacenterId can't be greater than %d or less than 0", maxId));
        }
        if (workerId < 0 || workerId > maxId) {
            throw new IllegalArgumentException(
                    String.format("workerId can't be greater than %d or less than 0", maxId));
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;

        //初始化机器id
        machineId = ((this.datacenterId << 5) | this.workerId) << 12;
    }

    public long nextId() {
        StampedSequence seq = nextSequence();
        long time = seq.timestamp - START_TIME;
        if (time > MAX_TIME) {
            throw new RuntimeException("Timestamp overflow! " + new Date(seq.timestamp));
        }
        long value = (time << 22) | machineId | seq.sequence;
        return value;
    }

    private StampedSequence nextSequence() {
        //下一个时间戳序列
        StampedSequence nextSequence = new StampedSequence(0, 0);

        //版本
        int[] versionHolder = new int[1];
        while (true) {
            long now = System.currentTimeMillis();
            //拿到当前引用和版本号
            StampedSequence curSequence = current.get(versionHolder);
            if (now < curSequence.timestamp) {
                throw new RuntimeException("Clock moved backwards!");
            } else if (curSequence.timestamp == now) {
                if (curSequence.sequence >= MAX_SEQUENCE) {
                    //满序列等待下一毫秒
                    continue;
                }
                nextSequence.timestamp = curSequence.timestamp;
                nextSequence.sequence = curSequence.sequence + 1;
                boolean set = current.compareAndSet(curSequence, nextSequence, versionHolder[0], versionHolder[0] + 1);
                if (!set) {
                    // 无锁更新失败，重新获取
                    continue;
                }
                break;
            } else {
                nextSequence.timestamp = now;
                nextSequence.sequence = 0;
                boolean set = current.compareAndSet(curSequence, nextSequence, versionHolder[0], versionHolder[0] + 1);
                if (!set) {
                    // 无锁更新失败，重新获取
                    continue;
                }
                break;
            }
        }
        return nextSequence;
    }

    /**
     * 带时间戳的序列
     */
    static class StampedSequence {
        private long timestamp; // 时间戳
        private long sequence; // 序列

        public StampedSequence(long stamp, long sequence) {
            this.timestamp = stamp;
            this.sequence = sequence;
        }
    }

    /* 测试 */
    public static void main(String[] args) throws InterruptedException {
        SnowFlower snow = new SnowFlower(1, 1);
        snow.nextId();
        CopyOnWriteArrayList<Long> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> list.add(snow.nextId())).start();
        }
        Thread.sleep(1000L);
        Set<Long> set = new HashSet<>(list);
        System.out.println(set.size() == list.size());
    }
}
