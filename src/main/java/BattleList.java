package com.league.pubgleague;


public class BattleList extends BattleListId {

    public String name,time,type,map,limit,fee,kills,w1,w2,w3;

    public BattleList(){}

    public BattleList(String  name, String time, String type,
                      String map, String limit, String fee,
                      String kills, String w1, String w2, String w3){

        this.name = name;
        this.time = time;
        this.type = type;
        this.map = map;
        this.limit = limit;
        this.fee = fee;
        this.kills = kills;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getKills() {
        return kills;
    }

    public void setKills(String kills) {
        this.kills = kills;
    }

    public String getW1() {
        return w1;
    }

    public void setW1(String w1) {
        this.w1 = w1;
    }

    public String getW2() {
        return w2;
    }

    public void setW2(String w2) {
        this.w2 = w2;
    }

    public String getW3() {
        return w3;
    }

    public void setW3(String w3) {
        this.w3 = w3;
    }
}
