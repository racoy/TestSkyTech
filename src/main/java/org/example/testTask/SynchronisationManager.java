package org.example.testTask;

public class SynchronisationManager {
    private static final int SYNCHRONISATION_TIMEOUT = 1000;
    private static final ClanRepository clanRepository = ClanRepository.INSTANCE;
    private static final Thread updatingThread;

    static {
        updatingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!updatingThread.isInterrupted()) {
                    try {
                        Thread.sleep(SYNCHRONISATION_TIMEOUT);
                    } catch (InterruptedException e) {
                        break;
                    } finally {
                        synchroniseInMemoryWithDb();
                    }
                }
            }
        });
    }

    public static Thread getUpdatingThread() {
        return updatingThread;
    }

    private static void synchroniseInMemoryWithDb() {
        updatingClans();
    }

    private static void updatingClans() {
        clanRepository.updateClansInDb(ClanManager.getInMemoryClans());
    }
}
