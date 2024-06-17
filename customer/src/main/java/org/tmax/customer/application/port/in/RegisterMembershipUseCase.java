package org.tmax.customer.application.port.in;


import org.tmax.customer.domain.Membership;

public interface RegisterMembershipUseCase {

    Membership registerMembership(RegisterMembershipCommand command);

}
