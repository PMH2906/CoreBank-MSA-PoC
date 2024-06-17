package org.tmax.depositwithdrawal.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.fastcampuspay.membership.application.port.out.FindMembershipPort;
import org.fastcampuspay.membership.application.port.out.RegisterMembershipPort;
import org.fastcampuspay.membership.common.PersistenceAdapter;
import org.fastcampuspay.membership.domain.Membership;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements RegisterMembershipPort, FindMembershipPort {

    private final SpringDataMembershipRepository membershipRepository;

    @Override
    public MembershipJpaEntity createMembership(Membership.MembershipName membershipName, Membership.MembershipEmail membershipEmail, Membership.MembershipAddress membershipAddress, Membership.MembershipIsValid membershipIsValid, Membership.MembershipIsCorp membershipIsCorp) {
        return membershipRepository.save(
                new MembershipJpaEntity(
                        membershipName.getNameValue(),
                        membershipEmail.getEmailValue(),
                        membershipAddress.getAddressValue(),
                        membershipIsValid.isValidValue(),
                        membershipIsCorp.isCorpValue()));
    }

    @Override
    public Optional<MembershipJpaEntity> findMembership(Membership.MembershipId membershipId) {
        return membershipRepository.findById(membershipId.getMembershipId());
    }
}
