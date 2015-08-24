/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.grandt;

import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.IndividualFactory;
import juanmf.ga.structure.PopulationFactory;
import juanmf.grandt.chromosome.Team;
import juanmf.grandt.chromosome.TeamAptitudeMeter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author juan.fernandez
 */
@Configuration
public class Config {
    
    @Bean(name="individualFactory")
    public IndividualFactory getIndividualFactory() {
        return new Team.TeamFactory();
    }
    
    @Bean(name="populationFactory")
    public PopulationFactory getPopulationFactory() {
        return new Team.TeamPopulationFactory();
    }
    
    @Bean(name="fitnessMeter")
    public FitnessMeter getFitnessMeter() {
        return new TeamAptitudeMeter();
    }
}
