/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.integration

import no.ndla.crowdingateway.{TestEnvironment, UnitSuite}


class CrowdinClientTest extends UnitSuite with TestEnvironment {

  override val ndlaClient = new NdlaClient
  override val crowdinClient = new CrowdinClient

  test("That we are able to call crowdin") {
    val projectInfo = crowdinClient.getProject("nb")
    print(s"PROJECT: $projectInfo" )
  }


}
