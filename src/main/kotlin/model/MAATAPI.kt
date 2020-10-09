package uk.gov.justice.laa.architecture

import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ViewSet

class MAATAPI private constructor() {
  companion object : LAASoftwareSystem {
    lateinit var system: SoftwareSystem
    lateinit var api: Container

    override fun defineModelEntities(model: Model) {
      system = model.addSoftwareSystem(
        "MAAT API",
        "The laa-maat-court-data-api system is a web service that connects to Legacy LAA systems and " +
          "Court Data Adapter"
      )

      api = system.addContainer(
        "MAAT API",
        "An API to allow LAA Legacy systems access Court Data Adapter",
        "Java Spring"
      ).apply {
        setUrl("https://github.com/ministryofjustice/laa-maat-court-data-api")
        AWSLegacy.ec2.add(this)
      }
    }

    override fun defineRelationships() {
      api.uses(
        CDA.system,
        "Sends status updates to and process events from"
      )

      api.uses(
        CDA.api,
        "Posts offence level status events",
        "REST"
      )

      api.uses(
        CDA.sqsQueue,
        "Processes Events from the queue",
        "SQS"
      )

      api.uses(
        MAATDB.db,
        "Accesses and stores Court information",
        "Oracle PL/SQL"
      )
    }

    override fun defineViews(views: ViewSet) {
      // declare views here
      views.createSystemContextView(system, "maat-api-context", null).apply {
        addDefaultElements()
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }

      views.createContainerView(system, "maat-api-container", null).apply {
        addDefaultElements()
        setExternalSoftwareSystemBoundariesVisible(true)
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 300)
      }
    }
  }
}
