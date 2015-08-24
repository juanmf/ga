package juanmf.ga;

import com.google.common.eventbus.EventBus;
import java.util.EventListener;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.implementation.BasicEvolution;
import juanmf.ga.implementation.operators.BasicMutator;
import juanmf.ga.implementation.operators.BasicReducesSurogateCrossOver;
import juanmf.ga.implementation.operators.ConcurrentBasicReducesSurogateCrossOver;
import juanmf.ga.implementation.operators.BasicSusSelector;
import juanmf.ga.operators.Crosser;
import juanmf.ga.operators.Mutator;
import juanmf.ga.operators.Selector;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.IndividualFactory;
import juanmf.ga.structure.PopulationFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 * @param <G> A Sub Type of Gen.
 * 
 * @author juan.fernandez
 */
@Configuration
public class AppConfig <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>, G extends Gen> {
        //Static and thread safe initialization
    
    private static String method() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
    
    @Bean(name="eventBus")
    public static EventBus getEventBus() {
        return new EventBus();
    }
    
    @Bean(name="crosser")
    public Crosser<I, G> getCrosser(
            IndividualFactory individualFactory
    ) {
        return new BasicReducesSurogateCrossOver<>(individualFactory);
    }
    
    @Bean(name="concurrentcrosser")
    @Primary
    public Crosser<I, G> getConcurrentCrosser(
            EventBus eb, IndividualFactory individualFactory
    ) {
        return new ConcurrentBasicReducesSurogateCrossOver<>(eb, individualFactory);
    }
    
    @Bean(name="selector")
    public Selector<I, A, C, V> getSelector(
            FitnessMeter aptitudeMeter
    ) {
        return new BasicSusSelector<>(aptitudeMeter);
    }
    
    @Bean(name="mutator")
    public Mutator<I> getMutator(
            IndividualFactory individualFactory
    ) {
        return new BasicMutator<>(individualFactory);
    }
    
    @Bean(name="evolution")
    public Evolution getEvolution(
            EventBus eb, Selector selector, Crosser crosser, Mutator mutator,
            IndividualFactory individualFactory, FitnessMeter aptitudeMeter,
            PopulationFactory populationFactory
    ) {
        return new BasicEvolution(eb, selector, crosser, mutator, individualFactory, aptitudeMeter, populationFactory);
    }
    
    
    @Component
    public static class EventBusPostProcessor implements BeanPostProcessor,
            ApplicationContextAware {
        
        ApplicationContext context; 
        
        @Override
        public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
            if (bean instanceof EventListener) {
                EventBus eb = context.getBean(EventBus.class);
                eb.register(bean);
            }
            return bean;
        }

        @Override
        public void setApplicationContext(ApplicationContext ac) throws BeansException {
            context = ac;
        }
    }
    
}
