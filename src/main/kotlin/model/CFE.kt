package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CFE private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Check Financial Eligibility",
        "A service for checking financial eligibility for legal aid")

      api = system.addContainer(
        "Check Financial Eligibility API",
        "A JSON API for Check Financial Eligibility",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/check-financial-eligibility")
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "cfe-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "cfe-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
