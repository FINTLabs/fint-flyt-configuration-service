package no.novari.flyt.configuration

import no.novari.flyt.audit.actor.Actor
import no.novari.flyt.audit.history.HistoryEventType
import no.novari.flyt.configuration.mapping.ConfigurationMappingService
import no.novari.flyt.configuration.mapping.InstanceCollectionReferencesMappingService
import no.novari.flyt.configuration.mapping.ObjectCollectionMappingMappingService
import no.novari.flyt.configuration.mapping.ObjectMappingMappingService
import no.novari.flyt.configuration.mapping.ObjectsFromCollectionMappingMappingService
import no.novari.flyt.configuration.mapping.PerKeyMappingService
import no.novari.flyt.configuration.mapping.ValueCollectionMappingMappingService
import no.novari.flyt.configuration.mapping.ValueMappingMappingService
import no.novari.flyt.configuration.mapping.ValuesFromCollectionMappingMappingService
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPatchDto
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto
import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.util.UUID

/**
 * Kjører mot en ekte Postgres slik at Flyway-migrasjonene, JSONB-aktørkolonnene og
 * Envers-revisjonene faktisk blir utøvd — `ddl-auto: none` gjør at skjemaet kommer fra
 * migrasjonene, ikke fra Hibernate.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(
    ConfigurationAuditIntegrationTest.AuditTestConfiguration::class,
    ConfigurationService::class,
    ConfigurationHistoryService::class,
    ConfigurationMappingService::class,
    ObjectMappingMappingService::class,
    ValueMappingMappingService::class,
    PerKeyMappingService::class,
    ObjectCollectionMappingMappingService::class,
    ValueCollectionMappingMappingService::class,
    ObjectsFromCollectionMappingMappingService::class,
    ValuesFromCollectionMappingMappingService::class,
    InstanceCollectionReferencesMappingService::class,
)
@TestPropertySource(
    properties = [
        // application-flyt-postgres binder hikari-skjemaet til fint.database.username, som ikke finnes her
        "spring.datasource.hikari.schema=public",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true",
        "spring.jpa.properties.org.hibernate.envers.store_data_at_delete=true",
        "novari.flyt.audit.display.unknown-user=Ukjent bruker",
    ],
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ConfigurationAuditIntegrationTest {
    /**
     * `@EnableFlytAuditing` på `Application` gir aktør- og revisjonsbønnene også i denne
     * slice-en; her aktiveres kun JPA-auditing, som ellers kommer fra en auto-config utenfor
     * slice-en. Navneoppslaget mot authorization-service er ikke tilgjengelig, så aktørnavn
     * faller tilbake til de konfigurerte visningsverdiene.
     */
    @TestConfiguration
    @EnableJpaAuditing(auditorAwareRef = "flytAuditorAware")
    class AuditTestConfiguration

    @Autowired
    lateinit var configurationService: ConfigurationService

    @Autowired
    lateinit var configurationHistoryService: ConfigurationHistoryService

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    private val transactionTemplate: TransactionTemplate by lazy { TransactionTemplate(transactionManager) }

    private val userOid: UUID = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        jdbcTemplate.execute("DELETE FROM configuration_aud")
        jdbcTemplate.execute("DELETE FROM revinfo")
        jdbcTemplate.execute("DELETE FROM configuration")
        setJwtWithOid(userOid)
    }

    @AfterEach
    fun clearSecurityContext() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `save populates audit fields with the authenticated actor`() {
        val saved = configurationService.save(configurationDto("v1"))

        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.createdByActor).isEqualTo(Actor.User(userOid))
        assertThat(saved.createdBy).isEqualTo("Ukjent bruker")
        assertThat(saved.lastModifiedAt).isNotNull()
        assertThat(saved.lastModifiedByActor).isEqualTo(Actor.User(userOid))
    }

    @Test
    fun `history returns newest revision first with metadata snapshot`() {
        val id = requireNotNull(configurationService.save(configurationDto("v1")).id)
        configurationService.updateById(id, ConfigurationPatchDto(comment = "endret"))

        val page = findHistory(id)

        assertThat(page.totalElements).isEqualTo(2)
        assertThat(page.content.map { it.type })
            .containsExactly(HistoryEventType.UPDATED, HistoryEventType.CREATED)
        assertThat(page.content.map { it.snapshot?.comment }).containsExactly("endret", null)
        assertThat(page.content).allSatisfy {
            assertThat(it.actor).isEqualTo(Actor.User(userOid))
            assertThat(it.actorDisplay).isEqualTo("Ukjent bruker")
        }
    }

    /**
     * `mapping` er `targetAuditMode = NOT_AUDITED`, så gamle revisjoner peker på en `object_mapping`-rad
     * som er orphan-removed. Relasjonen er en proxy som aldri dereferereres av det metadata-baserte
     * snapshotet — legges mapping inn i [ConfigurationSnapshot] senere, feiler denne testen med
     * EntityNotFoundException, og det er med vilje.
     */
    @Test
    fun `history survives a mapping replacement that orphan removed the previous tree`() {
        val id = requireNotNull(configurationService.save(configurationDto("v1")).id)
        val replacedMappingId =
            jdbcTemplate.queryForObject(
                "select mapping_id from configuration where id = ?",
                Long::class.java,
                id,
            )

        configurationService.updateById(id, ConfigurationPatchDto(mapping = objectMappingDto("v2")))

        val remainingRowsForReplacedMapping: Long? =
            jdbcTemplate.queryForObject(
                "select count(*) from object_mapping where id = ?",
                Long::class.java,
                requireNotNull(replacedMappingId),
            )
        assertThat(remainingRowsForReplacedMapping).isEqualTo(0L)

        val page = findHistory(id)

        assertThat(page.totalElements).isEqualTo(2)
        assertThat(page.content.map { it.type })
            .containsExactly(HistoryEventType.UPDATED, HistoryEventType.CREATED)
    }

    @Test
    fun `delete gives a DELETED entry with null snapshot`() {
        val id = requireNotNull(configurationService.save(configurationDto("v1")).id)

        configurationService.deleteById(id)

        val page = findHistory(id)

        assertThat(page.content.first().type).isEqualTo(HistoryEventType.DELETED)
        assertThat(page.content.first().snapshot).isNull()
    }

    @Test
    fun `history without authentication records the system actor`() {
        SecurityContextHolder.clearContext()

        val id = requireNotNull(configurationService.save(configurationDto("v1")).id)

        val page = findHistory(id)

        assertThat(page.content.first().actor).isEqualTo(Actor.System)
        assertThat(page.content.first().actorDisplay).isEqualTo("System")
    }

    @Test
    fun `findAll hydrates actor display names for every row on the page`() {
        configurationService.save(configurationDto("v1"))
        configurationService.save(configurationDto("v2"))

        val page = configurationService.findAll(ConfigurationFilter(null, null), true, PageRequest.of(0, 20))

        assertThat(page.totalElements).isEqualTo(2)
        assertThat(page.content).allSatisfy {
            assertThat(it.createdBy).isEqualTo("Ukjent bruker")
            assertThat(it.createdByActor).isEqualTo(Actor.User(userOid))
        }
    }

    /** Envers' AuditReader krever en åpen EntityManager, altså en aktiv transaksjon. */
    private fun findHistory(id: Long) =
        requireNotNull(
            transactionTemplate.execute {
                configurationHistoryService.findHistory(id, PageRequest.of(0, 20))
            },
        )

    private fun configurationDto(mappingString: String): ConfigurationDto =
        ConfigurationDto
            .builder()
            .integrationId(1L)
            .integrationMetadataId(2L)
            .mapping(objectMappingDto(mappingString))
            .build()

    private fun objectMappingDto(mappingString: String): ObjectMappingDto =
        ObjectMappingDto
            .builder()
            .valueMappingPerKey(
                mutableMapOf(
                    "felt" to
                        ValueMappingDto
                            .builder()
                            .type(ValueMapping.Type.STRING)
                            .mappingString(mappingString)
                            .build(),
                ),
            ).build()

    private fun setJwtWithOid(oid: UUID) {
        val jwt =
            Jwt
                .withTokenValue("token")
                .header("alg", "none")
                .claim("objectidentifier", oid.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build()
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken(jwt, null)
    }

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:17-alpine")
    }
}
