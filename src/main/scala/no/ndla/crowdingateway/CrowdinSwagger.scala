/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway

import org.scalatra.ScalatraServlet
import org.scalatra.swagger._

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase {
  get("/") {
    renderSwagger2(swagger.docs.toList)
  }
}

object CrowdinGatewayInfo {
  val apiInfo = ApiInfo(
  "Crowdin Gateway",
  "Documentation for the Crowdin Gateway of NDLA.no",
  "https://ndla.no",
  CrowdinGatewayProperties.ContactEmail,
  "GPL v3.0",
  "http://www.gnu.org/licenses/gpl-3.0.en.html")
}

class CrowdinSwagger extends Swagger("2.0", "0.8", CrowdinGatewayInfo.apiInfo) {
  addAuthorization(OAuth(List("crowdin:all"), List(ApplicationGrant(TokenEndpoint("/auth/tokens", "access_token")))))
}
