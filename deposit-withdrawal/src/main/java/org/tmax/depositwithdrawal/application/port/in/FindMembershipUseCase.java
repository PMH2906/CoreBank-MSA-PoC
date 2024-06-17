package org.tmax.depositwithdrawal.application.port.in;

import org.fastcampuspay.membership.domain.Membership;

public interface FindMembershipUseCase {

    Membership findMembership(FindMembershipCommand command);
}
