package org.tmax.account.application.service;

import lombok.RequiredArgsConstructor;
import org.tmax.account.adapter.out.persistence.MembershipJpaEntity;
import org.tmax.account.adapter.out.persistence.MembershipMapper;
import org.tmax.account.application.port.in.FindMembershipCommand;
import org.tmax.account.application.port.in.FindMembershipUseCase;
import org.tmax.account.application.port.out.FindMembershipPort;
import org.tmax.account.common.UseCase;
import org.tmax.account.domain.Membership;

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
