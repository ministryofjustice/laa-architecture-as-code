package uk.gov.justice.laa.architecture

import com.structurizr.Workspace
import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Enterprise
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.view.ViewSet

private val MODEL_ITEMS = listOf<LAASoftwareSystem>(
  Apply,
  HMRC,
  HMRCInterface,
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
  CommonPlatform,
  CDA,
  HUB,
  LFA,
  OSPlacesAPI,
  MAAT,
  BankHolidaysAPI,
  CCCD,
  CCR,
  CCLF,
  CIS,
  CWA,
  SendGrid,
  TAD,
  CorporateDocumentServices,
  MLRA,
  Libra,
  InfoX,
  ERIC,
  EDW,
  Rossendales,
  Allpay,
  Eckoh,
  Barclaycard,
  eForms,
  BankAccounts,
  Xhibit,
  FeeCalculator,
  ProviderCMS,
  DWP,
  ProviderTraining,
  GoogleMaps
)

private val TAGS_FOR_FILTER_VIEWS = listOf<Tags>(
  Tags.GET_ACCESS,
  Tags.GET_LEGAL_AID,
  Tags.GET_PAID,
  Tags.CRIME
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
  MODEL_ITEMS.forEach {
    it.defineInternalContainerRelationships()
    it.defineExternalRelationships()
    it.defineRelationships()
    it.defineUserRelationships()
    }
}

private fun defineViews(views: ViewSet) {
  MODEL_ITEMS.forEach { it.defineViews(views) }
  defineFilteredViews(TAGS_FOR_FILTER_VIEWS, views)
  defineGlobalViews(views)
}

fun defineWorkspace(): Workspace {
  val enterprise = Enterprise("Legal Aid Agency")
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
