package org.tmax.customer.application.port.out;


import org.tmax.customer.adapter.out.persistence.MembershipJpaEntity;
import org.tmax.customer.domain.Membership;

public interface RegisterMembershipPort {

    MembershipJpaEntity createMembership(
            Membership.MembershipName membershipName,
            Membership.MembershipEmail membershipEmail,
            Membership.MembershipAddress membershipAddress,
            Membership.MembershipIsValid membershipIsValid,
            Membership.MembershipIsCorp membershipIsCorp);
}
