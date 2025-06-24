package no.nav.syfo.etterlevelse

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import no.nav.syfo.application.metrics.PRODUCED_MESSAGE_COUNTER
import no.nav.syfo.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Duration

class EtterlevelseService(
    private val kafkaConsumer: KafkaConsumer<String, JuridiskVurderingResult>,
    private val internPikTopic: String,
    private val kafkaProducer: KafkaProducer<String, JuridiskVurderingKafkaMessage>,
    private val etterlevelseTopic: String
) {
    companion object {
        private val log = LoggerFactory.getLogger(EtterlevelseService::class.java)
    }
    suspend fun startConsumer() = coroutineScope {
            while (isActive) {
                try {
                    log.info("Starting consuming topic")
                    kafkaConsumer.subscribe(listOf(internPikTopic))
                    start()
                } catch (ex: Exception) {
                    log.error(
                        "Error running kafka consumer, unsubscribing and waiting 10 seconds for retry",
                        ex
                    )
                    kafkaConsumer.unsubscribe()
                    delay(10_000)
                }
            }
    }

    private suspend fun start() = coroutineScope {
        while (isActive) {
            val records = kafkaConsumer.poll(Duration.ofSeconds(10))
            records.forEach {
                it.value().juridiskeVurderinger.forEach { juridiskVurdering ->
                    sendToKafka(juridiskVurdering.tilJuridiskVurderingKafkaMessage())
                }
            }
        }
    }

    private fun sendToKafka(juridiskVurderingKafkaMessage: JuridiskVurderingKafkaMessage) {
        try {

            kafkaProducer
                .send(
                    ProducerRecord(
                        etterlevelseTopic,
                        juridiskVurderingKafkaMessage.fodselsnummer,
                        juridiskVurderingKafkaMessage
                    )
                )
                .get()
            PRODUCED_MESSAGE_COUNTER.inc()
        } catch (ex: Exception) {
            log.error("Failed to send message to kafka for id ${juridiskVurderingKafkaMessage.id}, sporing ${juridiskVurderingKafkaMessage.sporing}")
            throw ex
        }
    }
}
