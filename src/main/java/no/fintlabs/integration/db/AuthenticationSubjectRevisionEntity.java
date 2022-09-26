package no.fintlabs.integration.db;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

@Entity
@RevisionEntity(AuthenticationSubjectRevisionListener.class)
public class AuthenticationSubjectRevisionEntity extends DefaultRevisionEntity {

    @Getter
    @Setter
    private String subject;

}
