package org.tmax.account.application.service;

import lombok.RequiredArgsConstructor;
import org.fastcampuspay.membership.adapter.out.persistence.MembershipJpaEntity;
import org.fastcampuspay.membership.adapter.out.persistence.MembershipMapper;
import org.fastcampuspay.membership.application.port.in.RegisterMembershipCommand;
import org.fastcampuspay.membership.application.port.in.RegisterMembershipUseCase;
import org.fastcampuspay.membership.application.port.out.RegisterMembershipPort;
import org.fastcampuspay.membership.common.UseCase;
import org.fastcampuspay.membership.domain.Membership;

import javax.transaction.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class RegisterMembershipService implements RegisterMembershipUseCase{

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
