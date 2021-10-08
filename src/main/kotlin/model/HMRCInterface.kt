package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class HMRCInterface private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "LAA-HMRC Interface Service API",
        "An interface between the Legal Aid Agency and HMRC providing access to income, fraud and debt data"
      ).apply {
        Tags.GET_LEGAL_AID.addTo(this)
      }

      api = system.addContainer(
        "LAA-HMRC Interface Service API",
        "A JSON interface for the HMRC API",
        "Ruby on Rails"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-hmrc-interface-service-api")
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
      api.uses(
        HMRC.hmrcInterface,
        "Relays query to",
        null,
        null
      )
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "hmrc-interface-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "hmrc-interface-context-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
