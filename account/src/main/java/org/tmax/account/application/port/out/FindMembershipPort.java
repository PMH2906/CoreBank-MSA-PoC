package org.tmax.account.application.port.out;


import org.tmax.account.adapter.out.persistence.MembershipJpaEntity;
import org.tmax.account.domain.Membership;

import java.util.Optional;

public interface FindMembershipPort {

    Optional<MembershipJpaEntity> findMembership(
            Membership.MembershipId membershipId
    );
}
