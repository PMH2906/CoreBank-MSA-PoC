package org.tmax.account.application.port.in;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fastcampuspay.membership.common.SelfValidating;

import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class FindMembershipCommand extends SelfValidating<FindMembershipCommand> {

    @NotNull
    private Long membershipId;

    public FindMembershipCommand(Long membershipId) {
        this.membershipId = membershipId;
        this.validateSelf();
    }
}
