package org.tmax.depositwithdrawal.application.port.out;

import org.fastcampuspay.membership.adapter.out.persistence.MembershipJpaEntity;
import org.fastcampuspay.membership.domain.Membership;

import java.util.Optional;

public interface FindMembershipPort {

    Optional<MembershipJpaEntity> findMembership(
            Membership.MembershipId membershipId
    );
}
