package juanmf.grandt.chromosome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Population;
import juanmf.ga.structure.PopulationFactory;
import juanmf.ga.structure.IndividualFactory;
import juanmf.ga.implementation.BasicPopulation;
import org.apache.commons.collections.list.SetUniqueList;

/**
 *
 * @author juan.fernandez
 */
public final class Team implements Individual<Integer, Team.Position, Player, Team> {

    private final Integer aptitude;
    private final BigDecimal cost;
    private final Map<Position, Player> players;
    private static final TeamAptitudeMeter aptitudeMeter = new TeamAptitudeMeter();
    private boolean isElite;

    public static Team createTeam(Map<Position, Player> players) {
        return new Team(players);
    }
     
    private Team(Map<Position, Player> players) {
        this.players = players;
        aptitudeMeter.evaluate(this);
        aptitude = players.values().stream().mapToInt(Player::getScore).sum();
        cost = BigDecimal.valueOf(players.values().stream()
                .mapToDouble(p -> p.getPrice().doubleValue()).sum());
    }
    
    @Override
    public Integer getAptitude() {
        return aptitude;
    }

    @Override
    public Gen getGen(Collection<Position> genPositionKeys) {
        return players.get(genPositionKeys.iterator().next());
    }

    @Override
    public int compareTo(Team o) {
        return aptitude.compareTo(o.getAptitude());
    }

    @Override
    public Iterator<Player> iterator() {
        return players.values().iterator();
    }

    @Override
    public boolean isLegal() {
        return TeamValidator.validate(this.players);
    }

    @Override
    public int size() {
        return players.size();
    }

    @Override
    public String toString() {
        return hashCode() + " # " +  players.values().stream().mapToInt(Player::getScore).sum() + ":"
             + cost + players.toString();
    }

    @Override
    public boolean isElite() {
        return isElite;
    }

    @Override
    public void setElite(boolean isElite) {
        this.isElite = isElite;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team)) {
            return false;
        }
        Team t = (Team) obj;
        Set playersSet = new HashSet(players.values());
        playersSet.removeAll(t.players.values());
        return 0 == playersSet.size();
    }

    @Override
    public int hashCode() {
        return players.values().stream().mapToInt(Player::hashCode).map(i -> 31 * i).sum();
    }
    
    public static enum Position {
        ARQUERO_TITULAR(Player.Role.ARQ), 
        DEFENSOR_1_TITULAR(Player.Role.DEF), 
        DEFENSOR_2_TITULAR(Player.Role.DEF), 
        DEFENSOR_3_TITULAR(Player.Role.DEF), 
        DEFENSOR_4_TITULAR(Player.Role.DEF), 
        VOLANTE_1_TITULAR(Player.Role.VOL), 
        VOLANTE_2_TITULAR(Player.Role.VOL), 
        VOLANTE_3_TITULAR(Player.Role.VOL), 
        VOLANTE_4_TITULAR(Player.Role.VOL), 
        DELANTERO_1_TITULAR(Player.Role.DEL), 
        DELANTERO_2_TITULAR(Player.Role.DEL),
        
        ARQUERO_SUPLENTE(Player.Role.ARQ), 
        DEFENSOR_SUPLENTE(Player.Role.DEF), 
        VOLANTE_SUPLENTE(Player.Role.VOL), 
        DELANTERO_SUPLENTE(Player.Role.DEL);
        
        private final Player.Role role;
        
        private Position(Player.Role role) {        
            this.role = role;
        }
    }
    
    /**
     * Factory for generating first population, with random individuals.
     */
    public static class TeamFactory implements IndividualFactory<Team, Player> {
        @Override
        public Team createRandomIndividual() {
            Map<Position, Player> teamPlayers;
            do {
                teamPlayers = new LinkedHashMap<>();
                SetUniqueList uniq = SetUniqueList.decorate(new ArrayList<>());
                Player player;
                for (Position p : Position.values()) {
                    while (! uniq.add(player = Player.getRandomPlayer(p.role))) {}
                    teamPlayers.put(p, player);
                }
            } while (!TeamValidator.validate(teamPlayers));
            return new Team(teamPlayers);
        }

        @Override
        public Team createIndividual(Collection<Player> genList) {
            Map<Position, Player> teamPlayers = new LinkedHashMap<>();
            Iterator<Position> positions = Arrays.asList(Position.values()).iterator();
            for (Player p : genList) {
//                    while (! uniq.add(player = Player.getRandomPlayer(p.role))) {}
                //TODO: verificar que esto saque positions en el orden en que 
                // createRandomIndividual las crea. Y que no se rompa al intentar eliminar Position.values
                teamPlayers.put(positions.next(), p);
            }
            return (TeamValidator.validate(teamPlayers) ? new Team(teamPlayers) : null);
        }

        @Override
        public Team createIndividual(Team t, int genIdx) {
            Map<Position, Player> teamPlayers;
            int tries = 100;
            boolean valid = false;
            do {                
                SetUniqueList uniq = SetUniqueList.decorate(new ArrayList<>());
                for (Player pl : t) {
                    uniq.add(pl);
                }
                teamPlayers = new LinkedHashMap<>();
                Position pos;
                Player player;
                Iterator<Player> i = t.iterator();
                int j = 0;
                for (Iterator<Position> positions = Arrays.asList(Position.values()).iterator(); 
                     j < t.size(); j++) {
                    pos = positions.next();
                    if (genIdx == j) {
                        i.next();
                        while (! uniq.add(player = Player.getRandomPlayer(pos.role))) {}
                    } else {
                        player = i.next();
                    }
                    teamPlayers.put(pos, player);
                }
            } while (!(valid = TeamValidator.validate(teamPlayers)) && (0 < --tries));
            return valid ? new Team(teamPlayers) : null;
        }
    }
    
    /**
     * 
     */
    public static class TeamPopulationFactory implements PopulationFactory<Team, 
            TeamAptitudeMeter, Float, Integer> {

        @Override
        public Population<Team, TeamAptitudeMeter, Float, Integer> createPopulation(
                TeamAptitudeMeter aptitudMeter, List<Team> individuals) {
            return BasicPopulation.createPopulation(aptitudeMeter, individuals);
        }

        @Override
        public Population<Team, TeamAptitudeMeter, Float, Integer> createEmptyPopulation() {
            return BasicPopulation.createPopulation(aptitudeMeter);
        }
        
    }
}
