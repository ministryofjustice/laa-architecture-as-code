package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class CCR private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var web: Container
    lateinit var db: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "Crown Court Remuneration",
        "The CCR system is a web service that Manages Advocate fee claims."
      ).apply {
        Tags.CRIME.addTo(this)
      }

      web = system.addContainer(
        "Crown Court Remuneration UI",
        "Assessment and authorisation of payment of AGFS claims",
        "Java"
      ).apply {
        setUrl("https://github.com/ministryofjustice/ccr")
        Tags.WEB_BROWSER.addTo(this)
        AWSLegacy.ec2.add(this)
      }

      db = system.addContainer(
        "Crown Court Remuneration Database", "Stores user details for the application", "Oracle PL/SQL"
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
        CCCD.system,
        "Gets Claims information from and sends claim decision",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )

      web.uses(
        CCCD.sqsCCR,
        "Processes job to know to pull claim information",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
      web.uses(
        CCCD.sqsProcessResponse,
        "Notifies CCCD of success/failure of claim",
        null,
        null,
        tagsToArgument(Tags.CRIME)
      )
      web.uses(
        CCCD.web,
        "Pulls claims from",
        "HTTP API",
        null,
        tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineExternalRelationships() {
    }

    override fun defineUserRelationships() {
      LegalAidAgencyUsers.billingCaseWorker.uses(
        web, "Processes claim accessments for Advocate fees (AGFS)", null, null, tagsToArgument(Tags.CRIME)
      )
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "ccr-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "ccr-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
