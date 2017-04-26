# Crowdin Gateway

# Usage
Gateway for integrating with Crowdin for translation of content.

* The `content` of the request is assumed to be valid html.
* The `metaData` of the request is assumed to be valid json.

To interact with the API, you need valid security credentials; see [Access Tokens usage](https://github.com/NDLANO/auth/blob/master/README.md).

For a more detailed documentation of the API, please refer to the [API documentation](https://staging.api.ndla.no).

# Building and distribution

## Compile
    sbt compile

## Run tests
    sbt test

## Create Docker Image
    ./build.sh
