package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.model.Person
import com.structurizr.view.ViewSet

class LegalAidAgencyUsers private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var citizen: Person
    lateinit var provider: Person

    override fun defineModelEntities(model: Model) {
      citizen = model.addPerson("A member of the public in England and Wales")
      provider = model.addPerson("Legal Aid Provider")
    }

    override fun defineRelationships() {
    }

    override fun defineViews(views: ViewSet) {
    }
  }
}
