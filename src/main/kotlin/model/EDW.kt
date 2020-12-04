package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class EDW private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var edw003: Container
    lateinit var edw005: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Electronic Data Warehouse (EDW)",
        "Data lakes for the Management Information team"
      ).apply {
        setUrl("https://github.com/ministryofjustice/")
      }

      edw003 = system.addContainer(
        "EDW003 Database",
        "Contains MI data from EDW005 along with data from LAA applications",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }

      edw005 = system.addContainer(
        "EDW005 Database",
        "Contains MI data for LAA systems",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      edw003.uses(edw005, "Copies all data to")
    }

    override fun defineRelationships() {
      edw005.uses(CWA.db, "Copies all data from", "CDC")
      edw005.uses(CIS.db, "Copies submissions from", "CDC")
      edw005.uses(ERIC.db, "Pushes financial data from CWA and CIS (CIS & CCMS) to", "HUB")
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "edw-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "edw-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
