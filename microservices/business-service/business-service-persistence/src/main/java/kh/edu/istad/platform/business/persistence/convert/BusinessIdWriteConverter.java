package kh.edu.istad.platform.business.persistence.convert;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class BusinessIdWriteConverter implements Converter<BusinessId, Document> {

    @Override
    public Document convert(BusinessId source) {
        Document document = new Document();
        document.put("_id",source.value().toString());
        return document;
    }
}
