package org.tmax.account.application.port.in;


import org.tmax.account.domain.Membership;

public interface FindMembershipUseCase {

    Membership findMembership(FindMembershipCommand command);
}
