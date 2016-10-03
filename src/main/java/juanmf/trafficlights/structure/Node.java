package juanmf.trafficlights.structure;

import juanmf.ga.structure.Gen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A node is part of a crossing point of 2 or more ways, where a list of semaphores must coordinate
 * to share green time. Only one can be green at a time.
 *
 * Each semaphore is part of one way, which ideally should have a green wave.
 * All Semaphores in a Node must have same period.
 * Phase & switchTime of each should be so that none is green at the same time with other.
 *
 * @author juanmf@gmail.com
 */
public class Node implements Gen {

    /**
     * A list of lights, where only one should be green at a time
     */
    List<Node.Semaphore> semaphores;


    /**
     * A Semaphore acts like a square signal function, in that it has a period, starts geen
     * at switch time it turns red, and has a phase that delays the green start time from zero
     * seconds to some int value less than period
     *
     * Invariants:
     *   0 > phase > period
     *   0 > switchTime > period
     *
     * @author juanmf@gmail.com
     */
    public static class Semaphore {
        static final Set<Semaphore> possibleCombinations = new HashSet<>();

        final int period;
        final int switchTime;
        final int phase;

        static {
            // max period is 5 minutes; 300 seconds
            int lowestPeriod = 50;
            for (int somePeriod = lowestPeriod; somePeriod <= 300; somePeriod++ ){
                for (int someSwitchTime = lowestPeriod + 1; someSwitchTime < somePeriod; someSwitchTime++ ){
                    for (int somePhase = lowestPeriod + 1; somePhase < somePeriod; somePhase++ ){
                        possibleCombinations.add(new Semaphore(somePeriod, someSwitchTime, somePhase));
                    }
                }
            }
        }

        private Semaphore(int period, int switchTime, int phase) {
            this.period = period;
            this.switchTime = switchTime;
            this.phase = phase;
        }

        public boolean isGreen(int daySeconds) {
            if (0 > daySeconds) {
                throw new IllegalArgumentException("daySeconds should be positive");
            }
            return switchTime >= ((daySeconds - phase) % period) ;
        }

        public boolean isRed(int daySeconds) {
            return ! isGreen(daySeconds);
        }

        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof Semaphore)) {
                return false;
            }
            Semaphore o = (Semaphore) obj;
            return o.period == period && o.phase == phase && o.switchTime == switchTime;
        }

        @Override
        public int hashCode() {
            return 31 * period + 37 * phase + 101 * switchTime;
        }
    }
}
