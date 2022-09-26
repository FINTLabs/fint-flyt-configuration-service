package no.fintlabs.integration.db;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationSubjectRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuthenticationSubjectRevisionEntity authenticationSubjectRevisionEntity = (AuthenticationSubjectRevisionEntity) revisionEntity;

        // TODO: 23/09/2022 How do we get sub here?
//        var context = ReactiveSecurityContextHolder.getContext();
//        AuthenticationUtil.getSub(
//                ReactiveSecurityContextHolder.getContext()
//                        .map(SecurityContext::getAuthentication)
//        ).block();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            authenticationSubjectRevisionEntity.setSubject(authentication.getName());
        }
    }

}
