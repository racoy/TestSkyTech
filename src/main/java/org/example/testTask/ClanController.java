package org.example.testTask;

public class ClanController {
    public static final ClanController INSTANCE = new ClanController(); //подумалось что логично если это будет Singleton(зависит от архитектуры),лучше использовать enum, но по заданию обычный класс

    private ClanController(){}

    public void incGold(long clanId, int gold) {
        long userId = 1;   //скорее всего тут будет что-то вроде sessionManager.getCurrentSession().getUser().getUserId()
        Clan clan = ClanManager.getClan(clanId);
        if (clan == null) {
            System.out.println(clanId);
        }
        clan.incGold(gold);
        TrackerManager.trackerClanGold(clanId, userId, gold);
    }
}
