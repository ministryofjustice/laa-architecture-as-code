package uk.gov.justice.laa.architecture

import com.structurizr.model.Model
import com.structurizr.view.ViewSet

interface LAASoftwareSystem {
  fun defineModelEntities(model: Model)
  fun defineInternalContainerRelationships()
  // fun defineContainerRelationships()
  // fun defineExternalContainerRelationships()
  // fun defineUserRelationships()
  fun defineRelationships()
  fun defineViews(views: ViewSet)
}
