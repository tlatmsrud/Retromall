package com.retro

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*

class ApiDocumentUtils {

    companion object {
        fun getDocumentRequest(): OperationRequestPreprocessor? {
            return preprocessRequest(
                modifyUris()
                    .scheme("https")
                    .host("localhost")
                    .port(12376),
                prettyPrint()
            )
        }

        fun getDocumentResponse(): OperationResponsePreprocessor? {
            return preprocessResponse(prettyPrint())
        }
    }

}