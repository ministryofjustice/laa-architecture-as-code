package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Container
import com.structurizr.view.ViewSet

class HMRC private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var hmrcInterface: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "HM Revenue & Customs",
        "Contains information about people's Employment and Tax status"
      ).apply {
        OutsideLAA.addTo(this)
      }

      hmrcInterface = system.addContainer(
        "Employment and Income Checker",
        "API to query Employment and Tax status",
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
