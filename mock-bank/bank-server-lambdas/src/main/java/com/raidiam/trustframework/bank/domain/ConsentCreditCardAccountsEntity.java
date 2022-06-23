package com.raidiam.trustframework.bank.domain;

import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Audited
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consent_credit_card_accounts")
public class ConsentCreditCardAccountsEntity extends BaseEntity  {

    @Id
    @GeneratedValue
    @Column(name = "reference_id", unique = true, nullable = false, updatable = false, insertable = false)
    private Integer referenceId;

    @Column(name = "consent_id")
    private String consentId;

    @Column(name = "credit_card_account_id")
    private UUID creditCardAccountId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_id", referencedColumnName = "consent_id", insertable = false, nullable = false, updatable = false)
    @NotAudited
    private ConsentEntity consent;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    // this is given "EAGER" fetch type, because there is no back-link from contracts to this table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_card_account_id", referencedColumnName = "credit_card_account_id", insertable = false, nullable = false, updatable = false)
    @NotAudited
    private CreditCardAccountsEntity creditCardAccount;

    public ConsentCreditCardAccountsEntity(ConsentEntity consent, CreditCardAccountsEntity creditCardAccount){
        this.consentId = consent.getConsentId();
        this.creditCardAccountId = creditCardAccount.getCreditCardAccountId();
    }
}