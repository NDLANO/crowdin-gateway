import javax.servlet.ServletContext

import no.ndla.crowdingateway.{ComponentRegistry, CrowdinGatewayProperties}
import org.scalatra.LifeCycle

/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(ComponentRegistry.crowdinController, CrowdinGatewayProperties.CrowdinControllerPath, "crowdin")
    context.mount(ComponentRegistry.resourcesApp, CrowdinGatewayProperties.ApiDocsPath)
    context.mount(ComponentRegistry.healthController, CrowdinGatewayProperties.HealthControllerPath)
  }
}
