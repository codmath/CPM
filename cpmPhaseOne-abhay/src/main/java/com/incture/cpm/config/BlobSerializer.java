package com.incture.cpm.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
 
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
 
@JsonComponent
public class BlobSerializer extends JsonSerializer<Blob> {
 
    @Override
    public void serialize(Blob blob, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            jsonGenerator.writeBinary(bytes);
        } catch (SQLException e) {
            throw new IOException("Error serializing Blob to JSON", e);
        }
    }
}