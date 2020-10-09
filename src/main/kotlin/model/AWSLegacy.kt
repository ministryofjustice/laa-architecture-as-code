package uk.gov.justice.laa.architecture

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

class AWSLegacy private constructor() {
  companion object {
    lateinit var rds: DeploymentNode
    lateinit var ec2: DeploymentNode
    lateinit var s3: DeploymentNode
    lateinit var sns: DeploymentNode
    lateinit var sqs: DeploymentNode
    lateinit var elasticache: DeploymentNode
    lateinit var elasticsearch: DeploymentNode

    fun defineDeploymentNodes(@Suppress("UNUSED_PARAMETER") model: Model) {
      var AWSLegacy = AWS.london.addDeploymentNode("Legacy account", "AWS shared hosting platform", "AWS")
      rds = AWSLegacy.addDeploymentNode("RDS", "AWS Relational Database Service database-as-a-service", "AWS")
      ec2 = AWSLegacy.addDeploymentNode("EC2", "AWS Elastic Compute Cloud", "AWS")
      s3 = AWSLegacy.addDeploymentNode("S3", "AWS Simple Storage Service", "AWS")
      sns = AWSLegacy.addDeploymentNode("SNS", "AWS Simple Notification Service", "AWS")
      sqs = AWSLegacy.addDeploymentNode("SQS", "AWS Simple Queue Service", "AWS")
      elasticache = AWSLegacy.addDeploymentNode(
        "ElastiCache",
        "Managed in-memory data store and cache service",
        "AWS"
      )
      elasticsearch = AWSLegacy.addDeploymentNode("ElasticSearch", "AWS ElasticSearch Service", "AWS")
    }
  }
}
