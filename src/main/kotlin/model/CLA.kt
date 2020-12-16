package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CLA private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var claPublic: Container
    lateinit var claBackend: Container
    lateinit var claFrontend: Container
    lateinit var claManagementInformation: Container
    lateinit var db: Container
    lateinit var replicaDb: Container
    lateinit var queue: Container
    lateinit var celery: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Civil Legal Advice",
        "Service for citizens to check if they are eligible for legal aid"
      ).apply {
        Tags.GET_ACCESS.addTo(this)
        Tags.LIVE.addTo(this)
      }

      claPublic = system.addContainer(
        "Civil Legal Advice Public UI",
        "A public web service which allows citizens to check if they are eligible for legal aid",
        "Flask"
      ).apply {
        setUrl("https://github.com/ministryofjustice/cla_public")
        Tags.WEB_BROWSER.addTo(this)
      }

      claBackend = system.addContainer(
        "Civil Legal Advice Backend API",
        "API for managing civil legal aid cases",
        "Django"
      ).apply {
        setUrl("https://github.com/ministryofjustice/cla_backend")
      }

      claFrontend = system.addContainer(
        "Civil Legal Advice Frontend",
        "A frontend web application that allows managing civil legal aid applications and callbacks",
        "Django"
      ).apply {
        setUrl("https://github.com/ministryofjustice/cla_frontend")
        Tags.WEB_BROWSER.addTo(this)
      }

      claManagementInformation = system.addContainer(
        "Civil Legal Advice Management Information",
        "Application to process the information from CLA Backend into TAD",
        "Oracle Application Express (APEX)"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-apex")
        Tags.WEB_BROWSER.addTo(this)
      }

      db = system.addContainer(
        "Civil Legal Aid Database",
        "Stores civil legal aid cases, callbacks, logins, and specialist provider submissions",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      replicaDb = system.addContainer(
        "Civil Legal Aid Replica Database",
        "Replica database used for generating reports",
        "PostgreSQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        CloudPlatform.rds.add(this)
      }

      celery = system.addContainer("Celery", "Listens to queued events and processes them", "Celery").apply {
        CloudPlatform.kubernetes.add(this)
      }

      queue = system.addContainer("Queue", "Queue used for scheduling jobs via Celery", "AWS SQS").apply {
        CloudPlatform.sqs.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      claPublic.uses(claBackend, "Stores cases and callback details using", "REST")
      claBackend.uses(db, "Connects to")
      claBackend.uses(replicaDb, "Generates reports from")
      db.uses(replicaDb, "Replicates to")
      claBackend.uses(queue, "Queues jobs to")
      celery.uses(queue, "Processes queued jobs from")
      celery.uses(claFrontend, "Sends notifications to")
      claFrontend.uses(claBackend, "Manages cases, callbacks, logins, and submissions using", "REST")
    }

    override fun defineRelationships() {
      claPublic.uses(FALA.laalaa, "Finds out which providers are near the user using", "REST")
      claManagementInformation.uses(TAD.db, "Stores data from OBIEE extract ZIP file upload")
    }

    override fun defineExternalRelationships() {
      claPublic.uses(OSPlacesAPI.system, "Gets address data from", "REST")
      claPublic.uses(SendGrid.system, "Sends confirmation and notification emails using", "REST")
    }

    override fun defineUserRelationships() {
      SendGrid.system.delivers(LegalAidAgencyUsers.citizen, "Sends email to")
      LegalAidAgencyUsers.citizen.uses(claPublic, "Checks if they can get legal aid using")
      LegalAidAgencyUsers.provider.uses(claFrontend, "Logs in and uploads work report CSV")

      LegalAidAgencyUsers.directServicesTeam.uses(
        system,
        "Downloads OBIEE extract ZIP files from CLA Backend and uploads to CLA MI, and views cases in CLA frontend " +
          "to manage contracts"
      )

      LegalAidAgencyUsers.directServicesTeam.uses(claBackend, "Downloads OBIEE extract ZIP file")
      LegalAidAgencyUsers.directServicesTeam.uses(claManagementInformation, "Uploads OBIEE extract ZIP file")
      LegalAidAgencyUsers.directServicesTeam.uses(
        claFrontend,
        "Veiws cases to manage the specialist provider and operator service " +
          "conracts"
      )

      LegalAidAgencyUsers.contactCentreOperator.uses(
        claFrontend,
        "Checks eligibility and data of incoming legal aid requests"
      )
    }

    override fun defineViews(views: ViewSet) {
      views.createSystemContextView(system, "cla-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300)
      }

      views.createContainerView(system, "cla-container", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
