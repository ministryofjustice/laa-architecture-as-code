package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CCMS private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var providerDetailsAPI: Container
    lateinit var soa: Container
    lateinit var ebsDb: Container
    lateinit var trainingWebsite: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Client and Cost Management System",
        "Legal aid applications, case management, financials, billing, and more"
      )

      providerDetailsAPI = system.addContainer(
        "Provider Details API",
        "An XML API to provide reference data required to integrate with CCMS services",
        "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-ccms-provider-details-api")
      }

      soa = system.addContainer("SOA", "A SOAP API to E-Business Suite", "Oracle SOA Suite").apply {
        setUrl("https://github.com/ministryofjustice/laa-ccms-app-soa")
      }

      ebsDb = system.addContainer(
        "CCMS E-Business Suite Database",
        "Customised E-Business Suite DB",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }

      trainingWebsite = system.addContainer(
        "CCMS training",
        "A website with training, guidance and support for external users of CCMS",
        "Squiz Matrix"
      ).apply {
        url = "https://ccmstraining.justice.gov.uk/"
      }
    }

    override fun defineInternalContainerRelationships() {
      soa.uses(ebsDb, "Connects to")
      providerDetailsAPI.uses(ebsDb, "Connects to")
    }

    override fun defineRelationships() {
      system.uses(CWA.system, "Gets contract data from CWA and synchronises updates each night")
      soa.uses(BenefitChecker.system, "Validates Universal Credit claimants via", "SOAP")
      soa.uses(
        CIS.system,
        "Imports CIS invoices approved for payment and, after payment, updates status of invoices in CIS"
      )
      soa.uses(CWA.system, "Synchronises user, provider and bank account data with")
      providerDetailsAPI.uses(CWA.system, "Looks up contract data from")
    }

    override fun defineExternalRelationships() {
      soa.uses(Northgate.system, "Manages documents in")
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.provider.uses(trainingWebsite, "Learns how to use CCMS")
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "ccms-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "ccms-context-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
