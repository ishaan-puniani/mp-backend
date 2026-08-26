package com.mp.be.services.process;

import com.mp.be.database.entities.User;
import com.mp.be.database.entities.process.ProcessConfiguration;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.database.repositories.MachineMasterRepository;
import com.mp.be.database.repositories.MaterialMasterRepository;
import com.mp.be.database.repositories.ProcessConfigurationRepository;
import com.mp.be.models.process.ProcessConfigurationModel;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessConfigurationIntegrationTest {

    @Mock
    private ProcessConfigurationRepository repository;

    @Spy
    private ProcessConfigurationValidator validator;

    @Mock
    private MaterialMasterRepository materialMasterRepository;

    @Mock
    private MachineMasterRepository machineMasterRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ProcessConfigurationServiceImpl service;

    private ServiceOptions serviceOptions;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId("user-123");
        user.setEmail("admin@factory.com");

        serviceOptions = mock(ServiceOptions.class);
        lenient().when(serviceOptions.getCurrentTenantId()).thenReturn("tenant-icecream-01");
        lenient().when(serviceOptions.getCurrentUserId()).thenReturn("user-123");
        lenient().when(serviceOptions.getCurrentUser()).thenReturn(user);
    }

    @Test
    void testSeedIceCreamProcessWorkflow() {
        when(repository.save(any(ProcessConfiguration.class))).thenAnswer(i -> i.getArgument(0));
        lenient().when(materialMasterRepository.findByTenantAndCode(any(), any())).thenReturn(Optional.empty());
        lenient().when(machineMasterRepository.findByTenantAndCode(any(), any())).thenReturn(Optional.empty());

        ProcessConfigurationModel seeded = service.seedIceCreamProcess(serviceOptions);

        assertNotNull(seeded);
        assertEquals("icecream-manufacturing", seeded.getId());
        assertEquals("Ice Cream Plant", seeded.getName());
        assertEquals(17, seeded.getNodes().size());
        assertEquals(23, seeded.getEdges().size());

        // Verify mixing node with recipes and India labour/cost metadata
        var mixingNode = seeded.getNodes().stream()
                .filter(n -> "mixing".equals(n.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(mixingNode);
        assertNotNull(mixingNode.getParameters().getMixRecipe());
        assertEquals(4, mixingNode.getParameters().getMixRecipe().size());
        assertEquals("Milk", mixingNode.getParameters().getMixRecipe().get(0).getIngredient());
        assertEquals("milk", mixingNode.getParameters().getMixRecipe().get(0).getIngredientCode());

        assertNotNull(mixingNode.getProcessLabour());
        assertEquals("mix-op", mixingNode.getProcessLabour().get(0).getId());
        assertEquals(22000.0, mixingNode.getProcessLabour().get(0).getMonthlyWage());

        // Verify packaging node
        var pkgNode = seeded.getNodes().stream()
                .filter(n -> "packaging".equals(n.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(pkgNode);
        assertNotNull(pkgNode.getConsumables());
        assertEquals("tub-500", pkgNode.getConsumables().get(0).getId());
    }

    @Test
    void testSeedIceCreamProcessIdempotency() {
        ProcessConfiguration existing = new ProcessConfiguration();
        existing.setId("existing-id-123");
        existing.setCode("PROC-ICECREAM-01");
        existing.setVersion(1);
        existing.tenant = "tenant-icecream-01";

        when(repository.findByTenantAndCodeAndVersion("tenant-icecream-01", "PROC-ICECREAM-01", 1))
                .thenReturn(Optional.of(existing));
        when(repository.findById("existing-id-123")).thenReturn(Optional.of(existing));
        when(repository.save(any(ProcessConfiguration.class))).thenAnswer(i -> i.getArgument(0));

        ProcessConfigurationModel seeded = service.seedIceCreamProcess(serviceOptions);

        assertNotNull(seeded);
        assertEquals("existing-id-123", seeded.getId());
        verify(repository, never()).delete(any());
    }
}
