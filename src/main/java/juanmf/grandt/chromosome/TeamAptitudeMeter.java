package juanmf.grandt.chromosome;

import java.util.Collection;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.implementation.fitness.BasicFitnessMeter;

/**
 * 
 * @author juan.fernandez
 */
public class TeamAptitudeMeter extends BasicFitnessMeter<Team, Integer, Float> {

    public TeamAptitudeMeter() {
        this.lastBestAptitude = new BasicFitnessMeter.FirstGenerationWithBestFitness(0, 1);
    }
    
    @Override
    public Integer evaluate(Team team) {
        int teamScore = 0;
        for (Player player : team) {
            teamScore += player.getScore();
        }
        return teamScore;
    }

    @Override
    public Float updateAverageOnAdd(Team team, int size, Float averageAptitude) {
        return ((team.getAptitude() - averageAptitude) / (size)) + averageAptitude;
    }

    @Override
    public Float updateAverageOnRemove(Team team, int size, Float averageAptitude) {
        if (1 == size) {
            return 0.0f;
        }
        return averageAptitude - ((team.getAptitude() - averageAptitude) / (size));
    }

    @Override
    public Float getAverageFitness(Collection<? extends Team> c) {
        return (float) c.stream().mapToInt(Team::getAptitude).average().getAsDouble();
    }

    @Override
    public Float getInitialAverage() {
        return 0f;
    }

    @Override
    public Float getAverageFitness(Integer totalAptitude, int populationSize) {
        return (totalAptitude.floatValue() / populationSize);
    }

    @Override
    public Integer castAverageToFitness(Float average) {
        return average.intValue();
    }

    @Override
    public Integer getZeroFitness() {
        return 0;
    }

    @Override
    public Integer getSum(Integer aptitude1, Integer aptitude2) {
        return aptitude1 + aptitude2;
    }

    @Override
    public float getProportionBtwTwoIndividuals(Team i1, Team i2) {
        return i1.getAptitude() == 0 ? 0f : (i1.getAptitude() / (i1.getAptitude() + i2.getAptitude()));
    }
}
