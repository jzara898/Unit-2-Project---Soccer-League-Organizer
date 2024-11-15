package com.teamtreehouse.model;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Team implements Comparable<Team> {
    private String name;
    private String coachName;
    private Set<Player> players;
    private static final int MAX_PLAYERS = 11;

    public Team(String name, String coachName) {
        this.name = name;
        this.coachName = coachName;
        this.players = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getCoachName() {
        return coachName;
    }

    public Set<Player> getPlayers() {
        return new TreeSet<>(players);
    }

    public boolean addPlayer(Player player) {
        if (players.size() >= MAX_PLAYERS) {
            return false;
        }
        return players.add(player);
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public int getExperiencedPlayerCount() {
        return (int) players.stream().filter(Player::isPreviousExperience).count();
    }

    public double getExperiencedPlayerPercentage() {
        if (players.isEmpty()) return 0.0;
        return (getExperiencedPlayerCount() * 100.0) / players.size();
    }

    public int getInexperiencedPlayerCount() {
        return players.size() - getExperiencedPlayerCount();
    }

    @Override
    public int compareTo(Team other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " (Coach: " + coachName + ")";
    }
}