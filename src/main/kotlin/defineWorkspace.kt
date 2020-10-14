package uk.gov.justice.laa.architecture

import com.structurizr.Workspace
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Enterprise
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.view.ViewSet

private val MODEL_ITEMS = listOf<LAASoftwareSystem>(
  Apply,
  CCMS,
  CFE,
  BenefitChecker,
  Geckoboard,
  TrueLayer,
  GOVUKNotify,
  Northgate,
  Portal,
  FALA,
  PostcodesIO,
  CLA,
  LegalAidAgencyUsers,
  EligibilityCalculator,
  VCD,
  CDA,
  CommonPlatform,
  OSPlacesAPI,
  MAAT
)

private fun defineModelItems(model: Model) {
  model.setImpliedRelationshipsStrategy(
    CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy()
  )

  AWS.defineDeploymentNodes(model)
  CloudPlatform.defineDeploymentNodes(model)
  AWSLegacy.defineDeploymentNodes(model)
  MODEL_ITEMS.forEach { it.defineModelEntities(model) }
}

private fun changeUndefinedLocationsToInternal(model: Model) {
  model.softwareSystems
    .filter { it.location == Location.Unspecified }.forEach { it.setLocation(Location.Internal) }
  model.people
    .filter { it.location == Location.Unspecified }.forEach { it.setLocation(Location.Internal) }
}

private fun defineRelationships() {
  MODEL_ITEMS.forEach { it.defineRelationships() }
}

private fun defineViews(views: ViewSet) {
  MODEL_ITEMS.forEach { it.defineViews(views) }
  defineGlobalViews(views)
}

fun defineWorkspace(): Workspace {
  val enterprise = Enterprise("Legal Aid Agency Digital")
  val workspace = Workspace(enterprise.name, "Systems related to the delivery of legal aid")
  workspace.id = 55246
  workspace.model.enterprise = enterprise

  defineModelItems(workspace.model)
  changeUndefinedLocationsToInternal(workspace.model)

  defineRelationships()
  defineViews(workspace.views)
  defineStyles(workspace.views.configuration.styles)
  defineDocumentation(workspace)

  return workspace
}
