package org.tmax.customer.application.port.out;

import org.tmax.customer.adapter.out.persistence.MembershipJpaEntity;
import org.tmax.customer.domain.Membership;

import java.util.Optional;

public interface FindMembershipPort {

    Optional<MembershipJpaEntity> findMembership(
            Membership.MembershipId membershipId
    );
}
