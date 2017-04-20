/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model

import no.ndla.crowdingateway.model.api.ValidationMessage

class ContentAlreadyInProgressException(message: String) extends RuntimeException(message)
class ProjectNotFoundException(message: String) extends RuntimeException(message)
class ImageNotFoundException(message: String) extends RuntimeException(message)
class AccessDeniedException(message: String) extends RuntimeException(message)
class ParameterMissingException(message: String) extends RuntimeException(message)
class ValidationException(message: String = "Validation error", val errors: Seq[ValidationMessage]) extends RuntimeException(message)


