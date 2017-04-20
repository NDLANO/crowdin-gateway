/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2016 NDLA
 *
 * See LICENSE
 *
 */

package no.ndla.crowdingateway

import com.typesafe.scalalogging.LazyLogging
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler}
import org.scalatra.servlet.ScalatraListener

import scala.io.Source


object JettyLauncher extends LazyLogging {
  def main(args: Array[String]) {
    logger.info(Source.fromInputStream(getClass.getResourceAsStream("/log-license.txt")).mkString)

     val startMillis = System.currentTimeMillis()

     val context = new ServletContextHandler()
     context setContextPath "/"
     context.addEventListener(new ScalatraListener)
     context.addServlet(classOf[DefaultServlet], "/")
     context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false")

     val server = new Server(CrowdinGatewayProperties.ApplicationPort)
     server.setHandler(context)
     server.start()

     val startTime = System.currentTimeMillis() - startMillis
     logger.info(s"Started at port ${CrowdinGatewayProperties.ApplicationPort} in $startTime ms.")

     server.join()
  }
}
