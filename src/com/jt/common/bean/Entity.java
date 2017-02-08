package com.jt.common.bean;

import java.util.concurrent.ConcurrentHashMap;

public class Entity
{
  private long date;
  private ConcurrentHashMap<String, Integer> map;

  public Entity()
  {
    this.map = new ConcurrentHashMap();
  }
  public long getDate() {
    return this.date;
  }
  public void setDate(long date) {
    this.date = date;
  }
  public ConcurrentHashMap<String, Integer> getMap() {
    return this.map;
  }
  public void setMap(ConcurrentHashMap<String, Integer> map) {
    this.map = map;
  }

  public static void main(String[] args) {
    Entity db = new Entity();
    ConcurrentHashMap map = new ConcurrentHashMap();
    db.setMap(map);
    for (int i = 0; i < 10000; i++)
      map.put("ip" + i, "100");
    try
    {
      Thread.sleep(10000L);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}