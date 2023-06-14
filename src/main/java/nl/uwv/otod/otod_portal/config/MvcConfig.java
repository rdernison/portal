package nl.uwv.otod.otod_portal.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	private static Logger logger = LogManager.getLogger();
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/resources/");	
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		logger.info("adding view controllers");
		registry.addViewController("/home").setViewName("home");
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		logger.info("Configuring view resolvers");
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		logger.info("Setting character encoding");
        resolver.setCharacterEncoding("UTF-8"); // <- this was added
        logger.info("Setting force content type");
        resolver.setForceContentType(true); // <- this was added
        logger.info("Setting content type with charset");
        resolver.setContentType("text/html; charset=UTF-8"); // <- this was added
        logger.info("Registring view resolver");
        registry.viewResolver(resolver);
//        logger.info(" Set character encoding");
//        registry.viewResolver(resolver);
	}
}
