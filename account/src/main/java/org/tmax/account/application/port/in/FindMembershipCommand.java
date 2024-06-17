package org.tmax.account.application.port.in;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class FindMembershipCommand {

    @NotNull
    private Long membershipId;

    public FindMembershipCommand(Long membershipId) {
        this.membershipId = membershipId;

    }
}
