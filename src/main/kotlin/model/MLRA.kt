package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class MLRA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "MAAT Libra Interface Application",
        "The MLRA system provides an interface between MAAT and HMCTS's LIBRA application for Caseworkers"
      ).apply {
        Tags.CRIME.addTo(this)
      }

      web = system.addContainer(
        "MLRA UI",
        "A Web application that manages interactions with LIBRA",
        "Java Spring"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-mlra-application")
        AWSLegacy.ec2.add(this)
        Tags.WEB_BROWSER.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
      web.uses(
        MAAT.db,
        "Uses as its own Database",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
      web.uses(
        InfoX.app,
        "Searches cases and sends representation order status",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
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
