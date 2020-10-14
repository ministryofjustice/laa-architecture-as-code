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
    }

    override fun defineInternalContainerRelationships() {
      soa.uses(ebsDb, "connects to")
      providerDetailsAPI.uses(ebsDb, "connects to")
    }

    override fun defineRelationships() {
      soa.uses(BenefitChecker.system, "validates Universal Credit claimants via", "SOAP")
    }

    override fun defineExternalRelationships() {
      soa.uses(Northgate.system, "manages documents in")
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
