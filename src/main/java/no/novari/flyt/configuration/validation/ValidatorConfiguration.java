package no.novari.flyt.configuration.validation;

import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

@Configuration
public class ValidatorConfiguration {

    @Bean
    Validator validator(ValidatorFactory validatorFactory) {
        return validatorFactory
                .getValidator();
    }

    @Bean
    ValidatorFactory validatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
        return Validation.byProvider(HibernateValidator.class)
                .configure()
                .constraintValidatorFactory(constraintValidatorFactory)
                .buildValidatorFactory();
    }

    @Bean
    ConstraintValidatorFactory constraintValidatorFactory(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        return new SpringConstraintValidatorFactory(autowireCapableBeanFactory);
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(ValidatorFactory validatorFactory) {
        return hibernateProperties -> hibernateProperties.put("javax.persistence.validation.factory", validatorFactory);
    }

}
