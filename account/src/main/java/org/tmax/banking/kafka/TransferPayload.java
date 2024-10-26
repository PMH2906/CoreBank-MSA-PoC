package org.tmax.banking.kafka;

public class TransferPayload {
    public String depositorName;
    public String depositorAccountNum;
    public String recipientName;
    public String recipientAccountNum;
    public String amount;

    public TransferStatus transferStatus() {

        // 임시로 모두 허용
        TransferStatus status = TransferStatus.APPROVED;

        return status;
    }
}
