package org.example;

import org.example.testTask.ClanController;
import org.example.testTask.ClanManager;
import org.example.testTask.SynchronisationManager;
import org.example.testTask.Clan;

import java.time.LocalDateTime;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        ClanController controller = ClanController.INSTANCE;

        SynchronisationManager.getUpdatingThread().start();
        System.out.println(ClanManager.getClan(10));

        for (int i = 0; i < 10000; i++) {
            Thread thread = getThread(controller);
            thread.start();
        }

        System.out.println(LocalDateTime.now());
        Thread.sleep(10000);
        System.out.println(LocalDateTime.now());
        SynchronisationManager.getUpdatingThread().interrupt();
        System.out.println(LocalDateTime.now());
        System.out.println(ClanManager.getClan(10));
        long sum = 0;
        for (Clan clan : ClanManager.getInMemoryClans()) {
            sum += clan.getGold();
            System.out.println(clan);
        }
        System.out.println(sum);
    }

    static Thread getThread(ClanController controller) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Math.round(Math.random() * 100));
                    controller.incGold(10 + Math.round(Math.random() * 9), 10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
