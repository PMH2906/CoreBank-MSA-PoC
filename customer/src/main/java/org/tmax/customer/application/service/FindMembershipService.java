package org.tmax.customer.application.service;

import lombok.RequiredArgsConstructor;
import org.fastcampuspay.membership.adapter.out.persistence.MembershipJpaEntity;
import org.fastcampuspay.membership.adapter.out.persistence.MembershipMapper;
import org.fastcampuspay.membership.application.port.in.FindMembershipCommand;
import org.fastcampuspay.membership.application.port.in.FindMembershipUseCase;
import org.fastcampuspay.membership.application.port.out.FindMembershipPort;
import org.fastcampuspay.membership.common.UseCase;
import org.fastcampuspay.membership.domain.Membership;

@UseCase
@RequiredArgsConstructor
public class FindMembershipService implements FindMembershipUseCase {

    private final FindMembershipPort findMembershipPort;
    private final MembershipMapper membershipMapper;

    @Override
    public Membership findMembership(FindMembershipCommand command) {

        MembershipJpaEntity membership = findMembershipPort.findMembership(
                new Membership.MembershipId(command.getMembershipId()))
                .orElseThrow();

        return membershipMapper.mapToDomainEntity(membership);
    }
}
