package org.tmax.account.application.port.in;

import org.fastcampuspay.membership.domain.Membership;

public interface FindMembershipUseCase {

    Membership findMembership(FindMembershipCommand command);
}
