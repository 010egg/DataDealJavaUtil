// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.mom.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.kafka.clients.producer.Producer;
import java.util.Properties;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class KafkaManager extends AbstractManager
{
    public static final String DEFAULT_TIMEOUT_MILLIS = "30000";
    static KafkaProducerFactory producerFactory;
    private final Properties config;
    private Producer<byte[], byte[]> producer;
    private final int timeoutMillis;
    private final String topic;
    
    public KafkaManager(final LoggerContext loggerContext, final String name, final String topic, final Property[] properties) {
        super(loggerContext, name);
        this.config = new Properties();
        this.topic = topic;
        this.config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.config.setProperty("batch.size", "0");
        for (final Property property : properties) {
            this.config.setProperty(property.getName(), property.getValue());
        }
        this.timeoutMillis = Integer.parseInt(this.config.getProperty("timeout.ms", "30000"));
    }
    
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0L) {
            this.closeProducer(timeout, timeUnit);
        }
        else {
            this.closeProducer(this.timeoutMillis, TimeUnit.MILLISECONDS);
        }
        return true;
    }
    
    private void closeProducer(final long timeout, final TimeUnit timeUnit) {
        if (this.producer != null) {
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (KafkaManager.this.producer != null) {
                        KafkaManager.this.producer.close();
                    }
                }
            };
            try {
                this.getLoggerContext().submitDaemon(task).get(timeout, timeUnit);
            }
            catch (InterruptedException ex) {}
            catch (ExecutionException ex2) {}
            catch (TimeoutException ex3) {}
        }
    }
    
    public void send(final byte[] msg) throws ExecutionException, InterruptedException, TimeoutException {
        if (this.producer != null) {
            this.producer.send(new ProducerRecord(this.topic, (Object)msg)).get(this.timeoutMillis, TimeUnit.MILLISECONDS);
        }
    }
    
    public void startup() {
        this.producer = KafkaManager.producerFactory.newKafkaProducer(this.config);
    }
    
    public String getTopic() {
        return this.topic;
    }
    
    static {
        KafkaManager.producerFactory = new DefaultKafkaProducerFactory();
    }
}
