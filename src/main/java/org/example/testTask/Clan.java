package org.example.testTask;

public class Clan {
    private final long id;
    private String name;
    private volatile int gold;

    public long getId() {
        return id;
    }

    public Clan(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public synchronized void incGold(int gold) {
        this.gold += gold;
    }

    @Override
    public String toString() {
        return "Clan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gold=" + gold +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        return ((Clan) obj).id == this.id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }
}
