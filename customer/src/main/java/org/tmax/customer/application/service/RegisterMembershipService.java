package org.tmax.customer.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.tmax.customer.adapter.out.persistence.MembershipJpaEntity;
import org.tmax.customer.adapter.out.persistence.MembershipMapper;
import org.tmax.customer.application.port.in.RegisterMembershipCommand;
import org.tmax.customer.application.port.in.RegisterMembershipUseCase;
import org.tmax.customer.application.port.out.RegisterMembershipPort;
import org.tmax.customer.common.UseCase;
import org.tmax.customer.domain.Membership;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisterMembershipService implements RegisterMembershipUseCase {

    private final RegisterMembershipPort registerMembershipPort;

    private final MembershipMapper membershipMapper;

    @Override
    public Membership registerMembership(RegisterMembershipCommand command) {

        // command -> DB
        // port -> adapter
        MembershipJpaEntity jpaEntity = registerMembershipPort.createMembership(
                new Membership.MembershipName(command.getName()),
                new Membership.MembershipEmail(command.getEmail()),
                new Membership.MembershipAddress(command.getAddress()),
                new Membership.MembershipIsValid(command.isValid()),
                new Membership.MembershipIsCorp(command.isCorp())
        );

        // entity -> Membership
        return membershipMapper.mapToDomainEntity(jpaEntity);
    }
}
