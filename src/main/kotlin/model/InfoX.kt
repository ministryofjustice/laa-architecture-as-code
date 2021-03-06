package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class InfoX private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var app: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "InfoX",
        "Adapter for LAA to HMCTS's Libra"
      ).apply {
        Tags.CRIME.addTo(this)
        Tags.GET_LEGAL_AID.addTo(this)
      }

      app = system.addContainer(
        "InfoX",
        "Adapter for the LAA MLRA/MAAT and HMCTS Libra communication",
        "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-infoX-application")
        AWSLegacy.ec2.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      app.uses(
        Libra.system, "Sends representation orders status", "SOAP", null, tagsToArgument(Tags.CRIME)
      )
      app.uses(
        MAAT.db, "Writes notifications to", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "infox-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "infox-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
