package kh.edu.istad.platform.business.persistence.convert;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.UUID;

@ReadingConverter
public class BusinessIdReadConverter implements Converter <Document, BusinessId> {

    @Override
    public BusinessId convert(Document source) {
        return new BusinessId(UUID.fromString(source.getString("_id")));
    }
}
