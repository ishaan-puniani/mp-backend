package com.mp.be.services.process;

import com.mp.be.database.entities.process.ProcessConfiguration;
import com.mp.be.database.entities.process.ProcessEdge;
import com.mp.be.database.entities.process.ProcessNode;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProcessConfigurationValidator {

    public void validate(ProcessConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException("Process configuration cannot be null");
        }
        if (config.getName() == null || config.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Process configuration name is required");
        }
        if (config.getNodes() == null || config.getNodes().isEmpty()) {
            throw new IllegalArgumentException("Process configuration must contain at least one node");
        }

        Set<String> nodeIds = new HashSet<>();
        for (ProcessNode node : config.getNodes()) {
            if (node.getId() == null || node.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("Each node must have a non-empty id");
            }
            if (!nodeIds.add(node.getId())) {
                throw new IllegalArgumentException("Duplicate nodeId detected: " + node.getId());
            }
        }

        if (config.getEdges() != null) {
            Set<String> edgeIds = new HashSet<>();
            for (ProcessEdge edge : config.getEdges()) {
                if (edge.getId() != null && !edge.getId().trim().isEmpty()) {
                    if (!edgeIds.add(edge.getId())) {
                        throw new IllegalArgumentException("Duplicate edgeId detected: " + edge.getId());
                    }
                }
                if (edge.getSource() == null || !nodeIds.contains(edge.getSource())) {
                    throw new IllegalArgumentException("Edge source '" + edge.getSource() + "' does not match any valid nodeId");
                }
                if (edge.getTarget() == null || !nodeIds.contains(edge.getTarget())) {
                    throw new IllegalArgumentException("Edge target '" + edge.getTarget() + "' does not match any valid nodeId");
                }
            }
        }
    }
}
