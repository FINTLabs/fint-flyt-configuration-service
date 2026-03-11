package no.novari.flyt.configuration.validation

import jakarta.validation.ConstraintValidatorFactory
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.hibernate.validator.HibernateValidator
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory

@Configuration
class ValidatorConfiguration {
    @Bean
    fun validator(validatorFactory: ValidatorFactory): Validator = validatorFactory.validator

    @Bean
    fun validatorFactory(constraintValidatorFactory: ConstraintValidatorFactory): ValidatorFactory =
        Validation
            .byProvider(HibernateValidator::class.java)
            .configure()
            .constraintValidatorFactory(constraintValidatorFactory)
            .buildValidatorFactory()

    @Bean
    fun constraintValidatorFactory(autowireCapableBeanFactory: AutowireCapableBeanFactory): ConstraintValidatorFactory =
        SpringConstraintValidatorFactory(autowireCapableBeanFactory)

    @Bean
    fun hibernatePropertiesCustomizer(validatorFactory: ValidatorFactory): HibernatePropertiesCustomizer =
        HibernatePropertiesCustomizer { hibernateProperties ->
            hibernateProperties["jakarta.persistence.validation.factory"] = validatorFactory
        }
}
