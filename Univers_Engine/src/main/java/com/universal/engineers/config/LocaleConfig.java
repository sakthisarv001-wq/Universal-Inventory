package com.universal.engineers.config;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import java.util.Locale;
@Configuration
public class LocaleConfig implements WebMvcConfigurer{
 @Bean public CookieLocaleResolver localeResolver(){CookieLocaleResolver r=new CookieLocaleResolver("language");r.setDefaultLocale(Locale.ENGLISH);return r;}
 @Override public void addInterceptors(InterceptorRegistry registry){registry.addInterceptor(new org.springframework.web.servlet.i18n.LocaleChangeInterceptor(){{setParamName("lang");}});}
}
