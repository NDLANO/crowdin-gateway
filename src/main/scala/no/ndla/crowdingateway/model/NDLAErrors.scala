/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model

class ContentAlreadyInProgressException(message: String) extends RuntimeException(message)
class ProjectNotFoundException(message: String) extends RuntimeException(message)
class ImageNotFoundException(message: String) extends RuntimeException(message)
class AccessDeniedException(message: String) extends RuntimeException(message)
class ValidationException(message: String = "Validation error", val errors: Seq[ValidationMessage]) extends RuntimeException(message)
case class ValidationMessage(field: String, message: String)

