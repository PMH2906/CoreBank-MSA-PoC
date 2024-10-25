//package com.tmax.orchestrator.config;
//
//import org.jboss.weld.environment.se.Weld;
//import org.jboss.weld.environment.se.WeldContainer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CdiConfig {
//
//    @Bean
//    public Weld weld() {
//        return new Weld();
//    }
//
//    @Bean
//    public WeldContainer weldContainer(Weld weld) {
//        return weld.initialize();
//    }
//
//    @Bean
//    public jakarta.enterprise.event.Event<Object> cdiEvent(WeldContainer weldContainer) {
//        return weldContainer.select(jakarta.enterprise.event.Event.class).get();
//    }
//}
