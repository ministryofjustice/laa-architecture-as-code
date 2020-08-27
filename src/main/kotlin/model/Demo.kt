package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class Demo private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem("Demo", "This system is a demonstration, delete me")

      app = system.addContainer("Demo UI", "Demo workflow creator", "Ruby on Rails").apply {
        Tags.WEB_BROWSER.addTo(this)
      }

      val db = system.addContainer("Demo Database", "Stores demo workflows", "PostgreSQL").apply {
        Tags.DATABASE.addTo(this)
      }

      app.uses(db, "connects to")
    }

    override fun defineRelationships() {
      // declare relationships to other systems and other system containers here
      app.uses(ExternalDemo.system, "retrieves demo files from")
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "demo-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "demo-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
