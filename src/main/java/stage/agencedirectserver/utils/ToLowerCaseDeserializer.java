package stage.agencedirectserver.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.IOException;

public class ToLowerCaseDeserializer extends StdConverter<String, String> {
    @Override
    public String convert(String value) {
        return value.toLowerCase();
    }
}
