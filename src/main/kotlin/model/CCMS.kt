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

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Client Cost Management System",
        "Legal aid applications, case management, financials, billing, and more"
      )

      providerDetailsAPI = system.addContainer(
        "Provider Details API",
        "An XML API to provide reference data required to integrate with CCMS services",
        "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-ccms-provider-details-api")
      }

      soa = system.addContainer("SOA", "An XML API to E-business Suite", "Oracle SOA Suite").apply {
        setUrl("https://github.com/ministryofjustice/laa-ccms-app-soa")
      }

      val ebsDb = system.addContainer(
        "CCMS EBusiness Suite Database",
        "Customised EBusiness Suite DB",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }

      soa.uses(ebsDb, "connects to")
      providerDetailsAPI.uses(ebsDb, "connects to")
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
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
