package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class NOLASA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Not On Libra Auto-Search Application",
        "Is a micro-service that reads cases that have been marked as 'not-on-libra' from the MLRA " +
          "database once a day and auto-searches the HMCTS Libra system"
      )

      app = system.addContainer(
        "NOLASA",
        "A Web application that manages the auto searching for cases",
        "Java Spring"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-nolasa")
        AWSLegacy.ec2.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      app.uses(MAAT.db, "Uses as its own Database")
      app.uses(InfoX.app, "Searches for cases")
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "mlra-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "mlra-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
