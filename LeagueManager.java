import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import java.util.*;

//code review:
//most project functionality is in league manager
//add more here:

public class LeagueManager {
  private Set<Team> teams;
  private Queue<Player> waitingList;
  private static final int HEIGHT_RANGES[][] = {{35, 40}, {41, 46}, {47, 50}};

  public LeagueManager() {
    teams = new TreeSet<>();
    waitingList = new LinkedList<>();
  }

  public static void main(String[] args) {
    LeagueManager manager = new LeagueManager();
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("\n=== Soccer League Manager ===");
      System.out.println("What would you like to do?");
      System.out.println("1. Create a new team");
      System.out.println("2. Add a player to a team");
      System.out.println("3. Remove a player from a team");
      System.out.println("4. View a team height report");
      System.out.println("5. View a league balance report");
      System.out.println("6. Display a team roster");
      System.out.println("7. Add a player to the wait list");
      System.out.println("8. Auto-generate balanced teams");
      System.out.println("9. Exit");
      System.out.print("\nPlease enter your choice (1-9): ");

      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }

      int choice;
      try {
        choice = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Please try your selection again.");
        continue;
      }

      switch (choice) {
        case 1:
          manager.createTeam(scanner);
          break;
        case 2:
          manager.addPlayerToTeam(scanner);
          break;
        case 3:
          manager.removePlayerFromTeam(scanner);
          break;
        case 4:
          manager.displayHeightReport(scanner);
          break;
        case 5:
          manager.displayLeagueBalanceReport();
          break;
        case 6:
          manager.printTeamRoster(scanner);
          break;
        case 7:
          manager.addPlayerToWaitingList(scanner);
          break;
        case 8:
          manager.autoGenerateTeams(scanner);
          break;
        case 9:
          System.out.println("Signing Off . . .");
          return;
        default:
          System.out.println("Please try your selection again.");
      }
    }
  }

  private void createTeam(Scanner scanner) {
    Player[] allPlayers = Players.load();
    if (teams.size() * 11 >= allPlayers.length) {
      System.out.println("You've reached the max number of teams.");
      return;
    }

    String teamName, coachName;

    while (true) {
      System.out.print("Enter a team name: ");
      teamName = scanner.nextLine().trim();
      if (teamName.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      break;
    }

    while (true) {
      System.out.print("Enter a coach name: ");
      coachName = scanner.nextLine().trim();
      if (coachName.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      break;
    }

    teams.add(new Team(teamName, coachName));
    System.out.println("Team has been created.");
  }

  private void addPlayerToTeam(Scanner scanner) {
    Team team = selectTeam(scanner);
    if (team == null) return;

    if (team.isFull()) {
      System.out.println("This team is already full.");
      return;
    }

    Set<Player> availablePlayers = getAvailablePlayers();
    if (availablePlayers.isEmpty()) {
      System.out.println("Sorry, there are no players available.");
      return;
    }

    displayPlayers(availablePlayers);

    while (true) {
      System.out.print("Enter a player number to add: ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      try {
        int playerNum = Integer.parseInt(input);
        Player selectedPlayer = findPlayerByNumber(availablePlayers, playerNum);
        if (selectedPlayer != null && team.addPlayer(selectedPlayer)) {
          System.out.println("SUCCESS.  Player is added.");
          return;
        } else {
          System.out.println("FAILED. Player was not added.");
        }
        break;
      } catch (NumberFormatException e) {
        System.out.println("Please try your selection again.");
      }
    }
  }

  private Team selectTeam(Scanner scanner) {
    if (teams.isEmpty()) {
      System.out.println("Sorry, there are no teams available.");
      return null;
    }

    System.out.println("\nAvailable Teams:");
    List<Team> teamList = new ArrayList<>(teams);
    for (int i = 0; i < teamList.size(); i++) {
      System.out.println((i + 1) + ". " + teamList.get(i));
    }

    while (true) {
      System.out.print("Select a team number: ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      try {
        int teamNum = Integer.parseInt(input);
        if (teamNum < 1 || teamNum > teamList.size()) {
          System.out.println("Sorry.  Please enter a different team number.");
          continue;
        }
        return teamList.get(teamNum - 1);
      } catch (NumberFormatException e) {
        System.out.println("Please try your selection again.");
      }
    }
  }

  private void removePlayerFromTeam(Scanner scanner) {
    Team team = selectTeam(scanner);
    if (team == null) return;

    Set<Player> teamPlayers = team.getPlayers();
    if (teamPlayers.isEmpty()) {
      System.out.println("There are no players on that team.");
      return;
    }

    displayPlayers(teamPlayers);

    while (true) {
      System.out.print("Enter a player number to remove: ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      try {
        int playerNum = Integer.parseInt(input);
        Player selectedPlayer = findPlayerByNumber(teamPlayers, playerNum);
        if (selectedPlayer != null && team.removePlayer(selectedPlayer)) {
          System.out.println("SUCCESS.  Player is removed.");
          if (!waitingList.isEmpty()) {
            Player nextPlayer = waitingList.poll();
            if (team.addPlayer(nextPlayer)) {
              System.out.println("Added " + nextPlayer.getFirstName() + " from the wait list.");
            }
          }
          return;
        } else {
          System.out.println("FAILED to removed player.");
        }
        break;
      } catch (NumberFormatException e) {
        System.out.println("Please try your selection again.");
      }
    }
  }

  private void displayPlayers(Set<Player> players) {
    int i = 1;
    for (Player player : players) {
      System.out.printf("%d. %s %s (Height: %d\", Experience: %s)%n",
              i++,
              player.getFirstName(),
              player.getLastName(),
              player.getHeightInInches(),
              player.isPreviousExperience() ? "Yes" : "No");
    }
  }

  private void displayHeightReport(Scanner scanner) {
    Team team = selectTeam(scanner);
    if (team == null) return;

    Map<String, List<Player>> heightGroups = new TreeMap<>();
    Map<String, Integer> heightCounts = new TreeMap<>();

    for (int[] range : HEIGHT_RANGES) {
      String rangeKey = range[0] + "-" + range[1] + " inches";
      heightGroups.put(rangeKey, new ArrayList<>());
      heightCounts.put(rangeKey, 0);
    }

    for (Player player : team.getPlayers()) {
      for (int[] range : HEIGHT_RANGES) {
        if (player.getHeightInInches() >= range[0] && player.getHeightInInches() <= range[1]) {
          String rangeKey = range[0] + "-" + range[1] + " inches";
          heightGroups.get(rangeKey).add(player);
          heightCounts.put(rangeKey, heightCounts.get(rangeKey) + 1);
          break;
        }
      }
    }

    System.out.println("\nHeight Report for " + team.getName());
    for (Map.Entry<String, List<Player>> entry : heightGroups.entrySet()) {
      System.out.println("\n" + entry.getKey() + " (" + heightCounts.get(entry.getKey()) + " players):");
      for (Player player : entry.getValue()) {
        System.out.println("  - " + player.getFirstName() + " " + player.getLastName() +
                " (" + player.getHeightInInches() + " inches)");
      }
    }
  }

  private void displayLeagueBalanceReport() {
    System.out.println("\nLeague Balance Report");
    System.out.println("====================");

    for (Team team : teams) {
      int experienced = team.getExperiencedPlayerCount();
      int inexperienced = team.getInexperiencedPlayerCount();
      double experiencePercentage = team.getExperiencedPlayerPercentage();

      System.out.printf("%s:%n", team.getName());
      System.out.printf("  Experienced: %d%n", experienced);
      System.out.printf("  Inexperienced: %d%n", inexperienced);
      System.out.printf("  Experience Percentage: %.1f%%%n%n", experiencePercentage);
    }
  }

  private void printTeamRoster(Scanner scanner) {
    Team team = selectTeam(scanner);
    if (team == null) return;

    System.out.println("\n=== Team Roster ===");
    System.out.println("Team: " + team.getName());
    System.out.println("Coach: " + team.getCoachName());
    System.out.println("\nPlayers:");
    System.out.println("---------");

    Set<Player> sortedPlayers = new TreeSet<>(team.getPlayers());

    if (sortedPlayers.isEmpty()) {
      System.out.println("There are no players on that team.");
      return;
    }

    int playerNumber = 1;
    for (Player player : sortedPlayers) {
      System.out.printf("%d. %-20s Height: %2d\" Experience: %-3s Jersey: #%-2d%n",
              playerNumber++,
              player.getFirstName() + " " + player.getLastName(),
              player.getHeightInInches(),
              player.isPreviousExperience() ? "Yes" : "No",
              (int) (Math.random() * 99 + 1));
    }

    System.out.println("\nTotal Players: " + sortedPlayers.size());
    System.out.println("Roster Spots Available: " + (11 - sortedPlayers.size()));
  }

  private void addPlayerToWaitingList(Scanner scanner) {
    Set<Player> availablePlayers = getAvailablePlayers();
    if (availablePlayers.isEmpty()) {
      System.out.println("No available players!");
      return;
    }

    displayPlayers(availablePlayers);

    while (true) {
      System.out.print("Enter a player number to add to the wait list: ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      try {
        int playerNum = Integer.parseInt(input);
        Player selectedPlayer = findPlayerByNumber(availablePlayers, playerNum);
        if (selectedPlayer != null) {
          waitingList.offer(selectedPlayer);
          System.out.println("SUCCESS.  Player added to wait list.");
          return;
        } else {
          System.out.println("INCORRECT player selection.");
        }
        break;
      } catch (NumberFormatException e) {
        System.out.println("Please try your selection again.");
      }
    }
  }

  private Player findPlayerByNumber(Set<Player> players, int number) {
    if (number < 1 || number > players.size()) return null;
    int i = 1;
    for (Player player : players) {
      if (i++ == number) return player;
    }
    return null;
  }

  private Set<Player> getAvailablePlayers() {
    Set<Player> allPlayers = new TreeSet<>(Arrays.asList(Players.load()));
    Set<Player> assignedPlayers = new HashSet<>();

    for (Team team : teams) {
      assignedPlayers.addAll(team.getPlayers());
    }

    allPlayers.removeAll(assignedPlayers);
    return allPlayers;
  }

  //discuss this method in code review
  private void autoGenerateTeams(Scanner scanner) {
    while (true) {
      System.out.print("Enter a number of teams to create: ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) {
        System.out.println("Please try your selection again.");
        continue;
      }
      try {
        int numTeams = Integer.parseInt(input);
        Player[] allPlayers = Players.load();
        if (numTeams * 11 > allPlayers.length) {
          System.out.println("Sorry, there's not enough players to create" + numTeams + " teams.");
          continue;
        }

        // Clear any existing teams
        teams.clear();

        List<Player> playerPool = new ArrayList<>(Arrays.asList(allPlayers));
        Collections.shuffle(playerPool);

        // Sort players by experience
        List<Player> experiencedPlayers = new ArrayList<>();
        List<Player> inexperiencedPlayers = new ArrayList<>();
        for (Player player : playerPool) {
          if (player.isPreviousExperience()) {
            experiencedPlayers.add(player);
          } else {
            inexperiencedPlayers.add(player);
          }
        }

        // Store new teams
        List<Team> newTeams = new ArrayList<>();

        // Create teams
        for (int i = 0; i < numTeams; i++) {
          String teamName, coachName;

          while (true) {
            System.out.print("Enter a name for team " + (i + 1) + ": ");
            teamName = scanner.nextLine().trim();
            if (teamName.isEmpty()) {
              System.out.println("Please try your selection again.");
              continue;
            }
            break;
          }

          while (true) {
            System.out.print("Enter a coach name for team " + (i + 1) + ": ");
            coachName = scanner.nextLine().trim();
            if (coachName.isEmpty()) {
              System.out.println("Please try your selection again.");
              continue;
            }
            break;
          }

          Team team = new Team(teamName, coachName);
          teams.add(team);
          newTeams.add(team);
        }

        // Calculate players per team
        int playersPerTeam = allPlayers.length / numTeams;
        int expPlayersPerTeam = experiencedPlayers.size() / numTeams;

        // Distribute players evenly
        for (Team team : newTeams) {
          // Add experienced players
          for (int i = 0; i < expPlayersPerTeam && !experiencedPlayers.isEmpty(); i++) {
            team.addPlayer(experiencedPlayers.remove(0));
          }

          // Fill remaining spots with inexperienced players
          while (team.getPlayers().size() < playersPerTeam && !inexperiencedPlayers.isEmpty()) {
            team.addPlayer(inexperiencedPlayers.remove(0));
          }
        }

        // Distribute any remaining players
        for (Team team : newTeams) {
          while (!team.isFull() && (!experiencedPlayers.isEmpty() || !inexperiencedPlayers.isEmpty())) {
            Player player = !experiencedPlayers.isEmpty() ? experiencedPlayers.remove(0) :
                    !inexperiencedPlayers.isEmpty() ? inexperiencedPlayers.remove(0) : null;
            if (player != null) {
              team.addPlayer(player);
            }
          }
        }

        // Display generated teams and their rosters
        System.out.println("\n=== Generated Teams ===");
        for (Team team : newTeams) {
          System.out.println("\nTeam: " + team.getName());
          System.out.println("Coach: " + team.getCoachName());
          System.out.println("Players:");
          System.out.println("---------");

          Set<Player> sortedPlayers = new TreeSet<>(team.getPlayers());
          for (Player player : sortedPlayers) {
            System.out.printf("  - %-20s Height: %2d\" Experience: %-3s%n",
                    player.getFirstName() + " " + player.getLastName(),
                    player.getHeightInInches(),
                    player.isPreviousExperience() ? "Yes" : "No");
          }

          System.out.println("\nTeam Statistics:");
          System.out.printf("Total Players: %d%n", team.getPlayers().size());
          System.out.printf("Experience Ratio: %.1f%%%n", team.getExperiencedPlayerPercentage());
          System.out.println("----------------------------------------");
        }
        return;
      } catch (NumberFormatException e) {
        System.out.println("Please try your selection again.");
      }
    }
  }
}