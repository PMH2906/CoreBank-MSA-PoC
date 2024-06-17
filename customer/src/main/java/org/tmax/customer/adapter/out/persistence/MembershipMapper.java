package org.tmax.customer.adapter.out.persistence;

import org.springframework.stereotype.Component;
import org.tmax.customer.domain.Membership;

@Component
public class MembershipMapper {

    public Membership mapToDomainEntity(MembershipJpaEntity membershipJpaEntity){
        return Membership.generateMember(
                new Membership.MembershipId(membershipJpaEntity.getMembershipId()),
                new Membership.MembershipName(membershipJpaEntity.getName()),
                new Membership.MembershipEmail(membershipJpaEntity.getEmail()),
                new Membership.MembershipAddress(membershipJpaEntity.getAddress()),
                new Membership.MembershipIsValid(membershipJpaEntity.isValid()),
                new Membership.MembershipIsCorp(membershipJpaEntity.isCorp()));

    }
}
