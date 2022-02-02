package no.nav.syfo.etterlevelse

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.metrics.PRODUCED_MESSAGE_COUNTER
import no.nav.syfo.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import no.nav.syfo.log
import no.nav.syfo.util.Unbounded
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.time.Instant

class EtterlevelseService(
    private val applicationState: ApplicationState,
    private val kafkaConsumer: KafkaConsumer<String, JuridiskVurderingResult>,
    private val internPikTopic: String,
    private val kafkaProducer: KafkaProducer<String, JuridiskVurderingKafkaMessage>,
    private val etterlevelseTopic: String
) {
    private var lastLogTime = Instant.now().toEpochMilli()
    private val logTimer = 60_000L

    @DelicateCoroutinesApi
    fun startConsumer() {
        GlobalScope.launch(Dispatchers.Unbounded) {
            while (applicationState.ready) {
                try {
                    log.info("Starting consuming topic")
                    kafkaConsumer.subscribe(listOf(internPikTopic))
                    start()
                } catch (ex: Exception) {
                    log.error("Error running kafka consumer, unsubscribing and waiting 10 seconds for retry", ex)
                    kafkaConsumer.unsubscribe()
                    delay(10_000)
                }
            }
        }
    }

    private fun start() {
        var processedMessages = 0
        while (applicationState.ready) {
            val records = kafkaConsumer.poll(Duration.ofSeconds(10))
            records.forEach {
                it.value().juridiskeVurderinger.forEach { juridiskVurdering ->
                    sendToKafka(juridiskVurdering.tilJuridiskVurderingKafkaMessage())
                }
            }
            processedMessages += records.count()
            processedMessages = logProcessedMessages(processedMessages)
        }
    }

    private fun sendToKafka(juridiskVurderingKafkaMessage: JuridiskVurderingKafkaMessage) {
        try {
            kafkaProducer.send(ProducerRecord(etterlevelseTopic, juridiskVurderingKafkaMessage.fodselsnummer, juridiskVurderingKafkaMessage)).get()
            PRODUCED_MESSAGE_COUNTER.inc()
        } catch (ex: Exception) {
            log.error("Failed to send message to kafka")
            throw ex
        }
    }

    private fun logProcessedMessages(processedMessages: Int): Int {
        val currentLogTime = Instant.now().toEpochMilli()
        if (processedMessages > 0 && currentLogTime - lastLogTime > logTimer) {
            log.info("Processed $processedMessages messages")
            lastLogTime = currentLogTime
            return 0
        }
        return processedMessages
    }
}
