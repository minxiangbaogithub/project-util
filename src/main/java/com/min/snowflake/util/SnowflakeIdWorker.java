package com.min.snowflake.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;

public class SnowflakeIdWorker {
	private static final long TWEPOCH = 1288834974657L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = 31L;
    private static final long MAX_DATA_CENTER_ID = 31L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_SHIFT = 12L;
    private static final long DATA_CENTER_ID_SHIFT = 17L;
    private static final long TIMESTAMP_LEFT_SHIFT = 22L;
    private static final long SEQUENCE_MASK = 4095L;
    private static  SnowflakeIdWorker snowflake = null;
    private static Object lock = new Object();
    private final long workerId;
    private final long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private  SnowflakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > 31L || workerId < 0L) {
            workerId = getRandom();
        }

        if (dataCenterId <= 31L && dataCenterId >= 0L) {
            this.workerId = workerId;
            this.dataCenterId = dataCenterId;
        } else {
            throw new IllegalArgumentException(String.format("%s 数据中心ID最大值 必须是 %d 到 %d 之间", dataCenterId, 0, 31L));
        }
    }

    public static SnowflakeIdWorker getInstanceSnowflake() {
        if (snowflake == null) {
            synchronized(lock) {
                long dataCenterId = getRandom();

                long workerId;
                try {
                    workerId = getWorkerId();
                } catch (Exception var7) {
                    workerId = getRandom();
                }
                if(snowflake==null) {
                	snowflake = new SnowflakeIdWorker(workerId, dataCenterId);
                }
            }
        }

        return snowflake;
    }

    private static long getRandom() {
        int max = 31;
        int min = 1;
        Random random = new Random();
        long result = (long)(random.nextInt(max - min) + min);
        return result;
    }

    private static long getWorkerId() throws SocketException, UnknownHostException, NullPointerException {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = null;
        Enumeration en = NetworkInterface.getNetworkInterfaces();

        while(en.hasMoreElements()) {
            NetworkInterface nint = (NetworkInterface)en.nextElement();
            if (!nint.isLoopback() && nint.getHardwareAddress() != null) {
                network = nint;
                break;
            }
        }

        byte[] mac = network.getHardwareAddress();
        Random rnd = new Random();
        byte rndByte = (byte)(rnd.nextInt() & 255);
        return (255L & (long)mac[mac.length - 1] | 65280L & (long)rndByte << 8) >> 6;
    }

    public synchronized long nextId() throws Exception {
        long timestamp = this.time();
        if (timestamp < this.lastTimestamp) {
            throw new Exception("时钟向后移动，拒绝生成id  " + (this.lastTimestamp - timestamp) + " milliseconds");
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            long nextId = timestamp - 1288834974657L << 22 | this.dataCenterId << 17 | this.workerId << 12 | this.sequence;
            return nextId;
        }  
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.time(); timestamp <= lastTimestamp; timestamp = this.time()) {
        }

        return timestamp;
    }

    private long time() {
        return System.currentTimeMillis();
    }
}
