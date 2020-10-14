package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CLA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var claPublic: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Civil Legal Advice",
        "Service for citizens to check if they are eligible for legal aid")

      claPublic = system.addContainer(
        "Civil Legal Advice Public UI",
        "A public web service which allows citizens to check if they are eligible for legal aid",
        "Flask"
      ).apply {
        setUrl("https://github.com/ministryofjustice/cla_public")
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers
      claPublic.uses(FALA.laalaa, "Performs legal adviser searches through", "REST")
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "cla-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "cla-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
