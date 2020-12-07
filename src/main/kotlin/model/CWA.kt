package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CWA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var ebs: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Contract & Work Administration",
        "CWA is a billing system that contains all provider contracts and schedules"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-cwa")
      }

      ebs = system.addContainer(
        "E-Business Suite",
        "Oracle EBS 11i application",
        "Oracle"
      )

      db = system.addContainer(
        "Oracle Database",
        "E-Business Suite DB",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      ebs.uses(db, "Connects to")
    }

    override fun defineRelationships() {
      db.uses(ERIC.db, "Pushes provider names, users, roles, and offices", "HUB")
    }

    override fun defineExternalRelationships() {
      db.uses(
        CCMS.ebsDb,
        "Pushes internal users, their roles and securing attributes. " +
          "Provider users, bank accounts, contracts and office details. Synchronises each night",
        "HUB",
        InteractionStyle.Asynchronous
      )

      db.uses(MAAT.db, "Pushes provider names and office addresses to", "HUB")
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "cwa-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "cwa-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
