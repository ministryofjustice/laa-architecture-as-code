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
    lateinit var oracleForms: Container
    lateinit var providerUserInterface: Container
    lateinit var temporaryDataStore: Container
    lateinit var trainingWebsite: Container
    lateinit var opaWebDeterminations: Container
    lateinit var connector: Container

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

      providerUserInterface = system.addContainer(
        "Provider User Interface",
        "Service that providers use to submit civil legal aid applications, and bills",
        "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-ccms-pui")
      }

      temporaryDataStore = system.addContainer(
        "Temporary Data Store",
        "Stores incomplete applications",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
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

      oracleForms = system.addContainer(
        "CCMS Oracle Forms",
        "Forms that provide a UI for interacting with the EBS database",
        "Oracle"
      )

      trainingWebsite = system.addContainer(
        "CCMS training",
        "A website with training, guidance and support for external users of CCMS",
        "Squiz Matrix"
      ).apply {
        url = "https://ccmstraining.justice.gov.uk/"
      }

      opaWebDeterminations = system.addContainer(
        "Oracle Web Determinations",
        "Dynamically generates interview questions based on business rules",
        "Oracle Policy Automation"
      ).apply {
        url = "https://github.com/ministryofjustice/laa-ccms-opa-policy-models"
      }

      connector = system.addContainer(
        "Connector",
        "Web Determinations data adaptor plugin",
        "Java"
      ).apply {
        url = "https://github.com/ministryofjustice/laa-ccms-pui"
      }
    }

    override fun defineInternalContainerRelationships() {
      soa.uses(ebsDb, "Connects to")
      providerDetailsAPI.uses(ebsDb, "Connects to")

      connector.uses(temporaryDataStore, "Reads and writes data to")

      opaWebDeterminations.uses(connector, "Reads and writes applications to")

      providerUserInterface.uses(temporaryDataStore, "Reads and writes data to")
      providerUserInterface.uses(soa, "Reads and writes applications to", "SOAP")
      providerUserInterface.uses(opaWebDeterminations, "Serves forms", "SOAP")

      oracleForms.uses(ebsDb, "Reads and writes data to")

      temporaryDataStore.uses(ebsDb, "Reads data from", "Shared database")
    }

    override fun defineRelationships() {
      soa.uses(CWA.db, "Looks up provider contracts", "JDBC")
      soa.uses(BenefitChecker.system, "Validates Universal Credit claimants via", "SOAP")
    }

    override fun defineExternalRelationships() {
      soa.uses(
        CorporateDocumentServices.system,
        "Transfers a nightly ZIP of PDF correspondence, and XML manifest, to be printed and posted",
        "FTP"
      )
      soa.uses(Northgate.system, "Manages documents in", "SOAP")
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.provider.uses(trainingWebsite, "Learns how to use CCMS")
      LegalAidAgencyUsers.provider.uses(providerUserInterface, "Completes applications")
      LegalAidAgencyUsers.meansCaseWorker.uses(oracleForms, "Assesses legal aid applications for means eligibility")
      LegalAidAgencyUsers.meritsCaseWorker.uses(oracleForms, "Assesses legal aid applications for merits eligibility")
      LegalAidAgencyUsers.billingCaseWorker.uses(oracleForms, "Verifies provider bills")
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
