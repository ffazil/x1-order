package com.tracebucket.x1.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tracebucket.x1.order.base.domain.BaseLineItem;
import com.tracebucket.x1.order.base.domain.BaseOrder;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.json.JsonSchema;
import org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer;
import org.springframework.data.util.TypeInformation;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Jackson customization utility.
 *
 * @author ffazil
 * @since 25/01/16
 */
@Configuration
public class JacksonCustomization {

    public @Bean
    Module moneyModule() {
        return new MoneyModule();
    }

    public @Bean
    Module orderFrameworkModule(){
        return new OrderFrameworkModule();
    }

    @SuppressWarnings("serial")
    static class OrderFrameworkModule extends SimpleModule{
        public OrderFrameworkModule(){
            setMixInAnnotation(BaseOrder.class, BaseOrderMixin.class);
            setMixInAnnotation(BaseLineItem.class, BaseLineItemMixin.class);

        }

        /**
         * Invoke {@link BaseOrder} constructor instead of setters to preserve immutability.
         */
        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
        static abstract class BaseOrderMixin{

            @JsonCreator
            public BaseOrderMixin(@JsonProperty("lineItems") Collection<BaseLineItem> items){

            }

        }

        /**
         * Invoke {@link BaseLineItem} constructor instead of setters to preserve immutability.
         */
        static abstract class BaseLineItemMixin{
            @JsonCreator
            public BaseLineItemMixin(@JsonProperty("sequenceNumber") Integer sequenceNumber,
                                     @JsonProperty("productReference") String productReference,
                                     @JsonProperty("unitPrice") Money unitPrice,
                                     @JsonProperty("quantity") int quantity,
                                     @JsonProperty("comment") String comment,
                                     @JsonProperty("orderReference") String orderReference){

            }
        }
    }

    @SuppressWarnings("serial")
    static class MoneyModule extends SimpleModule {

        private static final MonetaryAmountFormat FORMAT = MonetaryFormats.getAmountFormat(Locale.US);

        public MoneyModule() {

            addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
            addValueInstantiator(Money.class, new MoneyInstantiator());
        }

        /**
         * A dedicated serializer to render {@link MonetaryAmount} instances as formatted {@link String}. Also implements
         * {@link JsonSchemaPropertyCustomizer} to expose the different rendering to the schema exposed by Spring Data REST.
         */
        static class MonetaryAmountSerializer extends ToStringSerializer implements JsonSchemaPropertyCustomizer {

            private static final Pattern MONEY_PATTERN;

            static {

                StringBuilder builder = new StringBuilder();

                builder.append("(?=.)^"); // Start
                builder.append("[A-Z]{3}?"); // 3-digit currency code
                builder.append("\\s"); // single whitespace character
                builder.append("(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?"); // digits with optional grouping by "," every 3)
                builder.append("(\\.[0-9]{1,2})?$"); // End with a dot and two digits

                MONEY_PATTERN = Pattern.compile(builder.toString());
            }

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.ser.std.ToStringSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
             */
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                jgen.writeString(FORMAT.format((MonetaryAmount) value));
            }

            /*
             * (non-Javadoc)
             * @see org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer#customize(org.springframework.data.rest.webmvc.json.JsonSchema.JsonSchemaProperty, org.springframework.data.util.TypeInformation)
             */
            @Override
            public JsonSchema.JsonSchemaProperty customize(JsonSchema.JsonSchemaProperty property, TypeInformation<?> type) {
                return property.withType(String.class).withPattern(MONEY_PATTERN);
            }
        }

        static class MoneyInstantiator extends ValueInstantiator {

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.deser.ValueInstantiator#getValueTypeDesc()
             */
            @Override
            public String getValueTypeDesc() {
                return Money.class.toString();
            }

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.deser.ValueInstantiator#canCreateFromString()
             */
            @Override
            public boolean canCreateFromString() {
                return true;
            }

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.deser.ValueInstantiator#createFromString(com.fasterxml.jackson.databind.DeserializationContext, java.lang.String)
             */
            @Override
            public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
                return Money.parse(value, FORMAT);
            }
        }
    }

}
