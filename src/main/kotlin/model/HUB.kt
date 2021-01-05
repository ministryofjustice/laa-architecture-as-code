package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class HUB private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "HUB",
        "Links systems, scheduled jobs to copy data around, batch scheduling/ETL coordinator for the LAA."
      ).apply {
        Tags.CRIME.addTo(this)
        Tags.GET_PAID.addTo(this)
      }

      db = system.addContainer(
        "HUB DB",
        "Contains the application logic and storage for HUB jobs",
        "Oracle PL/SQL"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-hub-application")
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      db.uses(
        MAAT.db,
        "Sends Crown Court Outcomes to (XMAT1, XMAT2)",
        "HUB",
        null,
        tagsToArgument(Tags.CRIME)
      )
      db.uses(
        MAAT.db,
        "Loads completed & submitted forms (MAAT9)",
        "HUB",
        null,
        tagsToArgument(Tags.CRIME)
      )
      db.uses(
        CCR.db,
        "Sends Case information to (CCR01, CCR08, CCR09)",
        "HUB",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
      system.uses(
        eForms.system,
        "Pulls forms that are submitted",
        "SOAP",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "hub-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "hub-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
