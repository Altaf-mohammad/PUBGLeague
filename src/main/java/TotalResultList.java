package com.league.pubgleague;

public class TotalResultList {

    public String name;
    public Long amount,kills,position;

    public TotalResultList(){}

    public TotalResultList(Long amount, Long kills, String  name, Long position){
        this.amount = amount;
        this.kills = kills;
        this.name = name;
        this.position = position;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getKills() {
        return kills;
    }

    public void setKills(Long kills) {
        this.kills = kills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

}
