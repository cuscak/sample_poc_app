package andrej.cuscak.dms.model.mapper;

import andrej.cuscak.dms.model.Owner;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.io.IOException;

@JsonComponent
public class AggregateReferenceMapper {

    public static class Deserializer extends JsonDeserializer<AggregateReference> {

        @Override
        public AggregateReference deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            ObjectCodec codec = jsonParser.getCodec();
            JsonNode tree = codec.readTree(jsonParser);
            Long id = tree.get("id").asLong();
            return AggregateReference.to(id);
        }

    }
}
