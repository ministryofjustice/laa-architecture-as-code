package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class BenefitChecker private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Benefit Checker",
        "An interface between the Legal Aid Agency and the Department of Work & Pensions, providing access to the " +
          "latest pass-ported benefit entitlement checks"
      )

      api = system.addContainer("Benefit Checker API", "An XML API interface for Benefit Checker", "Java").apply {
        setUrl("https://github.com/ministryofjustice/laa-benefitchecker-1.0-big")
      }
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "benefit-checker-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "benefit-checker-context-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}