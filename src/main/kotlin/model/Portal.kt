package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class Portal private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("Portal", "Single sign on for the LAA").apply {
        setUrl("https://github.com/ministryofjustice/laa-portal")
      }
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "portal-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "portal-context-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
