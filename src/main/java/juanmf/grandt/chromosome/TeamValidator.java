package juanmf.grandt.chromosome;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import juanmf.ga.structure.Gen;

/**
 * Ensures that the Players grouped by a Virtual team meets all the restrictions.
 * 
 * @author juan.fernandez
 */
public final class TeamValidator {
    private static final BigDecimal BUDGET = new BigDecimal("60000000.0"); 
    private static final int MAX_SAMETEAM_PLAYERS = 3; 
    
    static boolean validate(Map<Team.Position, Player> t) {
        Set<Player> uniq = new HashSet<>();
        Collection<Player> genList = t.values();
        uniq.addAll(genList);
        if (uniq.size() != genList.size()) {
            return false;
        }
        BigDecimal cost = new BigDecimal(0);
        Map<String, Integer> clubs = new HashMap<>();
        Integer clubCount;        
        for (Player p : t.values()) {
            cost = cost.add(p.getPrice());
            clubCount = (clubs.get(p.getClub()) == null ? 0 : clubs.get(p.getClub()));
            clubs.put(p.getClub(), ++clubCount);
        }
        return fitsInBudget(cost); //&& compliesMaxPlayers(clubs);
    }

    private static boolean fitsInBudget(BigDecimal cost) {
        return BUDGET.compareTo(cost) >= 0;
    }

    private static boolean compliesMaxPlayers(Map<String, Integer> clubs) {
        return true;
//        for (Integer clubCount : clubs.values()) {
//            if (MAX_SAMETEAM_PLAYERS < clubCount) {
//                return false;
//            }
//        }
//        return true;
    }
}
