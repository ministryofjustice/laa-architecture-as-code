package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.InteractionStyle
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

class CIS private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Corporate Information System",
        "CIS is a legacy system that has been largely superseded but still performs invoicing services"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-cis")
        Tags.GET_LEGAL_AID.addTo(this)
        Tags.GET_PAID.addTo(this)
        Tags.LEGACY.addTo(this)
      }

      db = system.addContainer(
        "Oracle Database",
        "CIS database",
        "Oracle"
      ).apply {
        Tags.DATABASE.addTo(this)
      }
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
      // TODO: This should be the CIS DB component, not the CIS system
      system.uses(
        CCMS.ebsDb,
        "Pushes CIS invoices approved for payment and, after payment, updates status of invoices in CIS",
        "HUB",
        InteractionStyle.Asynchronous
      )
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
