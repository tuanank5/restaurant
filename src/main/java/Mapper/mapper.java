package Mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class mapper {
    private static ObjectMapper objectMapper;

    static{
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static  <S,T> T mapper(S s,Class<T> clazz){
        return objectMapper.convertValue(s,clazz);
    }

}