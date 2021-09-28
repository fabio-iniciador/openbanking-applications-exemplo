package com.raidiam.trustframework.bank.repository

import com.raidiam.trustframework.bank.controllers.ConsentFactory
import com.raidiam.trustframework.bank.domain.ConsentEntity
import com.raidiam.trustframework.bank.domain.ConsentPermissionEntity
import com.raidiam.trustframework.mockbank.models.generated.CreateConsent
import com.raidiam.trustframework.mockbank.models.generated.CreateConsentData
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

@MicronautTest(transactional = false, environments = ["db"])
class ConsentPermissionsRepositorySpec extends Specification {

    @Inject
    @Shared
    ConsentRepository consentRepository

    @Inject
    @Shared
    ConsentPermissionsRepository consentPermissionsRepository

    def "We can save a consent permission"() {
        given:
        CreateConsent toSave = ConsentFactory.createConsent()

        ConsentEntity consentEntity = ConsentEntity.fromRequest(toSave)

        when:
        ConsentEntity consent = consentRepository.save(consentEntity)
        Optional<ConsentEntity> consentOpt = consentRepository.findById(consent.getReferenceId())

        then:
        consentOpt.isPresent()
        ConsentEntity consentBack = consentOpt.get()
        consent == consentBack
        consentBack.getReferenceId() != null
        consentBack.getConsentId() != null

        when:
        ConsentPermissionEntity permEntity = ConsentPermissionEntity.fromRequest(CreateConsentData.PermissionsEnum.ACCOUNTS_READ, consentBack)
        ConsentPermissionEntity permSet = consentPermissionsRepository.save(permEntity)
        Optional<ConsentPermissionEntity> permOpt = consentPermissionsRepository.findById(permSet.getReferenceId())

        then:
        permOpt.isPresent()
        ConsentPermissionEntity permBack = permOpt.get()
        permSet == permBack
        permBack.getReferenceId() != null
        permBack.getConsentId() != null
    }
}


