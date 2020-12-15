package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class MAAT private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var api: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Means Assessment & Administration Tool",
        "The MAAT system is a collection of applications are used in the application process for Criminal Legal Aid"
      ).apply {
        Tags.CRIME.addTo(this)
        Tags.GET_LEGAL_AID.addTo(this)
      }

      web = system.addContainer(
        "MAAT UI",
        "A Web application that manages applications for Criminal Legal Aid",
        "Java TomEE"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-maat-application")
        AWSLegacy.ec2.add(this)
        Tags.WEB_BROWSER.addTo(this)
      }

      api = system.addContainer(
        "MAAT API",
        "An API to allow LAA Legacy systems access Court Data Adapter",
        "Java Spring"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-maat-court-data-api")
        AWSLegacy.ec2.add(this)
      }

      db = system.addContainer(
        "MAAT DB",
        "An Oracle Database storing case information from HMCTS and decisions regarding Legal Aid Applications",
        "Oracle"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-maat-database")
        AWSLegacy.rds.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      api.uses(db, "Accesses and stores Court information", "Oracle PL/SQL")
    }

    override fun defineRelationships() {
      api.uses(
        CDA.system,
        "Sends status updates to and process events from",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
      api.uses(
        CDA.api,
        "Posts offence level status events",
        "REST",
        null,
        tagsToArgument(Tags.CRIME)
      )
      api.uses(
        CDA.sqsQueue,
        "Processes notifications from the queue",
        "SQS",
        null,
        tagsToArgument(Tags.CRIME)
      )

      db.uses(
        eForms.system,
        "Loads completed & submitted forms",
        "HUB (MAAT9)",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.crimeApplicationCaseWorker.uses(
        web,
        "Access to Case Management tasks and assess means & merits for Legal Aid",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "maat-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "maat-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
