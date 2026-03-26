package ru.otus.hw.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.hw.filters.MdcLogSetupFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> mdcFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<OncePerRequestFilter>();
        registrationBean.setFilter(new MdcLogSetupFilter());
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
