package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CCLF private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Crown Court Litigator Fees",
        "The CCLF system is a web service that manages litigators fee claims."
      ).apply {
        Tags.CRIME.addTo(this)
        Tags.GET_PAID.addTo(this)
      }

      web = system.addContainer(
        "Crown Court Litigator Fees UI",
        "Assessment and authorisation of payment of LGFS claims",
        "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/cclf")
        Tags.WEB_BROWSER.addTo(this)
        AWSLegacy.ec2.add(this)
      }

      db = system.addContainer(
        "Crown Court Litigator Fees Database", "Stores user details for the application", "Oracle PL/SQL"
      ).apply {
        Tags.DATABASE.addTo(this)
        AWSLegacy.rds.add(this)
      }
    }

    override fun defineInternalContainerRelationships() {
      web.uses(db, "Connects to")
    }

    override fun defineRelationships() {
      web.uses(
        CCCD.system, "Gets Claims information from and sends claim decision", null, null, tagsToArgument(Tags.CRIME)
      )

      web.uses(CCCD.sqsCCLF, "Processes job to know to pull claim information")
      web.uses(CCCD.sqsProcessResponse, "Notifies CCCD of success/failure of claim")
      web.uses(CCCD.web, "Pulls claims from", "HTTP API")

      db.uses(
        MAAT.db,
        "Loads defendant data from",
        "HUB",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
      db.uses(
        HUB.db,
        "Takes Crown Court case data from",
        "HUB",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.billingCaseWorker.uses(
        web, "Processes claim accessments for Litigators fees (LGFS)", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "cclf-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "cclf-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
