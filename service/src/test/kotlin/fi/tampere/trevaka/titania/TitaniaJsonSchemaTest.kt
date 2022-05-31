// SPDX-FileCopyrightText: 2021-2022 City of Tampere
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.titania

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersionDetector
import fi.tampere.trevaka.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource

class TitaniaJsonSchemaTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var jsonMapper: JsonMapper

    private lateinit var jsonSchema: JsonSchema

    @BeforeEach
    fun setupTitaniaJsonSchemaTest() {
        val jsonNode =
            ClassPathResource("titania/schema/titania-update-request-input.schema.json").inputStream.use {
                jsonMapper.readTree(it)
            }
        val jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(jsonNode))
        jsonSchema = jsonSchemaFactory.getSchema(jsonNode)
    }

    @Test
    fun `example data should be valid with json schema`() {
        val jsonNode = jsonMapper.valueToTree<JsonNode>(titaniaUpdateRequestValidExampleData)
        val errors = jsonSchema.validate(jsonNode)
        assertThat(errors).withFailMessage("Errors should be empty but contains: %s", errors).isEmpty()
    }

    @Test
    fun `minimal data should be valid with json schema`() {
        val jsonNode = jsonMapper.valueToTree<JsonNode>(titaniaUpdateRequestValidMinimalData)
        val errors = jsonSchema.validate(jsonNode)
        assertThat(errors).withFailMessage("Errors should be empty but contains: %s", errors).isEmpty()
    }

}
