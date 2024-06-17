package org.tmax.depositwithdrawal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Membership {

    // Membership : 오염이 되면 안 되는 클래스. 고객 정보. 핵심 도메인

    private final Long membershipId;

    private final String name;

    private final String email;

    private final String address;

    private final boolean isValid;

    private final boolean isCorp;

    public static Membership generateMember(
            MembershipId membershipId,
            MembershipName membershipName,
            MembershipEmail membershipEmail,
            MembershipAddress membershipAddress,
            MembershipIsValid membershipIsValid,
            MembershipIsCorp membershipIsCorp) {
        return new Membership(
                membershipId.membershipId,
                membershipName.nameValue,
                membershipEmail.emailValue,
                membershipAddress.addressValue,
                membershipIsValid.isValidValue,
                membershipIsCorp.isCorpValue);
    }

    @Value
    public static class MembershipId {

        Long membershipId;

        public MembershipId(Long value) {
            this.membershipId = value;
        }
    }

    @Value
    public static class MembershipName {

        String nameValue;

        public MembershipName(String value) {
            this.nameValue = value;
        }
    }

    @Value
    public static class MembershipEmail {

        String emailValue;

        public MembershipEmail(String value) {
            this.emailValue = value;
        }
    }

    @Value
    public static class MembershipAddress {

        String addressValue;

        public MembershipAddress(String value) {
            this.addressValue = value;
        }
    }

    @Value
    public static class MembershipIsValid {

        boolean isValidValue;

        public MembershipIsValid(boolean value) {
            this.isValidValue = value;
        }
    }

    @Value
    public static class MembershipIsCorp {

        boolean isCorpValue;

        public MembershipIsCorp(boolean value) {
            this.isCorpValue = value;
        }
    }

    public static void main(String[] args) {
        Membership membership = Membership.generateMember(
                new MembershipId(1L),
                new MembershipName("name"),
                new MembershipEmail("email"),
                new MembershipAddress("address"),
                new MembershipIsValid(true),
                new MembershipIsCorp(false)
                );
    }
}
