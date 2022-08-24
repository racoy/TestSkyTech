package org.example.testTask;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ClanManager {
    private static final ConcurrentHashMap<Long, Clan> clanMap = new ConcurrentHashMap<>();
    private static final ClanRepository clanRepository = ClanRepository.INSTANCE;

    /**
     * @param clanId - clan id
     * @return Clan object or null if clan with same id not found
     */
    public static Clan getClan(long clanId) {
        if (clanMap.containsKey(clanId)) {
            return clanMap.get(clanId);
        } else {
            synchronized (clanMap) {
                if (clanMap.containsKey(clanId)) {
                    return clanMap.get(clanId);
                }
                Clan clan = clanRepository.getClanFromDb(clanId);
                if (clan != null) {
                    clanMap.put(clan.getId(), clan);
                }
                return clan;
            }
        }
    }

    public static Collection<Clan> getInMemoryClans() {
        return clanMap.values();
    }

    /**
     * @param clanId - clan id existing in the database
     * @return - true if save successful or false if clan not found/updating was not successful
     */
    public static boolean saveClan(long clanId) {
        Clan clan = getClan(clanId);
        if (clan == null) return false;  //вообще здесь можно кидать исключение, но это зависит от устройства проекта
        return clanRepository.saveClanToDb(clan);
    }
}
