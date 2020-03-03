package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Main {
    private static final int ARRAY_LIST_SIZE = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        long startTime = (new Date()).getTime();
        System.out.println("Start process ID=" + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Total memory before: " + runtime.totalMemory());
        System.out.println("Free memory before: " + runtime.freeMemory());
        List<Object> objectsList  = new ArrayList<>();
        try {
            while (true) {
                objectsList.addAll(Collections.nCopies(ARRAY_LIST_SIZE, new Object()));
                objectsList.subList(0, ARRAY_LIST_SIZE >> 2).clear();
                Thread.sleep(100);
            }
        } catch (OutOfMemoryError E) {
            long sec = ((new Date()).getTime() - startTime) / 1000;
            System.out.println("Продолжительность работы приложения - "+sec+" sec.");
            System.exit(0);
        }

    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
