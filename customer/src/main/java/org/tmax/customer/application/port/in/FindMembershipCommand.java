package org.tmax.customer.application.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class FindMembershipCommand  {

    @NotNull
    private Long membershipId;

    public FindMembershipCommand(Long membershipId) {
        this.membershipId = membershipId;
    }
}
