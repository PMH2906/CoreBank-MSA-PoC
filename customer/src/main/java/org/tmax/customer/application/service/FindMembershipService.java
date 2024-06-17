package org.tmax.customer.application.service;

import lombok.RequiredArgsConstructor;
import org.tmax.customer.adapter.out.persistence.MembershipJpaEntity;
import org.tmax.customer.adapter.out.persistence.MembershipMapper;
import org.tmax.customer.application.port.in.FindMembershipCommand;
import org.tmax.customer.application.port.in.FindMembershipUseCase;
import org.tmax.customer.application.port.out.FindMembershipPort;
import org.tmax.customer.common.UseCase;
import org.tmax.customer.domain.Membership;


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
