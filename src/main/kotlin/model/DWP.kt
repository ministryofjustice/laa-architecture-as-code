package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Container
import com.structurizr.view.ViewSet

class DWP private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var benefitChecker: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Department for Work and Pensions",
        "Contains information about people's Benefit status"
      ).apply {
        OutsideLAA.addTo(this)
        Tags.CRIME.addTo(this)
      }

      benefitChecker = system.addContainer(
        "Benefit Checker",
        "API to query Benefit status",
        "Web Service"
      )
    }

    override fun defineInternalContainerRelationships() {
    }

    override fun defineRelationships() {
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
