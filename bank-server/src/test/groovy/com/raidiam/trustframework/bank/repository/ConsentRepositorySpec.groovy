package com.raidiam.trustframework.bank.repository

import com.raidiam.trustframework.bank.controllers.ConsentFactory
import com.raidiam.trustframework.bank.domain.ConsentEntity
import com.raidiam.trustframework.mockbank.models.generated.CreateConsent
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest(transactional = false, environments = ["db"])
class ConsentRepositorySpec extends Specification {
    @Inject
    @Shared
    ConsentRepository consentRepository

    def "We can save a consent request"() {
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
    }

    def "Business entity is optional"() {
        given:
        CreateConsent toSave = ConsentFactory.createConsent()
        toSave.getData().setBusinessEntity(null)

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
        def id = consentBack.getConsentId()
        id ==~ /^urn:raidiambank:.*/
    }

}
