package juanmf.ga;

import com.google.common.eventbus.EventBus;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.implementation.BasicPopulation;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
        
/**
 * Hello world!
 *
 */
@Configuration
@ComponentScan("juanmf.")
public class App {
    
    public static ApplicationContext context;

    @Autowired(required = true)
    private FitnessMeter aptitudeMeter; 

    @Autowired
    private Evolution evolution;    
    
    public void execute() {
        System.out.println(
                "Final Result:"
              + evolution.evolve(BasicPopulation.createPopulation(aptitudeMeter))
                      .getBest()
            );
    }   
    
    public static void main(String[] args)
    {
        context = new AnnotationConfigApplicationContext(App.class);
        App app = context.getBean(App.class);
        app.execute();
    }
}
