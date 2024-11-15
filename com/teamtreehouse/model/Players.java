package com.teamtreehouse.model;

import java.util.Arrays;
import java.util.Comparator;

public class Players {
  private static Player[] players = {
          new Player("Chloe", "Alaska", 47, false),
          new Player("James", "Anderson", 42, false),
          new Player("Lucas", "Brown", 44, false),
          new Player("Andrew", "Chalklerz", 42, true),
          new Player("David", "Chen", 41, false),
          new Player("Les", "Clay", 42, true),
          new Player("Sal", "Dali", 41, false),
          new Player("Jim", "Davis", 45, true),
          new Player("Sarah", "Divis", 40, true),
          new Player("Ben", "Finkelstein", 44, false),
          new Player("Sofia", "Garcia", 37, true),
          new Player("Eva", "Gordon", 45, false),
          new Player("Phil", "Helm", 44, true),
          new Player("Joe", "Kavalier", 39, false),
          new Player("Rachel", "Kim", 43, true),
          new Player("Herschel", "Krustofski", 45, true),
          new Player("Mina", "Kubota", 41, true),
          new Player("Kenny", "Lovins", 35, true),
          new Player("Lisa", "Martinez", 39, true),
          new Player("Pasan", "Membrane", 39, false),
          new Player("Miguel", "Rocha", 44, true),
          new Player("Karl", "Saygan", 42, true),
          new Player("Tom", "Schultz", 42, true),
          new Player("Henry", "Shevlin", 37, false),
          new Player("Joe", "Smith", 42, true),
          new Player("Jill", "Tanner", 36, true),
          new Player("Emma", "Taylor", 38, true),
          new Player("Oliver", "Wilson", 41, true),
          new Player("Michael", "Wong", 40, false),
          new Player("Sam", "Zima", 38, false)
  };

  public static Player[] load() {
    Arrays.sort(players, new Comparator<Player>() {
      @Override
      public int compare(Player p1, Player p2) {
        int lastNameCompare = p1.getLastName().compareTo(p2.getLastName());
        if (lastNameCompare != 0) {
          return lastNameCompare;
        }
        return p1.getFirstName().compareTo(p2.getFirstName());
      }
    });
    return players;
  }
}