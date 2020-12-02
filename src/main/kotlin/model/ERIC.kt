package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class ERIC private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var db: Container
    lateinit var web: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Electronic Management Information (ERIC/EMI)",
        "Financial reports for legal aid providers based on Management Information data"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-eric-emi")
      }

      web = system.addContainer(
        "ERIC UI",
        "A read-only interface for providers to view their financial reports",
        "Java"
      )

      db = system.addContainer(
        "Oracle Database",
        "ERIC database",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      web.uses(db, "Connects to")
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "eric-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "eric-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
